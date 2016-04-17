package com.privateegy.privatecar.services;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.privateegy.privatecar.Const;
import com.privateegy.privatecar.R;
import com.privateegy.privatecar.activities.DriverHomeActivity;
import com.privateegy.privatecar.models.entities.Config;
import com.privateegy.privatecar.models.entities.PrivateCarLocation;
import com.privateegy.privatecar.models.entities.TripMeterInfo;
import com.privateegy.privatecar.models.entities.User;
import com.privateegy.privatecar.models.responses.GeneralResponse;
import com.privateegy.privatecar.requests.DriverRequests;
import com.privateegy.privatecar.utils.AppUtils;
import com.privateegy.privatecar.utils.RequestHelper;
import com.privateegy.privatecar.utils.RequestListener;
import com.privateegy.privatecar.utils.Utils;

import java.util.ArrayList;

/**
 * Created by basim on 4/3/16.
 */

//TODO: before starting this service, check if the fine location setting is enabled.
public class UpdateDriverLocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, RequestListener {
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequestFine;
    private RequestHelper updateLocationRequest;

    private Location prevLocation; // currently last location
    private Location tripStartLocation; // trip start location

    private boolean tripStarted = false;
    private int tripDuration = 0; // in minutes
    private int tripDistance = 0; // in meters
    private int tripWaitDuration = 0; // in minutes

    private ArrayList<Float> speedsBuffer = new ArrayList<>();
    private Location speedsBufferFirstLocation;

    //create fine location request for getting high accuracy driver location
    private void createFineLocationRequest() {
        locationRequestFine = new LocationRequest();
        String intervalString = AppUtils.getConfigValue(getApplicationContext(), Config.KEY_MAP_REFRESH_RATE);
        int interval = 1000 * (intervalString != null ? Integer.parseInt(intervalString) : Const.LOCATION_UPDATE_DURATION); //in milli sec
        Utils.LogE("interval_____:" + interval);
        locationRequestFine.setInterval(interval);
        locationRequestFine.setFastestInterval(interval);
        locationRequestFine.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void updateLocation(Location location) {
        if (!Utils.hasConnection(this)) {
            //TODO: check if gps disabled
            Utils.showLongToast(this, R.string.no_internet_connection);
            //TODO: start driver home activity if not opened with the message string
            return;
        }

        String provider = location.getProvider();
        float accuracy = location.getAccuracy();
        float speed = location.getSpeed();
        float bearingg = location.getBearing();
        double altitude = location.getAltitude();
        Log.e("____ location: ", String.format("provider:%s accuracy:%f speed:%f bearing:%f altitude:%f", provider, accuracy, speed, bearingg, altitude));


        if (tripStarted && tripStartLocation == null)
            tripStartLocation = location; //in case this is the first received last location


        //update the driver location in the backend
        setDriverBackendLocation(location);

        //send the location via local broadcast manager to activities that needs it
        sendLocationBroadCast(location);

        prevLocation = location;

        //save last driver location in the shared preferences
        Utils.cacheString(this, Const.CACHE_LOCATION, location.getLatitude() + "," + location.getLongitude());
    }

    private void setDriverBackendLocation(Location location) {
        // get cached user & send the request
        User user = AppUtils.getCachedUser(this);

        //cancel the request before making new one
        if (updateLocationRequest != null) updateLocationRequest.cancel(false);

        PrivateCarLocation tmpLocation = new PrivateCarLocation();
        tmpLocation.setLat(location.getLatitude());
        tmpLocation.setLng(location.getLongitude());

        float bearing = location.getBearing();
        String carId = "" + user.getDriverAccountDetails().getDefaultCarId();
        String driverId = "" + user.getDriverAccountDetails().getId();

        updateLocationRequest = DriverRequests.updateLocation(this, this, user.getAccessToken(), tmpLocation.toString(), bearing, carId, driverId);
    }

    private void sendLocationBroadCast(Location location) {
        Intent intent = new Intent(Const.ACTION_DRIVER_SEND_LOCATION);
        intent.putExtra(Const.KEY_LOCATION, location);

        if (tripStarted && prevLocation != null) {
            TripMeterInfo tripMeterInfo = new TripMeterInfo();
            int timeDiff = getTimeDifference(prevLocation, location); // in sec

            //setting trip duration
            tripDuration += timeDiff / 60; // in minutes
            tripMeterInfo.setDuration(tripDuration);

            float speed = location.getSpeed(); // TODO: handle speed at tunnel ways

            //setting trip distance
            tripDistance += speed * timeDiff;
            tripMeterInfo.setDistance(tripDistance);


            //setting trip wait duration
            if (speedsBuffer.isEmpty()) {
                speedsBufferFirstLocation = location;
            }
            speedsBuffer.add(speed);
            if (getTimeDifference(speedsBufferFirstLocation, location) >= 60 && !speedsBuffer.isEmpty()) {
                float speedAvg = getSpeedsAvg(); //in m/s
                String waitingTimeSpeedStr = AppUtils.getConfigValue(this, Config.KEY_WAITING_TIME_SPEED);
                int waitingSpeedKMH = Integer.parseInt(waitingTimeSpeedStr);//in km/h
                float waitingSpeedMS = waitingSpeedKMH * 1000 / 3600;//in m/s
                if (speedAvg <= waitingSpeedMS) {
                    tripWaitDuration++;
                }

                speedsBuffer.clear();
            }
            tripMeterInfo.setWaitDuration(tripWaitDuration);


            Log.e("____ trip info: ", String.format("tripDuration:%d tripDistance:%d tripWaitDuration:%d", tripDuration, tripDistance, tripWaitDuration));


            intent.putExtra(Const.KEY_TRIP_METER_INFO, tripMeterInfo);
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    //return the time difference (in sec) between two locations
    private int getTimeDifference(Location startLocation, Location endLocation) {
        return (int) ((endLocation.getElapsedRealtimeNanos() - startLocation.getElapsedRealtimeNanos()) / 1000000000.0);
    }

    //get average speed in m/sec
    private float getSpeedsAvg() {
        float sum = 0;
        for (float speed : speedsBuffer) {
            sum += speed;
        }

        return sum / speedsBuffer.size();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Utils.LogE("UpdateDriverLocationService onCreate()");

        // Create an instance of GoogleAPIClient.
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        createFineLocationRequest();

        googleApiClient.connect();

        //TODO: start the service as foreground
//        runAsForeground();
    }

    //TODO: handle service recreation
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //handle starting and ending the trip operations
        if (intent != null) {
            String operation = intent.getStringExtra(Const.KEY_SERVICE_OPERATION);
            if (operation != null) {
                switch (operation) {
                    case Const.START_TRIP:

                        tripStarted = true;
                        tripStartLocation = prevLocation;

                        Log.e("___________", "START_TRIP");

                        break;
                    case Const.END_TRIP:
                        updateLocation(prevLocation); //to send broadcast to DriverTrackTheTripActivity before ending the trip to update trip values
                        tripStarted = false;

                        Log.e("___________", "END_TRIP");

                        break;
                }
            }
        }

        return START_STICKY;
//        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //TODO: handle service instance resoration
    //http://stackoverflow.com/questions/19411744/android-when-a-service-is-killed-how-can-we-persist-the-service-state-for-late
    @Override
    public void onDestroy() {
        super.onDestroy();

        googleApiClient.disconnect();
        removeLocationUpdates();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder().addLocationRequest(locationRequestFine);
        PendingResult<LocationSettingsResult> pendingResult =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

        pendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates states = result.getLocationSettingsStates();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location requests here.
//                        getLastLocation();

                        Utils.LogE("UpdateDriverLocationService: LocationSettingsStatusCodes.SUCCESS");

                        requestLocationUpdates();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                        Utils.LogE("UpdateDriverLocationService: LocationSettingsStatusCodes.RESOLUTION_REQUIRED");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                            //TODO: start DriverHome activity with a message to enable fine location settings
                        } catch (Exception e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the settings so we won't show the dialog.
                        break;

                }


            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected void requestLocationUpdates() {
        //TODO: handle api 23 permission
        if (googleApiClient != null && googleApiClient.isConnected())
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequestFine, this);
    }


    protected void removeLocationUpdates() {
        if (googleApiClient != null && googleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        updateLocation(location);
    }


    @Override
    public void onSuccess(Object response, String apiName) {
        if (response instanceof GeneralResponse) {
            GeneralResponse generalResponse = (GeneralResponse) response;
            if (!generalResponse.isSuccess()) {
                Utils.LogE(generalResponse.getValidation().get(0));
            }
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        Utils.LogE(message);
    }

    private void runAsForeground() {
        Intent notificationIntent = new Intent(this, DriverHomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.toolbar_pvt_icon))
//                .setSmallIcon(R.drawable.notification_icon_small)
                .setContentTitle(getString(R.string.private_car))
                .setContentText(getString(R.string.driver_state_active))
                .setContentIntent(pendingIntent).build();

        startForeground(103, notification);
    }
}

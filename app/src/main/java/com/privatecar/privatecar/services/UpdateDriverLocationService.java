package com.privatecar.privatecar.services;

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
import android.support.v7.app.NotificationCompat;

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
import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.activities.DriverHomeActivity;
import com.privatecar.privatecar.models.entities.PrivateCarLocation;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.responses.GeneralResponse;
import com.privatecar.privatecar.requests.DriverRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.RequestHelper;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

/**
 * Created by basim on 4/3/16.
 */

//before starting this service, check if the fine location setting is enabled.
public class UpdateDriverLocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, RequestListener {
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequestFine;
    private RequestHelper updateLocationRequest;

    //create fine location request for getting high accuracy driver location
    private void createFineLocationRequest() {
        locationRequestFine = new LocationRequest();
        locationRequestFine.setInterval(Const.LOCATION_UPDATE_IN_MS);
        locationRequestFine.setFastestInterval(Const.LOCATION_UPDATE_IN_MS);
        locationRequestFine.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void updateLocation(Location location) {
        if (!Utils.hasConnection(this)) {
            //TODO: check if gps disabled
            Utils.showLongToast(this, R.string.no_internet_connection);
            //TODO: start driver home activity if not opened with the message string
            return;
        }

        // get cached user & send the request
        User user = AppUtils.getCachedUser(this);

        //cancel the request before making new one
        if (updateLocationRequest != null) updateLocationRequest.cancel(true);

        PrivateCarLocation tmpLocation = new PrivateCarLocation();
        tmpLocation.setLat(location.getLatitude());
        tmpLocation.setLng(location.getLongitude());

        String carId = "" + user.getDriverAccountDetails().getDefaultCarId();
        String driverId = "" + user.getDriverAccountDetails().getId();

        updateLocationRequest = DriverRequests.updateLocation(this, this, user.getAccessToken(), tmpLocation.toString(), carId, driverId);
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

        //TODO: start the service as foreground
//        runAsForeground();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        googleApiClient.connect();

        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

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

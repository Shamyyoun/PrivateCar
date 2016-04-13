package com.privatecar.privatecar.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.Config;
import com.privatecar.privatecar.models.entities.DriverTripRequest;
import com.privatecar.privatecar.models.entities.Fare;
import com.privatecar.privatecar.models.entities.TripMeterInfo;
import com.privatecar.privatecar.models.enums.PaymentType;
import com.privatecar.privatecar.models.responses.EndTripResponse;
import com.privatecar.privatecar.models.responses.FaresResponse;
import com.privatecar.privatecar.requests.CommonRequests;
import com.privatecar.privatecar.requests.DriverRequests;
import com.privatecar.privatecar.services.UpdateDriverLocationService;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.DialogUtils;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

import java.util.Locale;

public class DriverTrackTheTripActivity extends BaseActivity implements OnMapReadyCallback, RequestListener, View.OnClickListener {

    private TextView tvRideNo, tvRideHM, tvRideKMM, tvRideWaitingHM, tvRideCost;
    private ImageButton ibNavigate;


    SupportMapFragment mapFragment;
    private GoogleMap map;
    private Marker driverMarker, destinationMarker;

    private DriverTripRequest tripRequest;
    float openFare, kmFare, minuteWaitFare; // initialized by getFareInfo()
    int tripDuration; // in min
    int tripWaitDuration; // in min
    int tripDistance; // in meters
    boolean endingTrip = false;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_track_the_trip);

        // get trip request object
        if (savedInstanceState == null)
            tripRequest = (DriverTripRequest) getIntent().getSerializableExtra(Const.KEY_TRIP_REQUEST);
        else
            tripRequest = (DriverTripRequest) savedInstanceState.getSerializable(Const.KEY_TRIP_REQUEST);

        initViews();

        if (savedInstanceState == null)
            getFareInfo();
    }

    private void initViews() {
        tvRideNo = (TextView) findViewById(R.id.tv_ride_no);
        tvRideHM = (TextView) findViewById(R.id.tv_ride_h_m);
        tvRideKMM = (TextView) findViewById(R.id.tv_ride_km_m);
        tvRideWaitingHM = (TextView) findViewById(R.id.tv_ride_waiting_h_m);
        tvRideCost = (TextView) findViewById(R.id.tv_ride_cost);

        tvRideNo.setText(tripRequest.getCode());

        ibNavigate = (ImageButton) findViewById(R.id.ib_navigate);
        ibNavigate.setOnClickListener(this);

        //hide the navigation button if, destination= "guide the driver"
        if (tripRequest.getDestinationLocation() == null)
            ibNavigate.setVisibility(View.GONE);

    }

    private void getFareInfo() {
        String accessToken = AppUtils.getCachedUser(this).getAccessToken();
        CommonRequests.fares(this, this, accessToken, tripRequest.getServiceType(), null);
    }

    private int getActualFare() {
        String minTripFareStr = AppUtils.getConfigValue(this, Config.KEY_MIN_TRIP_FARE);
        float minTripFare = Float.parseFloat(minTripFareStr); //minimum trip fare

        float totalFare = openFare + minuteWaitFare * tripWaitDuration + kmFare * tripDistance / 1000.0f;
        float actualFare = Math.max(totalFare, minTripFare);
        return (int) Math.ceil(actualFare);
    }

    private void endTrip() {
        progressDialog = DialogUtils.showProgressDialog(this, R.string.ending_trip, false);

        int actualFare = getActualFare();
        DriverRequests.endTrip(this, this, AppUtils.getCachedUser(this).getAccessToken()
                , tripRequest.getId(), actualFare, tripDistance / 1000.0f, tripDuration * 60, null);
    }


    //broadcast receiver that receives the driver current location got from UpdateDriverLocationService
    BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(Const.KEY_LOCATION);
            Log.e("_________", location.toString());

            LatLng driverLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            if (driverMarker != null) {
                driverMarker.setPosition(driverLatLng);
            } else { //in case of no driver marker has been drawn because of lack of cached driver location
                driverMarker = map.addMarker(new MarkerOptions()
                        .position(driverLatLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.small_driver_pin)));
            }

            if (intent.getExtras().containsKey(Const.KEY_TRIP_METER_INFO)) {
                TripMeterInfo tripMeterInfo = intent.getParcelableExtra(Const.KEY_TRIP_METER_INFO);

                tripDuration = tripMeterInfo.getDuration();
                tvRideHM.setText(String.format(Locale.ENGLISH, "%02d:%02d", tripDuration / 60, tripDuration % 60));

                //TODO: get these values
                tripWaitDuration = 4;
                tripDistance = 500;
            }

        }
    };

    @Override
    protected void onStart() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        super.onStart();
    }

    @Override
    protected void onResume() {
        IntentFilter intentFilter = new IntentFilter(Const.ACTION_DRIVER_SEND_LOCATION);
        LocalBroadcastManager.getInstance(this).registerReceiver(locationReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationReceiver);
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);
        map.moveCamera(CameraUpdateFactory.zoomTo(16));

        String destinationLocation = tripRequest.getDestinationLocation();
        String driverLocation = Utils.getCachedString(this, Const.CACHE_LOCATION, null);

        if (destinationLocation != null) {
            // add destination marker
            LatLng destinationLatLng = AppUtils.getLatLng(destinationLocation);
            destinationMarker = map.addMarker(new MarkerOptions()
                    .position(destinationLatLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.small_pin)));

            map.moveCamera(CameraUpdateFactory.newLatLng(destinationLatLng));

            if (driverLocation != null) {
                // add driver marker
                LatLng driverLatLng = AppUtils.getLatLng(driverLocation);
                driverMarker = map.addMarker(new MarkerOptions()
                        .position(driverLatLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.small_driver_pin)));

                //add map bounds
                //https://developers.google.com/maps/documentation/android-api/views#setting_boundaries
                LatLng southWest = new LatLng(Math.min(driverLatLng.latitude, destinationLatLng.latitude),
                        Math.min(driverLatLng.longitude, destinationLatLng.longitude));
                LatLng northEast = new LatLng(Math.max(driverLatLng.latitude, destinationLatLng.latitude),
                        Math.max(driverLatLng.longitude, destinationLatLng.longitude));
                final LatLngBounds bounds = new LatLngBounds(southWest, northEast);

                View view = mapFragment.getView();
                if (view != null)
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            //run this after the map gets its width and height
                            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                        }
                    });
            }

        } else if (driverLocation != null) {
            // add driver marker
            LatLng driverLatLng = AppUtils.getLatLng(driverLocation);
            driverMarker = map.addMarker(new MarkerOptions()
                    .position(driverLatLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.small_driver_pin)));

            map.moveCamera(CameraUpdateFactory.newLatLng(driverLatLng));
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Const.KEY_TRIP_REQUEST, tripRequest);
        outState.putFloat(Const.KEY_OPEN_FARE, openFare);
        outState.putFloat(Const.KEY_KM_FARE, kmFare);
        outState.putFloat(Const.KEY_MINUTE_WAIT_FARE, minuteWaitFare);
        outState.putInt(Const.KEY_TRIP_DURATION, tripDuration);
        outState.putInt(Const.KEY_TRIP_WAIT_DURATION, tripWaitDuration);
        outState.putInt(Const.KEY_TRIP_DISTANCE, tripDistance);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        openFare = savedInstanceState.getFloat(Const.KEY_OPEN_FARE);
        kmFare = savedInstanceState.getFloat(Const.KEY_KM_FARE);
        minuteWaitFare = savedInstanceState.getFloat(Const.KEY_MINUTE_WAIT_FARE);
        tripDuration = savedInstanceState.getInt(Const.KEY_TRIP_DURATION);
        tripWaitDuration = savedInstanceState.getInt(Const.KEY_TRIP_WAIT_DURATION);
        tripDistance = savedInstanceState.getInt(Const.KEY_TRIP_DISTANCE);
    }


    @Override
    public void onBackPressed() {
        //Do nothing
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_end_ride:
                //TODO: add confirmation

                //end trip service operations
                Intent intent = new Intent(this, UpdateDriverLocationService.class);
                intent.putExtra(Const.KEY_SERVICE_OPERATION, Const.END_TRIP);
                startService(intent);

                //get updated trip fare info and end the trip
                endingTrip = true;
                getFareInfo();
                break;
            case R.id.ib_navigate:
                //start navigation in google maps app
                //https://developers.google.com/maps/documentation/android-api/intents#launch_turn-by-turn_navigation
                String pkg = "com.google.android.apps.maps";
                if (Utils.isAppInstalledAndEnabled(this, pkg)) {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + tripRequest.getDestinationLocation());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage(pkg);
                    startActivity(mapIntent);
                }
                break;
        }
    }

    @Override
    public synchronized void onSuccess(Object response, String apiName) {
        if (response instanceof FaresResponse) { //get fares
            FaresResponse faresResponse = (FaresResponse) response;
            if (faresResponse.isSuccess()) {
                Fare fare = faresResponse.getFares().get(0);
                openFare = fare.getOpenFare();
                kmFare = fare.getKilometerFare();
                minuteWaitFare = fare.getMinuteWaitFare();

                if (endingTrip) { // End the trip
                    if (tripRequest.getPaymentType().equals(PaymentType.CASH.getValue())) { // if cash payment
                        Intent intent = new Intent(this, DriverCashPaymentActivity.class);
                        intent.putExtra(Const.KEY_TRIP_REQUEST, tripRequest);
                        intent.putExtra(Const.KEY_ACTUAL_FARE, getActualFare());
                        intent.putExtra(Const.KEY_TRIP_DISTANCE, tripDistance);
                        intent.putExtra(Const.KEY_TRIP_DURATION, tripDuration);

                        startActivity(intent);
                        finish();
                    } else {
                        endTrip();
                    }
                }
            }
        } else if (response instanceof EndTripResponse) {
            progressDialog.dismiss();

            EndTripResponse endTripResponse = (EndTripResponse) response;
            if (endTripResponse.isSuccess()) {
                String key = endTripResponse.getContent().getKey();
                if (key.equals(Const.TRIP_PAY_REMAINING)) {
                    /*TODO: show a screen to the driver to take cash with this amount from the customer and a button to confirm this button will call {payremaining} and it will return for driver "Trip Ended".
                     */
                    Utils.showLongToast(this, "TRIP_PAY_REMAINING");
                    finish();
                } else {
                    startActivity(new Intent(this, DriverCreditPaymentActivity.class));
                    finish();
                }
            }
        }
    }

    @Override
    public synchronized void onFail(String message, String apiName) {
        if (progressDialog != null) progressDialog.dismiss();
        Utils.LogE(message);
        Utils.showLongToast(this, message);
    }

}

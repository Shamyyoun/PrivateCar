package com.privatecar.privatecar.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
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
import com.privatecar.privatecar.models.entities.DriverTripRequest;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.Utils;

public class DriverTrackTheTripActivity extends BaseActivity implements OnMapReadyCallback {

    private DriverTripRequest tripRequest;

    private TextView tvRideNo;


    SupportMapFragment mapFragment;
    private GoogleMap map;
    private Marker driverMarker, destinationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_track_the_trip);

        // get trip request object
        if (savedInstanceState != null) {
            tripRequest = (DriverTripRequest) savedInstanceState.getSerializable(Const.KEY_TRIP_REQUEST);
        } else
            tripRequest = (DriverTripRequest) getIntent().getSerializableExtra(Const.KEY_TRIP_REQUEST);

        tvRideNo = (TextView) findViewById(R.id.tv_ride_no);
        tvRideNo.setText(tripRequest.getCode());

        IntentFilter intentFilter = new IntentFilter(Const.ACTION_DRIVER_SEND_LOCATION);
        LocalBroadcastManager.getInstance(this).registerReceiver(locationReceiver, intentFilter);

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
            } else {
                driverMarker = map.addMarker(new MarkerOptions()
                        .position(driverLatLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.small_driver_pin)));
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);
        map.moveCamera(CameraUpdateFactory.zoomTo(16));

// add pickup marker
        LatLng destinationLatLng = AppUtils.getLatLng(tripRequest.getDestinationLocation());
        destinationMarker = map.addMarker(new MarkerOptions()
                .position(destinationLatLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.small_pin)));

        map.moveCamera(CameraUpdateFactory.newLatLng(destinationLatLng));

        // add driver marker
        String driverLocation = Utils.getCachedString(this, Const.CACHE_LOCATION, null);
        LatLng driverLatLng = AppUtils.getLatLng(driverLocation);
        if (driverLocation != null) {
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationReceiver);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tripRequest = (DriverTripRequest) savedInstanceState.getSerializable(Const.KEY_TRIP_REQUEST);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Const.KEY_TRIP_REQUEST, tripRequest);
    }


    @Override
    public void onBackPressed() {
        //Do nothing
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_end_ride:

                break;
        }
    }
}

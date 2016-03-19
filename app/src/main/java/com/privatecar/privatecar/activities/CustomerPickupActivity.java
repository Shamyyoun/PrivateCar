package com.privatecar.privatecar.activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.marshalchen.ultimaterecyclerview.ui.DividerItemDecoration;
import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.adapters.PlacesAdapter;
import com.privatecar.privatecar.models.entities.Config;
import com.privatecar.privatecar.models.entities.NearDriver;
import com.privatecar.privatecar.models.entities.Place;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.responses.NearDriversResponse;
import com.privatecar.privatecar.requests.CustomerRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.ButtonHighlighterOnTouchListener;
import com.privatecar.privatecar.utils.PlayServicesUtils;
import com.privatecar.privatecar.utils.RequestHelper;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CustomerPickupActivity extends BasicBackActivity implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, RequestListener<NearDriversResponse> {
    ImageButton buttonSearch;
    RecyclerView recyclerView;
    PlacesAdapter adapter;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequestCoarse;
    private GoogleMap map;
    private Marker locationMarker;

    private boolean now = true;
    private boolean firstTime = true; //first time only to move map camera

    RequestHelper<NearDriversResponse> requestHelper;
    List<NearDriver> nearDrivers;

    //create coarse location request for updating the heat map
    private void createCoarseLocationRequest() {
        locationRequestCoarse = new LocationRequest();
        String intervalString = AppUtils.getConfigValue(getApplicationContext(), Config.KEY_MAP_REFRESH_RATE);
        int interval = intervalString != null ? Integer.parseInt(intervalString) : 10;
        locationRequestCoarse.setInterval(interval);
        locationRequestCoarse.setFastestInterval(interval);
        locationRequestCoarse.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_pickup);

        if (!PlayServicesUtils.isPlayServicesAvailable(this)) {
            onBackPressed();
            return;
        }

        // Create an instance of GoogleAPIClient.
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        createCoarseLocationRequest();

        now = getIntent().getBooleanExtra(Const.KEY_NOW, true);

        // customize search button
        buttonSearch = (ImageButton) findViewById(R.id.btn_search);
        buttonSearch.setOnTouchListener(new ButtonHighlighterOnTouchListener(this, R.drawable.search_icon));
        buttonSearch.setOnClickListener(this);

        // customize recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // ===== DUMMY DATA =====
        List<Place> places = new ArrayList<>();
        Place myLocationPlace = new Place();
        myLocationPlace.setTitle("My Location");
        myLocationPlace.setAddress("Abbas Elakkad, Nasr City");
        myLocationPlace.setMyLocation(true);
        places.add(myLocationPlace);

        for (int i = 0; i < 15; i++) {
            Place place = new Place();
            place.setTitle("Title " + i);
            place.setAddress("Address " + i);
            place.setTime(5);
            places.add(place);
        }

        adapter = new PlacesAdapter(this, places, R.layout.item_places);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new PlacesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // open verify trip activity
                startActivity(new Intent(CustomerPickupActivity.this, CustomerVerifyTripActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                // TODO
                break;
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        googleApiClient.connect();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onStop() {
        super.onStop();

        googleApiClient.disconnect();

    }

    @Override
    public void onPause() {
        super.onPause();
        removeLocationUpdates();
        Utils.LogE("onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        requestLocationUpdates();
        Utils.LogE("onResume");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Utils.LogE("onMapReady");

        map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);
        map.moveCamera(CameraUpdateFactory.zoomTo(16));
    }


    @Override
    public void onConnected(Bundle bundle) {
//request locationRequestCoarse

        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder().addLocationRequest(locationRequestCoarse);
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
                        getLastLocation();
                        requestLocationUpdates();

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                            status.startResolutionForResult(CustomerPickupActivity.this, Const.REQUEST_COARSE_LOCATION_PERMISSION);

                        } catch (IntentSender.SendIntentException e) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.REQUEST_COARSE_LOCATION_PERMISSION && resultCode == RESULT_OK) {//this request is sent in DriverHomeFragment
            Log.e(Const.LOG_TAG, "resultCode: " + resultCode);
            onConnected(null);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        updateMapLocation(location);
        firstTime = false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void getLastLocation() {
        //TODO: support api 23 permission model
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                Utils.LogE(">>>>> Last Location: " + lastLocation.getLatitude() + ", " + lastLocation.getLongitude());
                updateMapLocation(lastLocation);
            } else {
                Utils.LogE("lastLocation == null");
            }
        }
    }

    protected void requestLocationUpdates() {
        //TODO: handle api 23 permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (googleApiClient != null && googleApiClient.isConnected())
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequestCoarse, this);
        }
    }

    protected void removeLocationUpdates() {
        if (googleApiClient != null && googleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    private void updateMapLocation(Location location) {
        Utils.LogE(location.toString());

        if (map != null) {
            LatLng curLocation = new LatLng(location.getLatitude(), location.getLongitude());

            if (firstTime) {
                map.moveCamera(CameraUpdateFactory.newLatLng(curLocation));
            }

            //get near drivers
            User user = AppUtils.getCachedUser(getApplicationContext());
            if (requestHelper != null)
                requestHelper.cancel(true); //cancel the request before making new one

            requestHelper = CustomerRequests.
                    nearDrivers(this, this, user.getAccessToken(), new com.privatecar.privatecar.models.entities.Location(location));

        }
    }

    @Override
    public void onSuccess(NearDriversResponse response, String apiName) {
        if (response.isSuccess()) {
            nearDrivers = response.getDrivers();
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        Utils.LogE(message);
        Utils.showLongToast(this, message);
    }
}

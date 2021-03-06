package com.privateegy.privatecar.activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
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
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.marshalchen.ultimaterecyclerview.ui.DividerItemDecoration;
import com.privateegy.privatecar.Const;
import com.privateegy.privatecar.R;
import com.privateegy.privatecar.adapters.PlacesAdapter;
import com.privateegy.privatecar.models.entities.Config;
import com.privateegy.privatecar.models.entities.DistanceMatrixElement;
import com.privateegy.privatecar.models.entities.NearDriver;
import com.privateegy.privatecar.models.entities.PrivateCarLocation;
import com.privateegy.privatecar.models.entities.PrivateCarPlace;
import com.privateegy.privatecar.models.entities.User;
import com.privateegy.privatecar.models.enums.CarType;
import com.privateegy.privatecar.models.responses.DistanceMatrixResponse;
import com.privateegy.privatecar.models.responses.NearDriversResponse;
import com.privateegy.privatecar.models.responses.NearbyPlacesResponse;
import com.privateegy.privatecar.requests.CommonRequests;
import com.privateegy.privatecar.requests.CustomerRequests;
import com.privateegy.privatecar.utils.AppUtils;
import com.privateegy.privatecar.utils.ButtonHighlighterOnTouchListener;
import com.privateegy.privatecar.utils.DialogUtils;
import com.privateegy.privatecar.utils.LatLngInterpolator;
import com.privateegy.privatecar.utils.MarkerAnimation;
import com.privateegy.privatecar.utils.PlayServicesUtils;
import com.privateegy.privatecar.utils.RequestHelper;
import com.privateegy.privatecar.utils.RequestListener;
import com.privateegy.privatecar.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomerPickupActivity extends BasicBackActivity implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, RequestListener {

    ImageButton buttonSearch;
    View layoutMap, layoutSearch, layoutMarker;
    TextView tvMarker;
    ProgressBar pbMarker;
    RadioGroup rgTripType;
    RecyclerView rvNearPlaces;
    List<PrivateCarPlace> nearPlaces = new ArrayList<>();
    PlacesAdapter rvNearPlacesAdapter;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequestCoarse;
    private GoogleMap map;
    private Geocoder geocoder;

    GetAddressAsyncTask getAddressAsyncTask;
    private boolean now = true; //now or later

    private boolean firstTime = true; //first time only to move map camera
    RequestHelper<NearDriversResponse> requestNearDrivers;
    List<NearDriver> nearDrivers = new ArrayList<>();
    SparseArray<Marker> sparseMarkersArray = new SparseArray<>(); //Integer(driverId) , Marker

    RequestHelper<NearbyPlacesResponse> requestNearbyPlaces;
    RequestHelper<DistanceMatrixResponse> distanceMatrixRequest;
    private boolean nearestDriverArrivalTimeGot = false; //ensure that request is called only once

    Handler handler = new Handler();
    MapCameraChangeRunnable runnable;

    CarType carType = CarType.ECONOMY;


    //create coarse location request for updating the heat map
    private void createCoarseLocationRequest() {
        locationRequestCoarse = new LocationRequest();
        String intervalString = AppUtils.getConfigValue(getApplicationContext(), Config.KEY_MAP_REFRESH_RATE);
        int interval = 1000 * (intervalString != null ? Integer.parseInt(intervalString) : Const.LOCATION_UPDATE_DURATION); //in milli sec
        locationRequestCoarse.setInterval(interval);
        locationRequestCoarse.setFastestInterval(interval);
        locationRequestCoarse.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    //a runnable to call some methods in this activity when the map camera is changed
    private static class MapCameraChangeRunnable implements Runnable {

        WeakReference<CustomerPickupActivity> activityReference;

        public MapCameraChangeRunnable(CustomerPickupActivity customerPickupActivity) {
            activityReference = new WeakReference<>(customerPickupActivity);
        }

        @Override
        public void run() {
            activityReference.get().getNearDrivers();
            activityReference.get().getStreetName();
            activityReference.get().getNearbyPlaces();
        }
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
                    .addApi(Places.PLACE_DETECTION_API)
                    .addApi(Places.GEO_DATA_API)
                    .build();
        }

        geocoder = new Geocoder(this, Locale.getDefault());
        runnable = new MapCameraChangeRunnable(this);

        createCoarseLocationRequest();

        now = getIntent().getBooleanExtra(Const.KEY_NOW, true);

        // customize search button
        buttonSearch = (ImageButton) findViewById(R.id.btn_search);
        buttonSearch.setOnTouchListener(new ButtonHighlighterOnTouchListener(this, R.drawable.search_icon));
        buttonSearch.setOnClickListener(this);

        layoutSearch = findViewById(R.id.layout_search);
        layoutMap = findViewById(R.id.layout_map);

        layoutMarker = findViewById(R.id.layout_marker);
        tvMarker = (TextView) findViewById(R.id.tv_marker);
        pbMarker = (ProgressBar) findViewById(R.id.pb_marker);

        layoutMarker.setVisibility(View.INVISIBLE); //invisible not gone
        layoutMarker.setOnClickListener(this);

        rgTripType = (RadioGroup) findViewById(R.id.rg_trip_type);
        rgTripType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_economy:
                        carType = CarType.ECONOMY;
                        setMarkerLoading();
                        if (!firstTime) getNearDrivers();
                        break;
                    case R.id.rb_business:
                        carType = CarType.BUSINESS;
                        setMarkerLoading();
                        if (!firstTime) getNearDrivers();
                        break;
                    case R.id.rb_full_day:
                        setResult(RESULT_OK);
                        onBackPressed();
                        break;
                }
            }
        });

        // customize recycler view
        rvNearPlaces = (RecyclerView) findViewById(R.id.recycler_view);
        rvNearPlaces.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvNearPlaces.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        rvNearPlaces.setHasFixedSize(true);
        rvNearPlaces.setItemAnimator(new DefaultItemAnimator());

        rvNearPlacesAdapter = new PlacesAdapter(this, nearPlaces, R.layout.item_places);
        rvNearPlaces.setAdapter(rvNearPlacesAdapter);
        rvNearPlacesAdapter.setOnItemClickListener(new PlacesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!now || sparseMarkersArray.size() > 0) { // if near drivers found or it's a later trip request
                    // open verify trip activity
                    Intent intent = new Intent(CustomerPickupActivity.this, CustomerVerifyTripActivity.class);
                    intent.putExtra(Const.KEY_NOW, now);
                    intent.putExtra(Const.KEY_PICKUP_PLACE, nearPlaces.get(position));
                    intent.putExtra(Const.KEY_CAR_TYPE, carType);
                    startActivity(intent);
                } else {
                    DialogUtils.showAlertDialog(CustomerPickupActivity.this, R.string.no_drivers_found_now, null);
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(Const.KEY_NOW, now);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        now = savedInstanceState.getBoolean(Const.KEY_NOW);
    }

    /**
     * Hides the textView in the marker and show the progressbar
     */
    private void setMarkerLoading() {
        tvMarker.setVisibility(View.INVISIBLE);
        pbMarker.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the marker's progressbar and set the texView with the given value
     *
     * @param value The value to set
     */
    private void setMarkerText(String value) {
        pbMarker.setVisibility(View.INVISIBLE);
        tvMarker.setVisibility(View.VISIBLE);
        tvMarker.setText(value);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                try {
//                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);
                    startActivityForResult(intent, Const.REQUEST_PLACE_AUTOCOMPLETE);
                } catch (GooglePlayServicesRepairableException e) {
                } catch (GooglePlayServicesNotAvailableException e) {
                }
                break;
            case R.id.layout_marker:
                if (!now || sparseMarkersArray.size() > 0) { // if near drivers found or it's a later trip request
                    // open verify trip activity
                    Intent intent = new Intent(CustomerPickupActivity.this, CustomerVerifyTripActivity.class);

                    if (nearPlaces.size() > 0 && nearPlaces.get(0).isMarkerLocation()) { //first item is the current marker location
                        intent.putExtra(Const.KEY_PICKUP_PLACE, nearPlaces.get(0));
                        intent.putExtra(Const.KEY_NOW, now);
                        intent.putExtra(Const.KEY_CAR_TYPE, carType);
                        startActivity(intent);
                    } else {
                        Utils.showLongToast(getApplicationContext(), R.string.please_wait);
                        return;
                    }
                } else {
                    DialogUtils.showAlertDialog(CustomerPickupActivity.this, R.string.no_drivers_found_now, null);
                }
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
        if (requestCode == Const.REQUEST_COARSE_LOCATION_PERMISSION && resultCode == RESULT_OK) {
            Log.e(Const.LOG_TAG, "resultCode: " + resultCode);
            onConnected(null);
        } else if (requestCode == Const.REQUEST_PLACE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                map.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                Log.e("_____", "Place: " + place.getName() + ", " + place.getAddress() + ", " + place.getLatLng().toString());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i("____", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        updateMapLocation(location);
        getNearDrivers();
        firstTime = false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void getLastLocation() {
        //TODO: support api 23 permission model
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //enable my location layer
            map.setMyLocationEnabled(true);

            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                Utils.LogE(">>>>> Last Location: " + lastLocation.getLatitude() + ", " + lastLocation.getLongitude());
                updateMapLocation(lastLocation);
            } else {
                Utils.LogE("lastLocation == null");
            }
        }
    }

    private void requestLocationUpdates() {
        //TODO: handle api 23 permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (googleApiClient != null && googleApiClient.isConnected())
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequestCoarse, this);
        }
    }

    private void removeLocationUpdates() {
        if (googleApiClient != null && googleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    private void getStreetName() {
        Log.e("_______", "getStreetName");

        if (getAddressAsyncTask != null && !getAddressAsyncTask.isCancelled())
            getAddressAsyncTask.cancel(false);

        LatLng latLng = map.getCameraPosition().target;
        getAddressAsyncTask = new GetAddressAsyncTask(geocoder, latLng, this);
        Utils.executeAsyncTask(getAddressAsyncTask);
    }

    private synchronized void updateMapLocation(Location location) {
        if (map != null) {
            LatLng curLocation = new LatLng(location.getLatitude(), location.getLongitude());

            if (firstTime) {
                map.moveCamera(CameraUpdateFactory.newLatLng(curLocation));

                //add the layout marker - set its middle bottom as the anchor point
                Point point = map.getProjection().toScreenLocation(curLocation);
                layoutMarker.setVisibility(View.VISIBLE);
                layoutMarker.setX(point.x - layoutMarker.getWidth() / 2.0f);
                layoutMarker.setY(point.y - layoutMarker.getHeight());

                map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(final CameraPosition cameraPosition) {
                        if (handler != null)
                            handler.removeCallbacks(runnable);
                        Utils.LogE("__onCameraChange" + cameraPosition.target.toString());
                        //run this after the map settles (after about 2 sec)
                        handler.postDelayed(runnable, 2000);
                        nearestDriverArrivalTimeGot = false; //reset to call nearest driver arrival time request
                        setMarkerLoading();
                        nearPlaces.clear();
                        rvNearPlacesAdapter.notifyDataSetChanged();
                    }
                });

            }

        }
    }

    //async task to get the address by the geocoder
    private static class GetAddressAsyncTask extends AsyncTask<Void, Void, Void> {
        Geocoder geocoder;
        LatLng latLng;
        String name;
        String address;

        WeakReference<CustomerPickupActivity> activityReference;

        public GetAddressAsyncTask(Geocoder geocoder, LatLng latLng, CustomerPickupActivity customerPickupActivity) {
            this.geocoder = geocoder;
            this.latLng = latLng;
            activityReference = new WeakReference<>(customerPickupActivity);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);
                    name = address.getAddressLine(0);
                    StringBuilder strAddress = new StringBuilder();
                    for (int i = 1; i < address.getMaxAddressLineIndex(); i++) {
                        if (i != 1) strAddress.append(", ");
                        strAddress.append(address.getAddressLine(i));
                    }

                    this.address = strAddress.toString();

                    Utils.LogE("Address: " + name + ", " + this.address);
                } else {
                    Utils.LogE("No address returned");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (activityReference != null) {
                if (Utils.isEmpty(name)) {
                    name = activityReference.get().getString(R.string.location_on_map);
                    address = null;
                }

                activityReference.get().updateRVMarkerLocation(latLng, name, address);
            }
        }
    }

    // update the first item in the recyclerView to set it to the marker position
    public void updateRVMarkerLocation(LatLng latLng, String name, String address) {

        PrivateCarPlace place = new PrivateCarPlace();
        place.setName(name);
        place.setAddress(address);
        place.setLocation(new PrivateCarLocation(latLng));
        place.setMarkerLocation(true);

        if (nearPlaces.size() > 0) {
            if (nearPlaces.get(0).isMarkerLocation()) { //first item is the marker location place
                nearPlaces.set(0, place);
                rvNearPlacesAdapter.notifyItemChanged(0);
            } else { // add it as the first item
                nearPlaces.add(0, place);
                rvNearPlacesAdapter.notifyItemInserted(0);
            }

            rvNearPlaces.smoothScrollToPosition(0);
        } else {
            nearPlaces.add(place);
            rvNearPlacesAdapter.notifyItemInserted(0);
        }
    }

    //get near drivers
    private RequestHelper<NearDriversResponse> getNearDrivers() {
        if (requestNearDrivers != null)
            requestNearDrivers.cancel(false); //cancel the request before making new one

        User user = AppUtils.getCachedUser(getApplicationContext());
        LatLng latLng = map.getCameraPosition().target;
        requestNearDrivers = CustomerRequests.
                nearDrivers(this, this, user.getAccessToken(), new PrivateCarLocation(latLng), carType.getValue());

        return requestNearDrivers;
    }

    private void getNearbyPlaces() {
        LatLng latLng = map.getCameraPosition().target;

        if (requestNearbyPlaces != null) requestNearbyPlaces.cancel(false);
        requestNearbyPlaces = CommonRequests.getNearbyPlacesByPlacesApi(this, this, latLng);
    }

    //get the nearest driver to the marker position
    private NearDriver getNearestDriver(List<NearDriver> nearDrivers) {
        LatLng currLocation = map.getCameraPosition().target; //marker position

        NearDriver nearestDriver = null;
        float nearestDriverDistance = Float.MAX_VALUE;
        float[] results = new float[3];

        for (NearDriver nearDriver : nearDrivers) {
            LatLng driverLocation = AppUtils.getLatLng(nearDriver.getLastLocation());
            Location.distanceBetween(currLocation.latitude, currLocation.longitude,
                    driverLocation.latitude, driverLocation.longitude, results);

            if (results.length > 0 && results[0] < nearestDriverDistance) {
                nearestDriverDistance = results[0];
                nearestDriver = nearDriver;
            }
        }

        return nearestDriver != null ? nearestDriver : nearDrivers.get(0);
    }

    private void getNearestDriverArrivalTime(NearDriver nearestDriver) {
        LatLng latLng = map.getCameraPosition().target;

        if (distanceMatrixRequest != null) distanceMatrixRequest.cancel(false);
        distanceMatrixRequest = CommonRequests.getTravelTimeByDistanceMatrixApi(this, this, nearestDriver.getLastLocation(), latLng);
    }

    @Override
    public synchronized void onSuccess(Object response, String apiName) {
        if (response instanceof NearDriversResponse) { //near drivers
            NearDriversResponse nearDriversResponse = (NearDriversResponse) response;
            if (nearDriversResponse.isSuccess()) {
                nearDrivers.clear();
                nearDrivers.addAll(nearDriversResponse.getDrivers());

                if (nearDrivers.size() == 0) { //no near drivers found
                    Utils.showLongToast(getApplicationContext(), R.string.no_driver);
                    setMarkerText(getString(R.string.question_mark_without_space));
                } else if (!nearestDriverArrivalTimeGot) { //near drivers found
                    getNearestDriverArrivalTime(getNearestDriver(nearDrivers));
                }

                if (nearDrivers.size() == 0) {
                    map.clear(); // clear all markers
                    sparseMarkersArray.clear(); // clear the map
                } else {
                    SparseArray<Marker> tmpSparseMarkersArray = new SparseArray<>();

                    for (NearDriver nearDriver : nearDrivers) {
                        int id = nearDriver.getId();
                        String location = nearDriver.getLastLocation();
                        double lat = Float.parseFloat(location.split(",")[0]);
                        double lng = Float.parseFloat(location.split(",")[1]);
                        LatLng nearDriverLatLng = new LatLng(lat, lng);
                        float bearing = nearDriver.getBearing();

                        if (sparseMarkersArray.indexOfKey(id) >= 0) { // id found in the array
                            // animate the marker in the array with the new location of the near driver then add it to the tmpSparseArray
                            Marker marker = sparseMarkersArray.get(id);
                            MarkerAnimation.animateMarkerToICSWithBearing(marker, nearDriverLatLng, bearing, new LatLngInterpolator.LinearFixed());

                            tmpSparseMarkersArray.append(id, marker);
                            sparseMarkersArray.remove(id);

                        } else {
                            // add a new marker to the map and add it to the tmpSparseMarkersArray

                            Marker marker = map.addMarker(new MarkerOptions()
                                    .position(nearDriverLatLng)
                                    .rotation(bearing)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon_top_view))
                                    .anchor(0.5f, 0.5f)
                                    .flat(true));

                            tmpSparseMarkersArray.append(id, marker);
                        }
                    }

                    for (int i = 0; i < sparseMarkersArray.size(); i++) { //remove all remaining markers from the map
                        Marker marker = sparseMarkersArray.valueAt(i);
                        marker.remove();
                        sparseMarkersArray.removeAt(i);
                    }

                    sparseMarkersArray = tmpSparseMarkersArray;
                }
            }
        } else if (response instanceof NearbyPlacesResponse) { //nearby places api
            NearbyPlacesResponse nearbyPlacesResponse = (NearbyPlacesResponse) response;
            if (nearbyPlacesResponse.isOk()) {
                nearPlaces.addAll(nearbyPlacesResponse.getPrivateCarPlaces());
                rvNearPlacesAdapter.notifyDataSetChanged();
            }
        } else if (response instanceof DistanceMatrixResponse) { //getting nearest driver arrival time
            nearestDriverArrivalTimeGot = true;
            DistanceMatrixResponse distanceMatrixResponse = (DistanceMatrixResponse) response;
            if (distanceMatrixResponse.isOk()) {
                DistanceMatrixElement element = distanceMatrixResponse.getRows().get(0).getElements().get(0);
                if (element.isOk()) {
                    int duration = element.getDuration().getValue(); // in seconds
                    int durationInMin = (int) Math.ceil(duration / 60.0);
                    setMarkerText(durationInMin + "\n" + getString(R.string.min));
                } else {
                    Utils.showLongToast(getApplicationContext(), R.string.could_not_get_time);
                    setMarkerText(getString(R.string.question_mark_without_space));
                }
            } else {
                Utils.showLongToast(getApplicationContext(), R.string.could_not_get_time);
                setMarkerText(getString(R.string.question_mark_without_space));
            }
        }
    }

    @Override
    public synchronized void onFail(String message, String apiName) {
        setMarkerText(getString(R.string.question_mark_without_space));
        Utils.LogE(message);
        Utils.showLongToast(this, message);
    }

}

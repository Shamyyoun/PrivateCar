package com.privatecar.privatecar.activities;

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
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.marshalchen.ultimaterecyclerview.ui.DividerItemDecoration;
import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.adapters.PlacesAdapter;
import com.privatecar.privatecar.models.entities.Config;
import com.privatecar.privatecar.models.entities.DistanceMatrixElement;
import com.privatecar.privatecar.models.entities.NearDriver;
import com.privatecar.privatecar.models.entities.PrivateCarLocation;
import com.privatecar.privatecar.models.entities.PrivateCarPlace;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.responses.DistanceMatrixResponse;
import com.privatecar.privatecar.models.responses.NearDriversResponse;
import com.privatecar.privatecar.models.responses.NearbyPlacesResponse;
import com.privatecar.privatecar.requests.CustomerRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.ButtonHighlighterOnTouchListener;
import com.privatecar.privatecar.utils.PlayServicesUtils;
import com.privatecar.privatecar.utils.RequestHelper;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomerPickupActivity extends BasicBackActivity implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, RequestListener {

    ImageButton buttonSearch;
    View layoutMap, layoutSearch, layoutMarker;
    TextView tvMarker;
    ProgressBar pbMarker;
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
    RequestHelper<NearbyPlacesResponse> requestNearbyPlaces;
    RequestHelper<DistanceMatrixResponse> distanceMatrixRequest;
    private boolean nearestDriverArrivalTimeGot = false; //ensure that request is called only once

    Handler handler = new Handler();
    MapCameraChangeRunnable runnable;


    //create coarse location request for updating the heat map
    private void createCoarseLocationRequest() {
        locationRequestCoarse = new LocationRequest();
        String intervalString = AppUtils.getConfigValue(getApplicationContext(), Config.KEY_MAP_REFRESH_RATE);
        int interval = intervalString != null ? Integer.parseInt(intervalString) : 10;
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
            activityReference.get().getStreetName();
            activityReference.get().getNearbyPlaces();
            activityReference.get().getNearDrivers();
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
                PrivateCarPlace place = nearPlaces.get(position);

                if (place.isMarkerLocation()) {
                    // open verify trip activity
                    Intent intent = new Intent(CustomerPickupActivity.this, CustomerVerifyTripActivity.class);
                    intent.putExtra(Const.KEY_NOW, now);
                    intent.putExtra(Const.KEY_PICKUP_PLACE, nearPlaces.get(position));
                    startActivity(intent);
                } else {
                    LatLng latLng = new LatLng(place.getLocation().getLat(), place.getLocation().getLng());
                    map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }
        });
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
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
//                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);
                    startActivityForResult(intent, Const.REQUEST_PLACE_AUTOCOMPLETE);
                } catch (GooglePlayServicesRepairableException e) {
                } catch (GooglePlayServicesNotAvailableException e) {
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

//                map.addCircle(new CircleOptions().center(curLocation).fillColor(0xf00).radius(3));

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

    private static class GetAddressAsyncTask extends AsyncTask<Void, Void, Void> {
        Geocoder geocoder;
        LatLng latLng;
        String name;
        String description;

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
                    StringBuilder strDescription = new StringBuilder();
                    for (int i = 1; i < address.getMaxAddressLineIndex(); i++) {
                        if (i != 1) strDescription.append(", ");
                        strDescription.append(address.getAddressLine(i));
                    }

                    description = strDescription.toString();

                    Utils.LogE("Address: " + name + ", " + description);
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

            if (activityReference != null && !Utils.isEmpty(name)) {
                activityReference.get().updateRVMarkerLocation(latLng, name, description);
            }
        }
    }

    // update the first item in the recyclerView to set it to the marker position
    public void updateRVMarkerLocation(LatLng latLng, String name, String description) {

        PrivateCarPlace place = new PrivateCarPlace();
        place.setName(name);
        place.setAddress(description);
        place.setLocation(new PrivateCarLocation(latLng));
        place.setMarkerLocation(true);

        if (nearPlaces.size() > 0) {
            if (nearPlaces.get(0).isMarkerLocation()) {
                nearPlaces.set(0, place);
                rvNearPlacesAdapter.notifyItemChanged(0);
            } else {
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
                nearDrivers(this, this, user.getAccessToken(), new PrivateCarLocation(latLng));

        return requestNearDrivers;
    }

    private RequestHelper<NearbyPlacesResponse> getNearbyPlaces() {
        LatLng latLng = map.getCameraPosition().target;
        int radiusInMeters = 1000; //1000 meters
        String language = Utils.getAppLanguage();
        String serverApiKey = getString(R.string.server_api_key);

        String url = String.format(Locale.ENGLISH, "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%f,%f&radius=%d&language=%s&key=%s", latLng.latitude, latLng.longitude, radiusInMeters, language, serverApiKey);

        if (requestNearbyPlaces != null) requestNearbyPlaces.cancel(false);
        requestNearbyPlaces = new RequestHelper<>(this, "", url, NearbyPlacesResponse.class, this);
        requestNearbyPlaces.executeFormUrlEncoded();
        return requestNearbyPlaces;
    }

    private void getNearestDriverArrivalTime(NearDriver nearestDriver) {

        LatLng latLng = map.getCameraPosition().target;
        String language = Utils.getAppLanguage();
        String serverApiKey = getString(R.string.server_api_key);

        String url = String.format(Locale.ENGLISH, "https://maps.googleapis.com/maps/api/distancematrix/json?origins=%s&destinations=%f,%f&language=%s&key=%s", nearestDriver.getLastLocation(), latLng.latitude, latLng.longitude, language, serverApiKey);
        if (distanceMatrixRequest != null) distanceMatrixRequest.cancel(false);
        distanceMatrixRequest = new RequestHelper<>(this, "", url, DistanceMatrixResponse.class, this);
        distanceMatrixRequest.executeFormUrlEncoded();

    }

    @Override
    public synchronized void onSuccess(Object response, String apiName) {
        if (response instanceof NearDriversResponse) { //near drivers
            NearDriversResponse nearDriversResponse = (NearDriversResponse) response;
            if (nearDriversResponse.isSuccess()) {
                nearDrivers.clear();
                nearDrivers.addAll(nearDriversResponse.getDrivers());
                //TODO: animate near drivers markers
                if (nearDrivers.size() == 0) { //no near drivers found
                    Utils.showLongToast(getApplicationContext(), R.string.no_driver);
                    setMarkerText(getString(R.string.question_mark));
                } else if (!nearestDriverArrivalTimeGot) { //near drivers found
                    getNearestDriverArrivalTime(nearDrivers.get(0));
                }
            }
        } else if (response instanceof NearbyPlacesResponse) { //nearby places api
            NearbyPlacesResponse nearbyPlacesResponse = (NearbyPlacesResponse) response;
            if (nearbyPlacesResponse.isOk()) {
                nearPlaces.addAll(nearbyPlacesResponse.getPrivateCarPlaces());
                rvNearPlacesAdapter.notifyDataSetChanged();
            }
        } else if (response instanceof DistanceMatrixResponse) {
            nearestDriverArrivalTimeGot = true;
            DistanceMatrixResponse distanceMatrixResponse = (DistanceMatrixResponse) response;
            if (distanceMatrixResponse.isOk()) {
                DistanceMatrixElement element = distanceMatrixResponse.getRows().get(0).getElements().get(0);
                if (element.isOk()) {
                    int duration = element.getDuration().getValue(); // in seconds
                    int durationInMin = duration / 60 + 1;
                    setMarkerText(durationInMin + "\n" + getString(R.string.min));
                } else {
                    Utils.showLongToast(getApplicationContext(), R.string.could_not_get_time);
                    setMarkerText(getString(R.string.question_mark));
                }
            } else {
                Utils.showLongToast(getApplicationContext(), R.string.could_not_get_time);
                setMarkerText(getString(R.string.question_mark));
            }
        }
    }

    @Override
    public synchronized void onFail(String message, String apiName) {
        Utils.LogE(message);
        Utils.showLongToast(this, message);
    }

}

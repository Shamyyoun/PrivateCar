package com.privatecar.privatecar.activities;

import android.Manifest;
import android.animation.LayoutTransition;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.Config;
import com.privatecar.privatecar.models.entities.PrivateCarLocation;
import com.privatecar.privatecar.models.entities.PrivateCarPlace;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.PlayServicesUtils;
import com.privatecar.privatecar.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

public class CustomerAddDropOffActivity extends BasicBackActivity implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Button btnSelectOnMap, btnGuideTheCaptain;
    View layoutMap;
    View layoutMarker;
    TextView tvMarker;
    ProgressBar pbMarker;

    PrivateCarPlace privateCarPlace;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequestCoarse;
    private GoogleMap map;
    private Geocoder geocoder;

    GetAddressAsyncTask getAddressAsyncTask;

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
        setContentView(R.layout.activity_customer_add_dropoff);

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

        geocoder = new Geocoder(this, Locale.getDefault());

        createCoarseLocationRequest();

        SupportPlaceAutocompleteFragment autocompleteFragment = (SupportPlaceAutocompleteFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.e("_____", "Place: " + place.getName() + ", " + place.getAddress() + ", " + place.getLatLng().toString());

                privateCarPlace = new PrivateCarPlace();
                privateCarPlace.setName(place.getName().toString());
                privateCarPlace.setAddress(place.getAddress().toString());
                privateCarPlace.setLocation(new PrivateCarLocation(place.getLatLng()));

                Intent intent = new Intent();
                intent.putExtra(Const.KEY_DROP_OFF_PLACE, privateCarPlace);
                setResult(RESULT_OK, intent);

                onBackPressed();
            }

            @Override
            public void onError(Status status) {
                Log.e("_____", "An error occurred: " + status);
            }
        });

        btnSelectOnMap = (Button) findViewById(R.id.btn_select_on_map);
        btnSelectOnMap.setOnClickListener(this);
        btnGuideTheCaptain = (Button) findViewById(R.id.btn_guide_the_captain);
        btnGuideTheCaptain.setOnClickListener(this);
        layoutMap = findViewById(R.id.layout_map);
        layoutMap.post(new Runnable() {
            @Override
            public void run() {
                //add the layout marker - set its middle bottom as the anchor point
                int centerX = layoutMap.getWidth() / 2;
                int centerY = layoutMap.getHeight() / 2;

                layoutMarker.setX(centerX - layoutMarker.getWidth() / 2);
                layoutMarker.setY(centerY - layoutMarker.getHeight());

                layoutMap.setVisibility(View.GONE);

                //set transition to the root view of this activity
                ((LinearLayout) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0)).setLayoutTransition(new LayoutTransition());
            }
        });

        layoutMarker = findViewById(R.id.layout_marker);
        layoutMarker.setOnClickListener(this);
        tvMarker = (TextView) findViewById(R.id.tv_marker);
        pbMarker = (ProgressBar) findViewById(R.id.pb_marker);
        setMarkerNotLoading();

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_select_on_map:
                if (layoutMap.getVisibility() == View.VISIBLE) {
                    layoutMap.setVisibility(View.GONE);
                } else {
                    layoutMap.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_guide_the_captain:
                setResult(RESULT_OK, new Intent());
                onBackPressed();
                break;
            case R.id.layout_marker:
                if (privateCarPlace != null) {
                    Intent intent = new Intent();
                    intent.putExtra(Const.KEY_DROP_OFF_PLACE, privateCarPlace);
                    setResult(RESULT_OK, intent);

                    onBackPressed();
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
    public void onMapReady(GoogleMap googleMap) {
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
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                            status.startResolutionForResult(CustomerAddDropOffActivity.this, Const.REQUEST_COARSE_LOCATION_PERMISSION);

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
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.REQUEST_COARSE_LOCATION_PERMISSION && resultCode == RESULT_OK) {
            Log.e(Const.LOG_TAG, "resultCode: " + resultCode);
            onConnected(null);
        }
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

    private synchronized void updateMapLocation(Location location) {
        if (map != null) {
            LatLng curLocation = new LatLng(location.getLatitude(), location.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLng(curLocation));

            map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(final CameraPosition cameraPosition) {
                    privateCarPlace = null; //location has changed so null the place
                    setMarkerLoading();
                    getStreetName();
                }
            });

        }
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
     */
    private void setMarkerNotLoading() {
        tvMarker.setVisibility(View.VISIBLE);
        pbMarker.setVisibility(View.GONE);
    }


    //async task to get the address by the geocoder
    private static class GetAddressAsyncTask extends AsyncTask<Void, Void, Void> {
        Geocoder geocoder;
        LatLng latLng;
        String name;
        String address;

        WeakReference<CustomerAddDropOffActivity> activityReference;

        public GetAddressAsyncTask(Geocoder geocoder, LatLng latLng, CustomerAddDropOffActivity activity) {
            this.geocoder = geocoder;
            this.latLng = latLng;
            activityReference = new WeakReference<>(activity);
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

            if (activityReference != null && !Utils.isEmpty(name)) {
                activityReference.get().privateCarPlace = new PrivateCarPlace();
                activityReference.get().privateCarPlace.setName(name);
                activityReference.get().privateCarPlace.setAddress(address);
                activityReference.get().privateCarPlace.setLocation(new PrivateCarLocation(latLng));
            }
            if (activityReference != null)
                activityReference.get().setMarkerNotLoading();
        }
    }


    private void getStreetName() {
        Log.e("_______", "getStreetName");

        if (getAddressAsyncTask != null && !getAddressAsyncTask.isCancelled())
            getAddressAsyncTask.cancel(false);

        LatLng latLng = map.getCameraPosition().target;
        getAddressAsyncTask = new GetAddressAsyncTask(geocoder, latLng, this);
        Utils.executeAsyncTask(getAddressAsyncTask);
    }


    //TODO: handle when the user click back (show confirmation dialog)


}

package com.privatecar.privatecar.fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.activities.DriverHomeActivity;
import com.privatecar.privatecar.dialogs.GpsOptionDialog;
import com.privatecar.privatecar.models.entities.Config;
import com.privatecar.privatecar.models.entities.DriverAccountDetails;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.responses.DriverAccountDetailsResponse;
import com.privatecar.privatecar.models.responses.LocationsResponse;
import com.privatecar.privatecar.requests.DriverRequests;
import com.privatecar.privatecar.services.UpdateDriverLocationService;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.ButtonHighlighterOnTouchListener;
import com.privatecar.privatecar.utils.DialogUtils;
import com.privatecar.privatecar.utils.PlayServicesUtils;
import com.privatecar.privatecar.utils.RequestHelper;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DriverHomeFragment extends BaseFragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener, RequestListener {
    private DriverHomeActivity activity;
    private Button btnBeActive;
    private TextView tvMessage;
    private TextView tvDate;
    private TextView tvTodayProfit;
    private TextView tvTotalTrips;
    private TextView tvTotalHours;

    private GpsOptionDialog gpsOptionDialog;
    private ProgressDialog progressDialog;

    private GoogleMap map;
    private Marker locationMarker;
    private HeatmapTileProvider provider;
    private TileOverlay overlay;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequestCoarse, locationRequestFine;
    private List<LatLng> heatmapData;

    private RequestHelper customersStatsRequest;


    //create coarse location request for updating the heat map
    private void createCoarseLocationRequest() {
        locationRequestCoarse = new LocationRequest();
        String intervalString = AppUtils.getConfigValue(getActivity(), Config.KEY_MAP_REFRESH_RATE);
        int interval = intervalString != null ? Integer.parseInt(intervalString) : 10;
        locationRequestCoarse.setInterval(interval);
        locationRequestCoarse.setFastestInterval(interval);
        locationRequestCoarse.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    //create fine location request for getting high accuracy driver location (for updatelocation message)
    private void createFineLocationRequest() {
        locationRequestFine = new LocationRequest();
        locationRequestFine.setInterval(Const.LOCATION_UPDATE_IN_MS);
        locationRequestFine.setFastestInterval(Const.LOCATION_UPDATE_IN_MS);
        locationRequestFine.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public DriverHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (DriverHomeActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!PlayServicesUtils.isPlayServicesAvailable(getActivity())) {
            getActivity().onBackPressed();
            return;
        }

        // Create an instance of GoogleAPIClient.
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        createCoarseLocationRequest();
        createFineLocationRequest();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_driver_home, container, false);

        // init views
        btnBeActive = (Button) fragment.findViewById(R.id.btn_be_active);
        if (Utils.isServiceRunning(getContext(), UpdateDriverLocationService.class)) {
            btnBeActive.setText(R.string.be_inactive);
        } else {
            btnBeActive.setText(R.string.be_active);
        }
        btnBeActive.setOnTouchListener(new ButtonHighlighterOnTouchListener(getActivity(), R.drawable.petroleum_bottom_rounded_corners_shape));
        btnBeActive.setOnClickListener(this);
        tvMessage = (TextView) fragment.findViewById(R.id.tv_message);
        tvDate = (TextView) fragment.findViewById(R.id.tv_date);
        tvTodayProfit = (TextView) fragment.findViewById(R.id.tv_today_profit);
        tvTotalTrips = (TextView) fragment.findViewById(R.id.tv_total_trips);
        tvTotalHours = (TextView) fragment.findViewById(R.id.tv_total_hours);

        // update today's date
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        tvDate.setText(dateFormat.format(calendar.getTime()));

        // load account details
        loadAccountDetails();

        return fragment;
    }

    /**
     * method, used to update UI and set new values
     */
    private void updateUI(DriverAccountDetails detailsDriverAccountDetails) {
        tvTodayProfit.setText(detailsDriverAccountDetails.getCredit() + " " + getString(R.string.currency));
        tvTotalTrips.setText(detailsDriverAccountDetails.getTotaltrips() + " " + getString(R.string.trips));
        tvTotalHours.setText(detailsDriverAccountDetails.getTodayhours() + " " + getString(R.string.hours));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_be_active:
                if (Utils.isServiceRunning(getContext(), UpdateDriverLocationService.class)) {
                    beActive(false);
                } else {
                    beActive(true);
                }
                break;
        }
    }

    /**
     * method, used to fetch driver's account details from server
     */
    private void loadAccountDetails() {
        // show progress dialog
        progressDialog = DialogUtils.showProgressDialog(activity, R.string.loading_please_wait);

        // get cached user & send the request
        User user = AppUtils.getCachedUser(activity);
        DriverRequests.accountDetails(activity, this, user.getAccessToken());
    }

    /**
     * method, used to get customers lat,lng values from server to display the heatmap
     */
    private void getCustomersStats(Location location) {

        // get cached user & send the request
        User user = AppUtils.getCachedUser(activity);
        //cancel the request before making new one
        if (customersStatsRequest != null) customersStatsRequest.cancel(true);

        com.privatecar.privatecar.models.entities.Location tmpLocation = new com.privatecar.privatecar.models.entities.Location();
        tmpLocation.setLat(location.getLatitude());
        tmpLocation.setLng(location.getLongitude());

        customersStatsRequest = DriverRequests.getCustomersStats(activity, this, user.getAccessToken(), tmpLocation.toString(), Const.HEATMAP_RADIUS);

    }


    /**
     * method, used to validate gps & send be active request to server
     */
    public void beActive(boolean active) {

        if (!active) {
            Intent intent = new Intent(getContext(), UpdateDriverLocationService.class);
            getActivity().stopService(intent);
            btnBeActive.setText(R.string.be_active);
            return;
        }

        // check internet connection
        if (!Utils.hasConnection(activity)) {
            Utils.showShortToast(activity, R.string.no_internet_connection);
            return;
        }

//        // check gps if he wanna be active
//        if (active && !Utils.isGpsEnabled(activity)) {
//            // show enable gps dialog
//            gpsOptionDialog = new GpsOptionDialog(this);
//            gpsOptionDialog.show();
//
//            return;
//        }


        if (!googleApiClient.isConnected()) return;


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

                        Intent intent = new Intent(getActivity(), UpdateDriverLocationService.class);
                        getActivity().startService(intent);
                        btnBeActive.setText(R.string.be_inactive);

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                            status.startResolutionForResult(getActivity(), Const.REQUEST_FINE_LOCATION_PERMISSION); //DriverHomeActivity receives the result
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                }


            }
        });
    }

    @Override
    public void onSuccess(Object response, String apiName) {
        // dismiss progress dialog
        progressDialog.dismiss();

        // check request type
        if (response instanceof DriverAccountDetailsResponse) {
            // account details response
            DriverAccountDetailsResponse detailsResponse = (DriverAccountDetailsResponse) response;

            // check status
            if (detailsResponse.getAccountDetails() != null) {
                // response is valid
                // update ui in the fragment
                updateUI(detailsResponse.getAccountDetails());

                // update personal info in the navigation drawer
                activity.updatePersonalInfo(detailsResponse.getAccountDetails());

                // update cached user
                User user = AppUtils.getCachedUser(activity);
                user.setAccountDetails(detailsResponse.getAccountDetails());
                AppUtils.cacheUser(activity, user);
            } else {
                // invalid response
                // show error toast & exit
                Utils.showLongToast(activity, R.string.unexpected_error_try_again);
                activity.onBackPressed();
            }
        } else if (response instanceof LocationsResponse) {
            LocationsResponse locationsResponse = (LocationsResponse) response;
            if (locationsResponse.isSuccess() && locationsResponse.getContent() != null) {
                ArrayList<com.privatecar.privatecar.models.entities.Location> locations = locationsResponse.getContent();

                boolean firstResponse = heatmapData == null; // to addHeatmap() or updateHeatMap()
                if (firstResponse) {
                    heatmapData = new ArrayList<>();
                } else {
                    heatmapData.clear();
                }

                for (int i = 0; i < locations.size(); i++) {
                    heatmapData.add(new LatLng(locations.get(i).getLat(), locations.get(i).getLng()));
                }

                if (firstResponse) {
                    addHeatMap();
                } else {
                    updateHeatMap();
                }
            }

        }
    }

    @Override
    public void onFail(String message, String apiName) {
        // dismiss progress dialog
        progressDialog.dismiss();

        // check api name
        if (apiName.equals(Const.MESSAGE_DRIVER_ACCOUNT_DETAILS)) {
            // show error toast & exits
            Utils.showLongToast(activity, message);
            Log.e(Const.LOG_TAG, message);
            activity.onBackPressed();
        } else if (apiName.equals(Const.MESSAGE_DRIVER_GET_CUSTOMERS_STATS)) {
            Utils.showLongToast(activity, message);
            Log.e(Const.LOG_TAG, message);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Const.REQUEST_GPS_SETTINGS) {
            gpsOptionDialog.dismiss();
            beActive(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Utils.LogE("onStart");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Utils.LogE("onMapReady");

        map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);
        map.moveCamera(CameraUpdateFactory.zoomTo(19));
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    @Override
    public void onStop() {
        super.onStop();

        googleApiClient.disconnect();

        Utils.LogE("onStop");
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
    public void onConnected(Bundle bundle) {
        Utils.LogE("onConnected");

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
                            status.startResolutionForResult(getActivity(), Const.REQUEST_COARSE_LOCATION_PERMISSION); //DriverHomeActivity receives the result
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
        Utils.LogE("onConnectionSuspended");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Utils.LogE("onConnectionFailed");
    }

    @Override
    public void onLocationChanged(Location location) {
        Utils.LogE(">>>>> Location: " + location.getLatitude() + ", " + location.getLongitude());
        updateMapLocation(location);
        getCustomersStats(location);
    }

    private void getLastLocation() {
        //TODO: support api 23 permission model
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (googleApiClient != null && googleApiClient.isConnected())
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequestCoarse, this);
        }
    }


    protected void removeLocationUpdates() {
        if (googleApiClient != null && googleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    private void updateMapLocation(Location location) {
        if (map != null) {
            // Add a marker in Sydney, Australia, and move the camera.
            LatLng markerPosition = new LatLng(location.getLatitude(), location.getLongitude());
            if (locationMarker == null)
                locationMarker = map.addMarker(new MarkerOptions().position(markerPosition).title("current location"));
            else
                locationMarker.setPosition(markerPosition);
            map.moveCamera(CameraUpdateFactory.newLatLng(markerPosition));
        }
    }

    private void addHeatMap() {
        //https://developers.google.com/maps/documentation/android-api/utility/heatmap#custom

//        heatmapData = new ArrayList<>();
//        heatmapData.add(new LatLng(-37.1886, 145.708));
//        heatmapData.add(new LatLng(-37.8361, 144.845));
//        heatmapData.add(new LatLng(-38.4034, 144.192));
//        heatmapData.add(new LatLng(-38.7597, 143.67));
//        heatmapData.add(new LatLng(-36.9672, 141.083));
//        heatmapData.add(new LatLng(-37.2843, 142.927));
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(heatmapData.get(0), 5));

        if (heatmapData.size() < 1) return;

        // Create the gradient.
        int[] colors = {
                ContextCompat.getColor(getActivity(), R.color.low_crowd),
                ContextCompat.getColor(getActivity(), R.color.medium_crowd),
                ContextCompat.getColor(getActivity(), R.color.high_crowd)
        };

        float[] startPoints = {
                0.2f,
                0.5f,
                0.9f
        };

        Gradient gradient = new Gradient(colors, startPoints);

        // Create a heat map tile provider, passing it the latlngs

        provider = new HeatmapTileProvider.Builder()
                .data(heatmapData)
                .gradient(gradient)
                .build();

        // Add a tile overlay to the map, using the heat map tile provider.
        overlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(provider));

    }

    private void updateHeatMap() {
        if (provider != null && overlay != null && heatmapData.size() > 0) {
            provider.setData(heatmapData);
            overlay.clearTileCache();
        }
    }

}

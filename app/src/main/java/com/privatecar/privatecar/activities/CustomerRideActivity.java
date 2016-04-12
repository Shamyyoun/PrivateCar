package com.privatecar.privatecar.activities;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.privatecar.privatecar.models.entities.Car;
import com.privatecar.privatecar.models.entities.Config;
import com.privatecar.privatecar.models.entities.CustomerTripRequest;
import com.privatecar.privatecar.models.entities.DistanceMatrixElement;
import com.privatecar.privatecar.models.entities.TripInfo;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.responses.DistanceMatrixResponse;
import com.privatecar.privatecar.models.responses.DriverLocationResponse;
import com.privatecar.privatecar.models.responses.GeneralResponse;
import com.privatecar.privatecar.models.responses.LocationResponse;
import com.privatecar.privatecar.requests.CommonRequests;
import com.privatecar.privatecar.requests.CustomerRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.DialogUtils;
import com.privatecar.privatecar.utils.LatLngInterpolator;
import com.privatecar.privatecar.utils.MarkerAnimation;
import com.privatecar.privatecar.utils.PermissionUtil;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.SavePrefs;
import com.privatecar.privatecar.utils.Utils;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

public class CustomerRideActivity extends BaseActivity implements RequestListener<Object>, OnMapReadyCallback {
    public static CustomerRideActivity currentInstance;
    private CustomerRideActivity activity;
    private TripInfo tripInfo;
    private CustomerTripRequest tripRequest;

    private TextView tvRideNo;
    private ImageView ivDriverImage;
    private TextView tvMessage;
    private TextView tvDriverName;
    private TextView tvCarName;
    private RatingBar ratingBar;
    private TextView tvAvg;
    private ImageButton btnCall;
    private ImageButton btnCancel;

    SupportMapFragment mapFragment;

    private GoogleMap map;
    private Marker driverMarker, pickUpMarker;

    private int mapRefreshRate; //in MS

    Handler driverLocationRequestHandler = new Handler();
    DriverLocationRequestRunnable driverLocationRequestRunnable;

    //a runnable used request driver location
    private static class DriverLocationRequestRunnable implements Runnable {
        WeakReference<CustomerRideActivity> activityWeakReference;

        DriverLocationRequestRunnable(CustomerRideActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void run() {
            CustomerRideActivity activity = activityWeakReference.get();
            String accessToken = AppUtils.getCachedUser(activity).getAccessToken();
            int tripId = activity.tripRequest.getId();
            int requestRate = activity.mapRefreshRate;

            CustomerRequests.tripDriverLocation(activity, activity, accessToken, tripId);
            activity.driverLocationRequestHandler.postDelayed(this, requestRate);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_ride);

        // get activity reference
        activity = this;
        currentInstance = this;

        //get the refresh rate (driver location request rate)
        String strMapRefreshRate = AppUtils.getConfigValue(this, Config.KEY_MAP_REFRESH_RATE);
        mapRefreshRate = strMapRefreshRate != null ? Integer.parseInt(strMapRefreshRate) * 1000 : 10000;

        // get main objects
        tripInfo = (TripInfo) getIntent().getSerializableExtra(Const.KEY_TRIP_INFO);
        SavePrefs<CustomerTripRequest> savePrefs = new SavePrefs<>(this, CustomerTripRequest.class);
        tripRequest = savePrefs.load(Const.CACHE_LAST_TRIP_REQUEST);

        // init views
        tvRideNo = (TextView) findViewById(R.id.tv_ride_no);
        ivDriverImage = (ImageView) findViewById(R.id.iv_driver_image);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        tvDriverName = (TextView) findViewById(R.id.tv_driver_name);
        tvCarName = (TextView) findViewById(R.id.tv_car_name);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        tvAvg = (TextView) findViewById(R.id.tv_avg);
        btnCall = (ImageButton) findViewById(R.id.btn_call);
        btnCancel = (ImageButton) findViewById(R.id.btn_cancel);

        // update the ui
        tvRideNo.setText(tripRequest.getCode());
        tvDriverName.setText(tripInfo.getFullname());
        Car car = tripInfo.getCars().get(0);
        tvCarName.setText(car.getBrand() + " " + car.getModel());
        tvAvg.setText("" + tripInfo.getOverallRating());
        ratingBar.setRating(tripInfo.getOverallRating());

        // load the driver image
        String driverImageUrl = Const.IMAGES_BASE_URL + tripInfo.getPersonalPhoto();
        Picasso.with(this).load(driverImageUrl).placeholder(R.drawable.def_user_photo)
                .error(R.drawable.def_user_photo).into(ivDriverImage);

        // load the driver location from the server
        loadDriverLocation();

        // add click listeners
        btnCall.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        // wake up the device
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();
    }

    /**
     * method, used to load driver location from the server
     */
    private void loadDriverLocation() {
        if (!Utils.hasConnection(this)) {
            return;
        }

        // show progress dialog
        progressDialog = DialogUtils.showProgressDialog(this, R.string.loading_please_wait);

        // create and send the request
        User user = AppUtils.getCachedUser(this);
        CommonRequests.driverTripLocation(this, this, user.getAccessToken(), tripRequest.getId());
    }

    @Override
    public void onStart() {
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
        LatLng pickupLatLng = AppUtils.getLatLng(tripRequest.getPickupLocation());
        pickUpMarker = map.addMarker(new MarkerOptions()
                .position(pickupLatLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.small_pin)));

        map.moveCamera(CameraUpdateFactory.newLatLng(pickupLatLng));

        // add driver marker
        String driverLocation = tripInfo.getLastLocation();
        if (driverLocation != null) {
            LatLng driverLatLng = AppUtils.getLatLng(driverLocation);
            driverMarker = map.addMarker(new MarkerOptions()
                    .position(driverLatLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.small_driver_pin)));

            //add map bounds
            //https://developers.google.com/maps/documentation/android-api/views#setting_boundaries
            LatLng southWest = new LatLng(Math.min(driverLatLng.latitude, pickupLatLng.latitude),
                    Math.min(driverLatLng.longitude, pickupLatLng.longitude));
            LatLng northEast = new LatLng(Math.max(driverLatLng.latitude, pickupLatLng.latitude),
                    Math.max(driverLatLng.longitude, pickupLatLng.longitude));
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


        driverLocationRequestRunnable = new DriverLocationRequestRunnable(this);
        driverLocationRequestHandler.postDelayed(driverLocationRequestRunnable, mapRefreshRate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_call:
                // check call permission
                if (PermissionUtil.isGranted(this, Manifest.permission.CALL_PHONE)) {
                    // show call dialog
                    DialogUtils.showCallDialog(this, tripInfo.getMobile());
                } else {
                    // not granted
                    // request the permission
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, Const.PERM_REQ_CALL);
                }
                break;

            case R.id.btn_cancel:
                // cancel trip
                onCancel();
                break;
        }
    }

    /**
     * method, used to show confirm dialog then cancel the trip
     */
    private void onCancel() {
        // check internet connection
        if (!Utils.hasConnection(this)) {
            // show error msg
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        // show confirm dialog
        DialogUtils.showConfirmDialog(this, R.string.cancel_the_trip, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // show progress dialog
                progressDialog = DialogUtils.showProgressDialog(activity, R.string.cancelling_trip_please_wait);

                // create and send the request
                User user = AppUtils.getCachedUser(activity);
                CustomerRequests.cancelTrip(activity, activity, user.getAccessToken(), 52); // TODO
            }
        }, null);
    }

    @Override
    public synchronized void onSuccess(Object response, String apiName) {
        if (response instanceof GeneralResponse) {
            // dismiss the progress dialog
            progressDialog.dismiss();

            // parse the response
            GeneralResponse generalResponse = (GeneralResponse) response;

            // check the response
            if (generalResponse.isSuccess()) {
                // show success msg and finish the activity
                Utils.showLongToast(this, R.string.trip_cancelled_successfully);
                finish();
            } else {
                // prepare error msg
                String errorMsg = "";
                for (int i = 0; i < generalResponse.getValidation().size(); i++) {
                    if (i != 0) {
                        errorMsg += "\n";
                    }

                    errorMsg += generalResponse.getValidation().get(i);
                }

                if (errorMsg.isEmpty()) {
                    errorMsg = getString(R.string.error_cancelling_trip_try_again);
                }

                // show error msg
                Utils.showLongToast(this, errorMsg);
            }

        } else if (response instanceof LocationResponse) {
            LocationResponse locationResponse = (LocationResponse) response;
            if (locationResponse.isSuccess()) {
                if (locationResponse.getContent() != null) {
                    LatLng location = AppUtils.getLatLng(locationResponse.getContent());
                    MarkerAnimation.animateMarkerToICS(driverMarker, location, new LatLngInterpolator.LinearFixed());
                }
            } else {
                Utils.showLongToast(this, locationResponse.getValidation());
            }
        } else if (response instanceof DriverLocationResponse) {
            // this was the driver trip location request
            // cast the response
            DriverLocationResponse locationResponse = (DriverLocationResponse) response;

            // check the response
            if (!Utils.isNullOrEmpty(locationResponse.getContent())) {
                // create and send the time and distance estimation request from google
                String[] latLngStrs = tripRequest.getPickupLocation().split(",");
                LatLng destination = new LatLng(Utils.convertToDouble(latLngStrs[0]), Utils.convertToDouble(latLngStrs[1]));
                CommonRequests.getTravelTimeByDistanceMatrixApi(this, this, locationResponse.getContent(), destination);
            } else {
                // error
                // dismiss the progress dialog
                progressDialog.dismiss();
            }
        } else if (response instanceof DistanceMatrixResponse) {
            // this was the estimation request
            // dismiss the progress dialog
            progressDialog.dismiss();

            // cast the response
            DistanceMatrixResponse estimationResponse = (DistanceMatrixResponse) response;

            // check response
            if (estimationResponse.isOk()) {
                // get element and check it
                DistanceMatrixElement element = estimationResponse.getRows().get(0).getElements().get(0);
                if (element.isOk()) {
                    // get and format duration
                    float duration = element.getDuration().getValue(); // in seconds
                    duration = duration < 60 ? 1 : (duration / 60f); // in minutes
                    String durationStr = String.format("%.0f", duration);

                    // format distance then dormat the final message
                    float distance = element.getDistance().getValue(); // in meters
                    String driverMessage = null;
                    if (distance >= 1000) {
                        // more than one kilo, then convert to kilo meters
                        distance = distance / 1000f; // in kilo meters
                        String distanceStr = String.format("%.0f", distance);
                        driverMessage = String.format(getString(R.string.ride_driver_message_km), distanceStr, durationStr);
                    } else {
                        // less than one kilo, keep it in meters
                        String distanceStr = String.format("%.0f", distance);
                        driverMessage = String.format(getString(R.string.ride_driver_message_m), distanceStr, durationStr);
                    }

                    // update the driver message
                    tvMessage.setText(driverMessage);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        //TODO: remove this deprecated code
        //TODO: make the activity unlock the screen

        // release screen lock
        KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();

        // remove the callbacks
        driverLocationRequestHandler.removeCallbacks(driverLocationRequestRunnable);

        // null the current instance
        currentInstance = null;

        super.onDestroy();
    }

    @Override
    public void onFail(String message, String apiName) {
        // dismiss the progress dialog & show error msg
        if (progressDialog != null)
            progressDialog.dismiss();
        Utils.showLongToast(this, message);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Const.PERM_REQ_CALL:
                // check if granted
                if (PermissionUtil.isAllGranted(grantResults)) {
                    // granted
                    // show call dialog
                    DialogUtils.showCallDialog(this, tripInfo.getMobile());
                } else {
                    // show msg
                    Utils.showShortToast(this, R.string.we_need_call_permission_to_call_your_driver);
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

package com.privatecar.privatecar.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.privatecar.privatecar.models.entities.Config;
import com.privatecar.privatecar.models.entities.DriverAccountDetails;
import com.privatecar.privatecar.models.entities.DriverTripRequest;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.enums.PaymentType;
import com.privatecar.privatecar.models.responses.GeneralResponse;
import com.privatecar.privatecar.requests.DriverRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.DialogUtils;
import com.privatecar.privatecar.utils.PermissionUtil;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

public class DriverTripInfoActivity extends BaseActivity implements RequestListener<GeneralResponse>, OnMapReadyCallback {
    private DriverTripRequest tripRequest;

    private ImageView ivDefUserImage;
    private ImageView ivUserImage;
    private TextView tvRideNo;
    private TextView tvClientName;
    private TextView tvPaymentType;
    private RatingBar ratingBar;
    private TextView tvRating;
    private Button btnStartTrip;
    private ImageButton ibCall, ibCancel;
    private ImageButton ibNavigate;

    SupportMapFragment mapFragment;

    private GoogleMap map;
    private Marker driverMarker, pickUpMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_trip_info);

        // get trip request object
        if (savedInstanceState != null) {
            tripRequest = (DriverTripRequest) savedInstanceState.getSerializable(Const.KEY_TRIP_REQUEST);
        } else
            tripRequest = (DriverTripRequest) getIntent().getSerializableExtra(Const.KEY_TRIP_REQUEST);

        // init views
        ivDefUserImage = (ImageView) findViewById(R.id.iv_user_def_image);
        ivUserImage = (ImageView) findViewById(R.id.iv_user_image);
        tvRideNo = (TextView) findViewById(R.id.tv_ride_no);
        tvClientName = (TextView) findViewById(R.id.tv_client_name);
        tvPaymentType = (TextView) findViewById(R.id.tv_payment_type);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        tvRating = (TextView) findViewById(R.id.tv_rating);
        btnStartTrip = (Button) findViewById(R.id.btn_start_trip);
        ibCall = (ImageButton) findViewById(R.id.ib_call);
        ibCancel = (ImageButton) findViewById(R.id.ib_cancel);
        ibNavigate = (ImageButton) findViewById(R.id.ib_navigate);

        // render data to the UI
        tvRideNo.setText("" + tripRequest.getCode());
        tvClientName.setText(tripRequest.getCustomer());
        tvPaymentType.setText(tripRequest.getPaymentType().equals(PaymentType.CASH.getValue()) ?
                R.string.cash : R.string.account_credit);
        tvRating.setText(tripRequest.getCustomerRating() + " " + getString(R.string.avg));
        ratingBar.setRating(tripRequest.getCustomerRating());

        // load user image if possible
        String userImage = AppUtils.getConfigValue(getApplicationContext(), Config.KEY_BASE_URL) + File.separator + tripRequest.getCustomerImage();
        Picasso.with(this).load(userImage).into(ivUserImage, new Callback() {
            @Override
            public void onSuccess() {
                // hide default image
                ivDefUserImage.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
            }
        });

        // add click listeners
        btnStartTrip.setOnClickListener(this);
        ibCall.setOnClickListener(this);
        ibCancel.setOnClickListener(this);
        ibNavigate.setOnClickListener(this);

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_trip:
                startTrip();
                break;

            case R.id.ib_cancel:
                showCancelDialog();
                break;

            case R.id.ib_call:
                // check call permission
                if (PermissionUtil.isGranted(this, Manifest.permission.CALL_PHONE)) {
                    // show call dialog
                    showCallDialog();
                } else {
                    // not granted
                    // request the permission
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, Const.PERM_REQ_CALL);
                }
                break;

            case R.id.ib_navigate:
                //start navigation in google maps app
                //https://developers.google.com/maps/documentation/android-api/intents#launch_turn-by-turn_navigation
                String pkg = "com.google.android.apps.maps";
                if (Utils.isAppInstalledAndEnabled(this, pkg)) {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + tripRequest.getPickLocation());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage(pkg);
                    startActivity(mapIntent);
                }

                break;
            default:
                super.onClick(v);
        }
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
        LatLng pickupLatLng = AppUtils.getLatLng(tripRequest.getPickLocation());
        pickUpMarker = map.addMarker(new MarkerOptions()
                .position(pickupLatLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.small_pin)));

        map.moveCamera(CameraUpdateFactory.newLatLng(pickupLatLng));

        // add driver marker
        String driverLocation = Utils.getCachedString(this, Const.CACHE_LOCATION, null);
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
    }

    /**
     * method, used to send start trip request to  the server
     */
    private void startTrip() {
        // check internet connection
        if (!Utils.hasConnection(this)) {
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        // show loading
        progressDialog = DialogUtils.showProgressDialog(this, R.string.starting_trip_please_wait);

        // create & send the request
        User user = AppUtils.getCachedUser(this);
        DriverAccountDetails accountDetails = user.getDriverAccountDetails();
        DriverRequests.startTrip(this, this, user.getAccessToken(), "" + accountDetails.getId(), "" + tripRequest.getId(),
                "" + accountDetails.getDefaultCarId());
    }

    /**
     * method, used to show yes / no dialog to cancel the trip
     */
    private void showCancelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.cancel_trip_q));
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelTrip();
            }
        });
        builder.setNegativeButton(R.string.no, null);
        builder.show();
    }

    /**
     * method, used to send cancel trip request to  the server
     */
    private void cancelTrip() {
        // check internet connection
        if (!Utils.hasConnection(this)) {
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        // show loading
        progressDialog = DialogUtils.showProgressDialog(this, R.string.cancelling_trip_please_wait);

        // create & send the request
        User user = AppUtils.getCachedUser(this);
        DriverAccountDetails accountDetails = user.getDriverAccountDetails();
        DriverRequests.cancelTrip(this, this, user.getAccessToken(), "" + accountDetails.getId(), "" + tripRequest.getId(),
                "" + accountDetails.getDefaultCarId());
    }

    @Override
    public void onSuccess(GeneralResponse response, String apiName) {
        // dismiss progress
        progressDialog.dismiss();

        // check api name
        if (apiName.equals(Const.MESSAGE_DRIVER_START_TRIP)) {
            // goto driver track trip activity & finish
            Intent intent = new Intent(this, DriverTrackTheTripActivity.class);
            intent.putExtra(Const.KEY_TRIP_REQUEST, tripRequest);
            startActivity(intent);
            finish();
        } else if (apiName.equals(Const.MESSAGE_DRIVER_CANCEL_TRIP)) {
            // show toast & finish
            Utils.showLongToast(this, R.string.trip_cancelled_successfully);
            finish();
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        // dismiss progress & show error toast
        progressDialog.dismiss();
        Utils.showLongToast(this, R.string.connection_error);
    }

    /**
     * method, used to show call dialog
     */
    private void showCallDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.call) + " " + tripRequest.getCustomer() + getString(R.string.question_mark));
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tripRequest.getMobile()));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.no, null);
        builder.show();
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Const.PERM_REQ_CALL:
                // check if granted
                if (PermissionUtil.isAllGranted(grantResults)) {
                    // granted
                    // show call dialog
                    showCallDialog();
                } else {
                    // show msg
                    Utils.showShortToast(this, R.string.we_need_call_permission_to_call_customer_service);
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // check if cance the trip
        boolean cancelTrip = intent.getBooleanExtra(Const.KEY_CANCEL_TRIP, false);
        if (cancelTrip) {
            // show message & finish
            Utils.showLongToast(this, R.string.driver_cancel_trip_message);
            finish();
        }

        super.onNewIntent(intent);
    }
}

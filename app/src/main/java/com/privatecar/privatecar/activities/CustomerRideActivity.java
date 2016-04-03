package com.privatecar.privatecar.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.Car;
import com.privatecar.privatecar.models.entities.CustomerTripRequest;
import com.privatecar.privatecar.models.entities.TripInfo;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.responses.GeneralResponse;
import com.privatecar.privatecar.requests.CustomerRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.DialogUtils;
import com.privatecar.privatecar.utils.PermissionUtil;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.SavePrefs;
import com.privatecar.privatecar.utils.Utils;
import com.squareup.picasso.Picasso;

public class CustomerRideActivity extends BasicBackActivity implements RequestListener<Object> {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_ride);

        // get activity reference
        activity = this;

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
//        tvMessage.setText(tripInfo.get); TODO set the message
        tvDriverName.setText(tripInfo.getFullname());
        Car car = tripInfo.getCars().get(0);
        tvCarName.setText(car.getBrand() + " " + car.getModel());
        tvAvg.setText("" + tripInfo.getOverallRating());
        ratingBar.setRating(tripInfo.getOverallRating());

        // load the driver image
        String driverImageUrl = Const.IMAGES_BASE_URL + tripInfo.getPersonalPhoto();
        Picasso.with(this).load(driverImageUrl).placeholder(R.drawable.def_user_photo)
                .error(R.drawable.def_user_photo).into(ivDriverImage);

        // add click listeners
        btnCall.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
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
    public void onSuccess(Object response, String apiName) {
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
    }

    @Override
    public void onFail(String message, String apiName) {
        // dismiss the progress dialog & show error msg
        progressDialog.dismiss();
        Utils.showLongToast(this, R.string.connection_error);
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

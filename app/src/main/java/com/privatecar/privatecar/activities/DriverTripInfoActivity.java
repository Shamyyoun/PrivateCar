package com.privatecar.privatecar.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.TripRequest;
import com.privatecar.privatecar.models.enums.PaymentType;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class DriverTripInfoActivity extends BaseActivity {
    private TripRequest tripRequest;
    private ImageView ivDefUserImage;
    private ImageView ivUserImage;
    private TextView tvRideNo;
    private TextView tvClientName;
    private TextView tvPaymentType;
    private RatingBar ratingBar;
    private TextView tvRating;
    private Button btnStartTrip;
    private ImageButton ibCall, ibCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_trip_info);

        // get trip request object
        tripRequest = (TripRequest) getIntent().getSerializableExtra(Const.KEY_TRIP_REQUEST);

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

        // render data to the UI
        tvRideNo.setText("" + tripRequest.getCode());
        tvClientName.setText(tripRequest.getCustomer());
        tvPaymentType.setText(tripRequest.getPaymentType().equals(PaymentType.CASH.getValue()) ?
                R.string.cash : R.string.account_credit);
        tvRating.setText(tripRequest.getCustomerRating() + " " + getString(R.string.avg));
        ratingBar.setRating(tripRequest.getCustomerRating());

        // load user image if possible
        String userImage = tripRequest.getCustomerImage();
        if (userImage != null && !userImage.isEmpty()) {
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
        }

        // add click listeners
        btnStartTrip.setOnClickListener(this);
        ibCall.setOnClickListener(this);
        ibCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_trip:
                break;

            case R.id.btn_call:
                break;

            case R.id.btn_cancel:
                break;

            default:
                super.onClick(v);
        }
    }
}

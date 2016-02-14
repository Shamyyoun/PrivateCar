package com.privatecar.privatecar.activities;

import android.os.Bundle;
import android.widget.Button;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.utils.ButtonHighlighterOnTouchListener;

public class TripRequestActivity extends BaseActivity {

    Button btnAccept, btnDecline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_request);


        btnAccept = (Button) findViewById(R.id.btn_accept);
        btnAccept.setOnTouchListener(new ButtonHighlighterOnTouchListener(this, R.drawable.greenish_start_rounded_corners_shape));


        btnDecline = (Button) findViewById(R.id.btn_decline);
        btnDecline.setOnTouchListener(new ButtonHighlighterOnTouchListener(this, R.drawable.reddish_end_rounded_corners_shape));


    }
}

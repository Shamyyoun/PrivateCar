package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.adapters.PlacesAdapter;
import com.privatecar.privatecar.models.entities.Place;
import com.privatecar.privatecar.utils.ButtonHighlighterOnTouchListener;
import com.privatecar.privatecar.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class CustomerRideActivity extends BasicBackActivity implements View.OnClickListener {
    ImageButton buttonCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_ride);

        // customize call button
        buttonCall = (ImageButton) findViewById(R.id.btn_call);
        buttonCall.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_call:
                // open cash payment activity
                startActivity(new Intent(this, CustomerCashPaymentActivity.class));
                break;
        }
    }
}

package com.privatecar.privatecar.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.activities.CustomerPickupActivity;

public class BookALiftFragment extends BaseFragment implements OnMapReadyCallback, View.OnClickListener {

    Activity activity;
    TextView tvUserName, tvUserID;
    View layoutPickNow;

    public BookALiftFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_alift, container, false);

        tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
        tvUserID = (TextView) view.findViewById(R.id.tv_user_id);
        layoutPickNow = view.findViewById(R.id.layout_pick_now);

        // add click listeners
        layoutPickNow.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        super.onStart();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap mMap = googleMap;
        mMap.getUiSettings().setAllGesturesEnabled(false);

//        // Add a marker in Sydney, Australia, and move the camera.
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_pick_now:
                // open pick up activity
                startActivity(new Intent(activity, CustomerPickupActivity.class));
                break;
        }
    }
}

package com.privatecar.privatecar.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.utils.ButtonHighlighterOnTouchListener;

public class DriverHomeFragment extends BaseFragment implements OnMapReadyCallback {

    Button btnBeActive;

    public DriverHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_driver_home, container, false);


        btnBeActive = (Button) fragment.findViewById(R.id.btn_be_active);
        btnBeActive.setOnTouchListener(new ButtonHighlighterOnTouchListener(getActivity(), R.drawable.petroleum_bottom_rounded_corners_shape));


        return fragment;
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
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

}

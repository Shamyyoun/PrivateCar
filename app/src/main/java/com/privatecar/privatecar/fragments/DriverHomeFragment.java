package com.privatecar.privatecar.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.dialogs.GpsOptionDialog;
import com.privatecar.privatecar.utils.ButtonHighlighterOnTouchListener;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

public class DriverHomeFragment extends BaseFragment implements OnMapReadyCallback, View.OnClickListener, RequestListener {
    private Activity activity;
    private Button btnBeActive;

    private GpsOptionDialog gpsOptionDialog;
    private ProgressDialog progressDialog;

    public DriverHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_driver_home, container, false);

        btnBeActive = (Button) fragment.findViewById(R.id.btn_be_active);
        btnBeActive.setOnTouchListener(new ButtonHighlighterOnTouchListener(getActivity(), R.drawable.petroleum_bottom_rounded_corners_shape));
        btnBeActive.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_be_active:
                beActive(true);
                break;
        }
    }

    /**
     * method, used to validate gps & send be active request to server
     */
    private void beActive(boolean active) {
        // check internet connection
        if (!Utils.hasConnection(activity)) {
            Utils.showShortToast(activity, R.string.no_internet_connection);
            return;
        }

        // check gps if he wanna be active
        if (active && !Utils.isGpsEnabled(activity)) {
            // show enable gps dialog
            gpsOptionDialog = new GpsOptionDialog(this);
            gpsOptionDialog.show();

            return;
        }

        // TODO send goonline request
    }

    @Override
    public void onSuccess(Object response, String apiName) {

    }

    @Override
    public void onFail(String message) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check request code
        if (requestCode == Const.REQ_GPS_SETTINGS) {
            gpsOptionDialog.dismiss();
            beActive(true);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

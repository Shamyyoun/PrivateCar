package com.privatecar.privatecar.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.privatecar.privatecar.R;


public class DriverRatingsFragment extends BaseFragment {

    public DriverRatingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_driver_ratings, container, false);


        return fragment;
    }

}

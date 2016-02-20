package com.privatecar.privatecar.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.privatecar.privatecar.R;

public class CustomerPricesFragment extends BaseFragment {
    public static final String TAG = CustomerPricesFragment.class.getName();

    Activity activity;
    View rootView;

    public CustomerPricesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_customer_prices, container, false);
        }

        return rootView;
    }
}

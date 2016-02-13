package com.privatecar.privatecar.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.privatecar.privatecar.R;

public class DriverStatementFragment extends BaseFragment {


    Button btnSearch;

    public DriverStatementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_driver_statement, container, false);


        btnSearch = (Button) fragment.findViewById(R.id.btn_search);

        return fragment;
    }

}

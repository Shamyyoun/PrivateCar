package com.privateegy.privatecar.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by basim on 12/2/16.
 */
public class BaseFragment extends Fragment implements View.OnClickListener {
    protected ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onClick(View v) {
    }
}

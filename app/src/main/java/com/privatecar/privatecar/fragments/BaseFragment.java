package com.privatecar.privatecar.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by basim on 12/2/16.
 */
public class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }
}

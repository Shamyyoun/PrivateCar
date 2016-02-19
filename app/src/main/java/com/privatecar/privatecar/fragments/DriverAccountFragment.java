package com.privatecar.privatecar.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.utils.Utils;

public class DriverAccountFragment extends BaseFragment {


    public DriverAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_driver_account, container, false);

        Button btnChangeCar = (Button) fragment.findViewById(R.id.btn_change_car);
        Button btnChangeType = (Button) fragment.findViewById(R.id.btn_change_type);

        btnChangeCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangeCarDialog();
            }
        });

        btnChangeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangeCarType();
            }
        });


        return fragment;
    }

    private void openChangeCarDialog() {
        final String[] cars = {"AAA-123 (Business)", "ABS-23 (Economy)"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_car)
                .setItems(cars, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.showToast(getActivity(), cars[which]);
                    }
                });
        builder.show();
    }

    private void openChangeCarType() {
        final String[] types = {getString(R.string.economy), getString(R.string.business), getString(R.string.both_economy_business)};


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_car_type)
                .setItems(types, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.showToast(getActivity(), types[which]);
                    }
                });
        builder.show();
    }


}

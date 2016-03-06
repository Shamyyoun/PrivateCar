package com.privatecar.privatecar.fragments;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.activities.DriverAddCarActivity;
import com.privatecar.privatecar.activities.DriverDocumentsActivity;
import com.privatecar.privatecar.activities.DriverSettingsActivity;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.Utils;

public class DriverAccountFragment extends BaseFragment implements View.OnClickListener {
    private Activity activity;
    private TextView tvStatus;
    private TextView tvCurrentCar;
    private TextView tvCarType;
    private TextView tvBalance;

    public DriverAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_driver_account, container, false);

        // init views
        tvStatus = (TextView) fragment.findViewById(R.id.tv_status);
        tvCurrentCar = (TextView) fragment.findViewById(R.id.tv_current_car);
        tvCarType = (TextView) fragment.findViewById(R.id.tv_car_type);
        tvBalance = (TextView) fragment.findViewById(R.id.tv_balance);

        // set data
        User user = AppUtils.getCachedUser(activity);
        tvStatus.setText(user.isOnline() ? R.string.online : R.string.offline);

        Button btnChangeCar = (Button) fragment.findViewById(R.id.btn_change_car);
        Button btnChangeType = (Button) fragment.findViewById(R.id.btn_change_type);

        btnChangeCar.setOnClickListener(this);
        btnChangeType.setOnClickListener(this);


        View layoutAddCar = fragment.findViewById(R.id.layout_add_car);
        layoutAddCar.setOnClickListener(this);

        View layoutDocuments = fragment.findViewById(R.id.layout_documents);
        layoutDocuments.setOnClickListener(this);

        View layoutSettings = fragment.findViewById(R.id.layout_settings);
        layoutSettings.setOnClickListener(this);


        return fragment;
    }

    private void openChangeCarDialog() {
        final String[] cars = {"AAA-123 (Business)", "ABS-23 (Economy)"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_car)
                .setItems(cars, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.showLongToast(getActivity(), cars[which]);
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
                        Utils.showLongToast(getActivity(), types[which]);
                    }
                });
        builder.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change_car:
                openChangeCarDialog();
                break;
            case R.id.btn_change_type:
                openChangeCarType();
                break;
            case R.id.layout_add_car:
                startActivity(new Intent(getActivity(), DriverAddCarActivity.class));
                break;
            case R.id.layout_documents:
                startActivity(new Intent(getActivity(), DriverDocumentsActivity.class));
                break;
            case R.id.layout_settings:
                startActivity(new Intent(getActivity(), DriverSettingsActivity.class));
                break;
        }
    }
}

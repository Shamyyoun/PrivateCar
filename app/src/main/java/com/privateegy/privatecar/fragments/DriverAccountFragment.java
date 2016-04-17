package com.privateegy.privatecar.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.privateegy.privatecar.R;
import com.privateegy.privatecar.activities.AnonymousHomeActivity;
import com.privateegy.privatecar.activities.DriverAddCarActivity;
import com.privateegy.privatecar.activities.DriverDocumentsActivity;
import com.privateegy.privatecar.activities.DriverSettingsActivity;
import com.privateegy.privatecar.models.entities.Car;
import com.privateegy.privatecar.models.entities.DriverAccountDetails;
import com.privateegy.privatecar.models.entities.User;
import com.privateegy.privatecar.models.enums.CarType;
import com.privateegy.privatecar.models.responses.GeneralResponse;
import com.privateegy.privatecar.requests.DriverRequests;
import com.privateegy.privatecar.services.UpdateDriverLocationService;
import com.privateegy.privatecar.utils.AppUtils;
import com.privateegy.privatecar.utils.DialogUtils;
import com.privateegy.privatecar.utils.RequestListener;
import com.privateegy.privatecar.utils.Utils;

public class DriverAccountFragment extends BaseFragment implements RequestListener<GeneralResponse> {
    private static final int REQUEST_CHANGE_CAR = 1;
    private static final int REQUEST_CHANGE_CAR_TYPE = 2;

    private Activity activity;
    private User user;
    private DriverAccountDetails accountDetails;

    private TextView tvStatus;
    private TextView tvCurrentCar;
    private TextView tvCarType;
    private TextView tvBalance;
    private Button btnChangeCar;
    private Button btnChangeType;
    private View layoutAddCar;
    private View layoutDocuments;
    private View layoutSettings;
    private View layoutSignOut;

    private int currentRequest; // used to hold number to indicate which request is being handled
    private AlertDialog visibleDialog; // used to hold current visible visibleDialog
    private Car carBeingChanged; // used to hold car that has being changed
    private CarType carTypeBeingChanged; // used to hold car type that has being changed

    public DriverAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_driver_account, container, false);

        // init views
        tvStatus = (TextView) fragment.findViewById(R.id.tv_status);
        tvCurrentCar = (TextView) fragment.findViewById(R.id.tv_current_car);
        tvCarType = (TextView) fragment.findViewById(R.id.tv_car_type);
        tvBalance = (TextView) fragment.findViewById(R.id.tv_balance);
        btnChangeCar = (Button) fragment.findViewById(R.id.btn_change_car);
        btnChangeType = (Button) fragment.findViewById(R.id.btn_change_type);
        layoutSettings = fragment.findViewById(R.id.layout_settings);
        layoutDocuments = fragment.findViewById(R.id.layout_documents);
        layoutAddCar = fragment.findViewById(R.id.layout_add_car);
        layoutSignOut = fragment.findViewById(R.id.layout_sign_out);

        // get cached user & details
        user = AppUtils.getCachedUser(activity);
        accountDetails = user.getDriverAccountDetails();

        // update status according to running service
        if (Utils.isServiceRunning(getContext(), UpdateDriverLocationService.class)) {
            tvStatus.setText(R.string.online);
        } else {
            tvStatus.setText(R.string.offline);
        }

        // loop to get default car & update car data in the header
        for (Car car : accountDetails.getCars()) {
            if (car.getId() == accountDetails.getDefaultCarId()) {
                // update car data
                updateCarUI(car);
                break;
            }
        }

        // set the balance in header
        tvBalance.setText(getString(R.string.your_balance_c) + " " + accountDetails.getCredit()
                + " " + getString(R.string.currency));

        // add click listeners
        btnChangeCar.setOnClickListener(this);
        btnChangeType.setOnClickListener(this);
        layoutAddCar.setOnClickListener(this);
        layoutDocuments.setOnClickListener(this);
        layoutSettings.setOnClickListener(this);
        layoutSignOut.setOnClickListener(this);

        return fragment;
    }

    /**
     * method, used to update car ui in the fragment view
     */
    private void updateCarUI(Car car) {
        if (car.getLicenseNo() != null && !car.getLicenseNo().isEmpty()) {
            tvCurrentCar.setText(car.getLicenseNo());
        } else {
            tvCurrentCar.setText("-----------");
        }

        if (car.getCarType() != null && !car.getCarType().isEmpty()) {
            tvCarType.setText(CarType.ECONOMY.getValue().equals(car.getCarType()) ? R.string.economy : R.string.business);
        } else {
            tvCarType.setText("-----------");
        }
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
            case R.id.layout_sign_out:
                // stop update service if running
                Intent locationIntent = new Intent(getContext(), UpdateDriverLocationService.class);
                activity.stopService(locationIntent);

                // clear cache and goto anonymous home activity
                AppUtils.clearCache(activity);
                Intent homeIntent = new Intent(activity, AnonymousHomeActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                break;
        }
    }

    /**
     * method, used to prepare cars array as strings and show alert dialog with these cars
     */
    private void openChangeCarDialog() {
        final String[] cars = new String[accountDetails.getCars().size()];

        // loop to prepare cars strings array
        for (int i = 0; i < cars.length; i++) {
            Car car = accountDetails.getCars().get(i);
            String carString = car.getLicenseNo() + "   (" +
                    getString(CarType.ECONOMY.getValue().equals(car.getCarType()) ? R.string.economy : R.string.business) + ")";
            cars[i] = carString;
        }

        // create and show the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_car)
                .setItems(cars, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int position) {
                        changeCar(accountDetails.getCars().get(position));
                    }
                });
        visibleDialog = builder.show();
    }

    /**
     * method, used to send change car request to server
     */
    private void changeCar(Car car) {
        // check internet connection
        if (!Utils.hasConnection(activity)) {
            // show error toast
            Utils.showShortToast(activity, R.string.no_internet_connection);
            return;
        }

        // show progress dialog
        progressDialog = DialogUtils.showProgressDialog(activity, R.string.changing_car_please_wait);

        // create & send the request
        currentRequest = REQUEST_CHANGE_CAR;
        carBeingChanged = car;
        DriverRequests.changeCar(activity, this, user.getAccessToken(), car.getId());
    }

    /**
     * method, used to show alert dialog with car types
     */
    private void openChangeCarType() {
        // get types
        final String[] types = getResources().getStringArray(R.array.car_types);

        // show alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_car_type)
                .setItems(types, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int position) {
                        changeCarType(position == 0 ? CarType.ECONOMY : CarType.BUSINESS);
                    }
                });
        visibleDialog = builder.show();
    }

    /**
     * method, used to send change car type request to server
     */
    private void changeCarType(CarType type) {
        // loop to get default car to validate type
        for (Car car : accountDetails.getCars()) {
            if (car.getId() == accountDetails.getDefaultCarId()) {
                // check types
                CarType defaultCarType = CarType.ECONOMY.getValue().equals(car.getCarType()) ? CarType.ECONOMY : CarType.BUSINESS;
                if (defaultCarType == CarType.ECONOMY) {
                    // check type the driver wanna change to it
                    if (type == CarType.BUSINESS) {
                        // show error toast
                        Utils.showShortToast(activity, R.string.your_current_car_cant_be_business);
                        return;
                    }
                }
                break;
            }
        }

        // check internet connection
        if (!Utils.hasConnection(activity)) {
            // show error toast
            Utils.showShortToast(activity, R.string.no_internet_connection);
            return;
        }

        // show progress dialog
        progressDialog = DialogUtils.showProgressDialog(activity, R.string.changing_car_please_wait);

        // create & send the request
        currentRequest = REQUEST_CHANGE_CAR_TYPE;
        carTypeBeingChanged = type;
        DriverRequests.changeCarType(activity, this, user.getAccessToken(), accountDetails.getDefaultCarId(), type.getValue());
    }

    @Override
    public void onSuccess(GeneralResponse response, String apiName) {
        // dismiss progress
        progressDialog.dismiss();

        // check api name
        if (currentRequest == REQUEST_CHANGE_CAR) {
            // this was change car request
            // check if success
            if (response.isSuccess()) {
                // update car data in the ui
                updateCarUI(carBeingChanged);

                // show toast & dismiss change car dialog
                visibleDialog.dismiss();
                Utils.showLongToast(activity, getString(R.string.your_current_car_change_to) + " " + carBeingChanged.getLicenseNo());

                // change cached default car
                accountDetails.setDefaultCarId(carBeingChanged.getId());
                user.setDriverAccountDetails(accountDetails);
                AppUtils.cacheUser(activity, user);
            } else {
                // show error toast
                Utils.showLongToast(activity, R.string.unexpected_error_try_again);
            }
        } else {
            // this was change car type request
            // check if success
            if (response.isSuccess()) {
                // show toast & dismiss change car dialog
                visibleDialog.dismiss();
                Utils.showLongToast(activity, getString(R.string.your_car_to_changed_to) + " " +
                        getString(carTypeBeingChanged == CarType.ECONOMY ? R.string.economy : R.string.business));
            } else {
                // show error toast
                Utils.showLongToast(activity, R.string.unexpected_error_try_again);
            }
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        // dismiss progress & show error toast
        progressDialog.dismiss();
        Utils.showLongToast(activity, R.string.connection_error);
    }
}

package com.privatecar.privatecar.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.adapters.TripsRVAdapter;
import com.privatecar.privatecar.controllers.CustomerTripsController;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.responses.CustomerTrip;
import com.privatecar.privatecar.models.responses.CustomerTripsResponse;
import com.privatecar.privatecar.requests.CustomerRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.RequestHelper;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

import java.util.List;

public class CustomerMyRidesFragment extends ProgressFragment implements RequestListener<CustomerTripsResponse> {
    public static final String TAG = CustomerMyRidesFragment.class.getName();

    private Activity activity;
    private TextView tvTotalRides;
    private TextView tvTotalMoney;
    private RecyclerView rvTrips;
    private TripsRVAdapter adapter;
    private RequestHelper requestHelper;

    public CustomerMyRidesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // find header views
        tvTotalRides = (TextView) rootView.findViewById(R.id.tv_total_rides);
        tvTotalMoney = (TextView) rootView.findViewById(R.id.tv_total_money);

        // customize recycler view
        rvTrips = (RecyclerView) rootView.findViewById(R.id.rv_trips);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvTrips.setLayoutManager(linearLayoutManager);
        rvTrips.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // load trips
        loadTrips();
    }

    /**
     * method, used to fetch trips from server
     */
    private void loadTrips() {
        // check the internet connection
        if (!Utils.hasConnection(activity)) {
            // show error view
            showError(R.string.no_internet_connection);
            return;
        }

        // show progress view
        showProgress();

        // create & send the request
        User user = AppUtils.getCachedUser(activity);
        requestHelper = CustomerRequests.trips(activity, this, user.getAccessToken(), user.getCustomerAccountDetails().getId());
    }

    @Override
    public void onSuccess(CustomerTripsResponse response, String apiName) {
        // check trips
        if (response.getTrips() != null) {
            // check trips size
            if (response.getTrips().size() == 0) {
                // show empty view
                showEmpty(R.string.you_have_not_do_any_trip_yet);
            } else {
                // update ui
                updateUI(response.getTrips());
                showMain();
            }
        } else {
            // show error view
            showError(R.string.unexpected_error_try_again);
        }
    }

    /**
     * method, used to update ui
     *
     * @param trips
     */
    private void updateUI(List<CustomerTrip> trips) {
        // create new customer trips controller
        CustomerTripsController tripsController = new CustomerTripsController(trips);

        // update the header data
        tvTotalRides.setText(trips.size() + " " + getString(R.string.rides));
        tvTotalMoney.setText(tripsController.getTotalMoney() + " " + getString(R.string.currency));

        // create new trips adapter
        adapter = new TripsRVAdapter(activity, trips);
        rvTrips.setAdapter(adapter);
    }

    @Override
    public void onFail(String message, String apiName) {
        // show error view
        showError(R.string.connection_error);
        Utils.LogE("ERROR: " + message);
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.fragment_customer_my_rides;
    }

    @Override
    protected int getMainViewResId() {
        return R.id.rv_trips;
    }

    @Override
    protected View.OnClickListener getRefreshListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTrips();
            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestHelper != null) {
            requestHelper.cancel(true);
        }
    }
}

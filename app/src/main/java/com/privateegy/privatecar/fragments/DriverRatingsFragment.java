package com.privateegy.privatecar.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.ui.DividerItemDecoration;
import com.privateegy.privatecar.R;
import com.privateegy.privatecar.adapters.AdsRVAdapter;
import com.privateegy.privatecar.models.entities.Ad;
import com.privateegy.privatecar.models.entities.User;
import com.privateegy.privatecar.models.responses.AdsResponse;
import com.privateegy.privatecar.requests.DriverRequests;
import com.privateegy.privatecar.services.UpdateDriverLocationService;
import com.privateegy.privatecar.utils.AppUtils;
import com.privateegy.privatecar.utils.DialogUtils;
import com.privateegy.privatecar.utils.RequestHelper;
import com.privateegy.privatecar.utils.RequestListener;
import com.privateegy.privatecar.utils.Utils;

import java.util.List;


public class DriverRatingsFragment extends BaseFragment implements RequestListener<AdsResponse> {
    private Activity activity;
    private TextView tvStatus;
    private TextView tvTripsCount;
    private TextView tvAds;
    private RecyclerView rvAds;
    private RequestHelper requestHelper;

    public DriverRatingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_driver_ratings, container, false);

        // init views
        tvStatus = (TextView) fragment.findViewById(R.id.tv_status);
        tvTripsCount = (TextView) fragment.findViewById(R.id.tv_trips_count);
        tvAds = (TextView) fragment.findViewById(R.id.tv_ads);
        rvAds = (RecyclerView) fragment.findViewById(R.id.rv_ads);
        rvAds.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        rvAds.setHasFixedSize(true);
        rvAds.setItemAnimator(new DefaultItemAnimator());
        rvAds.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST));

        // set driver info
        if (Utils.isServiceRunning(getContext(), UpdateDriverLocationService.class)) {
            tvStatus.setText(R.string.online);
        } else {
            tvStatus.setText(R.string.offline);
        }
        User user = AppUtils.getCachedUser(activity);
        tvTripsCount.setText(user.getDriverAccountDetails().getTotaltrips() + " " + getString(R.string.trips));

        // load ads
        loadAds();

        return fragment;
    }

    /**
     * method, used to load ads from server
     */
    private void loadAds() {
        // show progress dialog
        progressDialog = DialogUtils.showProgressDialog(activity, R.string.loading_ads_please_wait);

        // create & send the request
        User user = AppUtils.getCachedUser(activity);
        requestHelper = DriverRequests.ads(activity, this, user.getAccessToken());
    }

    @Override
    public void onSuccess(AdsResponse response, String apiName) {
        // dismiss progress
        progressDialog.dismiss();

        // check ads
        if (response.getAds() != null) {
            // check size
            if (response.getAds().size() > 0) {
                // cache ads
                AppUtils.cacheAds(activity, response.getAds());
                // update UI
                updateUI(response.getAds());
            }
        } else {
            // show error toast
            Utils.showLongToast(activity, R.string.unexpected_error_try_again);
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        // dismiss progress
        progressDialog.dismiss();

        // check cached ads
        List<Ad> cachedAds = AppUtils.getCachedAds(activity);
        if (cachedAds != null && cachedAds.size() != 0) {
            // update UI with cached ads
            updateUI(cachedAds);
        } else {
            // show error toast
            Utils.showLongToast(activity, R.string.error_loading_ads);
        }
    }

    /**
     * method, used set recycler view adapter
     */
    private void updateUI(List<Ad> ads) {
        AdsRVAdapter adapter = new AdsRVAdapter(activity, ads);
        rvAds.setAdapter(adapter);
        tvAds.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        // cancel request if still running
        if (requestHelper != null) requestHelper.cancel(false);
        super.onDestroy();
    }
}

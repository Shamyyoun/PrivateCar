package com.privatecar.privatecar.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.activities.DriverHomeActivity;
import com.privatecar.privatecar.activities.DriverStatementSearchResultActivity;
import com.privatecar.privatecar.controllers.StatementsController;
import com.privatecar.privatecar.models.entities.Statement;
import com.privatecar.privatecar.models.entities.StatementsGroup;
import com.privatecar.privatecar.models.entities.Trip;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.responses.StatementsResponse;
import com.privatecar.privatecar.models.responses.TripResponse;
import com.privatecar.privatecar.models.wrappers.SerializableListWrapper;
import com.privatecar.privatecar.requests.DriverRequests;
import com.privatecar.privatecar.services.UpdateDriverLocationService;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.DatePickerFragment;
import com.privatecar.privatecar.utils.DateUtil;
import com.privatecar.privatecar.utils.DialogUtils;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

import java.util.List;

public class DriverStatementFragment extends BaseFragment implements View.OnClickListener, RequestListener<Object> {
    private static final String TRIP_DATE_FORMAT = "dd/MM/yyyy";
    private static final String STATEMENT_DATE_FORMAT = "dd-MM-yyyy";

    private DriverHomeActivity activity;
    private TextView tvStatus;
    private TextView tvLastTripPrice;
    private TextView tvLastTripDate;
    private TextView tvFrom;
    private TextView tvTo;
    private ImageButton ibFrom;
    private ImageButton ibTo;
    private Button btnSearch;
    private DatePickerFragment fromPickerFragment;
    private DatePickerFragment toPickerFragment;
    private String fromDateStr;
    private String toDateStr;
    private ProgressDialog progressDialog;

    public DriverStatementFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (DriverHomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_driver_statement, container, false);

        // init views
        tvStatus = (TextView) fragment.findViewById(R.id.tv_status);
        tvLastTripPrice = (TextView) fragment.findViewById(R.id.tv_last_trip_price);
        tvLastTripDate = (TextView) fragment.findViewById(R.id.tv_last_trip_date);
        tvFrom = (TextView) fragment.findViewById(R.id.tv_from);
        tvTo = (TextView) fragment.findViewById(R.id.tv_to);
        ibFrom = (ImageButton) fragment.findViewById(R.id.ib_from);
        ibFrom.setOnClickListener(this);
        ibTo = (ImageButton) fragment.findViewById(R.id.ib_to);
        ibTo.setOnClickListener(this);
        btnSearch = (Button) fragment.findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this);

        // update status
        if (Utils.isServiceRunning(getContext(), UpdateDriverLocationService.class)) {
            tvStatus.setText(R.string.online);
        } else {
            tvStatus.setText(R.string.offline);
        }

        // load last trip
        loadLastTrip();

        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_from:
                // hide errors
                tvFrom.setError(null);

                // create new instance of from picker fragment
                fromPickerFragment = new DatePickerFragment();
                fromPickerFragment.setDate(fromDateStr, STATEMENT_DATE_FORMAT);

                // show it
                fromPickerFragment.setDatePickerListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        fromDateStr = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        tvFrom.setText(fromDateStr);
                    }
                });
                fromPickerFragment.show(activity.getSupportFragmentManager(), "FromPickerFragment");
                break;

            case R.id.ib_to:
                // hide errors
                tvTo.setError(null);

                // create new instance of to picker fragment
                toPickerFragment = new DatePickerFragment();
                toPickerFragment.setDate(toDateStr, STATEMENT_DATE_FORMAT);

                // show it
                toPickerFragment.setDatePickerListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        toDateStr = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        tvTo.setText(toDateStr);
                    }
                });
                toPickerFragment.show(activity.getSupportFragmentManager(), "ToPickerFragment");
                break;

            case R.id.btn_search:
                search();
                break;
        }
    }

    private void loadLastTrip() {
        // show loading
        progressDialog = DialogUtils.showProgressDialog(activity, R.string.loading_please_wait);

        // create & send the request
        User user = AppUtils.getCachedUser(activity);
        DriverRequests.lastTrip(activity, this, user.getAccessToken());
    }

    @Override
    public void onSuccess(Object response, String apiName) {
        // hide loading
        progressDialog.dismiss();

        // check response type
        if (response instanceof TripResponse) {
            // this last trip response
            TripResponse tripResponse = (TripResponse) response;

            // check last trip
            Trip trip = tripResponse.getTrip();
            if (tripResponse.getTrip() != null) {
                // render response to the ui
                tvLastTripPrice.setText(trip.getEstimateFare() + " " + getString(R.string.currency));
                tvLastTripDate.setText(DateUtil.formatDate(trip.getPickupDateTime(), "yyyy-MM-dd hh:mm:ss", TRIP_DATE_FORMAT));
            } else {
                Utils.showLongToast(activity, R.string.unexpected_error_try_again);
            }
        } else if (response instanceof StatementsResponse) {
            // this statements request
            StatementsResponse statementsResponse = (StatementsResponse) response;

            /*
            // ====DUMMY====
            String res = "{\n" +
                    "\"status\": true,\n" +
                    "\"content\": [\n" +
                    "{ \"profit\": 100, \"comment\": \"Trip fare\", \"trip_id\": 12, \"created_at\": \"2016-02-25 13:08:37\" }\n" +
                    ",\n" +
                    "{ \"profit\": 122, \"comment\": \"Trip cash\", \"trip_id\": 25, \"created_at\": \"2016-02-13 19:20:35\" }\n" +
                    ",\n" +
                    "{ \"profit\": 16, \"comment\": \"Trip fare\", \"trip_id\": 25, \"created_at\": \"2016-02-25 13:08:43\" }\n" +
                    ",\n" +
                    "{ \"profit\": 122, \"comment\": \"Trip cash\", \"trip_id\": 25, \"created_at\": \"2016-02-13 19:24:08\" }\n" +
                    ",\n" +
                    "{ \"profit\": 16, \"comment\": \"Trip fare\", \"trip_id\": 25, \"created_at\": \"2016-02-13 19:24:08\" }\n" +
                    ",\n" +
                    "{ \"profit\": 122, \"comment\": \"Trip cash\", \"trip_id\": 28, \"created_at\": \"2016-02-13 19:38:45\" }\n" +
                    ",\n" +
                    "{ \"profit\": 16, \"comment\": \"Trip fare\", \"trip_id\": 28, \"created_at\": \"2016-02-13 19:38:45\" }\n" +
                    "],\n" +
                    "\"validation\": null\n" +
                    "}";
            Gson gson = new Gson();
            statementsResponse = gson.fromJson(res, StatementsResponse.class);
            */

            // check statements size
            List<Statement> statements = statementsResponse.getStatements();
            if (statements != null && statements.size() != 0) {
                // get statements groups
                StatementsController controller = new StatementsController(statements);
                List<StatementsGroup> groups = controller.getAsGroups();

                // create groups wrapper to send it to the result activity
                SerializableListWrapper<StatementsGroup> groupsWrapper = new SerializableListWrapper<StatementsGroup>(groups);

                // open statements result activity
                Intent intent = new Intent(activity, DriverStatementSearchResultActivity.class);
                intent.putExtra(Const.KEY_STATEMENT_GROUPS, groupsWrapper);
                startActivity(intent);
            } else {
                // show empty toast
                Utils.showLongToast(activity, R.string.no_statements_found_in_this_period);
            }

        } else {
            // show error toast
            Utils.showLongToast(activity, R.string.unexpected_error_try_again);
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        // dismiss progress dialog
        progressDialog.dismiss();
        // show error toast
        Utils.showLongToast(activity, message);
    }

    /**
     * method, used to validate inputs and send statement search request
     */
    private void search() {
        // check dates
        if (fromDateStr == null) {
            // set error
            tvFrom.setError(getString(R.string.please_choose_from_date));
            return;
        }
        if (toDateStr == null) {
            // set error
            tvTo.setError(getString(R.string.please_choose_to_date));
            return;
        }

        // show progress dialog
        progressDialog = DialogUtils.showProgressDialog(activity, R.string.loading_your_statements);

        // create and send the request
        User user = AppUtils.getCachedUser(activity);
        DriverRequests.statements(activity, this, user.getAccessToken(), fromDateStr, toDateStr);
    }
}

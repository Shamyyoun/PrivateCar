package com.privatecar.privatecar.requests;

import android.content.Context;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.models.responses.DriverAccountDetailsResponse;
import com.privatecar.privatecar.models.responses.GeneralResponse;
import com.privatecar.privatecar.models.responses.LocationsResponse;
import com.privatecar.privatecar.models.responses.StatementsResponse;
import com.privatecar.privatecar.models.responses.TripResponse;
import com.privatecar.privatecar.utils.RequestHelper;
import com.privatecar.privatecar.utils.RequestListener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shamyyoun on 2/24/2016.
 */
public class DriverRequests {
    public static RequestHelper<DriverAccountDetailsResponse> accountDetails(Context context, RequestListener<DriverAccountDetailsResponse> listener, String accessToken) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_ACCESS_TOKEN, accessToken);

        // create & send request
        RequestHelper<DriverAccountDetailsResponse> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL, Const.MESSAGE_DRIVER_ACCOUNT_DETAILS, DriverAccountDetailsResponse.class, listener, params);
        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }

    public static RequestHelper<GeneralResponse> driverSignup(Context ctx, RequestListener<GeneralResponse> listener, String firstName, String lastName, String email, String password, String mobile, File imageUserPhotoCropped, File imageCarPhotoCropped, File imageIdFrontPhotoCropped, File imageIdBackPhotoCropped, File imageDriverLicenceFrontPhotoCropped, File imageDriverLicenceBackPhotoCropped, File imageCarLicenceFrontPhotoCropped, File imageCarLicenceBackPhotoCropped) {

        Map<String, String> params = new HashMap<>();
        params.put("FirstName", firstName);
        params.put("LastName", lastName);
        params.put("Email", email);
        params.put("Password", password);
        params.put("Mobile", mobile);
        params.put("RegisterType", "1");

        Map<String, File> files = new HashMap<>();
        files.put("IDnumber", imageIdFrontPhotoCropped);
        files.put("IDnumberBack", imageIdBackPhotoCropped);
        files.put("Driverlicense", imageDriverLicenceFrontPhotoCropped);
        files.put("DriverlicenseBack", imageDriverLicenceBackPhotoCropped);
        files.put("Carlicense", imageCarLicenceFrontPhotoCropped);
        files.put("CarlicenseBack", imageCarLicenceBackPhotoCropped);
        files.put("Userphoto", imageUserPhotoCropped);
        files.put("Carphoto", imageCarPhotoCropped);

        RequestHelper<GeneralResponse> request = new RequestHelper<>(ctx, Const.MESSAGES_BASE_URL, Const.MESSAGE_DRIVER_SIGNUP, GeneralResponse.class, listener, params, files);

        request.executeMultiPart();
        return request;
    }

    public static RequestHelper<Object> lastTrip(Context context, RequestListener<Object> listener, String accessToken) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_ACCESS_TOKEN, accessToken);

        // create & send request
        RequestHelper<Object> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL,
                Const.MESSAGE_DRIVER_LAST_TRIP, TripResponse.class, listener, params);
        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }

    public static RequestHelper<Object> statements(Context context, RequestListener<Object> listener,
                                                   String accessToken, String fromDate, String toDate) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_ACCESS_TOKEN, accessToken);
        params.put(Const.MSG_PARAM_FROM, fromDate);
        params.put(Const.MSG_PARAM_TO, toDate);

        // create & send request
        RequestHelper<Object> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL,
                Const.MESSAGE_DRIVER_GET_STATEMENT, StatementsResponse.class, listener, params);
        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }

    public static RequestHelper<Object> declineTrip(Context context, RequestListener<Object> listener,
                                                    String accessToken, String driverId, String tripId,
                                                    String carId, String reasonId, String comment) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_ACCESS_TOKEN, accessToken);
        params.put(Const.MSG_PARAM_DRIVER_ID, driverId);
        params.put(Const.MSG_PARAM_TRIP_ID, tripId);
        params.put(Const.MSG_PARAM_CAR_ID, carId);
        if (comment == null) {
            params.put(Const.MSG_PARAM_REASON_ID, reasonId);
        } else {
            params.put(Const.MSG_PARAM_COMMENT, comment);
        }

        // create & send request
        RequestHelper<Object> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL,
                Const.MESSAGE_DRIVER_DECLINE_TRIP, GeneralResponse.class, listener, params);
        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }

    public static RequestHelper<GeneralResponse> acceptTrip(Context context, RequestListener<GeneralResponse> listener,
                                                            String accessToken, String driverId, String tripId, String carId) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_ACCESS_TOKEN, accessToken);
        params.put(Const.MSG_PARAM_DRIVER_ID, driverId);
        params.put(Const.MSG_PARAM_TRIP_ID, tripId);
        params.put(Const.MSG_PARAM_CAR_ID, carId);

        // create & send request
        RequestHelper<GeneralResponse> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL,
                Const.MESSAGE_DRIVER_ACCEPT_TRIP, GeneralResponse.class, listener, params);
        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }


    public static RequestHelper<LocationsResponse> getCustomersStats(Context context, RequestListener<LocationsResponse> listener, String accessToken, String location, int radiusInKM) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("location", location);
        params.put("radius", String.valueOf(radiusInKM));

        // create & send request
        RequestHelper<LocationsResponse> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL,
                Const.MESSAGE_DRIVER_GET_CUSTOMERS_STATS, LocationsResponse.class, listener, params);

        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }


    public static RequestHelper<GeneralResponse> updateLocation(Context context, RequestListener<GeneralResponse> listener, String accessToken, String location, String carId, String driverId) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("location", location);
        params.put("carId", carId);
        params.put("driverId", driverId);

        // create & send request
        RequestHelper<GeneralResponse> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL,
                Const.MESSAGE_DRIVER_UPDATE_LOCATION, GeneralResponse.class, listener, params);

        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }


}

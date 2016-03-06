package com.privatecar.privatecar.requests;

import android.content.Context;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.models.responses.DriverAccountDetailsResponse;
import com.privatecar.privatecar.models.responses.GeneralResponse;
import com.privatecar.privatecar.models.responses.LocationsResponse;
import com.privatecar.privatecar.models.responses.TripsResponse;
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

    public static RequestHelper<TripsResponse> lastTrip(Context context, RequestListener<TripsResponse> listener, String accessToken) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_ACCESS_TOKEN, accessToken);

        // create & send request
        RequestHelper<TripsResponse> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL,
                Const.MESSAGE_DRIVER_LAST_TRIP, TripsResponse.class, listener, params);
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


    public static RequestHelper<GeneralResponse> updateLocation(Context context, RequestListener<GeneralResponse> listener, String accessToken, String location, int carId, int driverId) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("location", location);
        params.put("carId", String.valueOf(carId));
        params.put("driverId", String.valueOf(driverId));

        // create & send request
        RequestHelper<GeneralResponse> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL,
                Const.MESSAGE_DRIVER_UPDATE_LOCATION, GeneralResponse.class, listener, params);

        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }


}

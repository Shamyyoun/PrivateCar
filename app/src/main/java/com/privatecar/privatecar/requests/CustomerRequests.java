package com.privatecar.privatecar.requests;

import android.content.Context;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.models.responses.AccessTokenResponse;
import com.privatecar.privatecar.models.responses.CustomerAccountDetailsResponse;
import com.privatecar.privatecar.models.responses.CustomerTripsResponse;
import com.privatecar.privatecar.models.responses.FaresResponse;
import com.privatecar.privatecar.models.responses.GeneralResponse;
import com.privatecar.privatecar.utils.RequestHelper;
import com.privatecar.privatecar.utils.RequestListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shamyyoun on 2/24/2016.
 */
public class CustomerRequests {

    public static RequestHelper<GeneralResponse> regCustomerEmailPassword(Context ctx, RequestListener<GeneralResponse> listener, String firstName, String lastName, String email, String password, String mobile) {

        Map<String, String> params = new HashMap<>();
        params.put("FirstName", firstName);
        params.put("LastName", lastName);
        params.put("Email", email);
        params.put("Password", password);
        params.put("Mobile", mobile);
        params.put("RegisterType", "1");
        params.put("GrantType", "password");

        RequestHelper<GeneralResponse> request = new RequestHelper<>(ctx, Const.MESSAGES_BASE_URL, Const.MESSAGE_REGISTER_CUSTOMER, GeneralResponse.class, listener, params);

        request.executeFormUrlEncoded();
        return request;
    }

    public static RequestHelper<Object> regCustomerSocial(Context ctx, RequestListener<Object> listener, String firstName, String lastName, String email, String mobile, String provider, String id, String token) {

        Map<String, String> params = new HashMap<>();
        params.put("FirstName", firstName);
        params.put("LastName", lastName);
        params.put("Email", email);
        params.put("Id", id); //facebook or google+ id
        params.put("Token", token);//facebook or google+ access token
        params.put("Provider", provider); //facebook & google
        params.put("Mobile", mobile);
        params.put("RegisterType", "2");

        RequestHelper<Object> request = new RequestHelper<>(ctx, Const.MESSAGES_BASE_URL, Const.MESSAGE_REGISTER_CUSTOMER, GeneralResponse.class, listener, params);

        request.executeFormUrlEncoded();
        return request;
    }


    public static RequestHelper<CustomerAccountDetailsResponse> accountDetails(Context context, RequestListener<CustomerAccountDetailsResponse> listener, String accessToken) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_ACCESS_TOKEN, accessToken);

        // create & send request
        RequestHelper<CustomerAccountDetailsResponse> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL, Const.MESSAGE_CUSTOMER_ACCOUNT_DETAILS, CustomerAccountDetailsResponse.class, listener, params);
        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }

    public static RequestHelper<AccessTokenResponse> verifyUser(Context context, RequestListener<AccessTokenResponse> listener, String email, String code) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_CLIENT_ID, Const.CLIENT_ID);
        params.put(Const.MSG_PARAM_CLIENT_SECRET, Const.CLIENT_SECRET);
        params.put(Const.MSG_PARAM_GRANT_TYPE, "verify");
        params.put(Const.MSG_PARAM_EMAIL, email);
        params.put(Const.MSG_PARAM_CODE, code);

        // create & send request
        RequestHelper<AccessTokenResponse> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL, Const.MESSAGE_CUSTOMER_VERIFY_USER, AccessTokenResponse.class, listener, params);
        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }

    public static RequestHelper<CustomerTripsResponse> trips(Context context, RequestListener<CustomerTripsResponse> listener, String accessToken, int customerId) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_ACCESS_TOKEN, accessToken);
        params.put(Const.MSG_PARAM_CUSTOMER_ID, "" + customerId);

        // create & send request
        RequestHelper<CustomerTripsResponse> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL, Const.MESSAGE_CUSTOMER_TRIPS, CustomerTripsResponse.class, listener, params);
        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }

    public static RequestHelper<FaresResponse> fares(Context context, RequestListener<FaresResponse> listener, String accessToken, String theClass, String pickupTime) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_ACCESS_TOKEN, accessToken);
        params.put(Const.MSG_PARAM_CLASS, theClass);
        params.put(Const.MSG_PARAM_PICKUP_TIME, pickupTime);

        // create & send request
        RequestHelper<FaresResponse> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL, Const.MESSAGE_CUSTOMER_FARES, FaresResponse.class, listener, params);
        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }
}

package com.privatecar.privatecar.requests;

import android.content.Context;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.models.responses.CustomerAccountDetailsResponse;
import com.privatecar.privatecar.models.responses.DriverAccountDetailsResponse;
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

    public static RequestHelper<GeneralResponse> regCustomerSocial(Context ctx, RequestListener<GeneralResponse> listener, String firstName, String lastName, String email, String mobile, String provider, String id, String token) {

        Map<String, String> params = new HashMap<>();
        params.put("FirstName", firstName);
        params.put("LastName", lastName);
        params.put("Email", email);
        params.put("Id", id); //facebook or google+ id
        params.put("Token", token);//facebook or google+ access token
        params.put("Provider", provider); //facebook & google
        params.put("Mobile", mobile);
        params.put("RegisterType", "2");

        RequestHelper<GeneralResponse> request = new RequestHelper<>(ctx, Const.MESSAGES_BASE_URL, Const.MESSAGE_REGISTER_CUSTOMER, GeneralResponse.class, listener, params);

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
}

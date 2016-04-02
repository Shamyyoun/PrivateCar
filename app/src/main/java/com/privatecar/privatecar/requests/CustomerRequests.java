package com.privatecar.privatecar.requests;

import android.content.Context;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.models.entities.PrivateCarLocation;
import com.privatecar.privatecar.models.enums.AddressType;
import com.privatecar.privatecar.models.requests.TripRequest;
import com.privatecar.privatecar.models.responses.CustomerAccountDetailsResponse;
import com.privatecar.privatecar.models.responses.AccessTokenResponse;
import com.privatecar.privatecar.models.responses.CustomerTripsResponse;
import com.privatecar.privatecar.models.responses.FaresResponse;
import com.privatecar.privatecar.models.responses.GeneralResponse;
import com.privatecar.privatecar.models.responses.NearDriversResponse;
import com.privatecar.privatecar.models.responses.PromoCodeResponse;
import com.privatecar.privatecar.models.responses.TripRequestResponse;
import com.privatecar.privatecar.utils.DateUtil;
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

    public static RequestHelper<NearDriversResponse> nearDrivers(Context context, RequestListener<NearDriversResponse> listener, String accessToken, PrivateCarLocation location) {

        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_ACCESS_TOKEN, accessToken);
        params.put("location", location.toString());

        // create & send request
        RequestHelper<NearDriversResponse> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL, Const.MESSAGE_NEAR_DRIVERS, NearDriversResponse.class, listener, params);
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
        params.put(Const.MSG_PARAM_CUSTOMERID, "" + customerId);

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

    public static RequestHelper<PromoCodeResponse> activatePromoCode(Context context, RequestListener<PromoCodeResponse> listener, String accessToken, int customerId, String promoCode) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_ACCESS_TOKEN, accessToken);
        params.put(Const.MSG_PARAM_CUSTOMERID, "" + customerId);
        params.put(Const.MSG_PARAM_PROMO_CODE, promoCode);

        // create & send request
        RequestHelper<PromoCodeResponse> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL, Const.MESSAGE_CUSTOMER_ACTIVATE_PROMO_CODE, PromoCodeResponse.class, listener, params);
        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }

    public static RequestHelper<TripRequestResponse> requestTrip(Context context, RequestListener<TripRequestResponse> listener,
                                                                 String accessToken, int customerId, TripRequest tripRequest) {

        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_ACCESS_TOKEN, accessToken);
        params.put(Const.MSG_PARAM_CUSTOMER_ID, "" + customerId);
        params.put(Const.MSG_PARAM_PICKUP_ADDRESS, tripRequest.getPickupPlace().getFullAddress());
        params.put(Const.MSG_PARAM_SERVICE_TYPE, tripRequest.getCarType().getValue());
        params.put(Const.MSG_PARAM_PAYMENT_TYPE, tripRequest.getPaymentType().getValue());
        params.put("address_type", "1");
        params.put(Const.MSG_PARAM_PICKUP_LOCATION, tripRequest.getPickupPlace().getLocation().getLat() + ","
                + tripRequest.getPickupPlace().getLocation().getLng());
        params.put(Const.MSG_PARAM_PICKUP_NOW, tripRequest.isPickupNow() ? "1" : "0");
        params.put(Const.MSG_PARAM_DESTINATION_TYPE, tripRequest.getDestinationType().getValue());
        params.put(Const.MSG_PARAM_NOTES_FOR_DRIVER, tripRequest.getNotes());
        params.put(Const.MSG_PARAM_PICKUP_ADDRESS_NOTES, tripRequest.getPickupDetails());

        // check destination type to add its params
        if (tripRequest.getDestinationType() == AddressType.ADDRESS) {
            params.put(Const.MSG_PARAM_DESTINATION_LOCATION, tripRequest.getDestinationPlace().getLocation().getLat()
                    + "," + tripRequest.getDestinationPlace().getLocation().getLng());
            params.put(Const.MSG_PARAM_ESTIMATE_DISTANCE, "" + tripRequest.getEstimateDistance());
            params.put(Const.MSG_PARAM_ESTIMATE_FARE, "" + tripRequest.getEstimateFare());
            params.put(Const.MSG_PARAM_ESTIMATE_TIME, "" + tripRequest.getEstimateTime());
            params.put(Const.MSG_PARAM_DESTINATION_ADDRESS, tripRequest.getDestinationPlace().getFullAddress());
        }

        // check pickup now to add pickup time
        if (!tripRequest.isPickupNow()) {
            String pickupTime = DateUtil.convertToString(tripRequest.getPickupTime(), "yyyy-MM-dd hh:mm:ss");
            params.put(Const.MSG_PARAM_PICKUP_DATE_TIME, pickupTime);
        }

        // create & send request
        RequestHelper<TripRequestResponse> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL,
                Const.MESSAGE_CUSTOMER_REQUEST_TRIP, TripRequestResponse.class, listener, params);
        requestHelper.setTimeOut(4 * 60 * 1000);
        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }

    public static RequestHelper<Object> cancelTrip(Context context, RequestListener<Object> listener, String accessToken, int tripId) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_ACCESS_TOKEN, accessToken);
        params.put(Const.MSG_PARAM_TRIP_ID, "" + tripId);

        // create & send request
        RequestHelper<Object> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL, Const.MESSAGE_CUSTOMER_CANCEL_TRIP, GeneralResponse.class, listener, params);
        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }
}

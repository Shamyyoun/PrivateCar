package com.privatecar.privatecar.requests;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.enums.GrantType;
import com.privatecar.privatecar.models.responses.AccessTokenResponse;
import com.privatecar.privatecar.models.responses.ConfigResponse;
import com.privatecar.privatecar.models.responses.DistanceMatrixResponse;
import com.privatecar.privatecar.models.responses.FaresResponse;
import com.privatecar.privatecar.models.responses.DriverLocationResponse;
import com.privatecar.privatecar.models.responses.GeneralResponse;
import com.privatecar.privatecar.models.responses.MessagesResponse;
import com.privatecar.privatecar.models.responses.NearbyPlacesResponse;
import com.privatecar.privatecar.models.responses.OptionsResponse;
import com.privatecar.privatecar.utils.PlayServicesUtils;
import com.privatecar.privatecar.utils.RequestHelper;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Shamyyoun on 2/24/2016.
 */
public class CommonRequests {
    public static RequestHelper<Object> normalLogin(Context context, RequestListener<Object> listener, String username, String password) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_CLIENT_ID, Const.CLIENT_ID);
        params.put(Const.MSG_PARAM_CLIENT_SECRET, Const.CLIENT_SECRET);
        params.put(Const.MSG_PARAM_GRANT_TYPE, GrantType.PASSWORD.getValue());
        params.put(Const.MSG_PARAM_OS, "1");
        params.put(Const.MSG_PARAM_GCM_ACCESS_TOKEN, PlayServicesUtils.getCachedGCMToken(context));
        params.put(Const.MSG_PARAM_USERNAME, username); // the email
        params.put(Const.MSG_PARAM_PASSWORD, password);

        // create & send request
        RequestHelper<Object> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL, Const.MESSAGE_ACCESS_TOKEN, AccessTokenResponse.class, listener, params);
        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }

    public static RequestHelper socialLogin(Context context, RequestListener<Object> listener, String id, String token, String provider) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_CLIENT_ID, Const.CLIENT_ID);
        params.put(Const.MSG_PARAM_CLIENT_SECRET, Const.CLIENT_SECRET);
        params.put(Const.MSG_PARAM_GRANT_TYPE, GrantType.SOCIAL.getValue());
        params.put(Const.MSG_PARAM_OS, "1");
        params.put(Const.MSG_PARAM_GCM_ACCESS_TOKEN, PlayServicesUtils.getCachedGCMToken(context));
        params.put(Const.MSG_PARAM_ID, id);
        params.put(Const.MSG_PARAM_TOKEN, token);
        params.put(Const.MSG_PARAM_PROVIDER, provider);

        // create & send request
        RequestHelper<Object> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL, Const.MESSAGE_ACCESS_TOKEN, AccessTokenResponse.class, listener, params);
        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }

    public static RequestHelper startupConfig(Context context, RequestListener<ConfigResponse> listener) {
        // create & send request
        RequestHelper<ConfigResponse> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL, Const.MESSAGE_STARTUP_CONFIG, ConfigResponse.class, listener);
        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }

    public static RequestHelper options(Context context, RequestListener<Object> listener, String accessToken, String type) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_ACCESS_TOKEN, accessToken);
        params.put(Const.MSG_PARAM_TYPE, type);

        // create & send request
        RequestHelper<Object> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL, Const.MESSAGE_GET_OPTIONS, OptionsResponse.class, listener, params);
        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }

    public static RequestHelper changePassword(Context context, RequestListener<GeneralResponse> listener, String accessToken, String oldPassword, String newPassword) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_ACCESS_TOKEN, accessToken);
        params.put(Const.MSG_PARAM_OLD_PASSWORD, oldPassword);
        params.put(Const.MSG_PARAM_NEW_PASSWORD, newPassword);

        // create & send request
        RequestHelper<GeneralResponse> requestHelper = new RequestHelper<>(context,
                Const.MESSAGES_BASE_URL, Const.MESSAGE_CHANGE_PASSWORD, GeneralResponse.class, listener, params);
        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }

    public static RequestHelper forgetPassword(Context context, RequestListener<GeneralResponse> listener, String email) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_EMAIL, email);

        // create & send request
        RequestHelper<GeneralResponse> requestHelper = new RequestHelper<>(context,
                Const.MESSAGES_BASE_URL, Const.MESSAGE_FORGET_PASSWORD, GeneralResponse.class, listener, params);
        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }

    public static RequestHelper<GeneralResponse> changePhoto(Context ctx, RequestListener<GeneralResponse> listener, String accessToken, File imageUserPhotoCropped) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_ACCESS_TOKEN, accessToken);

        Map<String, File> files = new HashMap<>();
        files.put("Userphoto", imageUserPhotoCropped);

        RequestHelper<GeneralResponse> request = new RequestHelper<>(ctx, Const.MESSAGES_BASE_URL, Const.MESSAGE_DRIVER_CHANGE_PHOTO, GeneralResponse.class, listener, params, files);

        request.executeMultiPart();
        return request;
    }

    public static RequestHelper<NearbyPlacesResponse> getNearbyPlacesByPlacesApi(Context ctx, RequestListener<NearbyPlacesResponse> listener, LatLng latLng) {
        int radiusInMeters = 1000; //1000 meters
        String language = Utils.getAppLanguage();
        String serverApiKey = ctx.getString(R.string.server_api_key);

        String url = String.format(Locale.ENGLISH, "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%f,%f&radius=%d&language=%s&key=%s", latLng.latitude, latLng.longitude, radiusInMeters, language, serverApiKey);

        RequestHelper<NearbyPlacesResponse> requestNearbyPlaces = new RequestHelper<>(ctx, "", url, NearbyPlacesResponse.class, listener);
        requestNearbyPlaces.executeFormUrlEncoded();

        return requestNearbyPlaces;
    }

    public static RequestHelper<DistanceMatrixResponse> getTravelTimeByDistanceMatrixApi(Context ctx, RequestListener listener,String origin, LatLng destination) {
        String language = Utils.getAppLanguage();
        String serverApiKey = ctx.getString(R.string.server_api_key);

        String url = String.format(Locale.ENGLISH, "https://maps.googleapis.com/maps/api/distancematrix/json?origins=%s&destinations=%f,%f&language=%s&key=%s", origin, destination.latitude, destination.longitude, language, serverApiKey);

        RequestHelper<DistanceMatrixResponse> distanceMatrixRequest = new RequestHelper<>(ctx, "", url, DistanceMatrixResponse.class, listener);
        distanceMatrixRequest.executeFormUrlEncoded();

        return distanceMatrixRequest;
    }

    public static RequestHelper<MessagesResponse> getInbox(Context context, RequestListener<MessagesResponse> listener, String accessToken, int start) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_ACCESS_TOKEN, accessToken);
        params.put("start", String.valueOf(start));

        // create & send request
        RequestHelper<MessagesResponse> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL,
                Const.MESSAGE_DRIVER_GET_INBOX, MessagesResponse.class, listener, params);

        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }

    public static RequestHelper<GeneralResponse> sendMessage(Context context, RequestListener<GeneralResponse> listener, String accessToken, String message) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_ACCESS_TOKEN, accessToken);
        params.put("message", message);

        // create & send request
        RequestHelper<GeneralResponse> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL,
                Const.MESSAGE_DRIVER_SEND_MESSAGE, GeneralResponse.class, listener, params);
        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }

    public static RequestHelper<GeneralResponse> messagesMarkAsRead(Context context, RequestListener<GeneralResponse> listener, String accessToken, int messagesIds) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_ACCESS_TOKEN, accessToken);
        params.put("messages_ids", String.valueOf(messagesIds));

        // create & send request
        RequestHelper<GeneralResponse> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL,
                Const.MESSAGE_DRIVER_MESSAGES_MARK_AS_READ, GeneralResponse.class, listener, params);

        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }

    public static RequestHelper<GeneralResponse> messagesMarkAsUnRead(Context context, RequestListener<GeneralResponse> listener, String accessToken, int messagesIds) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_ACCESS_TOKEN, accessToken);
        params.put("messages_ids", String.valueOf(messagesIds));

        // create & send request
        RequestHelper<GeneralResponse> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL,
                Const.MESSAGE_DRIVER_MESSAGES_MARK_AS_UNREAD, GeneralResponse.class, listener, params);

        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }

    /**
     * @param context
     * @param listener
     * @param accessToken
     * @param messagesIds comma separated message ids (2,34,22,....)
     * @return
     */
    public static RequestHelper<GeneralResponse> messagesDelete(Context context, RequestListener<GeneralResponse> listener, String accessToken, String messagesIds) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_ACCESS_TOKEN, accessToken);
        params.put("messages_ids", messagesIds);

        // create & send request
        RequestHelper<GeneralResponse> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL,
                Const.MESSAGE_DRIVER_MESSAGES_DELETE, GeneralResponse.class, listener, params);
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

    public static RequestHelper<DriverLocationResponse> driverTripLocation(Context context, RequestListener listener, String accessToken, int tripId) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_ACCESS_TOKEN, accessToken);
        params.put(Const.MSG_PARAM_TRIP_ID, "" + tripId);

        // create & send request
        RequestHelper<DriverLocationResponse> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL,
                Const.MESSAGE_DRIVER_TRIP_LOCATION, DriverLocationResponse.class, listener, params);

        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }

}

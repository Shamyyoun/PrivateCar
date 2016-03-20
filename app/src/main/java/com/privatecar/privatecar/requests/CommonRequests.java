package com.privatecar.privatecar.requests;

import android.content.Context;
import android.util.Log;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.models.enums.GrantType;
import com.privatecar.privatecar.models.responses.AccessTokenResponse;
import com.privatecar.privatecar.models.responses.ConfigResponse;
import com.privatecar.privatecar.models.responses.GeneralResponse;
import com.privatecar.privatecar.models.responses.OptionsResponse;
import com.privatecar.privatecar.utils.PlayServicesUtils;
import com.privatecar.privatecar.utils.RequestHelper;
import com.privatecar.privatecar.utils.RequestListener;

import java.io.File;
import java.util.HashMap;
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
}

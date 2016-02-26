package com.privatecar.privatecar.requests;

import android.content.Context;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.models.enums.GrantType;
import com.privatecar.privatecar.models.responses.AccessTokenResponse;
import com.privatecar.privatecar.models.responses.ConfigResponse;
import com.privatecar.privatecar.utils.PlayServicesUtils;
import com.privatecar.privatecar.utils.RequestHelper;
import com.privatecar.privatecar.utils.RequestListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shamyyoun on 2/24/2016.
 */
public class CommonRequests {
    public static RequestHelper normalLogin(Context context, RequestListener<AccessTokenResponse> listener, String username, String password) {
        // prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.MSG_PARAM_CLIENT_ID, Const.CLIENT_ID);
        params.put(Const.MSG_PARAM_CLIENT_SECRET, Const.CLIENT_SECRET);
        params.put(Const.MSG_PARAM_GRANT_TYPE, GrantType.PASSWORD.getValue());
        params.put(Const.MSG_PARAM_OS, "1");
        params.put(Const.MSG_PARAM_GCM_ACCESS_TOKEN, PlayServicesUtils.getCachedGCMToken(context));
        params.put(Const.MSG_PARAM_USERNAME, username);
        params.put(Const.MSG_PARAM_PASSWORD, password);

        // create & send request
        RequestHelper<AccessTokenResponse> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL, Const.MESSAGE_ACCESS_TOKEN, AccessTokenResponse.class, listener, params);
        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }

    public static RequestHelper startupConfig(Context context, RequestListener<ConfigResponse> listener) {
        // create & send request
        RequestHelper<ConfigResponse> requestHelper = new RequestHelper<>(context, Const.MESSAGES_BASE_URL, Const.MESSAGE_STARTUP_CONFIG, ConfigResponse.class, listener);
        requestHelper.executeFormUrlEncoded();

        return requestHelper;
    }
}

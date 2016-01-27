package com.privatecar.privatecar.gcm;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.utils.Utils;

/**
 * Created by basim on 27/1/16.
 * A utils class, contains useful methods for working with gcm.
 * Some of these methods needs the GCM library to be added to the project.
 */
public class GCMUtils {

    public static final String KEY_GCM_TOKEN = "gcm_token_key";

    /**
     * Checks if the google play services is available on the device, because
     * other services depend on it such as the GCM service.
     *
     * @param activity an activity that can receive the result of the error dialog.
     * @return true if available or false.
     */
    public static boolean isPlayServicesAvailable(Activity activity) {
        final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.e(Const.LOG_TAG, "This device doesn't support push notifications.");
                Toast.makeText(activity, "This device doesn't support push notifications.", Toast.LENGTH_LONG).show();
            }
            return false;
        }
        return true;
    }


    public static void cacheGCMToken(Context context, String token) {
        Utils.cacheString(context, KEY_GCM_TOKEN, token);
    }

    /**
     * Gets the cached GCM token (if found and the app version code didn't change) or return null.
     *
     * @param context
     * @return the cached GCM token if no new one is required otherwise null.
     */
    public static String getCachedGCMToken(Context context) {
        String token = Utils.getCachedString(context, KEY_GCM_TOKEN, null);
        if (token == null) {
            Log.e(Const.LOG_TAG, "GCM Token not found.");
            return null;
        }

        // Check if app was updated; if so, it must clear the token
        // since the existing token is not guaranteed to work with the new app version.
        int cachedVersionCode = Utils.getCachedAppVersionCode(context);
        int currentVersion = Utils.getAppVersionCode(context);
        if (cachedVersionCode != currentVersion) {
            Log.e(Const.LOG_TAG, "App version changed.");
            return null;
        }
        return token;
    }

    public static void clearGCMToken(Context context) {
        Utils.clearCachedKey(context, KEY_GCM_TOKEN);
    }


}

package com.privatecar.privatecar.gcm;

/**
 * Created by Basim Alamuddin on 13/12/2015.
 */

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.privatecar.privatecar.Const;

public class GCMMessageHandler extends GcmListenerService {

    //to test if GCM works:-
    //http://techzog.com/development/gcm-notification-test-tool-android/

//    private static int notification_id = 0; //should be static to avoid reinitialization
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String json_notification = data.getString("message");
        Log.e(Const.LOG_TAG, "GCM Message: " + json_notification);
    }



}

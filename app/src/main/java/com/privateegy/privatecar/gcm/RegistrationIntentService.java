package com.privateegy.privatecar.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.privateegy.privatecar.R;
import com.privateegy.privatecar.utils.PlayServicesUtils;
import com.privateegy.privatecar.utils.Utils;

import java.io.IOException;

/**
 * Created by Basim Alamuddin on 13/12/2015.
 */

/**
 * this service gets a token from the gcm server
 */

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Utils.executeAsyncTask(new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
                    String senderId = getResources().getString(R.string.project_number);
                    String token = instanceID.getToken(senderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE);

                    PlayServicesUtils.cacheGCMToken(getApplicationContext(), token);
                    Utils.cacheAppVersionCode(getApplicationContext());
                    Log.e(TAG, "New GCM Token: " + token);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });


    }


}

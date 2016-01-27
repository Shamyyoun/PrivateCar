package com.privatecar.privatecar.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Basim Alamuddin on 13/12/2015.
 */

/**
 * According to Google official documentation, the instance ID server issues callbacks periodically (i.e. 6 months) to request apps to refresh their tokens. To support this possibility, we need to extend from InstanceIDListenerService to handle token refresh changes.
 */

public class MyInstanceIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify of changes
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
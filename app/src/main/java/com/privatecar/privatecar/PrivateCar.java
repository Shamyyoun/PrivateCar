package com.privatecar.privatecar;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by basim on 21/1/16.
 */
public class PrivateCar extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}

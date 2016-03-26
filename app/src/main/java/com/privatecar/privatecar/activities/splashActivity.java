package com.privatecar.privatecar.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.gcm.RegistrationIntentService;
import com.privatecar.privatecar.models.entities.Config;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.enums.UserType;
import com.privatecar.privatecar.models.responses.AccessTokenResponse;
import com.privatecar.privatecar.models.responses.ConfigResponse;
import com.privatecar.privatecar.requests.CommonRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.PlayServicesUtils;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

public class SplashActivity extends BaseActivity implements RequestListener {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (PlayServicesUtils.isPlayServicesAvailable(this)) {
            if (PlayServicesUtils.getCachedGCMToken(getApplicationContext()) == null)
                startService(new Intent(this, RegistrationIntentService.class));
            else
                Utils.LogE(PlayServicesUtils.getCachedGCMToken(getApplicationContext()));
        } else { // if google play services is not available
            Utils.showLongToast(this, R.string.install_google_play_services);
            finish();
            return;
        }

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        Utils.printHashKey(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Const.REQUEST_FINE_LOCATION_PERMISSION);
        } else {
            // send startup config request
            CommonRequests.startupConfig(this, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Const.REQUEST_FINE_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
// send startup config request
                    CommonRequests.startupConfig(this, this);
                } else if (grantResults.length > 0) {
                    finish();
                }
                break;
        }
    }

    @Override
    public void onSuccess(Object response, String apiName) {
        // check the response
        if (response instanceof ConfigResponse) {
            // this was the startup config
            // hide the progress bar
            progressBar.setVisibility(View.GONE);

            // cast the response
            ConfigResponse configResponse = (ConfigResponse) response;

            // cache configs response
            AppUtils.cacheConfigs(getApplicationContext(), configResponse);

            // print them
            for (Config config : configResponse.getConfigs()) {
                Log.e(Const.LOG_TAG, config.getKey() + " : " + config.getValue());
            }

            // get cached user
            User user = AppUtils.getCachedUser(SplashActivity.this);

            // check user to go to suitable activity
            Intent intent;
            if (user == null) {
                // no logged in user
                // goto anonymous home activity
                intent = new Intent(SplashActivity.this, AnonymousHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                // check the user expiry timestamp to obtain new access token if expired
                long expiryTimestamp = user.getExpiryTimestamp();
                if (AppUtils.isTokenExpired(expiryTimestamp)) {
                    // check the user grant type
                    if (!Utils.isNullOrEmpty(user.getUserName())) {
                        // user had registered with normal login request
                        // resend normal login request to obtain new access token
                        CommonRequests.normalLogin(this, this, user.getUserName(), user.getPassword());
                    } else {
                        // user had registered with social login request
                        // resend social login request to obtain new access token
                        CommonRequests.socialLogin(this, this, user.getSocialUserId(), user.getSocialToken(), user.getSocialProvider());
                    }
                } else {
                    openHomeActivity();
                }
            }
        } else {
            // this was access token request
            // cast the response
            AccessTokenResponse accessTokenResponse = (AccessTokenResponse) response;

            // check the access token
            if (!Utils.isNullOrEmpty(accessTokenResponse.getAccessToken())) {
                // succeed
                // cache the new access token
                User user = AppUtils.getCachedUser(this);
                user.setAccessToken(accessTokenResponse.getAccessToken());
                long expiryTimestamp = System.currentTimeMillis() + (accessTokenResponse.getExpiresIn() * 1000);
                user.setExpiryTimestamp(expiryTimestamp);
                AppUtils.cacheUser(this, user);

                // open suitable home activity
                openHomeActivity();
            } else {
                // failed
                // show error toast & exit
                Utils.showLongToast(getApplicationContext(), R.string.unexpected_error_try_again);
                finish();
            }
        }
    }

    private void openHomeActivity() {
        // check the cached user type
        User user = AppUtils.getCachedUser(this);
        Intent intent;
        if (user.getType() == UserType.DRIVER) {
            // goto driver home activity
            intent = new Intent(SplashActivity.this, DriverHomeActivity.class);
        } else {
            // goto customer home activity
            intent = new Intent(SplashActivity.this, CustomerHomeActivity.class);
        }

        // start the suitable activity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onFail(String message, String apiName) {
        Utils.showLongToast(getApplicationContext(), message);
        Log.e(Const.LOG_TAG, message);
        finish();
    }
}

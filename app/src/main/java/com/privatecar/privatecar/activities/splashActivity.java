package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.gcm.RegistrationIntentService;
import com.privatecar.privatecar.models.entities.Config;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.enums.UserType;
import com.privatecar.privatecar.models.responses.ConfigResponse;
import com.privatecar.privatecar.requests.CommonRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.PlayServicesUtils;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

public class SplashActivity extends BaseActivity implements RequestListener<ConfigResponse> {

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
        }

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        Utils.printHashKey(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // send startup config request
        CommonRequests.startupConfig(this, this);
    }

    @Override
    public void onSuccess(ConfigResponse response, String apiName) {
        progressBar.setVisibility(View.GONE);

        // validate response
        if (response != null) {
            // cache configs response
            AppUtils.cacheConfigs(getApplicationContext(), response);

            // print them
            for (Config config : response.getConfigs()) {
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
            } else if (user.getType() == UserType.DRIVER) {
                // goto driver home activity
                intent = new Intent(SplashActivity.this, DriverHomeActivity.class);
            } else {
                // goto customer home activity
                intent = new Intent(SplashActivity.this, CustomerHomeActivity.class);
            }

            // goto activity
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            // show error toast
            Utils.showLongToast(getApplicationContext(), R.string.connection_error);
            finish();
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        Utils.showLongToast(getApplicationContext(), message);
        Log.e(Const.LOG_TAG, message);
        finish();
    }
}

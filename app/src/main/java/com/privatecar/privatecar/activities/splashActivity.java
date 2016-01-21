package com.privatecar.privatecar.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.Config;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.RequestHelper;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

public class splashActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

    }

    @Override
    protected void onResume() {
        super.onResume();

        RequestHelper<Config[]> helper = new RequestHelper<>(this, Const.MESSAGES_BASE_URL, Const.MESSAGE_STARTUP_CONFIG, Config[].class,
                new RequestListener<Config[]>() {
                    @Override
                    public void onSuccess(Config[] response, String apiName) {

                        progressBar.setVisibility(View.GONE);

                        AppUtils.cacheConfigs(getApplicationContext(), response);

                        for (Config config : response) {
                            Log.e(Const.LOG_TAG, config.getKey() + " : " + config.getValue());
                        }

                        if (AppUtils.isUserLoggedIn(getApplicationContext())) {
                            //TODO: get user preferences from the server and populate them
                            //TODO: populate user language
                            //TODO: navigate to picking screen
                        } else {
                            //TODO: navigate to anonymous home screen
                        }


                    }

                    @Override
                    public void onFail(String message) {
                        Utils.showToast(getApplicationContext(), message);
                        finish();
                    }
                });

        helper.executeFormUrlEncoded();
    }
}

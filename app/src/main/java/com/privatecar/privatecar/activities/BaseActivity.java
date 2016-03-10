package com.privatecar.privatecar.activities;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.utils.Utils;

/**
 * Created by basim on 25/1/16.
 * The very basic base activity that is the parent or grand parent to all activities
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //TODO: remove this when release

        //change app locale
        String defLang = Utils.getAppLanguage();
        String lang = Utils.getCachedString(getApplicationContext(), Const.CACHE_LOCALE, defLang);
        Utils.changeAppLocale(getApplicationContext(), lang);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {

    }

}

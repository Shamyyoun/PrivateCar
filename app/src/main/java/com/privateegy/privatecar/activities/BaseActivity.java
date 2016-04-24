package com.privateegy.privatecar.activities;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.privateegy.privatecar.Const;
import com.privateegy.privatecar.utils.Utils;

/**
 * Created by basim on 25/1/16.
 * The very basic base activity that is the parent or grand parent to all activities
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //change app locale
        String defLang = Utils.getAppLanguage();
        String lang = Utils.getCachedString(getApplicationContext(), Const.CACHE_LOCALE, defLang);
        Utils.changeAppLocale(getApplicationContext(), lang);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        // a work around for the actionbar title, not respecting the default language localization set.
        // http://stackoverflow.com/a/28113929
        try {
            ActivityInfo ai = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_ACTIVITIES | PackageManager.GET_META_DATA);
            if (ai.labelRes != 0) {
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle(ai.labelRes);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

}

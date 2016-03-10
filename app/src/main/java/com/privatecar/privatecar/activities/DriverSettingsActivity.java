package com.privatecar.privatecar.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.utils.Utils;

public class DriverSettingsActivity extends BasicBackActivity {
    private View layoutChangePassword, layoutChangeLanguage;
    private ImageButton ibUserPhoto;
    private TextView tvLanguage;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_settings);

        // init views
        layoutChangePassword = findViewById(R.id.layout_change_password);
        layoutChangePassword.setOnClickListener(this);
        layoutChangeLanguage = findViewById(R.id.layout_change_language);
        layoutChangeLanguage.setOnClickListener(this);

        tvLanguage = (TextView) findViewById(R.id.tv_language);
        String language = Utils.getAppLanguage();
        if (language.equals("ar")) {
            tvLanguage.setText(R.string.arabic);
        } else {
            tvLanguage.setText(R.string.english);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.layout_change_password:
                // open change password activity
                startActivity(new Intent(this, ChangePasswordActivity.class));
                break;
            case R.id.layout_change_language:
                builder = new AlertDialog.Builder(this);
                builder.setItems(R.array.languages, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: //english
                                Utils.changeAppLocale(getApplicationContext(), "en");
                                Utils.cacheString(getApplicationContext(), Const.CACHE_LOCALE, "en");
                                break;
                            case 1: //arabic
                                Utils.changeAppLocale(getApplicationContext(), "ar");
                                Utils.cacheString(getApplicationContext(), Const.CACHE_LOCALE, "ar");
                                break;
                        }

                        Intent intent = new Intent(DriverSettingsActivity.this, DriverHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                builder.show();

                break;
            case R.id.ib_user_photo:

                break;
        }
    }
}

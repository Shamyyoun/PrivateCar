package com.privatecar.privatecar.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.Config;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.ButtonHighlighterOnTouchListener;
import com.privatecar.privatecar.utils.Utils;

/**
 * Created by Shamyyoun on 2/20/2016.
 */
public class CustomerInviteFriendsActivity extends BasicBackActivity {
    private Button btnFacebook;
    private Button btnGooglePlus;
    private Button btnWhatsApp;
    private String inviteMsg;
    private String website;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_invite_friends);

        // init views
        btnFacebook = (Button) findViewById(R.id.btn_facebook);
        btnGooglePlus = (Button) findViewById(R.id.btn_google_plus);
        btnWhatsApp = (Button) findViewById(R.id.btn_whats_app);

        // check if whats app is installed
        if (!Utils.isAppInstalledAndEnabled(this, Utils.PACKAGE_WHATSAPP)) {
            // hide whats app button
            btnWhatsApp.setVisibility(View.GONE);
        }

        // get values from config
        website = AppUtils.getConfigValue(this, Config.KEY_WEBSITE_URL);
        inviteMsg = AppUtils.getConfigValue(this, Config.KEY_INVITE_TEMPLATE) + "\n" + website;

        // add touch listeners
        btnFacebook.setOnTouchListener(new ButtonHighlighterOnTouchListener(this, R.drawable.invite_friends_from_facebook));
        btnGooglePlus.setOnTouchListener(new ButtonHighlighterOnTouchListener(this, R.drawable.invite_friends_from_google_plus));
        btnWhatsApp.setOnTouchListener(new ButtonHighlighterOnTouchListener(this, R.drawable.invite_friends_from_whats_app));

        // add click listeners
        btnFacebook.setOnClickListener(this);
        btnGooglePlus.setOnClickListener(this);
        btnWhatsApp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_facebook:
                // check if facebook messenger is installed
                if (Utils.isAppInstalledAndEnabled(this, Utils.PACKAGE_FACEBOOK_MESSENGER)) {
                    // share to messenger
                    Utils.shareTextToApp(this, Utils.PACKAGE_FACEBOOK_MESSENGER, inviteMsg);
                } else if (Utils.isAppInstalledAndEnabled(this, Utils.PACKAGE_FACEBOOK)) {
                    // share to facebook
                    Utils.shareTextToApp(this, Utils.PACKAGE_FACEBOOK, inviteMsg);
                } else {
                    // open the facebook sharer url
                    String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + website;
                    Utils.openBrowser(this, sharerUrl);
                }
                break;

            case R.id.btn_google_plus:
                // check if google+ is installed
                if (Utils.isAppInstalledAndEnabled(this, Utils.PACKAGE_GOOGLE_PLUS)) {
                    // share to google+
                    Utils.shareTextToApp(this, Utils.PACKAGE_GOOGLE_PLUS, inviteMsg);
                } else {
                    // open google+ sharer url
                    String sharerUrl = "https://plus.google.com/share?url=" + website;
                    Utils.openBrowser(this, sharerUrl);
                }
                break;

            case R.id.btn_whats_app:
                Utils.shareTextToApp(this, Utils.PACKAGE_WHATSAPP, inviteMsg);
                break;

            default:
                super.onClick(v);
        }
    }
}

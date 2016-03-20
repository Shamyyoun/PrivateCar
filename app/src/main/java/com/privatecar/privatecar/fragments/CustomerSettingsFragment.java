package com.privatecar.privatecar.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.activities.AnonymousHomeActivity;
import com.privatecar.privatecar.activities.ChangePasswordActivity;
import com.privatecar.privatecar.activities.CustomerAboutPrivateActivity;
import com.privatecar.privatecar.activities.CustomerAddCreditCardActivity;
import com.privatecar.privatecar.activities.CustomerAddPromoCodeActivity;
import com.privatecar.privatecar.activities.CustomerHomeActivity;
import com.privatecar.privatecar.activities.CustomerInviteFriendsActivity;
import com.privatecar.privatecar.dialogs.ChangeLanguageDialog;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.enums.Language;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.Utils;

import java.util.Locale;

public class CustomerSettingsFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = CustomerSettingsFragment.class.getName();

    Activity activity;
    View rootView;
    private TextView tvName;
    private TextView tvMobile;
    private TextView tvEmail;
    private TextView tvLanguage;
    private View layoutChangePassword;
    private View layoutAddCreditCard;
    private View layoutPromoCode;
    private View layoutLanguage;
    private View layoutTellFriends;
    private View layoutAboutPrivate;
    private View layoutSignOut;

    private ChangeLanguageDialog languageDialog;

    public CustomerSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_customer_settings, container, false);

            // init views
            tvName = (TextView) rootView.findViewById(R.id.tv_name);
            tvMobile = (TextView) rootView.findViewById(R.id.tv_mobile);
            tvEmail = (TextView) rootView.findViewById(R.id.tv_email);
            tvLanguage = (TextView) rootView.findViewById(R.id.tv_language);
            layoutChangePassword = rootView.findViewById(R.id.layout_change_password);
            layoutAddCreditCard = rootView.findViewById(R.id.layout_add_credit_card);
            layoutPromoCode = rootView.findViewById(R.id.layout_promo_code);
            layoutLanguage = rootView.findViewById(R.id.layout_language);
            layoutTellFriends = rootView.findViewById(R.id.layout_tell_friends);
            layoutAboutPrivate = rootView.findViewById(R.id.layout_about_private);
            layoutSignOut = rootView.findViewById(R.id.layout_sign_out);

            // add click listeners
            layoutChangePassword.setOnClickListener(this);
            layoutAddCreditCard.setOnClickListener(this);
            layoutPromoCode.setOnClickListener(this);
            layoutLanguage.setOnClickListener(this);
            layoutTellFriends.setOnClickListener(this);
            layoutAboutPrivate.setOnClickListener(this);
            layoutSignOut.setOnClickListener(this);

            // update the ui
            User user = AppUtils.getCachedUser(activity);
            tvName.setText(user.getCustomerAccountDetails().getFullname());
            tvMobile.setText(user.getCustomerAccountDetails().getMobile());
            tvEmail.setText(user.getCustomerAccountDetails().getEmail());
            tvLanguage.setText(Utils.getAppLanguage().equals(Locale.ENGLISH.getLanguage()) ? R.string.english : R.string.arabic);
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_change_password:
                onChangePassword();
                break;

            case R.id.layout_add_credit_card:
                // open add credit card activity
                startActivity(new Intent(activity, CustomerAddCreditCardActivity.class));
                break;

            case R.id.layout_promo_code:
                // open add promo code activity
                startActivity(new Intent(activity, CustomerAddPromoCodeActivity.class));
                break;

            case R.id.layout_language:
                onChangeLanguage();
                break;

            case R.id.layout_tell_friends:
                // open invite friends activity
                startActivity(new Intent(activity, CustomerInviteFriendsActivity.class));
                break;

            case R.id.layout_about_private:
                // open about private activity
                startActivity(new Intent(activity, CustomerAboutPrivateActivity.class));
                break;

            case R.id.layout_sign_out:
                // clear cache and goto anonymous home activity
                AppUtils.clearCache(activity);
                Intent intent = new Intent(activity, AnonymousHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

        }
    }

    private void onChangePassword() {
        // check the user grant type
        User user = AppUtils.getCachedUser(activity);
        if (Utils.isNullOrEmpty(user.getUserName())) {
            // this is a social user
            // show msg
            Utils.showLongToast(activity, R.string.social_users_cant_change_password);
        } else {
            // open change password activity
            startActivity(new Intent(activity, ChangePasswordActivity.class));
        }
    }

    private void onChangeLanguage() {
        languageDialog = new ChangeLanguageDialog(activity, new ChangeLanguageDialog.OnLanguageSelectedListener() {
            @Override
            public void onLanguageSelected(Language language) {
                // change app locale and cache it
                Utils.changeAppLocale(activity, language.getValue());
                Utils.cacheString(activity, Const.CACHE_LOCALE, language.getValue());

                // finish and start customer home activity again
                Intent intent = new Intent(activity, CustomerHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        languageDialog.show();
    }
}

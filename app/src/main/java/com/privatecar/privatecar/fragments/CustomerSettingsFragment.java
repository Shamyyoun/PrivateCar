package com.privatecar.privatecar.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.activities.ChangePasswordActivity;
import com.privatecar.privatecar.activities.CustomerAboutPrivateActivity;
import com.privatecar.privatecar.activities.CustomerAddCreditCardActivity;
import com.privatecar.privatecar.activities.CustomerAddPromoCodeActivity;
import com.privatecar.privatecar.activities.CustomerInviteFriendsActivity;
import com.privatecar.privatecar.dialogs.CustomerChangeLanguageDialog;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.Utils;

public class CustomerSettingsFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = CustomerSettingsFragment.class.getName();

    Activity activity;
    View rootView;
    View layoutChangePassword;
    View layoutAddCreditCard;
    View layoutPromoCode;
    View layoutLanguage;
    View layoutTellFriends;
    View layoutAboutPrivate;

    CustomerChangeLanguageDialog languageDialog;

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

            // customize change password layout
            layoutChangePassword = rootView.findViewById(R.id.layout_change_password);
            layoutChangePassword.setOnClickListener(this);

            // customize add credit card layout
            layoutAddCreditCard = rootView.findViewById(R.id.layout_add_credit_card);
            layoutAddCreditCard.setOnClickListener(this);

            // customize promo code layout
            layoutPromoCode = rootView.findViewById(R.id.layout_promo_code);
            layoutPromoCode.setOnClickListener(this);

            // customize language layout
            layoutLanguage = rootView.findViewById(R.id.layout_language);
            layoutLanguage.setOnClickListener(this);

            // customize tell friends layout
            layoutTellFriends = rootView.findViewById(R.id.layout_tell_friends);
            layoutTellFriends.setOnClickListener(this);

            // customize about private  layout
            layoutAboutPrivate = rootView.findViewById(R.id.layout_about_private);
            layoutAboutPrivate.setOnClickListener(this);
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
                // show change language dialog
                if (languageDialog == null) {
                    languageDialog = new CustomerChangeLanguageDialog(activity);
                }
                languageDialog.show();

                break;

            case R.id.layout_tell_friends:
                // open invite friends activity
                startActivity(new Intent(activity, CustomerInviteFriendsActivity.class));
                break;

            case R.id.layout_about_private:
                // open about private activity
                startActivity(new Intent(activity, CustomerAboutPrivateActivity.class));
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
}

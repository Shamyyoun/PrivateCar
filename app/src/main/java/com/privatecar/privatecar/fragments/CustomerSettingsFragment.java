package com.privatecar.privatecar.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.activities.CustomerAddCreditCardActivity;
import com.privatecar.privatecar.activities.CustomerAddPromoCodeActivity;
import com.privatecar.privatecar.activities.CustomerChangePasswordActivity;
import com.privatecar.privatecar.dialogs.CustomerChangeLanguageDialog;

public class CustomerSettingsFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = CustomerSettingsFragment.class.getName();

    Activity activity;
    View rootView;
    View layoutChangePassword;
    View layoutAddCreditCard;
    View layoutPromoCode;
    View layoutLanguage;

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
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_change_password:
                // open change password activity
                startActivity(new Intent(activity, CustomerChangePasswordActivity.class));
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

        }
    }
}

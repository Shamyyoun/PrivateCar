package com.privateegy.privatecar.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.privateegy.privatecar.R;
import com.privateegy.privatecar.models.enums.PaymentType;

/**
 * Created by Shamyyoun on 2/17/2016.
 */
public class PaymentTypeDialog extends ParentDialog implements View.OnClickListener {
    private OnTypeSelectedListener listener;
    private Button btnCash;
    private Button btnAccountCredit;

    public PaymentTypeDialog(Context context, OnTypeSelectedListener listener) {
        super(context);
        setContentView(R.layout.dialog_payment_type);

        this.listener = listener;

        btnCash = (Button) findViewById(R.id.btn_cash);
        btnAccountCredit = (Button) findViewById(R.id.btn_account_credit);
        btnCash.setOnClickListener(this);
        btnAccountCredit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cash:
                listener.onTypeSelected(PaymentType.CASH);
                break;

            case R.id.btn_account_credit:
                listener.onTypeSelected(PaymentType.ACCOUNT_CREDIT);
                break;
        }
    }

    public interface OnTypeSelectedListener {
        public void onTypeSelected(PaymentType type);
    }
}

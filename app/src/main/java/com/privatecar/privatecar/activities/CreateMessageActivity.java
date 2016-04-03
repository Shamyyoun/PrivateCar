package com.privatecar.privatecar.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.responses.GeneralResponse;
import com.privatecar.privatecar.requests.CommonRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.DialogUtils;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

public class CreateMessageActivity extends BasicBackActivity implements View.OnClickListener, RequestListener<GeneralResponse> {

    private EditText etMessageBody;
    private Button btnSend;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);

        etMessageBody = (EditText) findViewById(R.id.et_message_body);
        btnSend = (Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.btn_send:
                if (Utils.isEmpty(etMessageBody)) {
                    Utils.showLongToast(getApplicationContext(), R.string.please_enter_message_body);
                } else {
                    dialog = DialogUtils.showProgressDialog(this, R.string.sending_message, true);
                    Utils.hideKeyboard(etMessageBody);
                    CommonRequests.sendMessage(this, this, AppUtils.getCachedUser(this).getAccessToken(), Utils.getText(etMessageBody));
                }

                break;
        }

    }

    @Override
    public void onSuccess(GeneralResponse response, String apiName) {
        dialog.dismiss();
        if (response.isSuccess()) {
            Utils.showLongToast(this, R.string.message_sent);
            onBackPressed();
        } else {
            Utils.showLongToast(this, R.string.failed_sending_message);
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        dialog.dismiss();
        Utils.showLongToast(this, message);
        Utils.LogE(message);
    }
}

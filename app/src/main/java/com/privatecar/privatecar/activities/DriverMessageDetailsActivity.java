package com.privatecar.privatecar.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.Message;
import com.privatecar.privatecar.models.responses.GeneralResponse;
import com.privatecar.privatecar.requests.DriverRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.DialogUtils;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

public class DriverMessageDetailsActivity extends BasicBackActivity implements RequestListener<GeneralResponse> {

    Message message;
    int position; // the position of the message in the array list

    private ImageButton ibDeleteMessage;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_message_details);

        message = (Message) getIntent().getSerializableExtra("message");
        position = getIntent().getIntExtra("position", -1);
        if (message == null || position == -1) {
            Log.e(Const.LOG_TAG, "message = null");
            onBackPressed();
            return;
        }

        TextView tvMessageDate = (TextView) findViewById(R.id.tv_message_date);
        TextView tvMessageBody = (TextView) findViewById(R.id.tv_message_body);

        tvMessageDate.setText(message.getCreatedAt());
        tvMessageBody.setText(message.getMessage());

        ibDeleteMessage = (ImageButton) findViewById(R.id.ib_delete_message);
        ibDeleteMessage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ib_delete_message:
                progressDialog = DialogUtils.showProgressDialog(this, R.string.deleting_message);
                DriverRequests.messagesDelete(this, this, AppUtils.getCachedUser(this).getAccessToken(), String.valueOf(message.getId()));
                break;
        }
    }

    @Override
    public void onSuccess(GeneralResponse response, String apiName) {
        progressDialog.dismiss();
        if (response.isSuccess()) {
            Utils.showLongToast(getApplicationContext(), R.string.message_deleted);
            Intent intent = new Intent();
            intent.putExtra("position", position);
            setResult(RESULT_OK, intent);
            onBackPressed();
        } else {
            Utils.showLongToast(getApplicationContext(), R.string.failed_deleting_message);
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        progressDialog.dismiss();
        Utils.LogE(message);
        Utils.showLongToast(getApplicationContext(), message);
    }
}

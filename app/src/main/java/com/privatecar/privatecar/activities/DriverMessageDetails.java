package com.privatecar.privatecar.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.Message;

public class DriverMessageDetails extends BasicBackActivity {

    Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_message_details);

        message = (Message) getIntent().getSerializableExtra("message");
        if (message == null) {
            Log.e(Const.LOG_TAG, "message = null");
            onBackPressed();
            return;
        }

        TextView tvMessageDate = (TextView) findViewById(R.id.tv_message_date);
        TextView tvMessageTitle = (TextView) findViewById(R.id.tv_message_title);
        TextView tvMessageBody = (TextView) findViewById(R.id.tv_message_body);

        tvMessageDate.setText(message.getDate());
        tvMessageTitle.setText(message.getTitle());
        tvMessageBody.setText(message.getBody());
    }
}

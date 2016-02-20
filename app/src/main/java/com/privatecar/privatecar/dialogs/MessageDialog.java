package com.privatecar.privatecar.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.Message;

/**
 * Created by Shamyyoun on 2/17/2016.
 */
public class MessageDialog extends ParentDialog {
    private Context context;
    private TextView textTitle;
    private TextView textMessage;
    private Button buttonPositive;

    public MessageDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_message);
        this.context = context;

        // init views
        textTitle = (TextView) findViewById(R.id.tv_title);
        textMessage = (TextView) findViewById(R.id.tv_message);
        buttonPositive = (Button) findViewById(R.id.btn_positive);

        // set default positive button click listener to dismiss the dialog
        buttonPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public MessageDialog setDialogTitle(String title) {
        textTitle.setText(title);
        return this;
    }

    public MessageDialog setDialogTitle(int titleResId) {
        return setDialogTitle(context.getString(titleResId));
    }

    public MessageDialog setMessage(String message) {
        textMessage.setText(message);
        return this;
    }

    public MessageDialog setMessage(int messageResId) {
        return setMessage(context.getString(messageResId));
    }

    public MessageDialog setPositiveButtonText(String text) {
        buttonPositive.setText(text);
        return this;
    }

    public MessageDialog setPositiveButtonText(int textResId) {
        return setPositiveButtonText(context.getString(textResId));
    }

    public MessageDialog setPositiveButtonClickListener(View.OnClickListener clickListener) {
        buttonPositive.setOnClickListener(clickListener);
        return this;
    }
}

package com.privatecar.privatecar.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TimePicker;

import com.privatecar.privatecar.R;

/**
 * Created by Shamyyoun on 2/17/2016.
 */
public class CustomerCarTypeDialog extends ParentDialog {

    public CustomerCarTypeDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_customer_car_type);
    }
}

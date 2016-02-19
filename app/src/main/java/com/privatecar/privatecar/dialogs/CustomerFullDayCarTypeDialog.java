package com.privatecar.privatecar.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.privatecar.privatecar.R;

/**
 * Created by Shamyyoun on 2/17/2016.
 */
public class CustomerFullDayCarTypeDialog extends ParentDialog {

    public CustomerFullDayCarTypeDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_customer_full_day_car_type);
    }
}

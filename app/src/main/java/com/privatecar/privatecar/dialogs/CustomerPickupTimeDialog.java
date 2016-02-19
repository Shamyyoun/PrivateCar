package com.privatecar.privatecar.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TimePicker;

import com.privatecar.privatecar.R;

/**
 * Created by Shamyyoun on 2/17/2016.
 */
public class CustomerPickupTimeDialog extends Dialog {

    public CustomerPickupTimeDialog(Context context) {
        super(context, R.style.MyDialogTheme);

        // set no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // set view layout
        setContentView(R.layout.dialog_customer_date_time_picker);

        // init views
        TimePicker timePicker = (TimePicker) findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
    }

    @Override
    public void show() {
        // customize dialog width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

        // show it
        super.show();
        getWindow().setAttributes(lp);
    }
}

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
public class CustomerCarTypeDialog extends Dialog {

    public CustomerCarTypeDialog(Context context) {
        super(context);

        // customize dialog window
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER);

        // set view layout
        setContentView(R.layout.dialog_customer_car_type);
    }

    @Override
    public void show() {
        // customize dialog width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        // show it
        super.show();
        getWindow().setAttributes(lp);
    }
}
package com.privateegy.privatecar.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.privateegy.privatecar.R;

/**
 * Created by Shamyyoun on 2/17/2016.
 */
public class ParentDialog extends Dialog {

    public ParentDialog(Context context) {
        super(context, R.style.MyDialogTheme);

        // set no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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

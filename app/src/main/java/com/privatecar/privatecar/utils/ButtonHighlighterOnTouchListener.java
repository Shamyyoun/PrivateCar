package com.privatecar.privatecar.utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by balamuddin on 10/4/2015.
 * A utility class to highlight the button on touch works with (Button & ImageButton)
 */

public class ButtonHighlighterOnTouchListener implements View.OnTouchListener {
    //http://stackoverflow.com/a/11070775/4332959

    private Activity activity;

    int drawableId;
    public ButtonHighlighterOnTouchListener(Activity activity, int drawableId){
        this.drawableId = drawableId;
        this.activity = activity;

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Color.LTGRAY sets how much to darken - tweak as desired
                setColorFilter(v, Color.LTGRAY);
                break;
            // remove the filter when moving off the button
            // the same way a selector implementation would
            case MotionEvent.ACTION_MOVE:
                Rect r = new Rect();
                v.getLocalVisibleRect(r);
                if (!r.contains((int) event.getX(), (int) event.getY())) {
                    setColorFilter(v, null);
                }
                break;
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                setColorFilter(v, null);
                break;
        }
        return false;
    }

    private void setColorFilter(View v, Integer filter) {
        Drawable drawable;
        if (v instanceof ImageButton) {
            drawable = ((ImageButton) v).getDrawable();
        } else {
            drawable = v.getBackground();
        }

        if (filter == null) {
            drawable.clearColorFilter();
        } else {
            // To lighten instead of darken, try this:
            // LightingColorFilter lighten = new LightingColorFilter(0xFFFFFF, filter);
            LightingColorFilter darken = new LightingColorFilter(filter, 1);

            // a workaround for lollipop mutate issue
            drawable = activity.getResources().getDrawable(drawableId).mutate();
            drawable.setColorFilter(darken);
            if(v instanceof ImageButton){
                ((ImageButton) v).setImageDrawable(drawable);
            }else {
                v.setBackgroundDrawable(drawable);
            }

        }

        // required on Android 2.3.7 for filter change to take effect (but not on 4.0.4)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            drawable.invalidateSelf();
        }

    }
}

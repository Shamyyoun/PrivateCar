package com.privatecar.privatecar.utils;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by balamuddin on 10/4/2015.
 * A utility class to highlight an image view on touch event (works with image view)
 */
public class ImageHighlighterOnTouchListener implements View.OnTouchListener {
    //http://stackoverflow.com/a/14278790/4332959

    private final int TRANSPARENT_GREY = Color.argb(0, 185, 185, 185);
    private final int FILTERED_GREY = Color.argb(155, 185, 185, 185);

    ImageView imageView;
    TextView textView;

    public ImageHighlighterOnTouchListener(final ImageView imageView) {
        super();
        this.imageView = imageView;
    }

    public ImageHighlighterOnTouchListener(final TextView textView) {
        super();
        this.textView = textView;
    }

    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (imageView != null) {
            if (action == MotionEvent.ACTION_DOWN) {
                imageView.setColorFilter(FILTERED_GREY);
            }else if(action == MotionEvent.ACTION_MOVE){
                Rect r = new Rect();
                view.getLocalVisibleRect(r);
                if (!r.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                    imageView.clearColorFilter();
                }
            } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                imageView.setColorFilter(TRANSPARENT_GREY); // or null
            }
        } else {
            for (final Drawable compoundDrawable : textView.getCompoundDrawables()) {
                if (compoundDrawable != null) {
                    if (action == MotionEvent.ACTION_DOWN) {
                        // we use PorterDuff.Mode. SRC_ATOP as our filter color is already transparent
                        // we should have use PorterDuff.Mode.LIGHTEN with a non transparent color
                        compoundDrawable.mutate().setColorFilter(FILTERED_GREY, PorterDuff.Mode.SRC_ATOP);
                    }else if(action == MotionEvent.ACTION_MOVE){
                        Rect r = new Rect();
                        view.getLocalVisibleRect(r);
                        if (!r.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                            compoundDrawable.clearColorFilter();
                        }
                    } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                            || action == MotionEvent.ACTION_OUTSIDE) {
                        compoundDrawable.mutate().setColorFilter(TRANSPARENT_GREY, PorterDuff.Mode.SRC_ATOP); // or null
                    }
                }
            }
        }
        return false;

    }
}

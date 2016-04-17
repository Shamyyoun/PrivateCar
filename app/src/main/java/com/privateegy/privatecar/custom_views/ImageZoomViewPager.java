package com.privateegy.privatecar.custom_views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Basim Alamuddin on 24/01/2016.
 * A hack to fix issue with PhotoView library.
 * https://github.com/chrisbanes/PhotoView/blob/master/sample/src/main/java/uk/co/senab/photoview/sample/HackyDrawerLayout.java
 */
public class ImageZoomViewPager extends ViewPager {

    public ImageZoomViewPager(Context context) {
        super(context);
    }

    public ImageZoomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
}

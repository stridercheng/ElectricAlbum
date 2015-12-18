package com.chengz.android.electricalbum.customview;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * description:
 * User: stridercheng
 * Date: 2015-12-18
 * Time: 21:16
 * FIXME
 */
public class FixedSpeedScroller extends Scroller {
    private int mDuration = 2000;

    public FixedSpeedScroller(Context context) {
        super(context);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}

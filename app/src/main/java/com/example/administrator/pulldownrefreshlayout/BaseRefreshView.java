package com.example.administrator.pulldownrefreshlayout;

import android.app.Activity;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

public abstract class BaseRefreshView extends Drawable implements Drawable.Callback, Animatable {
    private final PullDownRefreshLayout mPullDownRefreshLayout;
    public BaseRefreshView(PullDownRefreshLayout mPullDownRefreshLayout){
        this.mPullDownRefreshLayout = mPullDownRefreshLayout;
    }
    public Context getContext(){
        if(mPullDownRefreshLayout != null){
            return mPullDownRefreshLayout.getContext();
        }
        return null;
    }
    //设置百分比，是否重绘
    public abstract void setRefresh(boolean mRefresh, boolean invalidate);
    public abstract void setPercent(float percent, boolean invalidate);
    public abstract void setRotate(float mRotate,boolean invalidate);
    public abstract void offsetTopAndBottom(int offset);

    @Override
    public void invalidateDrawable(Drawable who){
        final Callback callback = getCallback();
        if(callback != null){
            callback.invalidateDrawable(this);
        }
    }
    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        final Callback callback = getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, what, when);
        }
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        final Callback callback = getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, what);
        }
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

}

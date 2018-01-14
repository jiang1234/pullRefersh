package com.example.administrator.pulldownrefreshlayout;

import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SunRefreshView extends BaseRefreshView{
    private final PullDownRefreshLayout mPullDownRefreshLayout;
    private int mTopTouch;
    private int mCurrentOffSetHeader;

    public SunRefreshView(PullDownRefreshLayout mPullDownRefreshLayout){
        super(mPullDownRefreshLayout);
        this.mPullDownRefreshLayout = mPullDownRefreshLayout;
        mTopTouch = mPullDownRefreshLayout.getTopTouch();
    }

    @Override
    public void draw(Canvas canvas){

    }
    @Override
    public void start(){

    }
    @Override
    public void stop(){

    }
    @Override
    public boolean isRunning(){
        return false;
    }

    @Override
    public void setPercent(float precent, boolean invalidate) {

    }

    @Override
    public void offsetTopAndBottom(int offset) {
        mTopTouch = mTopTouch + offset;
        invalidateSelf();
    }
}

package com.example.administrator.pulldownrefreshlayout;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PullDownRefreshLayout extends ViewGroup{
    private int mTouchSlop;
    private int mHeaderHight;
    //初始header的位置
    private int mOriginalOffSetHeader;
    //现在header的位置
    private int mCurrentOffSetHeader;
    private View mTouch;
    private ImageView mHeader;
    public PullDownRefreshLayout(Context context){
        this(context,null);
    }
    public PullDownRefreshLayout(Context context, AttributeSet attrs){
        super(context,attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        if(attrs != null){
            //以后有了自定义属性，再加
        }

    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        View child1 = getChildAt(0);
        View child2 = getChildAt(1);
        if(mHeader == null && mTouch == null){
            if(child1 instanceof ImageView){
                mHeader = (ImageView) child1;
                mTouch = child2;
            }else{
                mHeader = (ImageView) child2;
                mTouch = child1;
            }
        }
        measureChild(mHeader,widthMeasureSpec,heightMeasureSpec);
        measureChild(mTouch,widthMeasureSpec,heightMeasureSpec);

    }
    protected void onLayout(boolean changed,int left,int top,int right,int bottom){
        int mHeaderWidth = mHeader.getMeasuredWidth();
        int mHeaderHeight = mHeader.getMeasuredHeight();
        int mTouchWidth = mTouch.getMeasuredWidth();
        int mTouchHeight = mTouch.getMeasuredHeight();
        mOriginalOffSetHeader = -mHeaderHeight;
        mCurrentOffSetHeader = -mHeaderHeight;
        int mHeaderLeft = getPaddingLeft();
        int mHeaderRight = mHeaderLeft + mHeaderWidth;
        int mHeaderTop = mOriginalOffSetHeader;
        int mHeaderBottom = mHeaderTop + mHeaderHeight;
        mHeader.layout(mHeaderLeft,mHeaderTop,mHeaderRight,mHeaderBottom);
        int mTouchLeft = getPaddingLeft();
        int mTouchRight = mTouchLeft + mTouchWidth;
        int mTouchTop = getPaddingTop()+mHeaderBottom;
        int mTouchBottom = mTouchTop + mTouchHeight;
        mTouch.layout(mTouchLeft,mTouchTop,mTouchRight,mTouchBottom);
    }
}

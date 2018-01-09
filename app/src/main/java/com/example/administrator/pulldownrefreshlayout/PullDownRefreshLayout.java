package com.example.administrator.pulldownrefreshlayout;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.NestedScrollingParentHelper;



public class PullDownRefreshLayout extends ViewGroup implements NestedScrollingParent{
    private static final String Log_TAG = PullDownRefreshLayout.class.getSimpleName();
    private int mTouchSlop;
    private int mHeaderHight;
    //初始header的位置
    private int mOriginalOffSetHeader;
    //现在header的位置
    private int mCurrentOffSetHeader;
    private View mTouch;
    private ImageView mHeader;
    private  NestedScrollingParentHelper mNestedScrollingParentHelper;
    private boolean isHeaderShow;
    public PullDownRefreshLayout(Context context){
        this(context,null);
    }
    public PullDownRefreshLayout(Context context, AttributeSet attrs){
        super(context,attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        if(attrs != null){
            //以后有了自定义属性，再加
        }
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this){};
        isHeaderShow = false;
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
    //一下为NestedScrolling嵌套滚动机制
    //NestedScrollingParent
    @Override
    public boolean onStartNestedScroll(View child,View target,int nestedScrollAxes){
        Log.i(Log_TAG, "onStartNestedScroll: ");
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL)!=0;
    }
    @Override
    public void onNestedScrollAccepted(View child,View target,int nestedScrollAxes){
        Log.i(Log_TAG, "onNestedScrollAccepted: ");
        mNestedScrollingParentHelper.onNestedScrollAccepted(child,target,nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        Log.i(Log_TAG, "onStopNestedScroll: ");
        mNestedScrollingParentHelper.onStopNestedScroll(target);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.i(Log_TAG, "onNestedScroll: ");

    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        Log.i(Log_TAG, "onNestedPreScroll: ");
        if(dy<0 && !canScrollingUp()){
            if(dy < mCurrentOffSetHeader){
                //滑动距离会使Header超出所允许的高度
                consumed[1] = (int)(mCurrentOffSetHeader*0.5);
                //dy -= consumed[1];
                mCurrentOffSetHeader -= consumed[1];
            }else{
                consumed[1] = (int)(dy*0.5);
                //dy = 0;
                mCurrentOffSetHeader -= consumed[1];
            }
            scrollBy(0,consumed[1]);
            isHeaderShow = true;
            Log.i(Log_TAG, "onNestedPreScroll:Up "+dy+"consumed"+consumed[1]+"header"+mCurrentOffSetHeader);
        }
        if(dy>0 && !canScrollingDown()){
            if(mCurrentOffSetHeader - dy <mOriginalOffSetHeader){
                //滑动距离会使Header超出所允许的高度
                consumed[1] = mCurrentOffSetHeader - mOriginalOffSetHeader;
                //dy -= consumed[1];
                mCurrentOffSetHeader = mOriginalOffSetHeader;
            }else{
                consumed[1] = dy;
                //dy = 0;
                mCurrentOffSetHeader -= dy;
            }
            scrollBy(0,consumed[1]);
            Log.i(Log_TAG, "onNestedPreScroll:Down "+dy+"consumed"+consumed[1]+"header"+mCurrentOffSetHeader);
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.i(Log_TAG, "onNestedFling: ");
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.i(Log_TAG, "onNestedPreFling: ");
        return false;
    }

    @Override
    public int getNestedScrollAxes(){
        Log.i(Log_TAG, "getNestedScrollAxes: ");
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }
    //判断可滚动的View是否滚到了顶部
    //
    public boolean canScrollingUp(){
        return ViewCompat.canScrollVertically(mTouch,-1);
    }
    public boolean canScrollingDown(){
        return ViewCompat.canScrollVertically(mTouch,1);
    }
}

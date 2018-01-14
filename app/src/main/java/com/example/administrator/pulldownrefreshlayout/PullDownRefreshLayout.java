package com.example.administrator.pulldownrefreshlayout;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.NestedScrollingParentHelper;
import android.widget.Scroller;


public class PullDownRefreshLayout extends ViewGroup implements NestedScrollingParent{
    private static final String Log_TAG = PullDownRefreshLayout.class.getSimpleName();
    private BaseRefreshView mBaseRefreshView;
    private int mTouchSlop;
    private int mHeaderHight;
    //初始header的位置
    private int mOriginalOffSetHeader;
    //现在header的位置
    private int mCurrentOffSetHeader;
    private int mLastXIntercept;
    private int mLastYIntercept;
    private float mPercent;
    private int mTopTouch;
    private int mInitTouch;
    private int mInitY;
    private View mTouch;
    private VelocityTracker mVelocityTracker;
    private Scroller mScroller;

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
        setWillNotDraw(false);
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this){};
        isHeaderShow = false;
        mVelocityTracker = VelocityTracker.obtain();
        mScroller = new Scroller(getContext());
        mBaseRefreshView = new SunRefreshView(this);
    }
    public int getTopTouch(){
        return this.mTopTouch;
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
        mTopTouch = mTouchTop;
        mTouch.layout(mTouchLeft,mTouchTop,mTouchRight,mTouchBottom);
    }
    //还未考虑多指触碰的情况，先考虑单指触碰
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){
        mVelocityTracker.addMovement(event);
        boolean intercepted = false;
        int x = (int)event.getX();
        int y = (int)event.getY();
      //  Log.i(Log_TAG, "onInterceptTouchEvent:"+"x"+x+"y"+y+"lastx"+mLastXIntercept+"lasty"+mLastYIntercept);
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                Log.i(Log_TAG, "onInterceptTouchEvent: ACTION_DOWN");
                mInitY = y;
               // Log.i(Log_TAG, "onInterceptTouchEvent:ACTION_DOWN"+"x"+x+"y"+y+"lastx"+mLastXIntercept+"lasty"+mLastYIntercept);
                intercepted = false;
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                Log.i(Log_TAG, "onInterceptTouchEvent: ACTION_MOVE1");
               // Log.i(Log_TAG, "onInterceptTouchEvent:ACTION_MOVE"+"x"+x+"y"+y+"lastx"+mLastXIntercept+"lasty"+mLastYIntercept);
                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastYIntercept;
                if(canScrollingUp()){
                    Log.i(Log_TAG, "onInterceptTouchEvent: ACTION_MOVE2"+canScrollingUp()+Math.abs(deltaY)+"aa" + mTouchSlop+"aa"+deltaY);
                    return false;
                }

                //如果是垂直方向的滑动
                //Log.i(Log_TAG, "onInterceptTouchEvent: ACTION_MOVE垂直"+"x"+x+"y"+y+"dy"+deltaY);
                if(Math.abs(deltaY)>Math.abs(deltaX)){
                    Log.i(Log_TAG, "onInterceptTouchEvent: ACTION_MOVE3");
                    mVelocityTracker.computeCurrentVelocity(1000);
                   //Log.i(Log_TAG, "onInterceptTouchEvent: ACTION_UP垂直"+isScrollingDown(mVelocityTracker.getXVelocity()));
                    if(isScrollingDown(mVelocityTracker.getYVelocity()) && Math.abs(deltaY) > mTouchSlop){
                        Log.i(Log_TAG, "onInterceptTouchEvent: ACTION_MOVE4"+mVelocityTracker.getYVelocity());
                        intercepted = true;
                    }else{
                        Log.i(Log_TAG, "onInterceptTouchEvent: ACTION_MOVE5");
                        intercepted = false;
                    }
                }else{
                    Log.i(Log_TAG, "onInterceptTouchEvent: ACTION_MOVE6");
                    intercepted = false;
                }
                mVelocityTracker.clear();
                break;
            }
            case MotionEvent.ACTION_CANCEL:
                Log.i(Log_TAG, "onInterceptTouchEvent: ACTION_CANCEL");
            case MotionEvent.ACTION_UP:{
                Log.i(Log_TAG, "onInterceptTouchEvent: ACTION_UP");
                break;
            }
            default:
                break;
        }
        mLastXIntercept = x;
        mLastYIntercept = y;
        Log.i(Log_TAG, "onInterceptTouchEvent: return"+intercepted);
        return intercepted;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        mVelocityTracker.addMovement(event);
        int x = (int)event.getX();
        int y = (int)event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                Log.i(Log_TAG, "onTouchEvent: ACTION_DOWN");
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                Log.i(Log_TAG, "onTouchEvent: ACTION_MOVE");
                int deltaY = mLastYIntercept - y;
                int scrollY = 0;
                if(deltaY < mCurrentOffSetHeader){
                    scrollY = (int)(mCurrentOffSetHeader*0.2);
                }else{
                    scrollY = (int)(deltaY*0.2);
                }
                mCurrentOffSetHeader -= scrollY;
                mPercent = mCurrentOffSetHeader/mOriginalOffSetHeader;
                offsetTopAndBottom(-scrollY);
                //scrollBy(0,scrollY);

                break;
            }
            case MotionEvent.ACTION_UP:{
                Log.i(Log_TAG, "onTouchEvent: ACTION_UP");
                int dy = mCurrentOffSetHeader - mOriginalOffSetHeader;
                int scrollY = (int)(dy);
                scrollBy(0,scrollY);
                mCurrentOffSetHeader -= scrollY;
                isHeaderShow = false;
                break;
            }
            default:
                break;
        }
        return true;
    }
    //一下为NestedScrolling嵌套滚动机制
    //NestedScrollingParent
    @Override
    public boolean onStartNestedScroll(View child,View target,int nestedScrollAxes){
      //  Log.i(Log_TAG, "onStartNestedScroll: ");
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL)!=0;
    }
    @Override
    public void onNestedScrollAccepted(View child,View target,int nestedScrollAxes){
      //  Log.i(Log_TAG, "onNestedScrollAccepted: ");
        mNestedScrollingParentHelper.onNestedScrollAccepted(child,target,nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
       // Log.i(Log_TAG, "onStopNestedScroll: ");
        mNestedScrollingParentHelper.onStopNestedScroll(target);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        //Log.i(Log_TAG, "onNestedScroll: ");

    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
       // Log.i(Log_TAG, "onNestedPreScroll: ");
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
            //Log.i(Log_TAG, "onNestedPreScroll:Up "+dy+"consumed"+consumed[1]+"header"+mCurrentOffSetHeader);
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
           // Log.i(Log_TAG, "onNestedPreScroll:Down "+dy+"consumed"+consumed[1]+"header"+mCurrentOffSetHeader);
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        //Log.i(Log_TAG, "onNestedFling: ");
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
      //  Log.i(Log_TAG, "onNestedPreFling: ");
        return false;
    }

    @Override
    public int getNestedScrollAxes(){
      //  Log.i(Log_TAG, "getNestedScrollAxes: ");
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
    public boolean isScrollingDown(float yVelocity){
        if(yVelocity < 0){
            return false;
        }else{
            return true;
        }
    }
    public boolean isScrollingRight(float xVelocity){
        if(xVelocity < 0){
            return false;
        }else{
            return true;
        }
    }
    public void offsetTopAndBottom(int offset){
        mTouch.offsetTopAndBottom(offset);
        mBaseRefreshView.offsetTopAndBottom(offset);
        mTopTouch = mTouch.getTop();
    }
}

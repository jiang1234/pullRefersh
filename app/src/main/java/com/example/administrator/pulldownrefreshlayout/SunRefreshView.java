package com.example.administrator.pulldownrefreshlayout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SunRefreshView extends BaseRefreshView{
    private final PullDownRefreshLayout mPullDownRefreshLayout;
    private int mTopTouch;
    private int mHeaderTop;
    private int mCurrentOffSetHeader;
    private int mOriginalOffSetHeader;
    private int mScreenWidth;
    private int mSkyHeight;
    private int mSkyWidth;
    private int mSkyOffsetWidth;
    private int mSkyOffsetHeight;
    private int mTownHeight;
    private int mTownWidth;
    private int mTownOffsetWidth;
    private int mTownOffsetHeight;
    private int mSunHeight;
    private int mSunWidth;
    private int mSunOffsetWidth;
    private int mSunOffsetHeight;
    private int mMaxPullDown;
    private float mPercent;
    private float mRotate;
    private float mTownRaito = 0.35f;
    private float mSkyRaito = 0.35f;
    private boolean mRefresh;
    private Matrix mMatrix;
    private Bitmap mSky;
    private Bitmap mTown;
    private Bitmap mSun;

    public SunRefreshView(final PullDownRefreshLayout mPullDownRefreshLayout){
        super(mPullDownRefreshLayout);
        this.mPullDownRefreshLayout = mPullDownRefreshLayout;
        mTopTouch = mPullDownRefreshLayout.getTopTouch();
        mCurrentOffSetHeader = mPullDownRefreshLayout.getCurrentOffSetHeader();
        mOriginalOffSetHeader = mPullDownRefreshLayout.getOriginalOffSetHeader();
        mMaxPullDown = mPullDownRefreshLayout.getMaxPullDown();
      // Log.i("initiateDimens","aaaaaaaa"+mMaxPullDown);
        mRefresh = false;
        mMatrix = new Matrix();
        mPullDownRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                initiateDimens(mPullDownRefreshLayout.getWidth());
            }
        });
    }
    public void initiateDimens(int viewWidth){
        mScreenWidth = viewWidth;
        mSkyWidth = mScreenWidth;
        mSkyHeight = (int)(mSkyWidth*mSkyRaito);
        mTownWidth = mScreenWidth;
        mTownHeight = (int)(mScreenWidth*mTownRaito);
        mSunWidth = (int)(mScreenWidth*0.1);
        mSunHeight = mSunWidth;
        mHeaderTop = -mMaxPullDown;
       // Log.i("mSkyHeight","aaaaaaaa"+mSkyHeight);
        setBitmap();
    }
    public void setBitmap(){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        mSky = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.sky,options);
        mSky = Bitmap.createScaledBitmap(mSky,mSkyWidth,mSkyHeight,true);
        mTown = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.buildings,options);
        mTown = Bitmap.createScaledBitmap(mTown,mTownWidth,mTownHeight,true);
        mSun = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.sun,options);
        mSun = Bitmap.createScaledBitmap(mSun,mSunWidth,mSunHeight,true);
    }

    @Override
    public void draw(Canvas canvas){
       // Log.i("SunRefresh","aaaaaaaa"+mHeaderTop);
        final int saveCount = canvas.save();

        //mCurrentOffSetHeader = 0;
        //mTopTouch = 0 - mOriginalOffSetHeader;
        canvas.translate(0,mHeaderTop);
        canvas.clipRect(0,-mHeaderTop,mScreenWidth,mPullDownRefreshLayout.getMaxPullDown());
        drawSky(canvas);
        drawTown(canvas);
        drawSun(canvas);
        canvas.restoreToCount(saveCount);
    }
     public void drawSky(Canvas canvas){
         Matrix matrix = mMatrix;
         matrix.reset();
         //Log.i("drawSky",""+mPercent);
         float skyScale = mPercent;
         float skyStartScale = 0.5f;
         float skyInitialScale = 1.05f;
         if(skyScale - skyStartScale < 0){
             skyScale = skyInitialScale;
         }else{
             skyScale = skyInitialScale - (skyInitialScale - 1)*((skyScale - skyStartScale)/0.5f);
         }
         //Log.i("skyScale",""+skyScale);
         mSkyOffsetWidth = (int)((mScreenWidth - mScreenWidth*skyScale)/2);
         mSkyOffsetHeight = (int)(mPullDownRefreshLayout.getMaxPullDown()*(1 - mPercent));
         matrix.postScale(skyScale,skyScale);
         matrix.postTranslate(mSkyOffsetWidth,mSkyOffsetHeight);
         canvas.drawBitmap(mSky,matrix,null);


     }

    public void drawTown(Canvas canvas){
        Matrix matrix = mMatrix;
        matrix.reset();
        float townScale = mPercent;
        float townStartScale = 0.5f;
        float townInitialScale = 1.05f;
        if(townScale - townStartScale < 0){
            townScale = townInitialScale;
        }else{
            townScale = townInitialScale - (townInitialScale - 1)*((townScale - townStartScale)/0.5f);
        }
        mTownOffsetWidth = (int)((mScreenWidth - mScreenWidth*townScale)/2);
        mTownOffsetHeight = (int)(mPullDownRefreshLayout.getMaxPullDown()*(1 - mPercent));
        matrix.postScale(townScale,townScale);
        matrix.postTranslate(mTownOffsetWidth,mTownOffsetHeight);
        canvas.drawBitmap(mTown,matrix,null);


    }

    public void drawSun(Canvas canvas){
        Matrix matrix = mMatrix;
        matrix.reset();
        float SunScale = mPercent;
        float SunStartScale = 0.5f;
        float SunInitialScale = 1.05f;
        if(SunScale - SunStartScale < 0){
            SunScale = SunInitialScale;
        }else{
            SunScale = SunInitialScale - (SunInitialScale - 1)*((SunScale - SunStartScale)/0.5f);
        }
        mSunOffsetWidth = 0;
        mSunOffsetHeight = (int)(mPullDownRefreshLayout.getMaxPullDown()*(1 - mPercent)) + (int)(mSkyHeight*(mPercent)) - mSunHeight;
        matrix.postScale(SunScale,SunScale);
        matrix.postTranslate(mSunOffsetWidth,mSunOffsetHeight);
        if(mRefresh){
            matrix.postRotate(-360*mRotate,mSunOffsetWidth + mSunWidth/2,mSunOffsetHeight + mSunHeight/2);
        }else{
            matrix.postRotate(360*mPercent,mSunOffsetWidth + mSunWidth/2,mSunOffsetHeight + mSunHeight/2);
        }

        canvas.drawBitmap(mSun,matrix,null);


    }
    @Override
    public void setRefresh(boolean mRefresh,boolean invalidate){
        this.mRefresh = mRefresh;
        if(invalidate)
            invalidateSelf();
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
    public void setPercent(float precent,boolean invalidate) {
        this.mPercent = precent;
        if(invalidate)
            invalidateSelf();
    }
    @Override
    public void setRotate(float mRotate,boolean invalidate) {
        this.mRotate = mRotate;
        if(invalidate)
            invalidateSelf();
    }

    @Override
    public void offsetTopAndBottom(int offset) {
        mHeaderTop = mHeaderTop + offset;
        //mCurrentOffSetHeader += offset;
        invalidateSelf();
    }
}

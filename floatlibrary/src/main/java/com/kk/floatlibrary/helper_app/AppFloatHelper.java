package com.kk.floatlibrary.helper_app;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.kk.floatlibrary.animtor.AdsorptionAnimFactory;
import com.kk.floatlibrary.base.BaseHelper;
import com.kk.floatlibrary.base.RootFrameLayout;
import com.kk.floatlibrary.util.ScreenUtils;


/**
 * Created by zk on 2020/9/18
 * APP单页面内悬浮
 */
public class AppFloatHelper extends BaseHelper {
    public static final String TAG = "AppFloatHelper";

    private RootFrameLayout frameLayout;
    private FrameLayout rootView;
    private Activity mContnet;
    /**
     * 获取屏幕高宽
     */
    private int mScreenWidth = 0;
    private int mScreenHeight = 0;
    /**
     * 获取状态啦高度
     */
    private int mStatusHeight = 0;

    private int mActionBarHight = 0;

    private boolean isDragEnablel = true;
    private FrameLayout parentFrame;
    //获取控件高宽
    private int mViewWidth = 0;
    private int mViewHight = 0;


    public AppFloatHelper(Activity context) {
        mContnet = context;
        mStatusHeight = ScreenUtils.getStatusHeight(mContnet);
        mActionBarHight = ScreenUtils.getActionBarHight(mContnet);
        mScreenWidth = ScreenUtils.getScreenWidth(mContnet);
        mScreenHeight = ScreenUtils.getScreenHeight(mContnet);
    }


    @Override
    public View inflate(int ids) {
        frameLayout = new RootFrameLayout(mContnet);
        rootView = (FrameLayout) LayoutInflater.from(mContnet).inflate(ids, frameLayout, true);
        parentFrame = mContnet.getWindow().getDecorView().findViewById(android.R.id.content);
        return rootView;
    }

    @Override
    public void show() {

        if (rootView == null) {
            Log.e(TAG, "view不能为空");
            return;
        }


        rootView.setVisibility(View.VISIBLE);
        parentFrame.removeView(rootView);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        rootView.setX(initPosX);
        rootView.setY(initPosY);

        parentFrame.addView(rootView, params);

        rootView.post(new Runnable() {
            @Override
            public void run() {
                mViewWidth = rootView.getWidth();
                mViewHight = rootView.getHeight();
            }
        });

        rootView.setOnTouchListener(mOnTouchListener);
        if (OnStatusCallBack != null) {
            OnStatusCallBack.successView(rootView);
        }
    }

    @Override
    public void hide() {
        rootView.setVisibility(View.GONE);
    }

    @Override
    public boolean removeView() {
        parentFrame.removeView(rootView);
        rootView.setX(0);
        rootView.setY(0);
        return true;
    }

    @Override
    public boolean changeView(int x, int y) {
        rootView.setX(x);
        rootView.setY(y);
        return true;
    }

    float upY;
    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (OnMotionEvent != null) {
                OnMotionEvent.getFloatMotionEvent(event);
            }
            if (isDragEnablel) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        AdsorptionAnimFactory.cancleAnimator();
                        //设置滑动边界
                        //获取控件坐标
                        float viewTopLeftX = event.getRawX() - frameLayout.getmDownX();
                        float viewTopRightX = event.getRawX() - frameLayout.getmDownX() + mViewWidth;
                        float viewTopY = event.getRawY() - mStatusHeight - mActionBarHight - frameLayout.getmDownY();
                        float viewBottomY = event.getRawY() - mStatusHeight - mActionBarHight - frameLayout.getmDownY() + mViewHight;

                        if (viewTopLeftX <= 0) {
                            viewTopLeftX = 0;
                        }
                        if (viewTopRightX >= mScreenWidth) {
                            viewTopLeftX = mScreenWidth - mViewWidth;
                        }
                        if (viewTopY <= 0) {
                            viewTopY = 0;
                        }
                        if (viewBottomY >= mScreenHeight - mStatusHeight - mActionBarHight) {
                            viewTopY = mScreenHeight - mViewHight - mStatusHeight - mActionBarHight;
                        }
                        rootView.setX(viewTopLeftX);
                        rootView.setY(viewTopY);
                        break;
                    case MotionEvent.ACTION_UP:

                        //执行吸附动画
                        if(isadsAnima){
                            float upRawX =event.getRawX() - frameLayout.getmDownX();
                            float upRawY = event.getRawY() - mStatusHeight - mActionBarHight - frameLayout.getmDownY();
                            if(upRawY<= 0){
                                upY =0;
                            }else{
                                upY = upRawY;
                            }
                            if(upRawY>=mScreenHeight-mViewHight- mStatusHeight - mActionBarHight){
                                upY = mScreenHeight-mViewHight -mStatusHeight - mActionBarHight;
                            }

                            if(upRawX<0){
                                upRawX=0;
                            }
                            if(upRawX>mScreenWidth-mViewWidth){
                                upRawX = mScreenWidth-mViewWidth;
                            }

                            if (upRawX < mScreenWidth / 2 - (mViewWidth / 2)) {
                                AdsorptionAnimFactory.startAdsAnim(mAdsorbTime, mAbsSorbInterpolator, (int) upRawX, 0, rootView, new AdsorptionAnimFactory.onAnimUpData() {
                                    @Override
                                    public void animX(int x) {
                                        rootView.setX(x);
                                        rootView.setY(upY);
                                    }
                                });
                            } else {
                                AdsorptionAnimFactory.startAdsAnim(mAdsorbTime, mAbsSorbInterpolator, (int) upRawX, mScreenWidth - mViewWidth, rootView, new AdsorptionAnimFactory.onAnimUpData() {
                                    @Override
                                    public void animX(int x) {
                                        rootView.setX(x);
                                        rootView.setY(upY);
                                    }
                                });
                            }
                        }
                        break;
                }
            }
            return true;
        }
    };
}

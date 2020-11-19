package com.kk.floatlibrary.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kk.floatlibrary.R;

/**
 * Created by zk on 2020/9/15
 * 用于处理RootView事件分发和拦截
 */

public class RootFrameLayout extends FrameLayout {

    /**
     * 获取拦截 down X Y
     */
    private int mDownX;
    private int mDownY;

    public int getmDownX() {
        return mDownX;
    }

    public void setmDownX(int mDownX) {
        this.mDownX = mDownX;
    }

    public int getmDownY() {
        return mDownY;
    }

    public void setmDownY(int mDownY) {
        this.mDownY = mDownY;
    }

    public RootFrameLayout(Context context) {
        super(context);
    }

    public RootFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RootFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            //这里我们处理一下，因为如果子控件被获取监听或者子控件是enable状态，那么viewgroup是不会走touch down up 方法也就获取不到xy
            //那么我们就在拦截的时候获取点击的x y
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();
                return false;
            //拦截滑动，方便处理控件跟随滑动
            case MotionEvent.ACTION_MOVE:
                return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

}

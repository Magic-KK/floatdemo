package com.kk.floatlibrary.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import com.kk.floatlibrary.helper_app.AppFloatHelper;
import com.kk.floatlibrary.helper_system.SystemFloatHelper;
import com.kk.floatlibrary.util.FloatConstant;

/**
 * Created by zk on 2020/9/14
 */
public abstract class BaseFloatWindow implements IBaseFloat, SystemFloatHelper.IStatusCallBack, BaseHelper.IMotionEvent {
    public Activity mContetn;
    /**
     * 超类Helper
     */
    private BaseHelper mBaseHelper;

    private int mFloatType;

    /**
     * 创建悬浮窗
     *
     * @param context
     * @param type    窗口类型
     */
    public BaseFloatWindow(Activity context, int type) {
        this.mContetn = context;
        init(type);
        initListener();
    }

    /**
     * 初始化 动态实例化窗口类型
     *
     * @param type
     */
    private void init(int type) {

        mFloatType = type;
        if (type == FloatConstant.FLOAT_SYSTEM_WINDOW || type == FloatConstant.FLOAT_APP_WINDOW || type == FloatConstant.FLOAT_BACKSTAGE_WINDOW) {
            mBaseHelper = new SystemFloatHelper(mContetn, type);
            mBaseHelper.setOnStatusCallBack(this);
            mBaseHelper.setFloatOnMotionEvent(this);
        } else if (type == FloatConstant.FLOAT_ACTIVITY) {
            mBaseHelper = new AppFloatHelper(mContetn);
            mBaseHelper.setOnStatusCallBack(this);
        }
        creatFloatView();
    }

    /**
     * 创建布局
     *
     * @param layoutId
     * @return
     */
    public View createFloatById(int layoutId) {
        return mBaseHelper.inflate(layoutId);
    }

    private void initListener() {

    }

    /**
     * 显示
     */
    public void show() {
        if (mFloatType != FloatConstant.FLOAT_ACTIVITY) {
            mBaseHelper.checkPermission();
        } else {
            mBaseHelper.show();
        }
    }

    /**
     * 隐藏
     */
    public void hide() {
        mBaseHelper.hide();
    }

    /**
     * 设置初始位置
     * @param x
     * @param y
     */
    public void setInitPostion(int x, int y) {
        mBaseHelper.initPostion(x,y);
    }


    /**
     * 更新位置
     *
     * @param x 屏幕坐标x
     * @param y 屏幕坐标y
     * @return
     */
    public boolean updateView(int x, int y) {
        return mBaseHelper.changeView(x, y);
    }

    /**
     * 销毁窗口
     *
     * @return
     */
    public boolean removeView() {
        return mBaseHelper.removeView();
    }

    /**
     * 是否可拖拽
     *
     * @param isDrag
     */
    public void setDragEnable(boolean isDrag) {
        mBaseHelper.setDragEnable(isDrag);
    }


    @Override
    public void getMotionEvent(MotionEvent event) {

    }

    /**
     * 返回布局信息
     *
     * @param view
     */
    @Override
    public void successView(View view) {
        getView(view);
    }

    @Override
    public void getFloatMotionEvent(MotionEvent event) {
        if (event != null) {
            getMotionEvent(event);
        }
    }

    //--------------------------------------------------吸附相关开始-------------------------------------------------

    /**
     * 是否吸附
     *
     * @param isSelf
     */
    public void isSelfAds(boolean isSelf) {
        mBaseHelper.isSelfAds(isSelf);
    }

    public boolean getDragEnable() {
        return mBaseHelper.isDragEnablel;
    }

    /**
     * 获取当前吸附状态
     * @return
     */
    public boolean getSelfAds(){
        return mBaseHelper.isadsAnima;
    }


    /**
     * 设置吸附时间
     *
     * @param time
     */
    public void setAdsorbTime(int time) {
        mBaseHelper.adsorbTime(time);
    }

    /**
     * 获取吸附时间
     * @return
     */
    public int getAdsorbTime(){
        return mBaseHelper.mAdsorbTime;
    }


    /**
     * 设置吸附动画
     */
    public void setAbsorbAnimType(int type) {
        mBaseHelper.absorbAnimType(type);
    }

    /**
     * 设置自定义吸附插值器
     */
    public void setAbsSorbObject(Interpolator interpolator) {
        mBaseHelper.getAbsSorbObject(interpolator);
    }

    //--------------------------------------------------吸附相关结束-------------------------------------------------

}

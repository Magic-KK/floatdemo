package com.kk.floatlibrary.base;

import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.kk.floatlibrary.animtor.AdsSorbContent;

/**
 * Created by zk on 2020/9/17
 * Helper超类
 */
public abstract class BaseHelper extends BaseCallBack {
    /**
     * 是否执行吸附动画
     */
    public boolean isadsAnima = false;
    /**
     * 执行吸附动画时间 默认300毫秒
     */
    public int mAdsorbTime = 300;
    /**
     * 状态监听器
     */
    public IStatusCallBack OnStatusCallBack;
    /**
     * 获取当前位置监听
     */
    public IMotionEvent OnMotionEvent;
    /**
     * 当前插值期默认 AdsSorbContent.ADS_ACCELERATEDECELERATEINTERPOLATOR
     */
    public Interpolator mAbsSorbInterpolator = new AccelerateDecelerateInterpolator();
    /**
     * 是否可拖拽
     */
    public boolean isDragEnablel = true;
    /**
     * 初始位置XY
     */
    public int initPosX = 0;
    public int initPosY =0;


    public void setFloatOnMotionEvent(IMotionEvent onMotionEvent) {
        OnMotionEvent = onMotionEvent;
    }

    public void setOnStatusCallBack(IStatusCallBack onStatusCallBack) {
        OnStatusCallBack = onStatusCallBack;
    }

    public interface IMotionEvent {
        void getFloatMotionEvent(MotionEvent event);
    }


    public interface IStatusCallBack {
        void successView(View view);
    }

    @Override
    public void isSelfAds(boolean self) {
        isadsAnima = self;
    }

    @Override
    public void adsorbTime(int time) {
        mAdsorbTime = time;
    }

    @Override
    public void setDragEnable(boolean isAg) {
        isDragEnablel =isAg;
    }

    @Override
    public void initPostion(int x, int y) {
        initPosX = x;
        initPosY = y;
    }

    /**
     * 设置插值器类型
     * @param type
     */
    @Override
    public void absorbAnimType(int type) {
        if(type== AdsSorbContent.ADS_ACCELERATEDECELERATEINTERPOLATOR){
            mAbsSorbInterpolator = new AccelerateDecelerateInterpolator();
        }else if(type== AdsSorbContent.ADS_LINEARINTERPOLATOR){
            mAbsSorbInterpolator = new LinearInterpolator();
        }else if(type== AdsSorbContent.ADS_DECELERATEINTERPOLATOR){
            mAbsSorbInterpolator = new DecelerateInterpolator();
        }else if(type== AdsSorbContent.ADS_BOUNCEINTERPOLATOR){
            mAbsSorbInterpolator = new BounceInterpolator();
        }
    }

    /**
     * 系统悬浮子类实现
     */
    @Override
    public void checkPermission() {

    }

    /**
     * 获取自定义吸附插值器
     * @param interpolator
     */
    @Override
    public void getAbsSorbObject(Interpolator interpolator) {
        mAbsSorbInterpolator = interpolator;
    }

}

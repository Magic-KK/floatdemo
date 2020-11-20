package com.kk.floatlibrary.base;

import android.view.View;
import android.view.animation.Interpolator;

/**
 * 通用弹窗实现方法抽象类
 */
public abstract class BaseCallBack {
    /**
     * 初始化布局
     *
     * @param ids
     * @return
     */
    public abstract View inflate(int ids);

    /**
     * 显示
     */
    public abstract void show();

    /**
     * 权限判断
     */
    public abstract void checkPermission();


    /**
     * 隐藏
     */
    public abstract void hide();

    /**
     * 销毁
     *
     * @return
     */
    public abstract boolean removeView();

    /**
     * 设置初始位置
     *
     * @param x
     * @param y
     */
    public abstract void initPostion(int x, int y);

    /**
     * 改变位置
     *
     * @param x
     * @param y
     * @return
     */
    public abstract boolean changeView(int x, int y);

    /**
     * 设置是否可拖拽
     *
     * @param isAg
     */
    public abstract void setDragEnable(boolean isAg);

    /**
     * 是否自动吸附
     *
     * @param self
     */
    public abstract void isSelfAds(boolean self);

    /**
     * 吸附时间
     *
     * @param time
     */
    public abstract void adsorbTime(int time);

    /**
     * 吸附动画类型
     *
     * @param type
     */
    public abstract void absorbAnimType(int type);

    /**
     * 获取自定义插值器
     *
     * @param interpolator
     */
    public abstract void getAbsSorbObject(Interpolator interpolator);



}

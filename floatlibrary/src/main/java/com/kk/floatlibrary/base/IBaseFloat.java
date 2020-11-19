package com.kk.floatlibrary.base;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by zk on 2020/9/14
 * 超类接口
 */
public interface IBaseFloat {
    /**
     * 创建悬浮窗view
     *
     * @return
     */
    View creatFloatView();

    /**
     * 返回悬浮窗View
     *
     * @param view
     */
    void getView(View view);

    /**
     * 获取控件Motion
     * @param event
     */
    void getMotionEvent(MotionEvent event);

}

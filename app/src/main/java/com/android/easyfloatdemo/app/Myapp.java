package com.android.easyfloatdemo.app;

import android.app.Application;

import com.kk.floatlibrary.lifecycle.FloatEngine;

/**
 * Created by zk on 2020/11/19
 */
public class Myapp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化引擎
        FloatEngine.getInstance().init(this);
    }
}

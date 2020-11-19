package com.kk.floatlibrary.lifecycle;

import android.app.Application;

/**
 * Created by zk on 2020/9/18
 * 用于监听App前后台状态
 */
public class FloatEngine implements MyActivityLifecycle.upDataStatus {

    private static MyActivityLifecycle myActivityLifecycle;
    public static FloatEngine mInstance = null;

    public MyActivityLifecycle.upDataStatus mstatus;

    public static FloatEngine getInstance() {
        if (mInstance == null) {
            synchronized (MyActivityLifecycle.class) {
                if (mInstance == null) {
                    mInstance = new FloatEngine();
                }
            }
        }
        return mInstance;
    }

    public void getAppBackground(MyActivityLifecycle.upDataStatus status) {
        mstatus = status;
    }


    /**
     * 初始化 Float
     *
     * @param application
     */
    public void init(Application application) {
        myActivityLifecycle = new MyActivityLifecycle();
        myActivityLifecycle.setOnUpDataStatus(this);
        application.registerActivityLifecycleCallbacks(myActivityLifecycle);
    }


    @Override
    public void upStatus(boolean status) {
        if (mstatus != null) {
            mstatus.upStatus(status);
        }
    }
}

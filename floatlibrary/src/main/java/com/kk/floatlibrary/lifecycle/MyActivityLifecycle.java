package com.kk.floatlibrary.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by zk on 2020/9/18
 */
public class MyActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    public interface upDataStatus {
        void upStatus(boolean status);
    }

    public upDataStatus onUpDataStatus;

    public void setOnUpDataStatus(upDataStatus onUpDataStatus) {
        this.onUpDataStatus = onUpDataStatus;
    }

    private int startCount;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.e("==============", "======>onActivityCreated");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.e("==============", "======>onActivityStarted");
        startCount++;
        if (onUpDataStatus != null) {
            onUpDataStatus.upStatus(isAppBackground());
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.e("==============", "======>onActivityResumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.e("==============", "======>onActivityPaused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.e("==============", "======>onActivityStopped");
        startCount--;
        if (onUpDataStatus != null) {
            onUpDataStatus.upStatus(isAppBackground());
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.e("==============", "======>onActivityDestroyed");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.e("==============", "======>onActivitySaveInstanceState");
    }

    public int getStartCount() {
        return startCount;
    }

    /**
     * @return true  处于后台   false  前台
     */
    public boolean isAppBackground() {
        if (getStartCount() == 0) {
            return true;
        }
        return false;
    }

}

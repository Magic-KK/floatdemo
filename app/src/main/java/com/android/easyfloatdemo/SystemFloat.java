package com.android.easyfloatdemo;

import android.app.Activity;
import android.view.View;

import com.kk.floatlibrary.base.BaseFloatWindow;

/**
 * Created by zk on 2020/11/19
 */
public class SystemFloat extends BaseFloatWindow {
    /**
     * 创建悬浮窗
     *
     * @param context
     * @param type    窗口类型
     */
    public SystemFloat(Activity context, int type) {
        super(context, type);
    }

    @Override
    public View creatFloatView() {
        return createFloatById(R.layout.system_float_layout);
    }

    @Override
    public void getView(View view) {

    }
}

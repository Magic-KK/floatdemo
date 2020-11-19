package com.android.easyfloatdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.kk.floatlibrary.util.FloatConstant;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SystemFloat systemFloat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        initData();
    }

    private void initData() {
        //初始化全局弹窗
        systemFloat = new SystemFloat(this, FloatConstant.FLOAT_SYSTEM_WINDOW);
    }

    private void initView() {


    }

    private void initListener() {
        findViewById(R.id.sys_creat_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sys_creat_btn:
                systemFloat.show();
                break;
        }
    }
}
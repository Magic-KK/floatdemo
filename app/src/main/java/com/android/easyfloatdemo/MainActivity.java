package com.android.easyfloatdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kk.floatlibrary.util.FloatConstant;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SystemFloat systemFloat;
    //系统级--》拖拽
    private Button sys_drag_btn;
    //系统级--》吸附
    private Button sys_adsorption_btn;

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
        sys_drag_btn = findViewById(R.id.sys_drag_btn);
        sys_adsorption_btn = findViewById(R.id.sys_adsorption_btn);
    }

    private void initListener() {
        findViewById(R.id.sys_creat_btn).setOnClickListener(this);
        findViewById(R.id.sys_hide_btn).setOnClickListener(this);
        findViewById(R.id.sys_show_btn).setOnClickListener(this);
        findViewById(R.id.sys_destory_btn).setOnClickListener(this);
        findViewById(R.id.sys_front_show_btn).setOnClickListener(this);
        findViewById(R.id.sys_backstage_show_btn).setOnClickListener(this);
        findViewById(R.id.sys_global_show_btn).setOnClickListener(this);
        sys_adsorption_btn.setOnClickListener(this);
        sys_drag_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sys_creat_btn://系统级--》创建
                systemFloat.show();
                break;
            case R.id.sys_hide_btn://系统级--》隐藏
                systemFloat.hide();
                break;
            case R.id.sys_show_btn://系统级--》显示
                systemFloat.show();
                break;
            case R.id.sys_destory_btn://系统级--》销毁
                systemFloat.removeView();
                break;
            case R.id.sys_adsorption_btn://系统级--》吸附
                systemFloat.isSelfAds(!systemFloat.getSelfAds());
                if (systemFloat.getSelfAds()) {
                    sys_adsorption_btn.setText("关闭吸附");
                } else {
                    sys_adsorption_btn.setText("开启吸附");
                }
                break;

            case R.id.sys_drag_btn://系统级--》拖拽
                systemFloat.setDragEnable(!systemFloat.getDragEnable());
                if (systemFloat.getDragEnable()) {
                    sys_drag_btn.setText("关闭拖拽");
                } else {
                    sys_drag_btn.setText("开启拖拽");
                }
                break;
        }
    }
}
package com.android.easyfloatdemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.kk.floatlibrary.base.BaseFloatWindow;
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
        findViewById(R.id.sys_adsorption_time_btn).setOnClickListener(this);
        findViewById(R.id.sys_setting_anima).setOnClickListener(this);
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
            case R.id.sys_adsorption_time_btn://系统级--》吸附时间
                showAdsorptionTimeDialog(systemFloat);
                break;

            case R.id.sys_setting_anima:  //系统级--》设置动画
                showSettingAnimaDialog(systemFloat);
                break;
        }
    }

    private void showAdsorptionTimeDialog(final BaseFloatWindow window) {
        final EditText editText = new EditText(MainActivity.this);
        editText.setHint(String.valueOf(window.getAdsorbTime()));
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(MainActivity.this);
        inputDialog.setTitle("请输入动画时间(毫秒)").setView(editText);
        inputDialog.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(editText.getText().toString())) {
                            int timesa = Integer.parseInt(editText.getText().toString());
                            window.setAdsorbTime(timesa);
                        }
                    }
                }).show();
    }

    private void showSettingAnimaDialog(final BaseFloatWindow window) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置动画")
                .setItems(new String[]{"先加速再减速", "匀速", "减速", "最后阶段弹球效果"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        window.setAbsorbAnimType(which);
                    }
                })
                .show();
    }
}
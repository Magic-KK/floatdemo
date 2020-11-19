package com.kk.floatlibrary.helper_system;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.kk.floatlibrary.animtor.AdsorptionAnimFactory;
import com.kk.floatlibrary.base.BaseHelper;
import com.kk.floatlibrary.base.RootFrameLayout;
import com.kk.floatlibrary.lifecycle.FloatEngine;
import com.kk.floatlibrary.lifecycle.MyActivityLifecycle;
import com.kk.floatlibrary.permission.rom.HuaweiUtils;
import com.kk.floatlibrary.permission.rom.MeizuUtils;
import com.kk.floatlibrary.permission.rom.MiuiUtils;
import com.kk.floatlibrary.permission.rom.OppoUtils;
import com.kk.floatlibrary.permission.rom.QikuUtils;
import com.kk.floatlibrary.permission.rom.RomUtils;
import com.kk.floatlibrary.util.FloatConstant;
import com.kk.floatlibrary.util.ScreenUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by zk on 2020/9/14
 * 系统级悬浮窗实现
 */
public class SystemFloatHelper extends BaseHelper implements MyActivityLifecycle.upDataStatus {
    public static final String TAG = "BaseFloatHelper";

    private Context mContnet;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams layoutParams;
    private int mTouchStartX, mTouchStartY;//手指按下时坐标
    /**
     * 获取屏幕高宽
     */
    private int mScreenWidth = 0;
    private int mScreenHeight = 0;
    /**
     * 获取状态啦高度
     */
    private int mStatusHeight = 0;

    private int mBackType = 0;


    private RootFrameLayout frameLayout;
    private FrameLayout rootView;


    /**
     * 是否处于后台  true：是
     */
    private boolean isBack = false;

    private Dialog dialog;
    /**
     * 自定义控件宽度
     */
    private int mViewWidth = 0;


    /**
     * 初始化
     *
     * @param context
     * @param back
     */
    public SystemFloatHelper(Context context, int back) {
        this.mContnet = context;
        mBackType = back;
        creat();
    }

    @Override
    public void checkPermission() {
        super.checkPermission();
        applyOrShowFloatWindow();
    }

    /**
     * 权限判断
     *
     * @param
     */
    public void applyOrShowFloatWindow() {
        if (checkPermission(mContnet)) {
            show();
        } else {
            applyPermission(mContnet);
        }
    }

    /**
     * 创建初始化
     */
    private void creat() {
        mWindowManager = (WindowManager) mContnet.getApplicationContext().getSystemService(WINDOW_SERVICE);
        mScreenWidth = ScreenUtils.getScreenWidth(mContnet);
        mScreenHeight = ScreenUtils.getScreenHeight(mContnet);
        mStatusHeight = ScreenUtils.getStatusHeight(mContnet);
        if (mBackType != FloatConstant.FLOAT_SYSTEM_WINDOW) {
            FloatEngine.getInstance().getAppBackground(this);
        }

        //初始化的时候也判断一次
        if (checkPermission(mContnet)) {
        } else {
            applyPermission(mContnet);
        }
    }


    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, final MotionEvent event) {
            if (OnMotionEvent != null) {
                OnMotionEvent.getFloatMotionEvent(event);
            }
            if (isDragEnablel) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mTouchStartX = (int) event.getX();
                        mTouchStartY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        layoutParams.x = (int) event.getRawX() - frameLayout.getmDownX();
                        layoutParams.y = (int) event.getRawY() - mStatusHeight - frameLayout.getmDownY();
                        mWindowManager.updateViewLayout(rootView, layoutParams);
                        AdsorptionAnimFactory.cancleAnimator();
                        break;
                    case MotionEvent.ACTION_UP:
                        //执行吸附动画
                        if (isadsAnima) {
                            float upRawX = event.getRawX() - frameLayout.getmDownX();
                            final int upRawY = (int) event.getRawY() - mStatusHeight - frameLayout.getmDownY();
                            if (upRawX < mScreenWidth / 2 - (mViewWidth / 2)) {
                                AdsorptionAnimFactory.startAdsAnim(mAdsorbTime, mAbsSorbInterpolator, (int) upRawX, 0, rootView, new AdsorptionAnimFactory.onAnimUpData() {
                                    @Override
                                    public void animX(int x) {
                                        changeView(x,upRawY);
                                    }
                                });
                            } else {
                                AdsorptionAnimFactory.startAdsAnim(mAdsorbTime, mAbsSorbInterpolator, (int) upRawX, mScreenWidth - mViewWidth, rootView, new AdsorptionAnimFactory.onAnimUpData() {
                                    @Override
                                    public void animX(int x) {
                                        layoutParams.x = x;
                                        layoutParams.y = upRawY;
                                        mWindowManager.updateViewLayout(rootView, layoutParams);
                                    }
                                });
                            }
                        }
                        break;
                }
            }
            return false;
        }
    };

    @Override
    public View inflate(int ids) {
        frameLayout = new RootFrameLayout(mContnet);
        rootView = (FrameLayout) LayoutInflater.from(mContnet).inflate(ids, frameLayout, true);
        return rootView;
    }

    @Override
    public void show() {
        if (rootView == null) {
            Log.e(TAG, "view不能为空");
            return;
        }


        if (mBackType == FloatConstant.FLOAT_BACKSTAGE_WINDOW) {
            if (isBack) {
                rootView.setVisibility(View.VISIBLE);
            } else {
                rootView.setVisibility(View.GONE);
            }
        } else {
            rootView.setVisibility(View.VISIBLE);
        }

        if (layoutParams != null) {
            return;
        }

        //设置view监听
        rootView.setOnTouchListener(mOnTouchListener);
        // 设置LayoutParam
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //默认初始左上角
        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;

        layoutParams.x = initPosX;
        layoutParams.y = initPosY;


        //设置图片格式，效果为背景透明
        layoutParams.format = PixelFormat.RGBA_8888;
        //设置显示规则
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        // 将悬浮窗控件添加到WindowManager
        mWindowManager.addView(rootView, layoutParams);
        rootView.post(new Runnable() {
            @Override
            public void run() {
                mViewWidth = rootView.getWidth();
            }
        });


        if (OnStatusCallBack != null) {
            OnStatusCallBack.successView(rootView);
        }
    }

    @Override
    public void hide() {
        if (rootView != null) {
            rootView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean removeView() {
        try {
            mWindowManager.removeView(rootView);
            layoutParams = null;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean changeView(int x, int y) {
        try {
            if(layoutParams!=null){
                layoutParams.x = x;
                layoutParams.y = y;
                mWindowManager.updateViewLayout(rootView, layoutParams);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 前后台回调
     *
     * @param status true 后台
     */
    @Override
    public void upStatus(boolean status) {
        isBack = status;
        if (layoutParams != null) {
            if (mBackType == FloatConstant.FLOAT_APP_WINDOW) {
                if (status) {
                    hide();
                } else {
                    show();
                }
            } else if (mBackType == FloatConstant.FLOAT_BACKSTAGE_WINDOW) {
                if (status) {
                    show();
                } else {
                    hide();
                }
            }
        }
    }

    private boolean checkPermission(Context context) {
        //6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                return miuiPermissionCheck(context);
            } else if (RomUtils.checkIsMeizuRom()) {
                return meizuPermissionCheck(context);
            } else if (RomUtils.checkIsHuaweiRom()) {
                return huaweiPermissionCheck(context);
            } else if (RomUtils.checkIs360Rom()) {
                return qikuPermissionCheck(context);
            } else if (RomUtils.checkIsOppoRom()) {
                return oppoROMPermissionCheck(context);
            }
        }
        return commonROMPermissionCheck(context);
    }

    private boolean huaweiPermissionCheck(Context context) {
        return HuaweiUtils.checkFloatWindowPermission(context);
    }

    private boolean miuiPermissionCheck(Context context) {
        return MiuiUtils.checkFloatWindowPermission(context);
    }

    private boolean meizuPermissionCheck(Context context) {
        return MeizuUtils.checkFloatWindowPermission(context);
    }

    private boolean qikuPermissionCheck(Context context) {
        return QikuUtils.checkFloatWindowPermission(context);
    }

    private boolean oppoROMPermissionCheck(Context context) {
        return OppoUtils.checkFloatWindowPermission(context);
    }

    private boolean commonROMPermissionCheck(Context context) {
        //最新发现魅族6.0的系统这种方式不好用，天杀的，只有你是奇葩，没办法，单独适配一下
        if (RomUtils.checkIsMeizuRom()) {
            return meizuPermissionCheck(context);
        } else {
            Boolean result = true;
            if (Build.VERSION.SDK_INT >= 23) {
                try {
                    Class clazz = Settings.class;
                    Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
                    result = (Boolean) canDrawOverlays.invoke(null, context);
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
            return result;
        }
    }

    private void applyPermission(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                miuiROMPermissionApply(context);
            } else if (RomUtils.checkIsMeizuRom()) {
                meizuROMPermissionApply(context);
            } else if (RomUtils.checkIsHuaweiRom()) {
                huaweiROMPermissionApply(context);
            } else if (RomUtils.checkIs360Rom()) {
                ROM360PermissionApply(context);
            } else if (RomUtils.checkIsOppoRom()) {
                oppoROMPermissionApply(context);
            }
        } else {
            commonROMPermissionApply(context);
        }
    }

    private void ROM360PermissionApply(final Context context) {
        showConfirmDialog(context, new SystemFloatHelper.OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    QikuUtils.applyPermission(context);
                } else {
                    Log.e(TAG, "ROM:360, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void huaweiROMPermissionApply(final Context context) {
        showConfirmDialog(context, new SystemFloatHelper.OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    HuaweiUtils.applyPermission(context);
                } else {
                    Log.e(TAG, "ROM:huawei, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void meizuROMPermissionApply(final Context context) {
        showConfirmDialog(context, new SystemFloatHelper.OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    MeizuUtils.applyPermission(context);
                } else {
                    Log.e(TAG, "ROM:meizu, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void miuiROMPermissionApply(final Context context) {
        showConfirmDialog(context, new SystemFloatHelper.OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    MiuiUtils.applyMiuiPermission(context);
                } else {
                    Log.e(TAG, "ROM:miui, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void oppoROMPermissionApply(final Context context) {
        showConfirmDialog(context, new SystemFloatHelper.OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    OppoUtils.applyOppoPermission(context);
                } else {
                    Log.e(TAG, "ROM:miui, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    /**
     * 通用 rom 权限申请
     */
    private void commonROMPermissionApply(final Context context) {
        //这里也一样，魅族系统需要单独适配
        if (RomUtils.checkIsMeizuRom()) {
            meizuROMPermissionApply(context);
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                showConfirmDialog(context, new SystemFloatHelper.OnConfirmResult() {
                    @Override
                    public void confirmResult(boolean confirm) {
                        if (confirm) {
                            try {
                                commonROMPermissionApplyInternal(context);
                            } catch (Exception e) {
                                Log.e(TAG, Log.getStackTraceString(e));
                            }
                        } else {
                            Log.d(TAG, "user manually refuse OVERLAY_PERMISSION");
                            //需要做统计效果
                        }
                    }
                });
            }
        }
    }

    public static void commonROMPermissionApplyInternal(Context context) throws NoSuchFieldException, IllegalAccessException {
        Class clazz = Settings.class;
        Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");

        Intent intent = new Intent(field.get(null).toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    private void showConfirmDialog(Context context, SystemFloatHelper.OnConfirmResult result) {
        showConfirmDialog(context, "您的手机没有授予悬浮窗权限，请开启后再试", result);
    }

    private void showConfirmDialog(Context context, String message, final SystemFloatHelper.OnConfirmResult result) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("现在去开启",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirmResult(true);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("暂不开启",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirmResult(false);
                                dialog.dismiss();
                            }
                        }).create();
        dialog.show();
    }

    private interface OnConfirmResult {
        void confirmResult(boolean confirm);
    }
}

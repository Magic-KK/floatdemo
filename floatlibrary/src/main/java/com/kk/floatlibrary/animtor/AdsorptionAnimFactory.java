package com.kk.floatlibrary.animtor;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;

import android.view.View;

import android.view.animation.Interpolator;



/**
 * Created by zk on 2020/9/27
 * 吸附动画工厂
 */
public class AdsorptionAnimFactory {

    private static ObjectAnimator animator;

    public static void startAdsAnim(int duration, Interpolator baseInterpolator, int start, int end, View view, final onAnimUpData callback){
        animator = ObjectAnimator.ofInt(view, "x", start, end);
        animator.setInterpolator(baseInterpolator);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int x = (int) animation.getAnimatedValue("x");
                callback.animX(x);
            }
        });
        animator.start();
    }


    public static void cancleAnimator(){
        if(animator!=null){
            animator.cancel();
        }
    }


    public interface onAnimUpData{
        void animX(int x);
    }
}

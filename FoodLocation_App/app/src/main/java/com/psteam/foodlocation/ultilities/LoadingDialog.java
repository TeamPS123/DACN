package com.psteam.foodlocation.ultilities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.LayoutLoadingDialogBinding;

public class LoadingDialog {

    private static Dialog dialog;

    public static int NoBack = 0;
    public static int Back = 1;

    //private static  int i=1;
    public static void show(Context context) {
        setFullScreen(context);
        LayoutLoadingDialogBinding binding = LayoutLoadingDialogBinding.inflate(LayoutInflater.from(context));
        dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(binding.getRoot());
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        binding.textViewBack.setOnClickListener(v -> {
            dialog.dismiss();
            ((Activity) context).finish();
        });

        /*binding.animationLoading.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                if(i==1){
                    binding.textViewLoading.setText(String.format("Đang lấy dữ liệu."));
                }else if(i==2){
                    binding.textViewLoading.setText(String.format("Đang lấy dữ liệu.."));
                }else if(i==3){
                    binding.textViewLoading.setText(String.format("Đang lấy dữ liệu..."));
                    i=0;
                    return;
                }else {
                    binding.textViewLoading.setText(String.format("Đang lấy dữ liệu"));
                }
                i++;
            }
        });*/

        dialog.show();

    }

    private static void setFullScreen(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = ((Activity)context).getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

            ((Activity)context).getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            ((Activity)context).getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.white));// set status background white
        }
    }

    public static void show(Context context, int buttonBack) {

        LayoutLoadingDialogBinding binding = LayoutLoadingDialogBinding.inflate(LayoutInflater.from(context));
        dialog = new Dialog(context, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(binding.getRoot());
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            dialog.getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.white));
        }

        if (buttonBack == NoBack) {
            binding.textViewBack.setVisibility(View.GONE);
        } else {
            binding.textViewBack.setVisibility(View.VISIBLE);
            binding.textViewBack.setOnClickListener(v -> {
                dialog.dismiss();
                ((Activity) context).finish();
            });
        }

        dialog.show();

    }

    public static void dismiss(Long delay) {
        if (dialog != null) {
            new CountDownTimer(delay,1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    dialog.dismiss();
                    dialog = null;
                }
            }.start();

        }
    }

}

package com.psteam.foodlocation.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.ActivityUserInfoBinding;
import com.psteam.foodlocation.databinding.LayoutChangePasswordDialogBinding;
import com.psteam.foodlocation.databinding.LayoutCheckPasswordDialogBinding;
import com.psteam.foodlocation.databinding.LayoutUpdateUserInfoDialogBinding;
import com.psteam.foodlocation.ultilities.Constants;

import java.util.concurrent.atomic.AtomicBoolean;

public class UserInfoActivity extends AppCompatActivity {

    private ActivityUserInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListeners();
    }

    private void init() {
        setFullScreen();
    }

    private void setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));// set status background white
        }
    }

    private void setListeners() {
        binding.buttonChangeUserInfo.setOnClickListener(v -> {
            openDialogCheckPassword(Constants.FLAG_UPDATE_USER_INFO);
        });

        binding.buttonChangePassword.setOnClickListener(v -> {
            openDialogCheckPassword(Constants.FLAG_CHANGE_PASSWORD);
        });

        binding.imageViewClose.setOnClickListener(v -> {
            finish();
        });
    }

    private AlertDialog dialog;

    private void openDialogChangeInfo() {
        final LayoutUpdateUserInfoDialogBinding
                layoutUpdateUserInfoDialogBinding = LayoutUpdateUserInfoDialogBinding.inflate(LayoutInflater.from(UserInfoActivity.this));

        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
        builder.setView(layoutUpdateUserInfoDialogBinding.getRoot());
        builder.setCancelable(false);
        dialog = builder.create();

        String[] items = new String[]{"Nam", "Nữ"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        layoutUpdateUserInfoDialogBinding.spinnerGender.setAdapter(adapter);

        layoutUpdateUserInfoDialogBinding.spinnerGender.setSelectedIndex(binding.textViewGender.getText().toString().equals("Nam") ? 0 : 1);

        layoutUpdateUserInfoDialogBinding.buttonSave.setOnClickListener(v -> {
            boolean gender = layoutUpdateUserInfoDialogBinding.spinnerGender.getSelectedIndex() == 0 ? true : false;
            Toast.makeText(getApplicationContext(), gender + "", Toast.LENGTH_SHORT).show();
        });

        layoutUpdateUserInfoDialogBinding.buttonBack.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();

    }

    private void openDialogChangePassword() {
        final LayoutChangePasswordDialogBinding
                layoutChangePasswordDialogBinding = LayoutChangePasswordDialogBinding.inflate(LayoutInflater.from(UserInfoActivity.this));

        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
        builder.setView(layoutChangePasswordDialogBinding.getRoot());
        builder.setCancelable(false);
        dialog = builder.create();

        dialog.show();
    }

    private void openDialogCheckPassword(int type) {
        final LayoutCheckPasswordDialogBinding
                layoutCheckPasswordDialogBinding = LayoutCheckPasswordDialogBinding.inflate(LayoutInflater.from(UserInfoActivity.this));

        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
        builder.setView(layoutCheckPasswordDialogBinding.getRoot());
        builder.setCancelable(false);
        dialog = builder.create();

        layoutCheckPasswordDialogBinding.buttonNext.setOnClickListener(v -> {
            if (type == Constants.FLAG_UPDATE_USER_INFO) {
                dialog.dismiss();
                openDialogChangeInfo();
            } else {
                dialog.dismiss();
                openDialogChangePassword();
            }
        });

        layoutCheckPasswordDialogBinding.buttonBack.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

}
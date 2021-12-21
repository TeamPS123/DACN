package com.psteam.foodlocation.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.ActivityBusinessOrNormalBinding;

public class BusinessOrNormalActivity extends AppCompatActivity {

    private ActivityBusinessOrNormalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBusinessOrNormalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListeners();
    }

    private void setListeners() {
        binding.buttonIsBusiness.setOnClickListener(v -> {
           // startActivity(new Intent(BusinessOrNormalActivity.this, RestaurantRegistrationActivity.class));
        });

        binding.buttonUserNormal.setOnClickListener(v -> {
            Intent intent = new Intent(BusinessOrNormalActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
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
            getWindow().setStatusBarColor(ContextCompat.getColor(BusinessOrNormalActivity.this, R.color.white));// set status background white
        }
    }
}

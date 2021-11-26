package com.psteam.foodlocation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.foodlocation.ultilities.PreferenceManager;

public class SplashActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        preferenceManager = new PreferenceManager(getApplicationContext());
        Handler handler = new Handler();
        if(!getIntent().getBooleanExtra("exit",false)) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //tde tam sau sửa
                    NextActivity();
                }
            }, 2000);
        }else {
            finishAffinity();
            System.exit(0);
        }
    }
    private void NextActivity() {
        if (!preferenceManager.getBoolean(Constants.IsLogin)) {
            Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            //tde tam sau sửa
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
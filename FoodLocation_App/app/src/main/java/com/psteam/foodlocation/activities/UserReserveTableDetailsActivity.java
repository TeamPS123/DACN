package com.psteam.foodlocation.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.ActivityUserReserveTableDetailsBinding;
import com.psteam.foodlocation.socket.models.BodySenderFromRes;

public class UserReserveTableDetailsActivity extends AppCompatActivity {

    private ActivityUserReserveTableDetailsBinding binding;
    private BodySenderFromRes response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserReserveTableDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            getWindow().setStatusBarColor(ContextCompat.getColor(UserReserveTableDetailsActivity.this, R.color.white));// set status background white
        }
    }

    private void init(){
        if(getIntent().getExtras() != null){
            getDataFromNoti();
            setBinding();
        }
    }

    private void getDataFromNoti(){
        if(getIntent().getExtras().getString("notification") != null){
            response = new BodySenderFromRes();
            response.setNotification(getIntent().getExtras().getString("notification"));
            response.setReserveTableId(getIntent().getExtras().getString("reserveTableId"));
        }else{
            response = (BodySenderFromRes) getIntent().getSerializableExtra("response");
        }
    }

    private void setBinding(){
        binding.inputNote.setText(response.getNotification());
    }
}
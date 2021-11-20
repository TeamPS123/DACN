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
import android.widget.Toast;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.ActivityBusinessOrNormalBinding;
import com.psteam.lib.Models.Insert.signUp;
import com.psteam.lib.Models.message;
import com.psteam.lib.Service.ServiceAPI_lib;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessOrNormalActivity extends AppCompatActivity {

    private ActivityBusinessOrNormalBinding binding;
    private signUp user;

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
            SignUp(1);
        });

        binding.buttonUserNormal.setOnClickListener(v -> {
            SignUp(2);
        });
    }

    private void init() {
        setFullScreen();

        user = (signUp)getIntent().getExtras().getSerializable("user");
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

    private void SignUp(int code){
        if(code == 2){
            user.setBusiness(false);
        }

        ServiceAPI_lib serviceAPI = com.psteam.lib.RetrofitServer.getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<message> call = serviceAPI.signup(user);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if(response.body().getStatus() == 1){
                    Toast.makeText(BusinessOrNormalActivity.this, response.body().getNotification(), Toast.LENGTH_SHORT).show();
                    if(code == 1){
                        startActivity(new Intent(BusinessOrNormalActivity.this, RestaurantRegistrationActivity.class));
                    }else if(code == 2){
                        Intent intent = new Intent(BusinessOrNormalActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(BusinessOrNormalActivity.this, response.body().getNotification(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Toast.makeText(BusinessOrNormalActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

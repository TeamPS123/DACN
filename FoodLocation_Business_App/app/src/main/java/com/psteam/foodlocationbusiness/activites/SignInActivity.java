package com.psteam.foodlocationbusiness.activites;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.databinding.ActivitySignInBinding;
import com.psteam.foodlocationbusiness.ultilities.DataTokenAndUserId;
import com.psteam.lib.Models.Input.signIn;
import com.psteam.lib.Models.message;
import com.psteam.lib.Service.ServiceAPI_lib;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.psteam.lib.RetrofitServer.getRetrofit_lib;


public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));// set status background white
        }
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkSaveUser();
        setListeners();
    }

    private void checkSaveUser() {
        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getApplicationContext());
        if(!dataTokenAndUserId.getUserId().equals("")){
            Intent intent = new Intent(SignInActivity.this, BusinessActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            loading(false);
            finish();
        }
    }

    private void setListeners() {
        binding.buttonSignIn.setOnClickListener(v -> {
            loading(true);
            if (isValidSignIn()) {
                signIn();
            }
            loading(false);
        });

        binding.textviewSignUp.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        });
    }

    private boolean isValidSignIn() {
        if (binding.inputPhone.getText().toString().trim().isEmpty()) {
            showToast("Vui lòng nhập số điện thoại");
            return false;
        } else if (!Patterns.PHONE.matcher(binding.inputPhone.getText().toString()).matches()) {
            showToast("Vui lòng nhập đúng định dạng số điện thoại");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Vui lòng nhâp mật khẩu");
            return false;
        } else {
            return true;
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void loading(boolean Loading) {
        if (Loading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.buttonSignIn.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.buttonSignIn.setVisibility(View.VISIBLE);
        }
    }

    private void signIn(){
        ServiceAPI_lib serviceAPI = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<message> call = serviceAPI.signin(new signIn(binding.inputPhone.getText()+"", binding.inputPassword.getText()+""));
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {

                if(response.body().getStatus() == 1){
                    DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getApplication());
                    dataTokenAndUserId.saveToken(response.body().getNotification());
                    dataTokenAndUserId.saveUserId(response.body().getId());

                    Intent intent = new Intent(SignInActivity.this, BusinessActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    loading(false);
                    finish();

                    Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SignInActivity.this, response.body().getNotification(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Toast.makeText(SignInActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
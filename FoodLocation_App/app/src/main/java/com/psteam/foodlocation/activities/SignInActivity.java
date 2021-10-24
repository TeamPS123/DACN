package com.psteam.foodlocation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListeners();
    }

    private void setListeners() {
        binding.buttonSignIn.setOnClickListener(v -> {
            loading(true);
            if(isValidSignIn()){
                startActivity(new Intent(SignInActivity.this,MainActivity.class));
                loading(false);
            }
            loading(false);
        });

        binding.textviewSignUp.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        });
    }

    private boolean isValidSignIn(){
        if(binding.inputPhone.getText().toString().trim().isEmpty()){
            showToast("Vui lòng nhập số điện thoại");
            return  false;
        }else if(!Patterns.PHONE.matcher(binding.inputPhone.getText().toString()).matches()){
            showToast("Vui lòng nhập đúng định dạng số điện thoại");
            return false;
        } else  if(binding.inputPassword.getText().toString().trim().isEmpty()){
            showToast("Vui lòng nhâp mật khẩu");
            return false;
        }else {
            return true;
        }
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void loading(boolean Loading){
        if(Loading){
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.buttonSignIn.setVisibility(View.GONE);
        }else {
            binding.progressBar.setVisibility(View.GONE);
            binding.buttonSignIn.setVisibility(View.VISIBLE);
        }
    }

}
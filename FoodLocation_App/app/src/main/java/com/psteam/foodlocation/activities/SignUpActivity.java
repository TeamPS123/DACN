package com.psteam.foodlocation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.psteam.foodlocation.databinding.ActivitySignUpBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners() {
        binding.buttonSignUp.setOnClickListener(v -> {
            if (isValidSignUp()) {
                Intent intent = new Intent(getApplicationContext(), VerifyOTPActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phoneNumber", binding.inputPhone.getText().toString());
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
        binding.buttonSignIn.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        });
    }

    private boolean isValidSignUp() {
        if (binding.inputFullName.getText().toString().trim().isEmpty()) {
            binding.inputFullName.setError("Họ và tên không được bỏ trống");
            return false;
        } else if (binding.inputPhone.getText().toString().trim().isEmpty()) {
            binding.inputPhone.setError("Số điện thoại không được bỏ trống");
            return false;
        } else if (!Patterns.PHONE.matcher(binding.inputPhone.getText().toString()).matches()) {
            binding.inputPhone.setError("Sai định dạng số điện thoại");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            binding.inputPassword.setError("Mật khẩu không được bỏ trống");
            return false;
        } else if (!isValidFormatPassword(binding.inputPassword.getText().toString().trim())) {
            binding.inputPassword.setError("Độ dài mật khẩu từ 6 đến 24 kí tự bao gồm chữ, số, kí tự in hoa và kí tự đặt biệt");
            return false;
        } else if (binding.inputConfirmPassword.getText().toString().trim().isEmpty()) {
            binding.inputConfirmPassword.setError("Nhập lại mật khẩu không được bỏ trống");
            return false;
        } else if (!binding.inputPassword.getText().toString().trim().equals(binding.inputConfirmPassword.getText().toString().trim())) {
            binding.inputConfirmPassword.setError("Nhập lại mật khẩu không đúng");
            return false;
        } else {
            return true;
        }
    }

    public boolean isValidFormatPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
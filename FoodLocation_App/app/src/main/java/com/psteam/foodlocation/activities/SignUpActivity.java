package com.psteam.foodlocation.activities;

import static com.psteam.lib.RetrofitClient.getRetrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.ActivitySignUpBinding;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.foodlocation.ultilities.CustomToast;
import com.psteam.foodlocation.ultilities.PreferenceManager;
import com.psteam.lib.Services.ServiceAPI;
import com.psteam.lib.modeluser.LogUpModel;
import com.psteam.lib.modeluser.LoginModel;
import com.psteam.lib.modeluser.message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private PreferenceManager preferenceManager;
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
        preferenceManager = new PreferenceManager(getApplicationContext());

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners() {
        binding.buttonSignUp.setOnClickListener(v -> {
            if (isValidSignUp()) {
//                Intent intent = new Intent(getApplicationContext(), VerifyOTPActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("phoneNumber", binding.inputPhone.getText().toString());
//                intent.putExtra("bundle", bundle);
//                startActivity(intent);
                String strName=binding.inputFullName.getText().toString().trim();
                String strPhone=binding.inputPhone.getText().toString().trim();
                String strPassword=binding.inputPassword.getText().toString().trim();
                boolean strGender=binding.radioButtonMale.isChecked();

                signUP(new LogUpModel(true,strGender,strPhone,strPassword,strName));

            }
        });
        binding.buttonSignIn.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        });
    }

    private void signUP(LogUpModel logUpModel) {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<message> call = serviceAPI.SignUp(logUpModel);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    preferenceManager.putString(Constants.USER_ID, response.body().getId());
                    preferenceManager.putBoolean(Constants.IsLogin,true);
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    CustomToast.makeText(getApplicationContext(), response.body().getNotification(), CustomToast.LENGTH_SHORT, CustomToast.ERROR).show();
                }
                loading(false);
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Log.d("Log:", t.getMessage());
            }
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
    private void loading(boolean Loading) {
        if (Loading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.buttonSignIn.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.buttonSignIn.setVisibility(View.VISIBLE);
        }
    }
}
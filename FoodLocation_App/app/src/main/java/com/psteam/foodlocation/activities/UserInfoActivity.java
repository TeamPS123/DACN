package com.psteam.foodlocation.activities;

import static com.psteam.lib.RetrofitClient.getRetrofit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
import com.psteam.foodlocation.ultilities.CustomToast;
import com.psteam.foodlocation.ultilities.PreferenceManager;
import com.psteam.foodlocation.ultilities.Token;
import com.psteam.lib.Services.ServiceAPI;
import com.psteam.lib.modeluser.ChangeInfoModel;
import com.psteam.lib.modeluser.GetInfoUser;
import com.psteam.lib.modeluser.LogUpModel;
import com.psteam.lib.modeluser.LoginModel;
import com.psteam.lib.modeluser.UserModel;
import com.psteam.lib.modeluser.message;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends AppCompatActivity {

    private ActivityUserInfoBinding binding;
    private String oldPassword;
    private PreferenceManager preferenceManager;
    private UserModel user;
    private String UserID;
    private Token dataToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        oldPassword = preferenceManager.getString(Constants.Password);
        dataToken = new Token(UserInfoActivity.this);
        UserID = preferenceManager.getString(Constants.USER_ID);
        init();
        setListeners();
    }

    private void init() {
        setFullScreen();
        Bundle bundle = getIntent().getBundleExtra("bundle");
        user = (UserModel) bundle.getSerializable("user");
        binding.textViewUserName.setText(user.getFullName());
        binding.textViewPhoneNumber.setText(user.getPhone());
        String gender = user.getGender() == true ? "Nam" : "Nữ";
        binding.textViewGender.setText(gender);

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

        layoutUpdateUserInfoDialogBinding.inputFullName.setText(user.getFullName());
        layoutUpdateUserInfoDialogBinding.buttonSave.setOnClickListener(v -> {


            if (isValid(layoutUpdateUserInfoDialogBinding)) {

                boolean gender = layoutUpdateUserInfoDialogBinding.spinnerGender.getSelectedIndex() == 0 ? true : false;
                String name = layoutUpdateUserInfoDialogBinding.inputFullName.getText().toString().trim();

                ChangeInfoModel userModel = new ChangeInfoModel(gender, oldPassword, true,
                        name, UserID);
                ChangeInfo(userModel);
            }


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
        layoutChangePasswordDialogBinding.buttonBack.setOnClickListener(v -> {
            dialog.dismiss();
        });
        layoutChangePasswordDialogBinding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidPass(layoutChangePasswordDialogBinding)) {
                    String password = layoutChangePasswordDialogBinding.inputNewPassword.getText().toString().trim();

                    ChangeInfoModel userModel = new ChangeInfoModel(user.getGender(), password, false,
                            user.getFullName(), UserID);

                    ChangeIPassword(userModel);
                }
            }
        });
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
            String strOldPass = layoutCheckPasswordDialogBinding.inputPassword.getText().toString().trim();
            if (strOldPass.equals(oldPassword)) {
                if (type == Constants.FLAG_UPDATE_USER_INFO) {
                    dialog.dismiss();
                    openDialogChangeInfo();
                } else {
                    dialog.dismiss();
                    openDialogChangePassword();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
            }
        });

        layoutCheckPasswordDialogBinding.buttonBack.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }


    private void GetInfo(String id) {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<GetInfoUser> call = serviceAPI.GetDetailUser(dataToken.getToken(), id);
        call.enqueue(new Callback<GetInfoUser>() {
            @Override
            public void onResponse(Call<GetInfoUser> call, Response<GetInfoUser> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    user = response.body().getUser();
                    binding.textViewUserName.setText(user.getFullName());
                    binding.textViewPhoneNumber.setText(user.getPhone());
                    String gender = user.getGender() == true ? "Nam" : "Nữ";
                    binding.textViewGender.setText(gender);
                }
            }

            @Override
            public void onFailure(Call<GetInfoUser> call, Throwable t) {
                Log.d("Log:", t.getMessage());
            }
        });
    }

    private void ChangeInfo(ChangeInfoModel user) {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<message> call = serviceAPI.ChangeInfoUser(dataToken.getToken(), user);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetInfo(UserID);
                }
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Log.d("Log:", t.getMessage());


            }
        });
    }

    private void ChangeIPassword(ChangeInfoModel user) {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<message> call = serviceAPI.ChangeInfoUser(dataToken.getToken(), user);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    Toast.makeText(getApplicationContext(), "Cập nhật thành công, vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                    preferenceManager.clear();
                    Intent intent = new Intent(UserInfoActivity.this, SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    dialog.dismiss();
                    startActivity(intent);
                    finishAffinity();
                }

            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Log.d("Log:", t.getMessage());


            }
        });
    }

    private boolean isValid(LayoutUpdateUserInfoDialogBinding
                                    layoutUpdateUserInfoDialogBinding) {
        if (layoutUpdateUserInfoDialogBinding.inputFullName.getText().toString().trim().isEmpty()) {
            layoutUpdateUserInfoDialogBinding.inputFullName.setError("Họ và tên không được bỏ trống");
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidPass(LayoutChangePasswordDialogBinding
                                        layoutChangePasswordDialogBinding) {

        if (layoutChangePasswordDialogBinding.inputNewPassword.getText().toString().trim().isEmpty()) {
            layoutChangePasswordDialogBinding.inputNewPassword.setError("Mật khẩu không được bỏ trống");
            return false;
        } else if (!isValidFormatPassword(layoutChangePasswordDialogBinding.inputConfirmPassword.getText().toString().trim())) {
            layoutChangePasswordDialogBinding.inputNewPassword.setError("Độ dài mật khẩu từ 6 đến 24 kí tự bao gồm chữ, số, kí tự in hoa và kí tự đặt biệt");
            return false;
        } else if (layoutChangePasswordDialogBinding.inputConfirmPassword.getText().toString().trim().isEmpty()) {
            layoutChangePasswordDialogBinding.inputConfirmPassword.setError("Nhập lại mật khẩu không được bỏ trống");
            return false;
        } else if (!layoutChangePasswordDialogBinding.inputNewPassword.getText().toString().trim()
                .equals(layoutChangePasswordDialogBinding.inputConfirmPassword.getText().toString().trim())) {
            layoutChangePasswordDialogBinding.inputConfirmPassword.setError("Nhập lại mật khẩu không đúng");
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

}
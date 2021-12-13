package com.psteam.foodlocation.activities;

import static com.psteam.lib.RetrofitClient.getRetrofit;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.ActivitySignUpBinding;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.foodlocation.ultilities.CustomToast;
import com.psteam.foodlocation.ultilities.PreferenceManager;
import com.psteam.foodlocation.ultilities.Token;
import com.psteam.lib.Services.ServiceAPI;
import com.psteam.lib.modeluser.LogUpModel;
import com.psteam.lib.modeluser.LoginModel;
import com.psteam.lib.modeluser.message;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private PreferenceManager preferenceManager;
    private Token dataToken;

    public static FirebaseAuth mAuth;
    public static String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

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
        dataToken = new Token(SignUpActivity.this);


        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        mAuth = FirebaseAuth.getInstance();

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
                loading(true);
                sendVerificationCode("+84" + binding.inputPhone.getText().toString());
//                String strName=binding.inputFullName.getText().toString().trim();
//                String strPhone=binding.inputPhone.getText().toString().trim();
//                String strPassword=binding.inputPassword.getText().toString().trim();
//                boolean strGender=binding.radioButtonMale.isChecked();
//
//                signUP(new LogUpModel(true,strGender,strPhone,strPassword,strName),strPassword);

            }
        });
        binding.buttonSignIn.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        });
    }

    private void signUP(LogUpModel logUpModel,String password) {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<message> call = serviceAPI.SignUp(logUpModel);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    preferenceManager.putString(Constants.USER_ID, response.body().getId());
                    preferenceManager.putBoolean(Constants.IsLogin,true);
                    preferenceManager.putString(Constants.Password, password);
                    dataToken.saveToken(response.body().getNotification());
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
            binding.buttonSignUp.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.buttonSignUp.setVisibility(View.VISIBLE);
        }
    }

    //SignIn by Phone
    private void sendVerificationCode(String number){
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(number)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // callback xác thực sđt
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            //Hàm này được gọi trong hai trường hợp:
            //1. Trong một số trường hợp, điện thoại di động được xác minh tự động mà không cần mã xác minh.
            //2. Trên một số thiết bị, các dịch vụ của Google Play phát hiện SMS đến và thực hiện quy trình xác minh mà không cần người dùng thực hiện bất kỳ hành động nào.
            Log.d("Send", "onVerificationCompleted:" + credential);

            //tự động điền mã OTP
//            edtNum1.setText(credential.getSmsCode().substring(0,1));
//            edtNum2.setText(credential.getSmsCode().substring(1,2));
//            edtNum3.setText(credential.getSmsCode().substring(2,3));
//            edtNum4.setText(credential.getSmsCode().substring(3,4));
//            edtNum5.setText(credential.getSmsCode().substring(4,5));
//            edtNum6.setText(credential.getSmsCode().substring(5,6));

            verifyCode(credential.getSmsCode());
            loading(false);
        }

        //fail
        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.w("Send", "onVerificationFailed", e);
            //ShowNotification.dismissProgressDialog();

//            if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                ShowNotification.showAlertDialog(MainActivity.this, "Request fail");
//            } else if (e instanceof FirebaseTooManyRequestsException) {
//                ShowNotification.showAlertDialog(MainActivity.this, "Quota không đủ");
//            }
            loading(false);
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            Log.d("Send", "onCodeSent:" + verificationId);
            //ShowNotification.dismissProgressDialog();
            Toast.makeText(getApplicationContext(), "Đã gửi OTP", Toast.LENGTH_SHORT).show();
            mVerificationId = verificationId;
            mResendToken = token;

            String strName=binding.inputFullName.getText().toString().trim();
            String strPhone=binding.inputPhone.getText().toString().trim();
            String strPassword=binding.inputPassword.getText().toString().trim();
            boolean strGender=binding.radioButtonMale.isChecked();

            loading(false);
            Intent intent = new Intent(SignUpActivity.this, VerifyOTPActivity.class);
            intent.putExtra("account", new LogUpModel(true,strGender,strPhone,strPassword,strName));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    };

    //code xác thực OTP
    private void verifyCode(String code) {
        //ShowNotification.showProgressDialog(MainActivity.this, "Đang xác thực");
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //ShowNotification.dismissProgressDialog();
                        if (task.isSuccessful()) {
                            Log.d("Confirm", "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            //ShowNotification.showAlertDialog(MainActivity.this, "Thành công");
                        } else {
                            Log.w("Confirm", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                //ShowNotification.showAlertDialog(MainActivity.this, "Lỗi");
                            }
                        }
                    }
                });
    }
}
package com.psteam.foodlocation.activities;

import static com.psteam.lib.RetrofitClient.getRetrofit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
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
import com.psteam.foodlocation.databinding.ActivityVerifyOtpBinding;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.foodlocation.ultilities.CustomToast;
import com.psteam.foodlocation.ultilities.GenericTextWatcher;
import com.psteam.foodlocation.ultilities.PreferenceManager;
import com.psteam.foodlocation.ultilities.SmsBroadcastReceiver;
import com.psteam.foodlocation.ultilities.Token;
import com.psteam.lib.Services.ServiceAPI;
import com.psteam.lib.modeluser.LogUpModel;
import com.psteam.lib.modeluser.message;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOTPActivity extends AppCompatActivity {

    private ActivityVerifyOtpBinding binding;
    private String phoneNumber;
    private Bundle bundle;

    private LogUpModel account;
    private Token dataToken;
    private PreferenceManager preferenceManager;

    private long leftTimeInSecond = 60000;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final int REQ_USER_CONSENT = 200;
    private SmsBroadcastReceiver smsBroadcastReceiver;

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
        binding = ActivityVerifyOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        startSmartUserConsent();
        preferenceManager = new PreferenceManager(getApplicationContext());
        dataToken = new Token(VerifyOTPActivity.this);
        verifyStorePermission(VerifyOTPActivity.this);
        init();
        setListeners();
    }

    private void setListeners() {
        binding.buttonVerifyOTP.setOnClickListener(v -> {
            loading(true);
            if (binding.inputCode1.getText().toString().trim().isEmpty() ||
                    binding.inputCode2.getText().toString().trim().isEmpty() ||
                    binding.inputCode3.getText().toString().trim().isEmpty() ||
                    binding.inputCode4.getText().toString().trim().isEmpty() ||
                    binding.inputCode5.getText().toString().trim().isEmpty() ||
                    binding.inputCode6.getText().toString().trim().isEmpty()) {
                showToast("Vui lòng nhập đầy đủ mã");
                loading(false);
                return;
            }
            String code = binding.inputCode1.getText().toString() +
                    binding.inputCode2.getText().toString() +
                    binding.inputCode3.getText().toString() +
                    binding.inputCode4.getText().toString() +
                    binding.inputCode5.getText().toString() +
                    binding.inputCode6.getText().toString();
            if (code.length() == 6) {
                verifyCode(code);
            }
        });

        binding.inputCode1.addTextChangedListener(new GenericTextWatcher(binding.inputCode1, binding.inputCode2));
        binding.inputCode2.addTextChangedListener(new GenericTextWatcher(binding.inputCode2, binding.inputCode3));
        binding.inputCode3.addTextChangedListener(new GenericTextWatcher(binding.inputCode3, binding.inputCode4));
        binding.inputCode4.addTextChangedListener(new GenericTextWatcher(binding.inputCode4, binding.inputCode5));
        binding.inputCode5.addTextChangedListener(new GenericTextWatcher(binding.inputCode5, binding.inputCode6));
        binding.inputCode6.addTextChangedListener(new GenericTextWatcher(binding.inputCode6, null));

        binding.inputCode2.setOnKeyListener(new GenericKeyEvent(binding.inputCode2, binding.inputCode1));
        binding.inputCode3.setOnKeyListener(new GenericKeyEvent(binding.inputCode3, binding.inputCode2));
        binding.inputCode4.setOnKeyListener(new GenericKeyEvent(binding.inputCode4, binding.inputCode3));
        binding.inputCode5.setOnKeyListener(new GenericKeyEvent(binding.inputCode5, binding.inputCode4));
        binding.inputCode6.setOnKeyListener(new GenericKeyEvent(binding.inputCode6, binding.inputCode5));

        binding.textviewReSendOTP.setOnClickListener(v -> {
            leftTimeInSecond = 60000;
            countDownResendOTP(leftTimeInSecond);

            sendVerificationCode("+84" + account.getPhone());
        });
    }

    private static void verifyStorePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSION_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void loading(boolean Loading) {
        if (Loading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.buttonVerifyOTP.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.buttonVerifyOTP.setVisibility(View.VISIBLE);
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private String imageUri;

    private void init() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            account = (LogUpModel) bundle.getSerializable("account");
            imageUri = bundle.getString("imageUri");
            phoneNumber = account.getPhone();
            binding.textviewPhone.setText(phoneNumber);
        }
        countDownResendOTP(leftTimeInSecond);
    }

    private void addImgUser(String userId) {
        MultipartBody.Part part;
        File file = new File(imageUri);
        RequestBody photoContext = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        part = MultipartBody.Part.createFormData("photo", file.getName(), photoContext);

        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<message> call = serviceAPI.addImgUser(dataToken.getToken(), part, userId);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    Intent intent = new Intent(VerifyOTPActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Log.d("Tag", t.getMessage());
            }
        });
    }

    private void countDownResendOTP(long timeLeft) {
        CountDownTimer countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                leftTimeInSecond = millisUntilFinished / 1000;
                binding.textviewReSendOTP.setText(String.format("Gửi lại sau: %02d:%02d", leftTimeInSecond / 60, leftTimeInSecond % 60));
            }

            @Override
            public void onFinish() {
                binding.textviewReSendOTP.setText("Gửi lại");
            }
        };
        countDownTimer.start();
    }

    public class GenericKeyEvent implements View.OnKeyListener {

        private EditText currentView;
        private EditText previousView;

        public GenericKeyEvent(EditText currentView, EditText previousView) {
            this.currentView = currentView;
            this.previousView = previousView;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.getText().toString().isEmpty()) {
                if (previousView != null) {
                    previousView.requestFocus();
                }
                return true;
            }
            return false;
        }
    }

    private void signUP(LogUpModel logUpModel, String password) {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<message> call = serviceAPI.SignUp(logUpModel);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    preferenceManager.putString(Constants.USER_ID, response.body().getId());
                    preferenceManager.putBoolean(Constants.IsLogin, true);
                    preferenceManager.putString(Constants.Password, password);
                    dataToken.saveToken(response.body().getNotification());
                    addImgUser(response.body().getId());
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

    //SignIn by Phone
    private void sendVerificationCode(String number) {
        SignUpActivity.mAuth = FirebaseAuth.getInstance();

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(SignUpActivity.mAuth)
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
            Log.d("Send", "onVerificationCompleted:" + credential);
            verifyCode(credential.getSmsCode());
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.w("Send", "onVerificationFailed", e);
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            Log.d("Send", "onCodeSent:" + verificationId);
            Toast.makeText(getApplicationContext(), "Đã gửi OTP", Toast.LENGTH_SHORT).show();
            SignUpActivity.mVerificationId = verificationId;
        }
    };

    //code xác thực OTP
    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(SignUpActivity.mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        SignUpActivity.mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Confirm", "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            signUP(account, account.getPass());
                        } else {
                            loading(false);
                            Log.w("Confirm", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                CustomToast.makeText(VerifyOTPActivity.this, "Mã OTP đã hết hạn", CustomToast.LENGTH_SHORT, CustomToast.ERROR).show();
                            }
                        }
                        loading(false);
                    }
                });
    }


    // Đọc mã OTP từ tin nhắn
    private void startSmartUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(VerifyOTPActivity.this);
        client.startSmsUserConsent(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                getOtpFromMessage(message);
            }
        }
    }

    private void getOtpFromMessage(String message) {
        Pattern otpPattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = otpPattern.matcher(message);
        if (matcher.find()) {
            //etOTP.setText(matcher.group(0));
            binding.inputCode1.setText(matcher.group(0).substring(0, 1));
            binding.inputCode2.setText(matcher.group(0).substring(1, 2));
            binding.inputCode3.setText(matcher.group(0).substring(2, 3));
            binding.inputCode4.setText(matcher.group(0).substring(3, 4));
            binding.inputCode5.setText(matcher.group(0).substring(4, 5));
            binding.inputCode6.setText(matcher.group(0).substring(5, 6));
        }
    }

    private void registerBroadcastReceiver() {

        smsBroadcastReceiver = new SmsBroadcastReceiver();


        smsBroadcastReceiver.smsBroadcastReceiverListener = new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
            @Override
            public void onSuccess(Intent intent) {

                try {
                    // Start activity to show consent dialog to user, activity must be started in
                    // 5 minutes, otherwise you'll receive another TIMEOUT intent
                    if (intent != null)
                        startActivityForResult(intent, REQ_USER_CONSENT);
                } catch (ActivityNotFoundException e) {
                    // Handle the exception ...
                }
            }

            @Override
            public void onFailure() {
            }
        };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }
}
package com.psteam.foodlocation.activities;

import static com.psteam.lib.RetrofitClient.getRetrofit;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.psteam.foodlocation.databinding.ActivitySignUpBinding;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.foodlocation.ultilities.CustomToast;
import com.psteam.foodlocation.ultilities.PreferenceManager;
import com.psteam.foodlocation.ultilities.SmsBroadcastReceiver;
import com.psteam.foodlocation.ultilities.Token;
import com.psteam.lib.Services.ServiceAPI;
import com.psteam.lib.modeluser.LogUpModel;
import com.psteam.lib.modeluser.LoginModel;
import com.psteam.lib.modeluser.message;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

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
        verifyStorePermission(SignUpActivity.this);
        setListeners();
    }

    private void setListeners() {
        binding.buttonSignUp.setOnClickListener(v -> {
            if (isValidSignUp()) {
                loading(true);
                checkPhoneNumber(binding.inputPhone.getText().toString());
            }
        });
        binding.buttonSignIn.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        });

        binding.layoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private boolean isValidSignUp() {
        if (binding.imageProfile.getTag().equals("null")) {
            CustomToast.makeText(getApplicationContext(), "Chọn ảnh đại điện", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (binding.inputFullName.getText().toString().trim().isEmpty()) {
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
        CustomToast.makeText(getApplicationContext(), message, CustomToast.LENGTH_SHORT, CustomToast.ERROR).show();
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
    private void sendVerificationCode(String number) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(number)
                .setTimeout(70L, TimeUnit.SECONDS)
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
            showToast("onVerificationCompleted");
            verifyCode(credential.getSmsCode());
            loading(false);
        }

        //fail
        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.w("Send", "onVerificationFailed", e);
            showToast(e.getMessage());
            loading(false);
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            Log.d("Send", "onCodeSent:" + verificationId);
            //ShowNotification.dismissProgressDialog();
            CustomToast.makeText(getApplicationContext(), "Đã gửi OTP", CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
            mVerificationId = verificationId;
            mResendToken = token;

            String strName = binding.inputFullName.getText().toString().trim();
            String strPhone = binding.inputPhone.getText().toString().trim();
            String strPassword = binding.inputPassword.getText().toString().trim();
            boolean strGender = binding.radioButtonMale.isChecked();

            loading(false);
            Intent intent = new Intent(SignUpActivity.this, VerifyOTPActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("account", new LogUpModel(true, strGender, strPhone, strPassword, strName));
            bundle.putString("imageUri", getRealPathFromURI(imageUri));
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    };

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    //code xác thực OTP
    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Confirm", "signInWithCredential:success");
                            showToast("success");
                            FirebaseUser user = task.getResult().getUser();
                        } else {
                            Log.w("Confirm", "signInWithCredential:failure", task.getException());
                            showToast("failure");
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            }
                        }
                    }
                });
    }

    private Uri imageUri;

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageProfile.setImageBitmap(bitmap);
                            binding.imageProfile.setTag("had");
                            binding.textAddImage.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private void checkPhoneNumber(String phone) {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<message> call = serviceAPI.checkPhoneNumber(phone);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    sendVerificationCode("+84" + binding.inputPhone.getText().toString());
                } else if (response.body() != null && response.body().getStatus().equals("0")) {
                    CustomToast.makeText(getApplicationContext(), "Số điện thoại này đã được sử dụng", CustomToast.LENGTH_SHORT, CustomToast.ERROR).show();
                    loading(false);
                }
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Log.d("Tag", t.getMessage());
            }
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



}
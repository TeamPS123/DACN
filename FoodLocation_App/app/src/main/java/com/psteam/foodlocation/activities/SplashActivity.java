package com.psteam.foodlocation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.ActivitySplashBinding;
import com.psteam.foodlocation.databinding.LayoutLoadingDialogBinding;
import com.psteam.foodlocation.services.FetchAddressIntentServices;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.foodlocation.ultilities.CustomToast;
import com.psteam.foodlocation.ultilities.LoadingDialog;
import com.psteam.foodlocation.ultilities.Para;
import com.psteam.foodlocation.ultilities.PreferenceManager;

public class SplashActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    private ActivitySplashBinding binding;

    private ResultReceiver resultReceiver;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private boolean loadDataSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setFullScreen();
        checkSelfPermission();
        Loading();
    }

    public void Loading() {
        binding.animation.addAnimatorListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                if (loadDataSuccess) {
                    NextActivity();
                }
            }
        });
    }

    private void NextActivity() {
        if (!preferenceManager.getBoolean(Constants.IsLogin)) {
            Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            //tde tam sau sửa
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
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

    @SuppressLint("MissingPermission")
    public void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(SplashActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                super.onLocationResult(locationResult);

                                LocationServices.getFusedLocationProviderClient(SplashActivity.this)
                                        .removeLocationUpdates(this);
                                if (locationResult != null && locationResult.getLastLocation() != null) {
                                    double latitude =
                                            locationResult.getLastLocation().getLatitude();
                                    double longitude = locationResult.getLastLocation().getLongitude();
                                    Para.latitude = latitude;
                                    Para.longitude = longitude;

                                    Location location = new Location("providerNA");
                                    location.setLatitude(latitude);
                                    location.setLongitude(longitude);
                                    fetchAddressFromLatLong(location);
                                } else {

                                }
                            }
                        }
                        , Looper.getMainLooper());

    }

    private void checkSelfPermission() {
        resultReceiver = new SplashActivity.AddressResultReceiver(new Handler());

        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    SplashActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );
        } else {
            getCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getCurrentLocation();
            else {
                CustomToast.makeText(getApplicationContext(), "Permission denied", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            }
        }
    }

    private void fetchAddressFromLatLong(Location location) {
        Intent intent = new Intent(this, FetchAddressIntentServices.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.SUCCESS_RESULT) {
                loadDataSuccess = true;
                Para.currentAddress = resultData.getString(Constants.RESULT_DATA_KEY);
                Para.currentUserAddress = resultData.getString(Constants.RESULT_DATA_KEY);
                if (resultData.getString(Constants.RESULT_DATA_KEY) != null && !resultData.getString(Constants.RESULT_DATA_KEY).isEmpty()) {
                    String s = resultData.getString(Constants.RESULT_SUB_CITY);
                    if (s == null) {
                        s = resultData.getString(Constants.RESULT_SUB_ADMIN_CITY);
                        if (s == null)
                            s = resultData.getString(Constants.RESULT_CITY);
                    }
                    Para.currentCity = s;
                }
            } else {
                loadDataSuccess = false;
                Toast.makeText(getApplicationContext(), resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
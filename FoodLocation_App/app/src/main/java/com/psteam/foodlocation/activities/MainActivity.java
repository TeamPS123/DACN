package com.psteam.foodlocation.activities;

import static com.psteam.lib.RetrofitClient.getRetrofit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.NotificationAdapter;
import com.psteam.foodlocation.databinding.ActivityMainBinding;
import com.psteam.foodlocation.fragments.MainFragment;
import com.psteam.foodlocation.fragments.NotificationFragment;
import com.psteam.foodlocation.fragments.ReviewFragment;
import com.psteam.foodlocation.socket.setupSocket;
import com.psteam.foodlocation.ultilities.Constants;


import com.psteam.foodlocation.ultilities.Token;
import com.psteam.lib.Models.reserveTableDetail.messageReserveTable;
import com.psteam.lib.Models.reserveTableDetail.reserveTable;
import com.psteam.lib.Services.ServiceAPI;
import com.psteam.foodlocation.ultilities.PreferenceManager;


import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private static PreferenceManager preferenceManager;
    private static Token token;
    private static Activity activity;
    private Socket mSocket;

    {
        try {
            mSocket = IO.socket(setupSocket.uriLocal);
        } catch (URISyntaxException e) {
            e.getMessage();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
        init();
        setListeners();
    }

    private void init() {
        setFullScreen();
        preferenceManager = new PreferenceManager(getApplicationContext());
        token = new Token(getApplicationContext());
        setFCM();
        socket();
    }

    private void setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.white));// set status background white
        }
    }

    private void setListeners() {

        getSupportFragmentManager().beginTransaction().replace(binding.layoutFragment.getId(), new MainFragment()).commit();

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.menuReserve:
                        selectedFragment = new MainFragment();
                        break;
                    case R.id.menuReview:
                        selectedFragment = new ReviewFragment();
                        break;
                    default:
                        selectedFragment = new NotificationFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(binding.layoutFragment.getId(), selectedFragment).commit();
                return true;
            }
        });
    }

    public static void updateNotification(String reserveTableId) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                GetUserReserveTableModel(preferenceManager.getString(Constants.USER_ID), reserveTableId);
            }
        });

    }

    private static reserveTable reserveTable;

    private static void GetUserReserveTableModel(String userId, String reserveTableId) {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<messageReserveTable> call = serviceAPI.getAllFoodByReserveTableId(token.getToken(), userId, reserveTableId);
        call.enqueue(new Callback<messageReserveTable>() {
            @Override
            public void onResponse(Call<messageReserveTable> call, Response<messageReserveTable> response) {
                if (response.body() != null && response.body().getStatus() == 1) {
                    reserveTable = response.body().getReserveTable();
                    NotificationAdapter.Notification notification=new NotificationAdapter.Notification(String.valueOf(reserveTable.getStatus()),reserveTable.getRestaurant().getMainPic(),
                            new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()),reserveTable);
                    preferenceManager.AddNotification(notification);
                    NotificationFragment.addNotification(notification);
                }
            }

            @Override
            public void onFailure(Call<messageReserveTable> call, Throwable t) {

            }
        });
    }


    private void setFCM() {
        // set notification FCM
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notification_channel", "notification_channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed Successfully";
                        if (!task.isSuccessful()) {
                            msg = "Subscription failed";
                        }
                        Log.e("Notification form FCM", msg);
                    }
                });
    }

    private void socket() {
        setupSocket.mSocket = mSocket;
        setupSocket.mSocket.connect();
        setupSocket.signIn(preferenceManager.getString(Constants.USER_ID));
    }

    @Override
    protected void onStop() {
        super.onStop();
        setupSocket.mSocket.disconnect();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //notification when come back activity
        setupSocket.mSocket.connect();
        setupSocket.reconnect(preferenceManager.getString(Constants.USER_ID), setupSocket.mSocket);
    }

}
package com.psteam.foodlocation.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;

import android.view.MenuItem;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.ActivityBusinessBinding;
import com.psteam.foodlocation.socket.models.User;
import com.psteam.foodlocation.socket.setupSocket;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class BusinessActivity extends AppCompatActivity {
    private ActivityBusinessBinding binding;
    private String user = "restaurant";

    public Socket mSocket;
    {
        try {
            mSocket = IO.socket(setupSocket.uriLocal);
        } catch (URISyntaxException e) {
            e.getMessage();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBusinessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();;
    }

    private void socket(){
        setupSocket.mSocket = mSocket;

        setupSocket.mSocket.connect();
        // notification login success or fail
        setupSocket.mSocket.on("noti_login", onLogin);
        // receiver notification when used app
        setupSocket.mSocket.on("send_notication", onNotification);

        setupSocket.signIn(user);
    }

    private void setListeners() {

        binding.imageMenu.setOnClickListener(v -> {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        });

        binding.navigationView.setItemIconTintList(null);

        NavController navController= Navigation.findNavController(this,R.id.navHostFragment);
        NavigationUI.setupWithNavController(binding.navigationView,navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                binding.textViewTitle.setText(navDestination.getLabel());
            }
        });
    }

    private void init() {
        setFullScreen();

        setFCM();
        socket();
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

    //FCM
    private void setFCM(){
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
                        Log.e("Notification form FCM",msg);
                    }
                });
    }
//
//    // get device token from FCM
//    public void getToken(String user){
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Log.e("notification_getToken", "Fetching FCM registration token failed", task.getException());
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        deviceId = task.getResult();
//
//                        Gson gson = new Gson();
//                        User user1 = new User("user", deviceId);
//                        mSocket.emit("login", gson.toJson(user1));
//                        // Log and toast
//                        Log.e("notification_getToken", deviceId);
//
//                    }
//                });
//    }
//
//    //socket.io
//    private void signIn(String user){
//        getToken(user);
//    }

    private final Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String notification = data.optString("message");
                    Toast.makeText(BusinessActivity.this, notification, Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private final Emitter.Listener onNotification = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String sender = data.optString("sender");
                    String title = data.optString("title");

                    JSONObject body = data.optJSONObject("body");
                    String reserveTableId = body.optString("reserveTableId");
                    int quantity = body.optInt("quantity");
                    String time = body.optString("time");
                    String restaurantId = body.optString("restaurantId");
                    String name = body.optString("name");
                    String phone = body.optString("phone");
                    String promotionId = body.optString("promotionId");

                    //receiver.setText(sender+": "+body);
                }
            });
        }
    };

    @Override
    protected void onStop() {
        super.onStop();

        //notification when out activity
        mSocket.disconnect();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //notification when come back activity
        mSocket.connect();

        setupSocket.reconnect(user, mSocket);
    }

}

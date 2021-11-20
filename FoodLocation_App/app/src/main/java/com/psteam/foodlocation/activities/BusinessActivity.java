package com.psteam.foodlocation.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.psteam.foodlocation.models.Socket.User;
import com.psteam.foodlocation.other.DataTokenAndUserId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class BusinessActivity extends AppCompatActivity {
    private ActivityBusinessBinding binding;
    private String deviceId = "";
    private DataTokenAndUserId dataTokenAndUserId;

    String uriGlobal = "https://food-location.herokuapp.com/";
    String uriLocal = "http://192.168.1.8:3030/";
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(uriLocal);
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
        setListeners();
    }

    private void setListeners() {
        binding.imageMenu.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),ReserveTableDetailsActivity.class));
        });

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

        dataTokenAndUserId = new DataTokenAndUserId(BusinessActivity.this);
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

    // get device token from FCM
    public void getToken(String user){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.e("notification_getToken", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        deviceId = task.getResult();

                        Gson gson = new Gson();
                        User user1 = new User(user, deviceId);
                        mSocket.emit("login", gson.toJson(user1));
                        // Log and toast
                        Log.e("notification_getToken", deviceId);

                    }
                });
    }

    //socket.io
    private void socket(){
        mSocket.connect();
        // notification login success or fail
        mSocket.on("noti_login", onLogin);
        // receiver notification when used app
        mSocket.on("send_notication", onNotification);
        // receiver notification when start app
        mSocket.on("new_notification", onNewNotification);

        signIn(dataTokenAndUserId.getUserId());
    }

    private void signIn(String user){
        getToken(user);
    }

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
                    String body = data.optString("body");
                    //receiver.setText(sender+": "+body);
                }
            });
        }
    };

    private final Emitter.Listener onNewNotification = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    JSONArray listNotification = data.optJSONArray("list");

//                    for(int i = 0; i < listNotification.length(); i++){
//                        try {
//                            JSONObject jsonObject = new JSONObject(listNotification.getString(i));
//
//                            BodyReceiver receiver1 = new BodyReceiver(jsonObject.optString("sender"), jsonObject.optString("title"), jsonObject.optString("body"));
//
//                            receiver.setText(receiver1.getTitle()+": "+receiver1.getBody());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//
//                        }
//                    }
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

        Gson gson = new Gson();
        User user1 = new User(dataTokenAndUserId.getUserId(), deviceId);
        mSocket.emit("login", gson.toJson(user1));
    }
}

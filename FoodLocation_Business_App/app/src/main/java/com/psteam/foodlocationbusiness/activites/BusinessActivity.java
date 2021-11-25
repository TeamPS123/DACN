package com.psteam.foodlocationbusiness.activites;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.databinding.ActivityBusinessBinding;
import com.psteam.foodlocationbusiness.socket.setupSocket;

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
        setListeners();
    }

    private void setListeners() {

        binding.imageMenu.setOnClickListener(v -> {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        });

        binding.navigationView.setItemIconTintList(null);

        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(binding.navigationView, navController);

        binding.navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        if (menuItem.getItemId() == R.id.menuLogOut) {
                            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finishAffinity();
                        }

                        // You need this line to handle the navigation
                        boolean handled = NavigationUI.onNavDestinationSelected(menuItem, navController);
                        if (handled) {
                            ViewParent parent = binding.navigationView.getParent();
                            if (parent instanceof DrawerLayout) {
                                ((DrawerLayout) parent).closeDrawer(binding.navigationView);
                            }
                        }
                        return handled;
                    }
                });


        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                binding.textViewTitle.setText(navDestination.getLabel());
            }
        });

    }

    private void init() {
        setFullScreen();
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

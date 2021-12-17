package com.psteam.foodlocation.activities;

import static com.psteam.lib.RetrofitClient.getRetrofit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.makeramen.roundedimageview.RoundedImageView;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.CategoryAdapter;
import com.psteam.foodlocation.adapters.ChooseCityAdapter;
import com.psteam.foodlocation.adapters.NotificationAdapter;
import com.psteam.foodlocation.adapters.PromotionAdapter;
import com.psteam.foodlocation.adapters.RestaurantPostAdapter;
import com.psteam.foodlocation.adapters.SliderAdapter;
import com.psteam.foodlocation.databinding.ActivityMainBinding;
import com.psteam.foodlocation.fragments.MainFragment;
import com.psteam.foodlocation.fragments.NotificationFragment;
import com.psteam.foodlocation.fragments.ReviewFragment;
import com.psteam.foodlocation.listeners.CategoryListener;
import com.psteam.foodlocation.models.PromotionModel;
import com.psteam.foodlocation.models.SliderItem;
import com.psteam.foodlocation.services.FetchAddressIntentServices;
import com.psteam.foodlocation.socket.models.BodySenderFromRes;
import com.psteam.foodlocation.socket.setupSocket;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.foodlocation.ultilities.CustomToast;
import com.psteam.foodlocation.ultilities.DividerItemDecorator;
import com.psteam.foodlocation.ultilities.Para;


import com.psteam.foodlocation.ultilities.Token;
import com.psteam.lib.Services.ServiceAPI;
import com.psteam.lib.modeluser.CategoryRes;
import com.psteam.lib.modeluser.GetCategoryResModel;
import com.psteam.lib.modeluser.GetInfoUser;
import com.psteam.lib.modeluser.GetRestaurantByDistance;
import com.psteam.lib.modeluser.GetRestaurantModel;
import com.psteam.lib.modeluser.RestaurantModel;
import com.psteam.foodlocation.ultilities.PreferenceManager;

import com.psteam.lib.modeluser.UserModel;
import com.psteam.library.TopSheetBehavior;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListeners();
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

}
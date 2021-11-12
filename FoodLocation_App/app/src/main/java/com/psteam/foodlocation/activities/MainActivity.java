package com.psteam.foodlocation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.CategoryAdapter;
import com.psteam.foodlocation.adapters.PromotionAdapter;
import com.psteam.foodlocation.adapters.SliderAdapter;
import com.psteam.foodlocation.databinding.ActivityMainBinding;
import com.psteam.foodlocation.listeners.CategoryListener;
import com.psteam.foodlocation.models.CategoryModel;
import com.psteam.foodlocation.models.PromotionModel;
import com.psteam.foodlocation.models.SliderItem;
import com.psteam.foodlocation.services.FetchAddressIntentServices;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.foodlocation.ultilities.Para;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryListener {

    private ActivityMainBinding binding;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    private SliderAdapter sliderAdapter;
    private ArrayList<SliderItem> sliderItemArrayList;
    private Handler sliderHandler = new Handler();

    private CategoryAdapter categoryAdapter;
    private ArrayList<CategoryModel> categoryModelArrayList;

    private PromotionAdapter promotionAdapter;
    private List<PromotionModel> promotionModels;

    private ResultReceiver resultReceiver;

    private MaterialButton buttonSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.white));// set status background white
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();

    }

    private void init() {
        buttonSignIn = binding.navigationView.getHeaderView(0).findViewById(R.id.buttonSignInNavigation);
        checkSelfPermission();
        initSliderImage();
        initCategory();
        initPromotion();
    }

    private void checkSelfPermission() {
        resultReceiver = new AddressResultReceiver(new Handler());

        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
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
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
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
                Para.currentAddress = resultData.getString(Constants.RESULT_DATA_KEY);
                binding.textviewCurrentLocation.setText(resultData.getString(Constants.RESULT_DATA_KEY));
            } else {
                Toast.makeText(getApplicationContext(), resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setListeners() {
        binding.textviewCurrentLocation.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        binding.buttonSearch.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        binding.imageMenu.setOnClickListener(v -> {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        });

        binding.navigationView.setItemIconTintList(null);

        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getTitle().equals(getString(R.string.text_logout))){
                    startActivity(new Intent(MainActivity.this,SignInActivity.class));
                    finishAffinity();
                }

                return false;
            }
        });

        buttonSignIn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this,SignInActivity.class));
        });
    }

    private void initSliderImage() {
        // init Data
        sliderItemArrayList = new ArrayList<>();
        sliderItemArrayList.add(new SliderItem(R.drawable.panner1));
        sliderItemArrayList.add(new SliderItem(R.drawable.paner2));
        sliderItemArrayList.add(new SliderItem(R.drawable.paner3));

        sliderAdapter = new SliderAdapter(sliderItemArrayList, binding.viewPagerSliderImage);
        binding.viewPagerSliderImage.setAdapter(sliderAdapter);

        binding.viewPagerSliderImage.setClipToPadding(false);
        binding.viewPagerSliderImage.setClipChildren(false);
        binding.viewPagerSliderImage.setOffscreenPageLimit(3);
        binding.viewPagerSliderImage.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(20));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        binding.viewPagerSliderImage.setPageTransformer(compositePageTransformer);
        binding.viewPagerSliderImage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });


    }

    private void initCategory() {
        categoryModelArrayList = new ArrayList<>();
        categoryModelArrayList.add(new CategoryModel(R.drawable.ic_baseline_restaurant_menu_24, "Lẩu"));
        categoryModelArrayList.add(new CategoryModel(R.drawable.ic_baseline_restaurant_menu_24, "Nướng"));
        categoryModelArrayList.add(new CategoryModel(R.drawable.ic_baseline_restaurant_menu_24, "Trà sửa"));
        categoryModelArrayList.add(new CategoryModel(R.drawable.ic_baseline_restaurant_menu_24, "Hải sản"));

        categoryAdapter = new CategoryAdapter(categoryModelArrayList, this);
        binding.recycleViewCategory.setAdapter(categoryAdapter);
    }

    private void initPromotion() {
        promotionModels = new ArrayList<>();
        promotionModels.add(new PromotionModel(R.drawable.suatuoi, "ToCoToCo", "Đồng giá 32k Toàn menu Size M"));
        promotionModels.add(new PromotionModel(R.drawable.suatuoi, "ToCoToCo", "Đồng giá 32k Toàn menu Size M"));
        promotionModels.add(new PromotionModel(R.drawable.suatuoi, "ToCoToCo", "Đồng giá 32k Toàn menu Size M"));
        promotionModels.add(new PromotionModel(R.drawable.suatuoi, "ToCoToCo", "Đồng giá 32k Toàn menu Size M"));
        promotionModels.add(new PromotionModel(R.drawable.suatuoi, "ToCoToCo", "Đồng giá 32k Toàn menu Size M"));
        promotionModels.add(new PromotionModel(R.drawable.suatuoi, "ToCoToCo", "Đồng giá 32k Toàn menu Size M"));

        promotionAdapter = new PromotionAdapter(promotionModels);
        binding.recycleViewPromotion.setAdapter(promotionAdapter);
        binding.recycleViewPromotion.setClipToPadding(false);
        binding.recycleViewPromotion.setClipChildren(false);

    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            binding.viewPagerSliderImage.setCurrentItem(binding.viewPagerSliderImage.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }

    @Override
    public void onCategoryClick(CategoryModel categoryModel) {

    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                super.onLocationResult(locationResult);

                                LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                        .removeLocationUpdates(this);
                                if (locationResult != null && locationResult.getLastLocation() != null) {
                                    double latitude =
                                            locationResult.getLastLocation().getLatitude();
                                    double longitude = locationResult.getLastLocation().getLongitude();
                                    Para.latitude = latitude;
                                    Para.longitude = longitude;
                                    //binding.textviewCurrentLocation.setText(String.format("Latitude: %s  Longitude:%s", latitude, longitude));

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
}
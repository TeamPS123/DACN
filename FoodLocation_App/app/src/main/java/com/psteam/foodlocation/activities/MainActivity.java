package com.psteam.foodlocation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.CategoryAdapter;
import com.psteam.foodlocation.adapters.ChooseCityAdapter;
import com.psteam.foodlocation.adapters.ChooseNumberPeopleAdapter;
import com.psteam.foodlocation.adapters.NotificationAdapter;
import com.psteam.foodlocation.adapters.PromotionAdapter;
import com.psteam.foodlocation.adapters.RestaurantPostAdapter;
import com.psteam.foodlocation.adapters.SliderAdapter;
import com.psteam.foodlocation.databinding.ActivityMainBinding;
import com.psteam.foodlocation.listeners.CategoryListener;
import com.psteam.foodlocation.models.CategoryModel;
import com.psteam.foodlocation.models.PromotionModel;
import com.psteam.foodlocation.models.SliderItem;
import com.psteam.foodlocation.services.FetchAddressIntentServices;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.foodlocation.ultilities.CustomToast;
import com.psteam.foodlocation.ultilities.DividerItemDecorator;
import com.psteam.foodlocation.ultilities.Para;
import com.psteam.foodlocation.ultilities.PreferenceManager;
import com.psteam.library.TopSheetBehavior;
import com.psteam.library.TopSheetDialog;

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

    private RestaurantPostAdapter restaurantPostAdapter;
    private ArrayList<RestaurantPostAdapter.FoodRestaurant> foodRestaurants;

    private ResultReceiver resultReceiver;

    private MaterialButton buttonSignIn;

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
        buttonSignIn = binding.navigationView.getHeaderView(0).findViewById(R.id.buttonSignInNavigation);
        checkSelfPermission();
        initSliderImage();
        initCategory();
        initPromotion();
        initFoodRestaurant();
        setNumberNotification(0);
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
                Para.currentAddress = resultData.getString(Constants.RESULT_DATA_KEY);
                binding.textviewCurrentLocation.setText(resultData.getString(Constants.RESULT_DATA_KEY));
                if (resultData.getString(Constants.RESULT_DATA_KEY) != null && !resultData.getString(Constants.RESULT_DATA_KEY).isEmpty())
                    binding.textTitle.setText(String.format("Các địa điểm ở %s", resultData.getString(Constants.RESULT_CITY)));
            } else {
                Toast.makeText(getApplicationContext(), resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ArrayList<NotificationAdapter.Notification> notifications;
    private TopSheetBehavior topSheetBehavior;
    private NotificationAdapter notificationAdapter;

    private void setListeners() {
        notifications = new ArrayList<>();
        topSheetBehavior = new TopSheetBehavior();
        binding.textviewCurrentLocation.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        binding.buttonNotification.setOnClickListener(v -> {
            notifications.add(new NotificationAdapter.Notification("Đơn đặt bàn của bạn đã được xác nhận", "1"));
            notifications.add(new NotificationAdapter.Notification("Bạn đã đến nhà hàng, bạn có thể gói món ngay trên ứng dụng", "1"));
            badgeDrawable.setNumber(notifications.size());
            if (notifications.size() > 0) {
                binding.textEmptyNotification.setVisibility(View.GONE);
            } else {
                binding.textEmptyNotification.setVisibility(View.VISIBLE);
            }
            notificationAdapter = new NotificationAdapter(notifications);
            binding.recycleViewNotification.setAdapter(notificationAdapter);
            new ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.recycleViewNotification);
            RecyclerView.ItemDecoration itemDecoration = new DividerItemDecorator(getDrawable(R.drawable.divider));
            binding.recycleViewNotification.addItemDecoration(itemDecoration);

            topSheetBehavior.from(binding.topSheet).setState(TopSheetBehavior.STATE_EXPANDED);

        });

        binding.textViewExpand.setOnClickListener(v -> {
            topSheetBehavior.from(binding.topSheet).setState(TopSheetBehavior.STATE_COLLAPSED);
        });


        binding.imageMenu.setOnClickListener(v -> {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        });

        binding.navigationView.setItemIconTintList(null);


        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menuUserInfo: {
                        startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
                        break;
                    }

                    case R.id.menuBookTable: {
                        startActivity(new Intent(MainActivity.this, UserBookTableHistoryActivity.class));
                        break;
                    }

                    case R.id.menuLogOut: {

                        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
                        preferenceManager.clear();

                        startActivity(new Intent(MainActivity.this, SignInActivity.class));
                        finishAffinity();
                        break;
                    }

                    case R.id.menuManager: {
                        startActivity(new Intent(MainActivity.this, BusinessActivity.class));
                        break;
                    }

                    case R.id.menuChangeRegionSearch: {
                        clickOpenBottomSheetChooseCityFragment();
                        break;
                    }
                }
                return false;
            }
        });

        buttonSignIn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
        });
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            notifications.remove(viewHolder.getAdapterPosition());
            notificationAdapter.notifyDataSetChanged();
            badgeDrawable.setNumber(notifications.size());
            if (notifications.size() <= 0) {
                topSheetBehavior.from(binding.topSheet).setState(TopSheetBehavior.STATE_COLLAPSED);
            }
        }
    };

    private BadgeDrawable badgeDrawable;

    public void setNumberNotification(int number) {
        binding.buttonNotification.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("UnsafeOptInUsageError")
            @Override
            public void onGlobalLayout() {
                badgeDrawable = BadgeDrawable.create(MainActivity.this);
                badgeDrawable.setNumber(number);
                badgeDrawable.setBadgeGravity(BadgeDrawable.TOP_END);
                BadgeUtils.attachBadgeDrawable(badgeDrawable, binding.buttonNotification, findViewById(R.id.layout));
                binding.buttonNotification.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(binding.buttonNotification.getWidth() / 4, 0, binding.buttonNotification.getWidth(), 0);
                binding.buttonNotification.setLayoutParams(lp);

            }
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
        categoryModelArrayList.add(new CategoryModel("Cơm", R.drawable.rice));
        categoryModelArrayList.add(new CategoryModel("Lẩu", R.drawable.hotpot));
        categoryModelArrayList.add(new CategoryModel("Trà sửa", R.drawable.bubble_tea));
        categoryModelArrayList.add(new CategoryModel("Bia", R.drawable.beer));
        categoryModelArrayList.add(new CategoryModel("Nướng", R.drawable.barbecue));
        categoryModelArrayList.add(new CategoryModel("Coffee", R.drawable.coffee));
        categoryModelArrayList.add(new CategoryModel("Phở", R.drawable.ramen));
        categoryModelArrayList.add(new CategoryModel("Nước ép", R.drawable.drink));
        categoryModelArrayList.add(new CategoryModel("Pizza", R.drawable.pizza));

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
        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
    }

    private void initFoodRestaurant() {
        foodRestaurants = new ArrayList<>();
        foodRestaurants.add(new RestaurantPostAdapter.FoodRestaurant(R.drawable.lau, "TAKA BBQ: GIẢM 15% TẤT CẢ GÓI BUFFET", 4, 10, "875/22 Nguyễn Văn Cừ", "-15%"));
        foodRestaurants.add(new RestaurantPostAdapter.FoodRestaurant(R.drawable.lau, "TAKA BBQ: GIẢM 20% TẤT CẢ GÓI BUFFET", 4.6, 10, "875/22 Nguyễn Văn Cừ", "-15%"));
        foodRestaurants.add(new RestaurantPostAdapter.FoodRestaurant(R.drawable.lau, "TAKA BBQ: GIẢM 25% TẤT CẢ GÓI BUFFET", 4.75, 10, "875/22 Nguyễn Văn Cừ", "-15%"));
        foodRestaurants.add(new RestaurantPostAdapter.FoodRestaurant(R.drawable.lau, "TAKA BBQ: GIẢM 30% TẤT CẢ GÓI BUFFET", 4.4, 10, "875/22 Nguyễn Văn Cừ", "-15%"));
        foodRestaurants.add(new RestaurantPostAdapter.FoodRestaurant(R.drawable.lau, "TAKA BBQ: GIẢM 35% TẤT CẢ GÓI BUFFET", 3, 10, "875/22 Nguyễn Văn Cừ", "-15%"));

        restaurantPostAdapter = new RestaurantPostAdapter(foodRestaurants, new RestaurantPostAdapter.RestaurantPostListeners() {
            @Override
            public void onRestaurantPostClicked(RestaurantPostAdapter.FoodRestaurant foodRestaurant) {
                startActivity(new Intent(getApplicationContext(), RestaurantDetailsActivity.class));
            }
        });
        binding.recycleViewPostFoodRestaurant.setAdapter(restaurantPostAdapter);
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

    private ChooseCityBottomSheetFragment chooseCityBottomSheetFragment;
    private ArrayList<ChooseCityAdapter.City> cities;


    private void clickOpenBottomSheetChooseCityFragment() {
        cities = new ArrayList<>();

        cities.add(new ChooseCityAdapter.City("Tp.Hồ Chí Minh", "79"));
        cities.add(new ChooseCityAdapter.City("Hà Nội", "1"));
        cities.add(new ChooseCityAdapter.City("Lâm Đồng", "68"));

        chooseCityBottomSheetFragment = new ChooseCityBottomSheetFragment(cities, new ChooseCityAdapter.ChooseCityListener() {
            @Override
            public void onChooseCityClicked(ChooseCityAdapter.City city) {
                Para.cityCode = city.getCode();
            }
        });

        chooseCityBottomSheetFragment.show(getSupportFragmentManager(), chooseCityBottomSheetFragment.getTag());

    }
}
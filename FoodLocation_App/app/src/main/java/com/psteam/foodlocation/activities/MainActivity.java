package com.psteam.foodlocation.activities;

import static com.psteam.lib.RetrofitClient.getRetrofit;

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
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.CategoryAdapter;
import com.psteam.foodlocation.adapters.ChooseCityAdapter;
import com.psteam.foodlocation.adapters.NotificationAdapter;
import com.psteam.foodlocation.adapters.PromotionAdapter;
import com.psteam.foodlocation.adapters.RestaurantPostAdapter;
import com.psteam.foodlocation.adapters.SliderAdapter;
import com.psteam.foodlocation.databinding.ActivityMainBinding;
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


public class MainActivity extends AppCompatActivity implements CategoryListener, RestaurantPostAdapter.RestaurantPostListeners {

    private ActivityMainBinding binding;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    private SliderAdapter sliderAdapter;
    private ArrayList<SliderItem> sliderItemArrayList;
    private Handler sliderHandler = new Handler();

    private CategoryAdapter categoryAdapter;
    private ArrayList<CategoryRes> categoryModelArrayList;
    private Token token;
    private PreferenceManager preferenceManager;
    private PromotionAdapter promotionAdapter;
    private List<PromotionModel> promotionModels;

    private RestaurantPostAdapter restaurantPostAdapter;

    private ResultReceiver resultReceiver;
    private UserModel user;
    private MaterialButton buttonSignIn;

    public Socket mSocket;
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
        token = new Token(getApplicationContext());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(binding.getRoot());
        init();
        setListeners();
    }

    private void init() {
        setFullScreen();
        buttonSignIn = binding.navigationView.getHeaderView(0).findViewById(R.id.buttonSignInNavigation);
        checkSelfPermission();
        GetInfo(preferenceManager.getString(Constants.USER_ID));
        initSliderImage();
        GetCategoryRes();
        setNumberNotification(0);

        setFCM();
        socket();
        GetRestaurantByDistance(new GetRestaurantByDistance("20",  "10.803312745723506","106.71158641576767"));
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
            Bundle bundle = new Bundle();
            bundle.putSerializable("categoryModelArrayList", categoryModelArrayList);
            bundle.putSerializable("getRestaurantModels",getRestaurantModels);
            bundle.putString("flag","normal");
            intent.putExtra("bundle", bundle);
            startActivity(intent);
        });

        binding.buttonNotification.setOnClickListener(v -> {
            badgeDrawable.setNumber(notifications.size());


            if (notifications.size() > 0) {
                binding.textEmptyNotification.setVisibility(View.GONE);
            } else {
                binding.textEmptyNotification.setVisibility(View.VISIBLE);
            }
            notificationAdapter = new NotificationAdapter(notifications, new NotificationAdapter.NotificationListeners() {
                @Override
                public void onClicked(NotificationAdapter.Notification notification, int position) {
                    Intent intent = new Intent(getApplicationContext(), UserReserveTableDetailsActivity.class);
                    intent.putExtra("response", new BodySenderFromRes(notification.getContent(), notification.getType()));
                    startActivity(intent);

                    notifications.remove(position);
                    notificationAdapter.notifyDataSetChanged();
                }
            });
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
                        Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user", user);
                        intent.putExtra("bundle", bundle);
                        startActivity(intent);
                        break;
                    }

                    case R.id.menuBookTable: {
                        startActivity(new Intent(MainActivity.this, UserBookTableHistoryActivity.class));
                        break;
                    }

                    case R.id.menuLogOut: {
                        setupSocket.signOut();

                        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
                        preferenceManager.clear();

                        startActivity(new Intent(MainActivity.this, SignInActivity.class));
                        finishAffinity();
                        logOut();
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


    private void GetInfo(String id) {

        if (token.getToken().equals(token.expired)) {
            logOut();
        }

        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<GetInfoUser> call = serviceAPI.GetDetailUser(token.getToken(), id);
        call.enqueue(new Callback<GetInfoUser>() {
            @Override
            public void onResponse(Call<GetInfoUser> call, Response<GetInfoUser> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    user = response.body().getUser();
                    Para.userModel = user;
                }
            }

            @Override
            public void onFailure(Call<GetInfoUser> call, Throwable t) {
                Log.d("Log:", t.getMessage());
            }
        });
    }

    public void logOut() {
        preferenceManager.clear();
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finishAffinity();
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
    public void onCategoryClick(CategoryRes categoryModel) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("categoryModelArrayList", categoryModelArrayList);
        bundle.putSerializable("categoryModel",categoryModel);
        bundle.putString("flag","categoryRes");
        intent.putExtra("bundle", bundle);
        startActivity(intent);
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

    //socket
    private final Emitter.Listener onNotification = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String senderUser = data.optString("sender");
                    String title = data.optString("title");
                    JSONObject body = data.optJSONObject("body");

                    notifications.add(new NotificationAdapter.Notification(body.optString("notification"), body.optString("reserveTableId")));
                    badgeDrawable.setNumber(notifications.size());
                    if (notifications.size() > 0) {
                        binding.textEmptyNotification.setVisibility(View.GONE);
                    } else {
                        binding.textEmptyNotification.setVisibility(View.VISIBLE);
                    }
                    notificationAdapter = new NotificationAdapter(notifications, new NotificationAdapter.NotificationListeners() {
                        @Override
                        public void onClicked(NotificationAdapter.Notification notification, int position) {
                            Intent intent = new Intent(getApplicationContext(), UserReserveTableDetailsActivity.class);
                            intent.putExtra("response", new BodySenderFromRes(notification.getContent(), notification.getType()));
                            startActivity(intent);

                            notifications.remove(position);
                            notificationAdapter.notifyDataSetChanged();
                        }
                    });
                    binding.recycleViewNotification.setAdapter(notificationAdapter);
                    new ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.recycleViewNotification);
                    RecyclerView.ItemDecoration itemDecoration = new DividerItemDecorator(getDrawable(R.drawable.divider));
                    binding.recycleViewNotification.addItemDecoration(itemDecoration);

                    topSheetBehavior.from(binding.topSheet).setState(TopSheetBehavior.STATE_EXPANDED);
                }
            });
        }
    };

    private void socket() {
        setupSocket.mSocket = mSocket;

        setupSocket.mSocket.connect();
        // receiver notification when used app
        setupSocket.mSocket.on("send_notication", onNotification);
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
    private void GetCategoryRes() {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<GetCategoryResModel> call = serviceAPI.GetCategoryRes();
        call.enqueue(new Callback<GetCategoryResModel>() {
            @Override
            public void onResponse(Call<GetCategoryResModel> call, Response<GetCategoryResModel> response) {
                if (response.body() != null && response.body().getStatus().equals("1") && response.body().getCategoryResList().size() > 0) {
                    categoryModelArrayList = new ArrayList<>();
                    categoryModelArrayList = response.body().getCategoryResList();
                    categoryAdapter = new CategoryAdapter(categoryModelArrayList, MainActivity.this::onCategoryClick, MainActivity.this);
                    binding.recycleViewCategory.setAdapter(categoryAdapter);
                }
            }

            @Override
            public void onFailure(Call<GetCategoryResModel> call, Throwable t) {
                Log.d("Log:", t.getMessage());
            }
        });
    }

    private GetRestaurantModel getRestaurantModels;

    private void GetRestaurantByDistance(GetRestaurantByDistance getRestaurantByDistance) {

        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<GetRestaurantModel> call = serviceAPI.GetResByDistance(getRestaurantByDistance);
        call.enqueue(new Callback<GetRestaurantModel>() {
            @Override
            public void onResponse(Call<GetRestaurantModel> call, Response<GetRestaurantModel> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    if (response.body().getResList().size() > 0) {
                        getRestaurantModels = response.body();
                        restaurantPostAdapter = new RestaurantPostAdapter(getRestaurantModels.getResList(), MainActivity.this::onRestaurantPostClicked, getApplicationContext());
                        binding.recycleViewPostFoodRestaurant.setAdapter(restaurantPostAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetRestaurantModel> call, Throwable t) {
                Log.d("Log:", t.getMessage());
            }
        });
    }

    @Override
    public void onRestaurantPostClicked(RestaurantModel restaurantModel) {
        Intent intent = new Intent(getApplicationContext(), RestaurantDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("restaurantModel", restaurantModel);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }
}
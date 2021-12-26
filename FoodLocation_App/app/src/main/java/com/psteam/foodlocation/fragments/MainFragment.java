package com.psteam.foodlocation.fragments;

import static com.psteam.lib.RetrofitClient.getRetrofit;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.activities.BusinessActivity;
import com.psteam.foodlocation.activities.ChooseCityBottomSheetFragment;
import com.psteam.foodlocation.activities.RestaurantDetailsActivity;
import com.psteam.foodlocation.activities.SearchActivity;
import com.psteam.foodlocation.activities.SettingActivity;
import com.psteam.foodlocation.activities.SignInActivity;
import com.psteam.foodlocation.activities.UserBookTableHistoryActivity;
import com.psteam.foodlocation.activities.UserInfoActivity;
import com.psteam.foodlocation.adapters.CategoryAdapter;
import com.psteam.foodlocation.adapters.ChooseCityAdapter;
import com.psteam.foodlocation.adapters.PromotionAdapter;
import com.psteam.foodlocation.adapters.RecommendResAdapter;
import com.psteam.foodlocation.adapters.RestaurantPostAdapter;
import com.psteam.foodlocation.adapters.RestaurantRecentAdapter;
import com.psteam.foodlocation.adapters.SliderAdapter;
import com.psteam.foodlocation.databinding.FragmentMainBinding;
import com.psteam.foodlocation.listeners.CategoryListener;
import com.psteam.foodlocation.models.PromotionModel;
import com.psteam.foodlocation.models.SliderItem;
import com.psteam.foodlocation.socket.setupSocket;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.foodlocation.ultilities.Para;
import com.psteam.foodlocation.ultilities.PreferenceManager;
import com.psteam.foodlocation.ultilities.Token;
import com.psteam.lib.Services.ServiceAPI;
import com.psteam.lib.modeluser.CategoryRes;
import com.psteam.lib.modeluser.GetCategoryResModel;
import com.psteam.lib.modeluser.GetInfoUser;
import com.psteam.lib.modeluser.GetRestaurantByDistance;
import com.psteam.lib.modeluser.GetRestaurantModel;
import com.psteam.lib.modeluser.InputSuggestRes;
import com.psteam.lib.modeluser.RestaurantModel;
import com.psteam.lib.modeluser.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment implements CategoryListener, RestaurantPostAdapter.RestaurantPostListeners {

    private SliderAdapter sliderAdapter;
    private ArrayList<SliderItem> sliderItemArrayList;
    private Handler sliderHandler = new Handler();
    private TextView textViewName;
    private RoundedImageView imageUserView;
    private CategoryAdapter categoryAdapter;
    private ArrayList<CategoryRes> categoryModelArrayList;
    private Token token;
    private PreferenceManager preferenceManager;
    private PromotionAdapter promotionAdapter;
    private List<PromotionModel> promotionModels;

    private RestaurantPostAdapter restaurantPostAdapter;
    private RecommendResAdapter recommendResAdapter;
    private ArrayList<RestaurantModel> restaurantSuggest;

    private ResultReceiver resultReceiver;
    private UserModel user;
    private MaterialButton buttonSignIn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private FragmentMainBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false);
        token = new Token(getContext());
        preferenceManager = new PreferenceManager(getContext());
        init();
        setListeners();
        return binding.getRoot();
    }

    private void init() {

        buttonSignIn = binding.navigationView.getHeaderView(0).findViewById(R.id.buttonSignInNavigation);
        textViewName = binding.navigationView.getHeaderView(0).findViewById(R.id.textViewName);
        imageUserView = binding.navigationView.getHeaderView(0).findViewById(R.id.imageUserView);
        restaurantModels = new ArrayList<>();
        restaurantSuggest=new ArrayList<>();
        //checkSelfPermission();
        GetInfo(preferenceManager.getString(Constants.USER_ID));
        initSliderImage();
        GetCategoryRes();
        initResRecent();
        initRecommendRes();
        //get distance Search
        String distance = preferenceManager.getString(Constants.TAG_DISTANCE);
        if (distance == null) {
            distance = "20";
            preferenceManager.putString(Constants.TAG_DISTANCE, distance);
        }

        String day_recommend = preferenceManager.getString(Constants.TAG_DAY_RECOMMEND);
        if (day_recommend == null) {
            day_recommend = "14";
            preferenceManager.putString(Constants.TAG_DAY_RECOMMEND, day_recommend);
        }

        GetRestaurantByDistance(new GetRestaurantByDistance(distance, "10.803312745723506", "106.71158641576767"));
        GetRestaurantSuggest(new InputSuggestRes(distance,"10.803312745723506",preferenceManager.getString(Constants.USER_ID),"106.71158641576767",day_recommend));
        binding.textviewCurrentLocation.setText(Para.currentUserAddress);
        binding.textTitle.setText(String.format("Các địa điểm ở %s", Para.currentCity));
    }

    private void initRecommendRes() {
        recommendResAdapter = new RecommendResAdapter(new RecommendResAdapter.RecommendResListeners() {
            @Override
            public void onClick(RestaurantModel restaurantModel) {

            }
        }, restaurantSuggest, getContext());


        binding.viewPagerSliderRestaurant.setAdapter(recommendResAdapter);

        binding.viewPagerSliderRestaurant.setClipToPadding(false);
        binding.viewPagerSliderRestaurant.setClipChildren(false);
        binding.viewPagerSliderRestaurant.setOffscreenPageLimit(3);
        binding.viewPagerSliderRestaurant.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(20));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.95f + r * 0.05f);
            }
        });

        binding.viewPagerSliderRestaurant.setPageTransformer(compositePageTransformer);

    }

    private ArrayList<RestaurantModel> restaurantModels;
    private ArrayList<RestaurantModel> tempRestaurantModels;
    private RestaurantRecentAdapter restaurantRecentAdapter;

    private void initResRecent() {

        restaurantModels = preferenceManager.getListRestaurantRecent(Constants.TAG_RESTAURANT_RECENT);
        tempRestaurantModels = restaurantModels;
        if (restaurantModels == null || restaurantModels.size() == 0) {
            binding.layout12.setVisibility(View.GONE);
        } else {
            if (binding.layout12.getVisibility() == View.GONE) {
                binding.layout12.setVisibility(View.VISIBLE);
            }
        }
        restaurantRecentAdapter = new RestaurantRecentAdapter(restaurantModels, new RestaurantRecentAdapter.RestaurantRecentListeners() {
            @Override
            public void onClick(RestaurantModel restaurantModel) {
                Intent intent = new Intent(getContext(), RestaurantDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("restaurantModel", restaurantModel);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });

        binding.recycleViewResRecent.setAdapter(restaurantRecentAdapter);
    }

    private void setListeners() {

        binding.textviewCurrentLocation.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("categoryModelArrayList", categoryModelArrayList);
            bundle.putSerializable("getRestaurantModels", getRestaurantModels);
            bundle.putString("flag", "normal");
            intent.putExtra("bundle", bundle);
            startActivity(intent);
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
                        Intent intent = new Intent(getContext(), UserInfoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user", user);
                        intent.putExtra("bundle", bundle);
                        startActivity(intent);
                        binding.drawerLayout.close();
                        break;
                    }

                    case R.id.menuBookTable: {
                        startActivity(new Intent(getContext(), UserBookTableHistoryActivity.class));
                        binding.drawerLayout.close();
                        break;
                    }

                    case R.id.menuLogOut: {
                        setupSocket.signOut();

                        PreferenceManager preferenceManager = new PreferenceManager(getContext());
                        preferenceManager.clear();

                        startActivity(new Intent(getContext(), SignInActivity.class));
                        getActivity().finishAffinity();
                        logOut();
                        binding.drawerLayout.close();
                        break;
                    }

                    case R.id.menuManager: {
                        startActivity(new Intent(getContext(), BusinessActivity.class));
                        binding.drawerLayout.close();
                        break;
                    }

                    case R.id.menuChangeRegionSearch: {
                        startActivity(new Intent(getContext(), SettingActivity.class));
                        binding.drawerLayout.close();
                        break;
                    }
                }
                return false;
            }
        });

        buttonSignIn.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), SignInActivity.class));
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
                page.setScaleY(0.95f + r * 0.05f);
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
                    textViewName.setText(user.getFullName());
                    Picasso.get().load(user.getPic()).into(imageUserView);
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
        Intent intent = new Intent(getContext(), SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finishAffinity();
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            binding.viewPagerSliderImage.setCurrentItem(binding.viewPagerSliderImage.getCurrentItem() + 1);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }

    @Override
    public void onResume() {
        super.onResume();
        tempRestaurantModels = preferenceManager.getListRestaurantRecent(Constants.TAG_RESTAURANT_RECENT);
        if (tempRestaurantModels != null && tempRestaurantModels != restaurantModels)
            initResRecent();
    }

    @Override
    public void onStop() {
        super.onStop();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onCategoryClick(CategoryRes categoryModel) {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("categoryModelArrayList", categoryModelArrayList);
        bundle.putSerializable("categoryModel", categoryModel);
        bundle.putString("flag", "categoryRes");
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    private ChooseCityBottomSheetFragment chooseCityBottomSheetFragment;
    private ArrayList<ChooseCityAdapter.City> cities;

    private void GetCategoryRes() {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<GetCategoryResModel> call = serviceAPI.GetCategoryRes();
        call.enqueue(new Callback<GetCategoryResModel>() {
            @Override
            public void onResponse(Call<GetCategoryResModel> call, Response<GetCategoryResModel> response) {
                if (response.body() != null && response.body().getStatus().equals("1") && response.body().getCategoryResList().size() > 0) {
                    categoryModelArrayList = new ArrayList<>();
                    categoryModelArrayList = response.body().getCategoryResList();
                    categoryAdapter = new CategoryAdapter(categoryModelArrayList, MainFragment.this::onCategoryClick, getContext());
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
                        restaurantPostAdapter = new RestaurantPostAdapter(getRestaurantModels.getResList(), MainFragment.this::onRestaurantPostClicked, getContext());
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

    private void GetRestaurantSuggest(InputSuggestRes inputSuggestRes) {

        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<GetRestaurantModel> call = serviceAPI.GetSuggestRes(inputSuggestRes);
        call.enqueue(new Callback<GetRestaurantModel>() {
            @Override
            public void onResponse(Call<GetRestaurantModel> call, Response<GetRestaurantModel> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    if (response.body().getResList().size() > 0) {
                        restaurantSuggest.clear();
                        restaurantSuggest.addAll(response.body().getResList());
                        recommendResAdapter.notifyDataSetChanged();
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
        Intent intent = new Intent(getContext(), RestaurantDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("restaurantModel", restaurantModel);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }
}
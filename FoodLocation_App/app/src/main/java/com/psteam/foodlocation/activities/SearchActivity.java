package com.psteam.foodlocation.activities;

import static com.psteam.foodlocation.ultilities.RetrofitClient.getRetrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.CategoryRestaurantAdapter;
import com.psteam.foodlocation.adapters.SearchDistrictsAdapter;
import com.psteam.foodlocation.adapters.SearchRestaurantAdapter;
import com.psteam.foodlocation.databinding.ActivitySearchBinding;
import com.psteam.foodlocation.databinding.LayoutCategoryRestaurantDialogBinding;
import com.psteam.foodlocation.models.DistrictModel;
import com.psteam.foodlocation.models.ProvinceModel;
import com.psteam.foodlocation.services.ServiceAPI;

import com.psteam.foodlocation.ultilities.Para;
import com.psteam.lib.modeluser.CategoryRes;
import com.psteam.lib.modeluser.GetRestaurantBySearch;
import com.psteam.lib.modeluser.GetRestaurantModel;
import com.psteam.lib.modeluser.RestaurantModel;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements SearchRestaurantAdapter.SearchRestaurantListeners {

    private ActivitySearchBinding binding;
    private SearchRestaurantAdapter searchRestaurantAdapter;
    private ArrayList<RestaurantModel> restaurantModels;
    private ArrayList<View> selectedViewArrayList;
    private ArrayList<String> selectedDistrictList;
    private ArrayList<String> selectedCategoryRes;
    private ArrayList<CategoryRes> categoryModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListeners();
    }

    private void init() {
        setFullScreen();
        categoryModelArrayList = new ArrayList<>();
        restaurantModel = new GetRestaurantModel();
        selectedCategoryRestaurants = new ArrayList<>();
        selectedViewArrayList = new ArrayList<>();
        selectedImageViews = new ArrayList<>();
        selectedDistrictList = new ArrayList<>();
        districtModels = new ArrayList<>();
        selectedCategoryRes = new ArrayList<>();
        restaurantModels = new ArrayList<>();
        initSearchRestaurantAdapter();
        initData();
        getDistrict(Para.cityCode);
    }

    private void loading(Boolean Loading) {
        if (Loading) {
            binding.recycleViewSearch.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.recycleViewSearch.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        }
    }

    private GetRestaurantModel restaurantModel;

    private void initData() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        categoryModelArrayList = (ArrayList<CategoryRes>) bundle.getSerializable("categoryModelArrayList");
        if (bundle.getString("flag").equals("normal")) {
            restaurantModel = (GetRestaurantModel) bundle.getSerializable("getRestaurantModels");
            if (restaurantModel != null)
                restaurantModels.addAll(restaurantModel.getResList());
            searchRestaurantAdapter.notifyDataSetChanged();
        } else if (bundle.getString("flag").equals("categoryRes")) {
            CategoryRes categoryModel = (CategoryRes) bundle.getSerializable("categoryModel");
            for (CategoryRes categoryRes : categoryModelArrayList) {
                if (categoryModel.getId().equals(categoryRes.getId())) {
                    categoryRes.setSelected(true);
                    break;
                }
            }
            selectedCategoryRes.add(categoryModel.getId());
            runThread(AfterTextChange, selectedDistrictList, selectedCategoryRes, 0);
        }
    }

    private void setFullScreen() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            getWindow().setStatusBarColor(ContextCompat.getColor(SearchActivity.this, R.color.white));// set status background white
        }
    }

    private static String AfterTextChange = "";

    private void setListeners() {
        binding.buttonMap.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("restaurantModels", restaurantModels);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        binding.iconClose.setOnClickListener(v -> {
            finish();
        });

        binding.textviewCategory.setOnClickListener(v -> {
            openCategoryRestaurantDialog();
        });

        binding.textviewDistrict.setOnClickListener(v -> {
            openDistrictDialog();
        });


        binding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                runThread(s.toString().trim().toLowerCase(Locale.ROOT), selectedDistrictList, selectedCategoryRes, 1000);
            }

            @Override
            public void afterTextChanged(Editable s) {
                AfterTextChange = s.toString().trim().toLowerCase(Locale.ROOT);
            }
        });
    }

    private void runThread(String textChange, ArrayList<String> district, ArrayList<String> category, long millis) {
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(millis);
                    if (textChange.equals(AfterTextChange)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(getApplicationContext(), textChange, Toast.LENGTH_SHORT).show();
                                loading(true);
                                // getRestaurantBySearch(new GetRestaurantBySearch(district, category, textChange, String.valueOf(Para.longitude), String.valueOf(Para.latitude)));
                                getRestaurantBySearch(new GetRestaurantBySearch(district, category, textChange, String.valueOf(108.200364), String.valueOf(16.090288)));
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private AlertDialog dialog;
    private CategoryRestaurantAdapter categoryRestaurantAdapter;

    private ArrayList<CategoryRes> selectedCategoryRestaurants;
    private ArrayList<ImageView> selectedImageViews;

    public void openCategoryRestaurantDialog() {

        final LayoutCategoryRestaurantDialogBinding layoutCategoryRestaurantDialogBinding
                = LayoutCategoryRestaurantDialogBinding.inflate(LayoutInflater.from(SearchActivity.this));
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
        builder.setView(layoutCategoryRestaurantDialogBinding.getRoot());
        dialog = builder.create();
        layoutCategoryRestaurantDialogBinding.buttonApply.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), selectedCategoryRes.toString(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            runThread(AfterTextChange, selectedDistrictList, selectedCategoryRes, 0);
        });

        layoutCategoryRestaurantDialogBinding.buttonDeleteAll.setOnClickListener(v -> {
            for (ImageView imageView : selectedImageViews) {
                imageView.setBackground(getApplicationContext().getDrawable(R.drawable.layout_category_restaurant));
                imageView.setTag("unSelected");
            }
            for (CategoryRes categoryRes : categoryModelArrayList) {
                categoryRes.setSelected(false);
            }
            selectedCategoryRes.clear();
            selectedImageViews.clear();
            selectedCategoryRestaurants.clear();
            if (categoryRestaurantAdapter != null)
                categoryRestaurantAdapter.notifyDataSetChanged();

        });

        categoryRestaurantAdapter = new CategoryRestaurantAdapter(categoryModelArrayList, getApplicationContext(), new CategoryRestaurantAdapter.CategoryRestaurantListeners() {
            @Override
            public void onCategoryRestaurantClicked(CategoryRes categoryRestaurant, int position, boolean isSelected, ImageView imageView) {
                if (isSelected) {
                    categoryRestaurant.setSelected(true);
                    selectedCategoryRestaurants.add(categoryRestaurant);
                    selectedCategoryRes.add(categoryRestaurant.getId());
                    selectedImageViews.add(imageView);
                } else {
                    categoryRestaurant.setSelected(false);
                    selectedCategoryRes.remove(categoryRestaurant.getId());
                    selectedCategoryRestaurants.remove(categoryRestaurant);
                    selectedImageViews.remove(imageView);
                }
            }
        });

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        layoutCategoryRestaurantDialogBinding.recycleView.setLayoutManager(layoutManager);
        layoutCategoryRestaurantDialogBinding.recycleView.setAdapter(categoryRestaurantAdapter);

        dialog.show();
    }

    public void openDistrictDialog() {

        final LayoutCategoryRestaurantDialogBinding layoutCategoryRestaurantDialogBinding
                = LayoutCategoryRestaurantDialogBinding.inflate(LayoutInflater.from(SearchActivity.this));
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
        builder.setView(layoutCategoryRestaurantDialogBinding.getRoot());
        dialog = builder.create();
        layoutCategoryRestaurantDialogBinding.textTitle.setText("Quận/Huyện");

        layoutCategoryRestaurantDialogBinding.buttonDeleteAll.setOnClickListener(v -> {
            for (View view : selectedViewArrayList) {
                (view.findViewById(R.id.layoutDistrict)).setBackground(dialog.getContext().getDrawable(R.drawable.background_districts_search));
                ((TextView) view.findViewById(R.id.textViewNameDistrict)).setTag("UnSelected");
                ((TextView) view.findViewById(R.id.textViewNameDistrict)).setTextColor(dialog.getContext().getColor(R.color.ColorTextUnSelected));
            }
            for (DistrictModel districtModel : districtModels) {
                districtModel.setSelected(false);
            }
            selectedDistrictList.clear();
            selectedViewArrayList.clear();
            if (searchDistrictsAdapter != null) {
                searchDistrictsAdapter.notifyDataSetChanged();
            }
        });

        layoutCategoryRestaurantDialogBinding.buttonApply.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), selectedDistrictList.toString(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            runThread(AfterTextChange, selectedDistrictList, selectedCategoryRes, 0);
        });

        searchDistrictsAdapter = new SearchDistrictsAdapter(districtModels, getApplicationContext(), new SearchDistrictsAdapter.SearchDistrictsListeners() {
            @Override
            public void onDistrictClicked(DistrictModel districtModel, View view, boolean isSelected) {
                if (isSelected) {
                    selectedDistrictList.add(districtModel.getName());
                    selectedViewArrayList.add(view);
                    districtModel.setSelected(true);
                } else {
                    selectedDistrictList.remove(districtModel.getName());
                    selectedViewArrayList.remove(view);
                    districtModel.setSelected(false);
                }
            }
        });
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        layoutCategoryRestaurantDialogBinding.recycleView.setLayoutManager(layoutManager);
        layoutCategoryRestaurantDialogBinding.recycleView.setAdapter(searchDistrictsAdapter);

        dialog.show();
    }

    private ArrayList<DistrictModel> districtModels;
    private SearchDistrictsAdapter searchDistrictsAdapter;

    private void getDistrict(String code) {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<ProvinceModel> call = serviceAPI.GetDistricts(code);
        call.enqueue(new Callback<ProvinceModel>() {
            @Override
            public void onResponse(Call<ProvinceModel> call, Response<ProvinceModel> response) {
                if (response.body() != null && response.body().getDistricts().size() > 0) {
                    districtModels = response.body().getDistricts();
                }
            }

            @Override
            public void onFailure(Call<ProvinceModel> call, Throwable t) {
                Log.d("log:", t.getMessage());
            }
        });
    }

    private void getRestaurantBySearch(GetRestaurantBySearch getRestaurantBySearch) {
        com.psteam.lib.Services.ServiceAPI serviceAPI = com.psteam.lib.RetrofitClient.getRetrofit().create(com.psteam.lib.Services.ServiceAPI.class);
        Call<GetRestaurantModel> call = serviceAPI.GetResBySearch(getRestaurantBySearch);
        call.enqueue(new Callback<GetRestaurantModel>() {
            @Override
            public void onResponse(Call<GetRestaurantModel> call, Response<GetRestaurantModel> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    if (response.body().getResList().size() > 0) {
                        binding.textViewNoFind.setVisibility(View.GONE);
                        restaurantModels.clear();
                        restaurantModels.addAll(response.body().getResList());
                        searchRestaurantAdapter.notifyDataSetChanged();
                        loading(false);
                    } else {
                        binding.textViewNoFind.setVisibility(View.VISIBLE);
                    }
                } else if (response.body() != null && response.body().getStatus().equals("0")) {
                    binding.textViewNoFind.setVisibility(View.VISIBLE);
                    if (searchRestaurantAdapter != null) {
                        restaurantModels.clear();
                        searchRestaurantAdapter.notifyDataSetChanged();
                        loading(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetRestaurantModel> call, Throwable t) {
                Log.d("Log", t.getMessage());
            }
        });
    }

    private void initSearchRestaurantAdapter() {
        searchRestaurantAdapter = new SearchRestaurantAdapter(restaurantModels, SearchActivity.this, this::onRestaurantClicked);
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(SearchActivity.this, R.anim.layout_animation_left_tp_right);
        binding.recycleViewSearch.setLayoutAnimation(layoutAnimationController);
        binding.recycleViewSearch.setAdapter(searchRestaurantAdapter);
    }

    @Override
    public void onRestaurantClicked(RestaurantModel restaurantModel) {
        Intent intent = new Intent(getApplicationContext(), RestaurantDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("restaurantModel", restaurantModel);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }
}
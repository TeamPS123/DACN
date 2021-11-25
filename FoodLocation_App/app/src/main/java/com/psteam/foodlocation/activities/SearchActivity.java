package com.psteam.foodlocation.activities;

import static com.psteam.foodlocation.ultilities.RetrofitClient.getRetrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.psteam.foodlocation.models.RestaurantModel;
import com.psteam.foodlocation.services.ServiceAPI;
import com.psteam.foodlocation.ultilities.DividerItemDecorator;
import com.psteam.foodlocation.ultilities.Para;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;
    private SearchRestaurantAdapter searchRestaurantAdapter;
    private ArrayList<RestaurantModel> restaurantModels;

    private ArrayList<View> selectedViewArrayList;
    private ArrayList<String> selectedDistrictList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            getWindow().setStatusBarColor(ContextCompat.getColor(SearchActivity.this, R.color.white));// set status background white
        }
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
    }

    private void init() {
        selectedCategoryRestaurants = new ArrayList<>();
        selectedViewArrayList = new ArrayList<>();
        selectedImageViews = new ArrayList<>();
        selectedDistrictList = new ArrayList<>();
        districtModels = new ArrayList<>();
        initSearchRestaurant();
        getDistrict(Para.cityCode);
    }

    private void setListeners() {
        binding.buttonMap.setOnClickListener(v -> {
            startActivity(new Intent(SearchActivity.this, MapActivity.class));
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
    }

    private void initSearchRestaurant() {
        restaurantModels = new ArrayList<>();
        restaurantModels.add(new RestaurantModel("ToCoToCo", "875/22, Đường Nguyễn văn Cừ, Phường Lộc Phát, Tp.Bảo Lộc, Tỉnh Lâm Đồng", "2.5", R.drawable.tocotoco_restaurant));
        restaurantModels.add(new RestaurantModel("ToCoToCo", "875/22, Đường Nguyễn văn Cừ, Phường Lộc Phát, Tp.Bảo Lộc, Tỉnh Lâm Đồng", "2.5", R.drawable.tocotoco_restaurant));
        restaurantModels.add(new RestaurantModel("ToCoToCo", "875/22, Đường Nguyễn văn Cừ, Phường Lộc Phát, Tp.Bảo Lộc, Tỉnh Lâm Đồng", "2.5", R.drawable.tocotoco_restaurant));
        restaurantModels.add(new RestaurantModel("ToCoToCo", "875/22, Đường Nguyễn văn Cừ, Phường Lộc Phát, Tp.Bảo Lộc, Tỉnh Lâm Đồng", "2.5", R.drawable.tocotoco_restaurant));
        restaurantModels.add(new RestaurantModel("ToCoToCo", "875/22, Đường Nguyễn văn Cừ, Phường Lộc Phát, Tp.Bảo Lộc, Tỉnh Lâm Đồng", "2.5", R.drawable.tocotoco_restaurant));
        restaurantModels.add(new RestaurantModel("ToCoToCo", "875/22, Đường Nguyễn văn Cừ, Phường Lộc Phát, Tp.Bảo Lộc, Tỉnh Lâm Đồng", "2.5", R.drawable.tocotoco_restaurant));
        restaurantModels.add(new RestaurantModel("ToCoToCo", "875/22, Đường Nguyễn văn Cừ, Phường Lộc Phát, Tp.Bảo Lộc, Tỉnh Lâm Đồng", "2.5", R.drawable.tocotoco_restaurant));
        restaurantModels.add(new RestaurantModel("ToCoToCo", "875/22, Đường Nguyễn văn Cừ, Phường Lộc Phát, Tp.Bảo Lộc, Tỉnh Lâm Đồng", "2.5", R.drawable.tocotoco_restaurant));
        restaurantModels.add(new RestaurantModel("ToCoToCo", "875/22, Đường Nguyễn văn Cừ, Phường Lộc Phát, Tp.Bảo Lộc, Tỉnh Lâm Đồng", "2.5", R.drawable.tocotoco_restaurant));
        restaurantModels.add(new RestaurantModel("ToCoToCo", "875/22, Đường Nguyễn văn Cừ, Phường Lộc Phát, Tp.Bảo Lộc, Tỉnh Lâm Đồng", "2.5", R.drawable.tocotoco_restaurant));
        restaurantModels.add(new RestaurantModel("ToCoToCo", "875/22, Đường Nguyễn văn Cừ, Phường Lộc Phát, Tp.Bảo Lộc, Tỉnh Lâm Đồng", "2.5", R.drawable.tocotoco_restaurant));
        restaurantModels.add(new RestaurantModel("ToCoToCo", "875/22, Đường Nguyễn văn Cừ, Phường Lộc Phát, Tp.Bảo Lộc, Tỉnh Lâm Đồng", "2.5", R.drawable.tocotoco_restaurant));

        searchRestaurantAdapter = new SearchRestaurantAdapter(restaurantModels);
        binding.recycleViewSearch.setAdapter(searchRestaurantAdapter);

       /* RecyclerView.ItemDecoration itemDecoration = new DividerItemDecorator(getDrawable(R.drawable.divider));
        binding.recycleViewSearch.addItemDecoration(itemDecoration);*/

    }

    private AlertDialog dialog;
    private CategoryRestaurantAdapter categoryRestaurantAdapter;
    private ArrayList<CategoryRestaurantAdapter.CategoryRestaurant> categoryRestaurants;

    private ArrayList<CategoryRestaurantAdapter.CategoryRestaurant> selectedCategoryRestaurants;
    private ArrayList<ImageView> selectedImageViews;

    public void openCategoryRestaurantDialog() {

        categoryRestaurants = new ArrayList<>();
        categoryRestaurants.add(new CategoryRestaurantAdapter.CategoryRestaurant("Cơm", R.drawable.rice, false));
        categoryRestaurants.add(new CategoryRestaurantAdapter.CategoryRestaurant("Lẩu", R.drawable.hotpot, false));
        categoryRestaurants.add(new CategoryRestaurantAdapter.CategoryRestaurant("Trà sửa", R.drawable.bubble_tea, true));
        categoryRestaurants.add(new CategoryRestaurantAdapter.CategoryRestaurant("Bia", R.drawable.beer, true));
        categoryRestaurants.add(new CategoryRestaurantAdapter.CategoryRestaurant("Nướng", R.drawable.barbecue, false));
        categoryRestaurants.add(new CategoryRestaurantAdapter.CategoryRestaurant("Coffee", R.drawable.coffee, false));
        categoryRestaurants.add(new CategoryRestaurantAdapter.CategoryRestaurant("Phở", R.drawable.ramen, true));
        categoryRestaurants.add(new CategoryRestaurantAdapter.CategoryRestaurant("Nước ép", R.drawable.drink, false));
        categoryRestaurants.add(new CategoryRestaurantAdapter.CategoryRestaurant("Pizza", R.drawable.pizza, true));

        final LayoutCategoryRestaurantDialogBinding layoutCategoryRestaurantDialogBinding
                = LayoutCategoryRestaurantDialogBinding.inflate(LayoutInflater.from(SearchActivity.this));
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
        builder.setView(layoutCategoryRestaurantDialogBinding.getRoot());
        dialog = builder.create();
        layoutCategoryRestaurantDialogBinding.buttonApply.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), selectedCategoryRestaurants.size() + "", Toast.LENGTH_SHORT).show();
        });

        layoutCategoryRestaurantDialogBinding.buttonDeleteAll.setOnClickListener(v -> {
            for (ImageView imageView : selectedImageViews) {
                imageView.setBackground(getApplicationContext().getDrawable(R.drawable.layout_category_restaurant));
                imageView.setTag("unSelected");
            }
            selectedImageViews.clear();
            selectedCategoryRestaurants.clear();
        });

        categoryRestaurantAdapter = new CategoryRestaurantAdapter(categoryRestaurants, getApplicationContext(), new CategoryRestaurantAdapter.CategoryRestaurantListeners() {
            @Override
            public void onCategoryRestaurantClicked(CategoryRestaurantAdapter.CategoryRestaurant categoryRestaurant, int position, boolean isSelected, ImageView imageView) {
                if (isSelected) {
                    selectedCategoryRestaurants.add(categoryRestaurant);
                    selectedImageViews.add(imageView);
                } else {
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
            selectedDistrictList.clear();
            selectedViewArrayList.clear();
        });

        layoutCategoryRestaurantDialogBinding.buttonApply.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), selectedDistrictList.toString(), Toast.LENGTH_SHORT).show();
        });

        searchDistrictsAdapter = new SearchDistrictsAdapter(districtModels, getApplicationContext(), new SearchDistrictsAdapter.SearchDistrictsListeners() {
            @Override
            public void onDistrictClicked(DistrictModel districtModel, View view, boolean isSelected) {
                if (isSelected) {
                    selectedDistrictList.add(districtModel.getName());
                    selectedViewArrayList.add(view);

                } else {
                    selectedDistrictList.remove(districtModel.getName());
                    selectedViewArrayList.remove(view);
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


}
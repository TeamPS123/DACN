package com.psteam.foodlocation.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.SearchRestaurantAdapter;
import com.psteam.foodlocation.databinding.ActivitySearchBinding;
import com.psteam.foodlocation.models.RestaurantModel;
import com.psteam.foodlocation.ultilities.DividerItemDecorator;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;
    private SearchRestaurantAdapter searchRestaurantAdapter;
    private ArrayList<RestaurantModel> restaurantModels;

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
        initSearchRestaurant();
    }

    private void setListeners() {
        binding.buttonMap.setOnClickListener(v -> {
            startActivity(new Intent(SearchActivity.this, MapActivity.class));
        });

        binding.iconClose.setOnClickListener(v -> {
            finish();
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

        RecyclerView.ItemDecoration itemDecoration=new DividerItemDecorator(getDrawable(R.drawable.divider));
        binding.recycleViewSearch.addItemDecoration(itemDecoration);

    }
}
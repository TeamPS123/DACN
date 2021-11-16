package com.psteam.foodlocation.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.RestaurantAddressAdapter;
import com.psteam.foodlocation.adapters.RestaurantPostAdapter;
import com.psteam.foodlocation.adapters.TimeBookTableAdapter;
import com.psteam.foodlocation.databinding.ActivityRestaurantDetailsBinding;
import com.psteam.foodlocation.adapters.RestaurantPhotoAdapter;

import java.util.ArrayList;

public class RestaurantDetailsActivity extends AppCompatActivity {

    private ActivityRestaurantDetailsBinding binding;

    private RestaurantPhotoAdapter restaurantPhotoAdapter;
    private ArrayList<RestaurantPhotoAdapter.Photo> photoArrayList;

    private TimeBookTableAdapter timeBookTableAdapter;
    private ArrayList<TimeBookTableAdapter.TimeBook> timeBooks;

    private RestaurantPostAdapter restaurantPostAdapter;
    private ArrayList<RestaurantPostAdapter.FoodRestaurant> foodRestaurants;

    private RestaurantAddressAdapter restaurantAddressAdapter;
    private ArrayList<RestaurantAddressAdapter.AddressRestaurant> addressRestaurants;

    private Handler sliderHandler = new Handler();

    private int currentItem;
    private int totalItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
    }

    private void init() {
        setFullScreen();
        initSliderPhotoRestaurant();
        initTimeBookTable();
        initFoodRestaurant();
    }

    private void setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            w.setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }
    }

    private void setListeners() {
        binding.textViewReadMore.setOnClickListener(v -> {
            if (binding.textViewReadMore.getText().equals(getText(R.string.read_more))) {
                binding.textViewPromotionForm.setMaxLines(Integer.MAX_VALUE);
                binding.textViewPromotionForm.setEllipsize(null);
                binding.textViewReadMore.setText(R.string.read_less);
                binding.textViewReadMore.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_round_keyboard_arrow_up_24, 0);
            } else {
                binding.textViewPromotionForm.setMaxLines(3);
                binding.textViewPromotionForm.setEllipsize(TextUtils.TruncateAt.END);
                binding.textViewReadMore.setText(R.string.read_more);
                binding.textViewReadMore.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_round_keyboard_arrow_down_24, 0);
            }
        });


        binding.textViewReadMoreRestaurantsText.setOnClickListener(v -> {
            if (binding.textViewReadMoreRestaurantsText.getText().equals(getText(R.string.read_more))) {
                binding.textViewRestaurantDetails.setMaxLines(Integer.MAX_VALUE);
                binding.textViewRestaurantDetails.setEllipsize(null);
                binding.textViewReadMoreRestaurantsText.setText(getText(R.string.read_less));
                binding.textViewReadMoreRestaurantsText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_round_keyboard_arrow_up_24, 0);
            } else {
                binding.textViewRestaurantDetails.setMaxLines(5);
                binding.textViewRestaurantDetails.setEllipsize(TextUtils.TruncateAt.END);
                binding.textViewReadMoreRestaurantsText.setText(R.string.read_more);
                binding.textViewReadMoreRestaurantsText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_round_keyboard_arrow_down_24, 0);
            }
        });

        binding.layoutAddress.setOnClickListener(v -> {


            clickOpenBottomSheetFragment();

        });

    }

    private ChooseAddressBottomSheetFragment chooseAddressBottomSheetFragment;

    private void clickOpenBottomSheetFragment() {

        addressRestaurants = new ArrayList<>();
        addressRestaurants.add(new RestaurantAddressAdapter.AddressRestaurant("Nguyễn Văn Cừ", "875/22 Nguyễn Văn Cừ, P.Lộc Phát, Bảo Lộc, Lâm Đồng", "20.0km"));
        addressRestaurants.add(new RestaurantAddressAdapter.AddressRestaurant("Nguyễn Công Chứ", "875/22 Nguyễn Công Chứ, P.Lộc Phát, Bảo Lộc, Lâm Đồng", "22.0km"));
        addressRestaurants.add(new RestaurantAddressAdapter.AddressRestaurant("Đình Phong Phú", "875/22 Đình Phong Phú, P.Lộc Phát, Bảo Lộc, Lâm Đồng", "23.0km"));
        addressRestaurants.add(new RestaurantAddressAdapter.AddressRestaurant("Phạm Văn Đồng", "875/22 Phạm Văn Đồng, P.Lộc Phát, Bảo Lộc, Lâm Đồng", "24.0km"));


        chooseAddressBottomSheetFragment = new ChooseAddressBottomSheetFragment(addressRestaurants, new RestaurantAddressAdapter.RestaurantAddressListener() {
            @Override
            public void onRestaurantAddressClicked(RestaurantAddressAdapter.AddressRestaurant addressRestaurant) {
                binding.textViewChooseAddress.setText(addressRestaurant.getAddress());
                binding.textViewChooseDistance.setText(addressRestaurant.getDistance());
                chooseAddressBottomSheetFragment.dismiss();

            }
        });
        chooseAddressBottomSheetFragment.show(getSupportFragmentManager(), chooseAddressBottomSheetFragment.getTag());
    }


    private void initSliderPhotoRestaurant() {
        photoArrayList = new ArrayList<>();
        photoArrayList.add(new RestaurantPhotoAdapter.Photo(R.drawable.p1));
        photoArrayList.add(new RestaurantPhotoAdapter.Photo(R.drawable.p2));
        photoArrayList.add(new RestaurantPhotoAdapter.Photo(R.drawable.p3));
        photoArrayList.add(new RestaurantPhotoAdapter.Photo(R.drawable.p4));
        photoArrayList.add(new RestaurantPhotoAdapter.Photo(R.drawable.p5));
        photoArrayList.add(new RestaurantPhotoAdapter.Photo(R.drawable.p6));

        restaurantPhotoAdapter = new RestaurantPhotoAdapter(photoArrayList);
        binding.viewPagerSlideImageRestaurant.setAdapter(restaurantPhotoAdapter);
        binding.circleIndicator.setViewPager(binding.viewPagerSlideImageRestaurant);
        restaurantPhotoAdapter.registerAdapterDataObserver(binding.circleIndicator.getAdapterDataObserver());
        binding.viewPagerSlideImageRestaurant.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });
    }


    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            currentItem = binding.viewPagerSlideImageRestaurant.getCurrentItem();
            totalItem = photoArrayList.size() - 1;
            if (currentItem < totalItem) {
                currentItem++;
                binding.viewPagerSlideImageRestaurant.setCurrentItem(currentItem);
            } else {
                binding.viewPagerSlideImageRestaurant.setCurrentItem(0);
            }


        }
    };

    private void initTimeBookTable() {
        timeBooks = new ArrayList<>();
        timeBooks.add(new TimeBookTableAdapter.TimeBook("05:00 CH", "-15% *"));
        timeBooks.add(new TimeBookTableAdapter.TimeBook("05:15 CH", "-15% *"));
        timeBooks.add(new TimeBookTableAdapter.TimeBook("05:30 CH", "-15% *"));
        timeBooks.add(new TimeBookTableAdapter.TimeBook("05:45 CH", "-15% *"));
        timeBooks.add(new TimeBookTableAdapter.TimeBook("06:00 CH", "-15% *"));
        timeBooks.add(new TimeBookTableAdapter.TimeBook("06:15 CH", "-15% *"));
        timeBooks.add(new TimeBookTableAdapter.TimeBook("06:30 CH", "-15% *"));
        timeBooks.add(new TimeBookTableAdapter.TimeBook("06:45 CH", "-15% *"));
        timeBooks.add(new TimeBookTableAdapter.TimeBook("07:00 CH", "-15% *"));

        timeBookTableAdapter = new TimeBookTableAdapter(timeBooks);
        binding.recycleViewTimeBookTable.setAdapter(timeBookTableAdapter);
    }

    private void initFoodRestaurant() {
        foodRestaurants = new ArrayList<>();
        foodRestaurants.add(new RestaurantPostAdapter.FoodRestaurant(R.drawable.lau, "TAKA BBQ: GIẢM 15% TẤT CẢ GÓI BUFFET", 4, 10, "875/22 Nguyễn Văn Cừ", "-15%"));
        foodRestaurants.add(new RestaurantPostAdapter.FoodRestaurant(R.drawable.lau, "TAKA BBQ: GIẢM 20% TẤT CẢ GÓI BUFFET", 4.6, 10, "875/22 Nguyễn Văn Cừ", "-15%"));
        foodRestaurants.add(new RestaurantPostAdapter.FoodRestaurant(R.drawable.lau, "TAKA BBQ: GIẢM 25% TẤT CẢ GÓI BUFFET", 4.75, 10, "875/22 Nguyễn Văn Cừ", "-15%"));
        foodRestaurants.add(new RestaurantPostAdapter.FoodRestaurant(R.drawable.lau, "TAKA BBQ: GIẢM 30% TẤT CẢ GÓI BUFFET", 4.4, 10, "875/22 Nguyễn Văn Cừ", "-15%"));
        foodRestaurants.add(new RestaurantPostAdapter.FoodRestaurant(R.drawable.lau, "TAKA BBQ: GIẢM 35% TẤT CẢ GÓI BUFFET", 3, 10, "875/22 Nguyễn Văn Cừ", "-15%"));

        restaurantPostAdapter = new RestaurantPostAdapter(foodRestaurants);
        binding.recycleViewPostFoodRestaurant.setAdapter(restaurantPostAdapter);
    }

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

}

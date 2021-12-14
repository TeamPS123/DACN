package com.psteam.foodlocation.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.ReviewAdapter;
import com.psteam.foodlocation.databinding.ActivityReviewRestaurantBinding;
import com.psteam.foodlocation.ultilities.DividerItemDecorator;
import com.psteam.lib.modeluser.GetReviewModel;
import com.psteam.lib.modeluser.Rate;
import com.psteam.lib.modeluser.RestaurantModel;
import com.psteam.lib.modeluser.UserModel;

import java.util.ArrayList;

public class ReviewRestaurantActivity extends AppCompatActivity {

    private ActivityReviewRestaurantBinding binding;
    private ArrayList<Rate> rates;
    private ReviewAdapter reviewAdapter;
    private RestaurantModel restaurantModel;
    private GetReviewModel.CountRating countRating;
    private GetReviewModel getReviewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewRestaurantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListeners();
    }

    private void init() {
        setFullScreen();
        binding.progressBar1.getProgressDrawable().setColorFilter(
                getColor(R.color.ratingbar), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.progressBar2.getProgressDrawable().setColorFilter(
                getColor(R.color.ratingbar), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.progressBar3.getProgressDrawable().setColorFilter(
                getColor(R.color.ratingbar), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.progressBar4.getProgressDrawable().setColorFilter(
                getColor(R.color.ratingbar), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.progressBar5.getProgressDrawable().setColorFilter(
                getColor(R.color.ratingbar), android.graphics.PorterDuff.Mode.SRC_IN);

        binding.textViewClose.setOnClickListener(v -> {
            finish();
        });
        getDataIntent();
        initReviewAdapter();
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

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        restaurantModel = (RestaurantModel) bundle.getSerializable("restaurantModel");
        getReviewModel= (GetReviewModel) bundle.getSerializable("getReviewModel");
        rates = getReviewModel.getRates();
        countRating= getReviewModel.getCountRating();
        binding.textViewNameRestaurant.setText(restaurantModel.getName());
        binding.textViewTotalCountReview.setText(String.format("%s đánh giá từ %s người dùng",countRating.getCount(),countRating.getCount()));
        binding.textViewTotalPoint.setText(getReviewModel.getRateTotal());
        binding.ratingBar.setRating(Float.valueOf(getReviewModel.getRateTotal()));
        binding.textViewTotalCountReview.setText(String.format("%s đánh giá từ %s người dùng",countRating.getCount(),countRating.getCount()));
        initData();
    }

    private void initData(){
        binding.progressBar5.setProgress((int)Math.round(Double.parseDouble(countRating.getCount5())/Double.parseDouble(countRating.getCount())*100));
        binding.textViewCount5.setText(countRating.getCount5());
        binding.textViewCount5start.setText(String.format("(%s)",countRating.getCount5()));

        binding.progressBar4.setProgress((int)Math.round(Double.parseDouble(countRating.getCount4())/Double.parseDouble(countRating.getCount())*100));
        binding.textViewCount4.setText(countRating.getCount4());
        binding.textViewCount4start.setText(String.format("(%s)",countRating.getCount4()));

        binding.progressBar3.setProgress((int)Math.round(Double.parseDouble(countRating.getCount3())/Double.parseDouble(countRating.getCount())*100));
        binding.textViewCount3.setText(countRating.getCount3());
        binding.textViewCount3start.setText(String.format("(%s)",countRating.getCount3()));

        binding.progressBar2.setProgress((int)Math.round(Double.parseDouble(countRating.getCount2())/Double.parseDouble(countRating.getCount())*100));
        binding.textViewCount2.setText(countRating.getCount2());
        binding.textViewCount2start.setText(String.format("(%s)",countRating.getCount2()));

        binding.progressBar1.setProgress((int)Math.round(Double.parseDouble(countRating.getCount1())/Double.parseDouble(countRating.getCount())*100));
        binding.textViewCount1.setText(countRating.getCount1());
        binding.textViewCount1start.setText(String.format("(%s)",countRating.getCount1()));
    }

    private void initReviewAdapter() {
        reviewAdapter = new ReviewAdapter(restaurantModel, rates);
        binding.recycleView.setAdapter(reviewAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecorator(getDrawable(R.drawable.diveder_review));
        binding.recycleView.addItemDecoration(itemDecoration);
        binding.textViewTotalCountReview1.setText(String.format("Có %d đánh giá", reviewAdapter.getItemCount()));
    }

    private void setListeners() {
        binding.textViewFilter1.setOnClickListener(v -> {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    reviewAdapter.getFilter().filter("1");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    binding.textViewTotalCountReview1.setText(String.format("Có %d đánh giá", reviewAdapter.getItemCount()));
                }
            });
        });

        binding.textViewFilter2.setOnClickListener(v -> {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    reviewAdapter.getFilter().filter("2");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    binding.textViewTotalCountReview1.setText(String.format("Có %d đánh giá", reviewAdapter.getItemCount()));
                }
            });
        });

        binding.textViewFilter3.setOnClickListener(v -> {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    reviewAdapter.getFilter().filter("3");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    binding.textViewTotalCountReview1.setText(String.format("Có %d đánh giá", reviewAdapter.getItemCount()));
                }
            });
        });

        binding.textViewFilter4.setOnClickListener(v -> {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    reviewAdapter.getFilter().filter("4");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    binding.textViewTotalCountReview1.setText(String.format("Có %d đánh giá", reviewAdapter.getItemCount()));
                }
            });
        });

        binding.textViewFilter5.setOnClickListener(v -> {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    reviewAdapter.getFilter().filter("5");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    binding.textViewTotalCountReview1.setText(String.format("Có %d đánh giá", reviewAdapter.getItemCount()));
                }
            });
        });

        binding.textViewViewAll.setOnClickListener(v -> {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    reviewAdapter.getFilter().filter("");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    binding.textViewTotalCountReview1.setText(String.format("Có %d đánh giá", reviewAdapter.getItemCount()));
                }
            });
        });
    }

}
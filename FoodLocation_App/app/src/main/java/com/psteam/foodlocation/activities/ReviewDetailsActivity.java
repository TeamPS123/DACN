package com.psteam.foodlocation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.PhotoReviewAdapter;
import com.psteam.foodlocation.databinding.ActivityReviewDetailsBinding;
import com.psteam.lib.modeluser.ReviewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ReviewDetailsActivity extends AppCompatActivity {

    private ActivityReviewDetailsBinding binding;
    private ArrayList<String> images;
    private PhotoReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListeners();
    }

    private void init() {
        images = new ArrayList<>();
        getDataIntent();
        initImageAdapter();
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ReviewModel reviewModel = (ReviewModel) bundle.getSerializable("reviewModel");
            images.addAll(reviewModel.getImgList());
            Picasso.get().load(reviewModel.getImageUser()).into(binding.imageViewIconUser);
            binding.textViewContent.setText(reviewModel.getContent());
            binding.textViewUserFullName.setText(reviewModel.getUserName());
            binding.textViewUserLike.setText(reviewModel.getUserName());

        }
    }

    private void initImageAdapter() {

        /*images.add("https://static.wikia.nocookie.net/kiminonawa/images/6/62/Kimi-no-Na-wa.-Visual.jpg/revision/latest?cb=20160927170951");
        images.add("https://static.wikia.nocookie.net/kiminonawa/images/6/62/Kimi-no-Na-wa.-Visual.jpg/revision/latest?cb=20160927170951");
        images.add("https://tuoitrechinhphuc.com/wp-content/uploads/2020/12/your-name-696x484-1.jpg");*/
        reviewAdapter = new PhotoReviewAdapter(images, binding.viewPager);
        binding.viewPager.setAdapter(reviewAdapter);
        binding.circleIndicator.setViewPager(binding.viewPager);
        reviewAdapter.registerAdapterDataObserver(binding.circleIndicator.getAdapterDataObserver());
        binding.viewPager.setClipToPadding(false);
        binding.viewPager.setClipChildren(false);
        binding.viewPager.setOffscreenPageLimit(1);
        binding.viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(20));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.99f + r * 0.01f);
            }
        });


    }

    private boolean flag = true;
    private static int heightInput;

    private void setListeners() {

    }
}
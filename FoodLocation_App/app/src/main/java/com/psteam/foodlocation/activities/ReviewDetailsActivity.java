package com.psteam.foodlocation.activities;

import static com.psteam.lib.RetrofitClient.getRetrofit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.PhotoReviewAdapter;
import com.psteam.foodlocation.databinding.ActivityReviewDetailsBinding;
import com.psteam.lib.Services.ServiceAPI;
import com.psteam.lib.modeluser.GetResInfo;
import com.psteam.lib.modeluser.RestaurantModel;
import com.psteam.lib.modeluser.ReviewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            GetResInfo(reviewModel.getRestaurantId());
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

    private void setListeners() {
        binding.layout2.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RestaurantDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("restaurantModel", restaurantModel);
            intent.putExtra("bundle", bundle);
            startActivity(intent);
        });

        binding.textViewShare.setOnClickListener(v -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Tasty");
                String shareMessage = String.format("%s, %s", restaurantModel.getName(), restaurantModel.getAddress());
                shareMessage = shareMessage + " https://ps.covid21tsp.space/Share/Code/" + restaurantModel.getRestaurantId();
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }
        });
    }

    public static boolean isPackageInstalled(Context c, String targetPackage) {
        PackageManager pm = c.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    private RestaurantModel restaurantModel;


    private void GetResInfo(String resId) {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<GetResInfo> call = serviceAPI.GetResInfo(resId);
        call.enqueue(new Callback<GetResInfo>() {
            @Override
            public void onResponse(Call<GetResInfo> call, Response<GetResInfo> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    restaurantModel = response.body().getRestaurant();

                    Picasso.get().load(restaurantModel.getMainPic()).into(binding.imageLogoRestaurant);
                    binding.textViewName.setText(String.format("%s - %s", restaurantModel.getName(), restaurantModel.getLine()));
                    binding.textViewAddress.setText(restaurantModel.getAddress());
                    binding.textViewDistance.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<GetResInfo> call, Throwable t) {
                Log.d("Tag", t.getMessage());
            }
        });
    }
}
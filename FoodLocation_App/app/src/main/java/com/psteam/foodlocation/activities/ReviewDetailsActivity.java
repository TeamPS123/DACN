package com.psteam.foodlocation.activities;

import static com.psteam.lib.RetrofitClient.getRetrofit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.CommentAdapter;
import com.psteam.foodlocation.adapters.PhotoReviewAdapter;
import com.psteam.foodlocation.databinding.ActivityReviewDetailsBinding;
import com.psteam.foodlocation.databinding.LayoutDialogReportReviewBinding;
import com.psteam.foodlocation.databinding.LayoutNotificationDialogBinding;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.foodlocation.ultilities.CustomToast;
import com.psteam.foodlocation.ultilities.Para;
import com.psteam.foodlocation.ultilities.PreferenceManager;
import com.psteam.foodlocation.ultilities.Token;
import com.psteam.lib.Services.ServiceAPI;
import com.psteam.lib.modeluser.CommentModel;
import com.psteam.lib.modeluser.GetCommentAndLikeReview;
import com.psteam.lib.modeluser.GetResInfo;
import com.psteam.lib.modeluser.GetReviewById;
import com.psteam.lib.modeluser.RestaurantModel;
import com.psteam.lib.modeluser.ReviewModel;
import com.psteam.lib.modeluser.UserLike;
import com.psteam.lib.modeluser.message;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewDetailsActivity extends AppCompatActivity {

    private ActivityReviewDetailsBinding binding;
    private ArrayList<String> images;
    private ArrayList<UserLike> userLikes;
    private PhotoReviewAdapter reviewAdapter;
    private PreferenceManager preferenceManager;
    private Token token;

    //Comment
    private ArrayList<CommentModel> comments;
    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        token = new Token(getApplicationContext());
        init();
        setListeners();
    }

    private void init() {
        setFullScreen();
        images = new ArrayList<>();
        comments = new ArrayList<>();
        userLikes = new ArrayList<>();
        getDataIntent();

    }

    private void initComment() {
        commentAdapter = new CommentAdapter(comments);
        binding.recycleViewComment.setAdapter(commentAdapter);
    }

    private ReviewModel reviewModel;

    private void insertComment(String userId, String reviewId, String content, String date) {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<message> call = serviceAPI.insertComment(token.getToken(), userId, reviewId, content, date);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    CommentModel comment = new CommentModel(date, Para.userModel.getFullName(), Para.userModel.getPic(), content);
                    comments.add(0, comment);
                    commentAdapter.notifyItemInserted(0);
                    binding.inputComment.setText(null);
                    binding.inputComment.clearFocus();
                    if (!Para.flagComment)
                        Para.flagComment = true;
                }
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Log.d("Log:", t.getMessage());
            }
        });
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        Uri uri = getIntent().getData();
        if (bundle != null && bundle.getSerializable("reviewModel") != null) {
            reviewModel = (ReviewModel) bundle.getSerializable("reviewModel");
            setData();
        } else if (uri != null) {
            String path = uri.toString();
            String[] parameter = path.split("/");
            GetReviewById(parameter[5]);

        }
    }

    private void setData() {
        images.addAll(reviewModel.getImgList());
        //comments = reviewModel.getComments();
        Picasso.get().load(reviewModel.getImageUser()).into(binding.imageViewIconUser);
        binding.textViewContent.setText(reviewModel.getContent());
        binding.textViewUserFullName.setText(reviewModel.getUserName());
        binding.textViewUserLike.setText(reviewModel.getUserName());
        GetResInfo(reviewModel.getRestaurantId());
        initImageAdapter();
        initComment();
        GetCommentAndLike(reviewModel.getId(), 0, 100);
    }

    private void setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));// set status background white
        }
    }

    private void initImageAdapter() {

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
                String shareMessage = String.format(" %s đã chia sẻ một đánh giá, trải nghiệm về nhà hàng %s", Para.userModel.getFullName(), restaurantModel.getName());
                shareMessage = shareMessage + " https://ps.covid21tsp.space/ShareReview/Code/" + reviewModel.getId();
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }
        });

        binding.buttonSendComment.setOnClickListener(v -> {
            if (binding.inputComment.getText().toString().trim().isEmpty()) {
                CustomToast.makeText(getApplicationContext(), "Nhập nội dung phản hồi của bạn", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
                return;
            }
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            insertComment(preferenceManager.getString(Constants.USER_ID), reviewModel.getId(), binding.inputComment.getText().toString(), new SimpleDateFormat("HH:mm dd/MM/yyyy", new Locale("vi", "VN")).format(new Date()));

        });

        binding.iconReport.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ReviewDetailsActivity.this);
            LayoutDialogReportReviewBinding layoutDialogReportReviewBinding = LayoutDialogReportReviewBinding.inflate(LayoutInflater.from(ReviewDetailsActivity.this));
            builder.setView(layoutDialogReportReviewBinding.getRoot());
            alertDialog = builder.create();
            layoutDialogReportReviewBinding.textViewReport.setOnClickListener(view -> {
                alertDialog.dismiss();
                openDialog();
            });

            layoutDialogReportReviewBinding.textViewCancel.setOnClickListener(view -> {
                alertDialog.dismiss();
            });
            alertDialog.show();

        });

        binding.textViewLike.setOnClickListener(v -> {
            likeOrDislike(preferenceManager.getString(Constants.USER_ID), reviewModel.getId());
        });

        binding.textViewClose.setOnClickListener(v -> {
            finish();
        });
    }

    AlertDialog alertDialog;

    public void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReviewDetailsActivity.this);
        LayoutNotificationDialogBinding layoutNotificationDialogBinding = LayoutNotificationDialogBinding.inflate(LayoutInflater.from(ReviewDetailsActivity.this));
        builder.setView(layoutNotificationDialogBinding.getRoot());
        builder.setCancelable(false);
        alertDialog = builder.create();
        layoutNotificationDialogBinding.textViewAccept.setOnClickListener(v -> {
            CustomToast.makeText(getApplicationContext(), "Đã gửi", CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
            alertDialog.dismiss();
        });

        layoutNotificationDialogBinding.textViewSkip.setOnClickListener(v -> {
            alertDialog.dismiss();
        });
        alertDialog.show();
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

    private void GetReviewById(String reviewId) {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<GetReviewById> call = serviceAPI.GetReviewById(reviewId);
        call.enqueue(new Callback<GetReviewById>() {
            @Override
            public void onResponse(Call<GetReviewById> call, Response<GetReviewById> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    reviewModel = response.body().getReview();
                    setData();
                }
            }

            @Override
            public void onFailure(Call<GetReviewById> call, Throwable t) {
                Log.d("Tag", t.getMessage());
            }
        });
    }

    private void GetCommentAndLike(String reviewId, int skip, int take) {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<GetCommentAndLikeReview> call = serviceAPI.GetCommentAndLike(reviewId, skip, take);
        call.enqueue(new Callback<GetCommentAndLikeReview>() {
            @Override
            public void onResponse(Call<GetCommentAndLikeReview> call, Response<GetCommentAndLikeReview> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    comments.clear();
                    userLikes.clear();
                    comments.addAll(response.body().getComments());
                    userLikes.addAll(response.body().getGetlike());
                    commentAdapter.notifyDataSetChanged();
                    Boolean checkLike = false;
                    for (UserLike userLike : response.body().getGetlike()) {
                        if (preferenceManager.getString(Constants.USER_ID).equals(userLike.getUserId())) {
                            binding.iconHeartLike.setImageResource(R.drawable.heart);
                            binding.iconHeartLike.setTag("Like");
                            checkLike = true;
                            break;
                        }
                    }

                    if (checkLike) {
                        if ((response.body().getGetlike() != null && response.body().getGetlike().size() == 1))
                            binding.textViewUserLike.setText("Bạn đã thả tim bài viết này");
                        else
                            binding.textViewUserLike.setText(String.format("Bạn và %d người khác đã thả tim bài viết này", response.body().getGetlike().size() - 1));
                    } else {
                        if (response.body().getGetlike() != null && response.body().getGetlike().size() != 0 &&
                                response.body().getGetlike().size() > 2)
                            binding.textViewUserLike.setText(String.format("%d người đã thả tim bài viết này",
                                    response.body().getGetlike().size() - 1));
                        else if (response.body().getGetlike() != null && response.body().getGetlike().size() != 0) {
                            binding.textViewUserLike.setText(String.format("%d người đã thả tim bài viết này", userLikes.size()));
                        } else {
                            binding.textViewUserLike.setText("Hãy là người thả tim đầu tiên nào");
                        }
                    }

                    if (!checkLike) {
                        binding.iconHeartLike.setImageResource(R.drawable.heart_small);
                        binding.iconHeartLike.setTag("DisLike");
                    }
                }
            }

            @Override
            public void onFailure(Call<GetCommentAndLikeReview> call, Throwable t) {
                Log.d("Tag", t.getMessage());
            }
        });
    }

    private void likeOrDislike(String userId, String reviewId) {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<message> call = serviceAPI.likeOrDislike(token.getToken(), userId, reviewId);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    /*if (binding.iconHeartLike.getTag().equals("DisLike")) {
                        binding.iconHeartLike.setImageResource(R.drawable.heart);
                        binding.iconHeartLike.setTag("Like");
                        if ((userLikes != null && userLikes.size() == 0))
                            binding.textViewUserLike.setText("Bạn đã thả tim bài viết này");
                        else
                            binding.textViewUserLike.setText(String.format("Bạn và %d người khác đã thả tim bài viết này", userLikes.size()));
                    } else {
                        binding.iconHeartLike.setImageResource(R.drawable.heart_small);
                        binding.iconHeartLike.setTag("DisLike");

                        if (userLikes != null && userLikes.size() != 0 &&
                                userLikes.size() > 2)
                            binding.textViewUserLike.setText(String.format("%d người đã thả tim bài viết này",
                                    userLikes.size() - 1));
                        else if (userLikes != null && userLikes.size() == 1) {
                            binding.textViewUserLike.setText(String.format("%d người đã thả tim bài viết này", userLikes.size()));
                        } else {
                            binding.textViewUserLike.setText("Hãy là người thả tim đầu tiên nào");
                        }
                    }*/
                    GetCommentAndLike(reviewId, 0, 100);
                    Para.flagComment = true;
                    Log.d("Log:", "Thành công");
                }
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Log.d("Log:", t.getMessage());
            }
        });
    }
}
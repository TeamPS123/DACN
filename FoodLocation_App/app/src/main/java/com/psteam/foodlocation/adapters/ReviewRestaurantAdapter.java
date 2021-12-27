package com.psteam.foodlocation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.LayoutReviewPostItemContainerBinding;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.foodlocation.ultilities.PreferenceManager;
import com.psteam.lib.modeluser.ReviewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewRestaurantAdapter extends RecyclerView.Adapter<ReviewRestaurantAdapter.ReviewRestaurantViewHolder> {

    private final List<ReviewModel> reviewModels;
    private final ReviewRestaurantListeners reviewRestaurantListeners;
    private final Context context;
    private PreferenceManager preferenceManager;

    public ReviewRestaurantAdapter(List<ReviewModel> reviewModels, ReviewRestaurantListeners reviewRestaurantListeners, Context context) {
        this.reviewModels = reviewModels;
        this.reviewRestaurantListeners = reviewRestaurantListeners;
        this.context = context;
        preferenceManager = new PreferenceManager(context);
    }

    @NonNull
    @Override
    public ReviewRestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewRestaurantViewHolder(LayoutReviewPostItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewRestaurantViewHolder holder, int position) {
        holder.setData(reviewModels.get(position));
    }

    @Override
    public int getItemCount() {
        return reviewModels != null ? reviewModels.size() : 0;
    }

    class ReviewRestaurantViewHolder extends RecyclerView.ViewHolder {

        final LayoutReviewPostItemContainerBinding binding;

        public ReviewRestaurantViewHolder(@NonNull LayoutReviewPostItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(ReviewModel reviewModel) {
            if (reviewModel == null) {
                binding.layoutReview.setVisibility(View.GONE);
                binding.layoutAddReview.setVisibility(View.VISIBLE);
                binding.getRoot().setOnClickListener(v -> {
                    reviewRestaurantListeners.onAddReviewClick(getAdapterPosition());
                });
            } else {
                binding.layoutReview.setVisibility(View.VISIBLE);
                binding.layoutAddReview.setVisibility(View.GONE);
                Picasso.get().load(reviewModel.getImageUser()).error(R.drawable.icon_tasty_small).into(binding.imageUser);
                Picasso.get().load(reviewModel.getImgList().get(0)).error(R.drawable.icon_tasty_small).into(binding.imageViewReviewPost);
                if (reviewModel.getContent().isEmpty()) {
                    binding.textViewContent.setVisibility(View.GONE);
                } else {
                    binding.textViewContent.setVisibility(View.VISIBLE);
                }

                binding.textViewContent.setText(reviewModel.getContent());
                binding.textViewFullName.setText(reviewModel.getUserName());

                binding.getRoot().setOnClickListener(v -> {
                    reviewRestaurantListeners.onClick(reviewModel);
                });

                binding.iconLike.setOnClickListener(v -> {
                    if (binding.iconLike.getTag().equals("Like")) {
                        binding.iconLike.setImageResource(R.drawable.heart_small);
                        binding.iconLike.setTag("DisLike");
                        reviewRestaurantListeners.onDisLikeClick(binding.iconLike, reviewModel);
                    } else {
                        binding.iconLike.setImageResource(R.drawable.heart);
                        binding.iconLike.setTag("Like");
                        reviewRestaurantListeners.onLikeClick(binding.iconLike, reviewModel);
                    }
                });

                if (reviewModel.checkUserLike(preferenceManager.getString(Constants.USER_ID))) {
                    binding.iconLike.setImageResource(R.drawable.heart);
                    binding.iconLike.setTag("Like");
                } else {
                    binding.iconLike.setImageResource(R.drawable.heart_small);
                    binding.iconLike.setTag("DisLike");
                }

            }
        }
    }

    public interface ReviewRestaurantListeners {
        void onClick(ReviewModel reviewModel);

        void onLikeClick(ImageView imageView, ReviewModel reviewModel);

        void onDisLikeClick(ImageView imageView, ReviewModel reviewModel);

        void onAddReviewClick(int position);
    }

}

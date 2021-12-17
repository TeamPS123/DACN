package com.psteam.foodlocation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.databinding.LayoutReviewPostItemContainerBinding;
import com.psteam.lib.modeluser.ReviewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewRestaurantAdapter extends RecyclerView.Adapter<ReviewRestaurantAdapter.ReviewRestaurantViewHolder> {

    private final List<ReviewModel> reviewModels;
    private final ReviewRestaurantListeners reviewRestaurantListeners;

    public ReviewRestaurantAdapter(List<ReviewModel> reviewModels, ReviewRestaurantListeners reviewRestaurantListeners) {
        this.reviewModels = reviewModels;
        this.reviewRestaurantListeners = reviewRestaurantListeners;
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
                Picasso.get().load(reviewModel.getImageUser()).into(binding.imageUser);
                if (reviewModel.getImgList().size() != 0)
                    Picasso.get().load(reviewModel.getImgList().get(0)).into(binding.imageViewReviewPost);

                if(reviewModel.getContent().isEmpty()){
                    binding.textViewContent.setVisibility(View.GONE);
                }else {
                    binding.textViewContent.setVisibility(View.VISIBLE);
                }

                binding.textViewContent.setText(reviewModel.getContent());
                binding.textViewFullName.setText(reviewModel.getUserName());

                binding.getRoot().setOnClickListener(v -> {
                    reviewRestaurantListeners.onClick(reviewModel);
                });
            }
        }
    }

    public interface ReviewRestaurantListeners {
        void onClick(ReviewModel reviewModel);

        void onAddReviewClick(int position);
    }

}

package com.psteam.foodlocation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.FoodRestaurantItemContainerBinding;
import com.psteam.lib.modeluser.RestaurantModel;

import java.util.List;
import java.util.Random;

public class RestaurantPostAdapter extends RecyclerView.Adapter<RestaurantPostAdapter.RestaurantPostViewHolder> {

    private final List<RestaurantModel> foodRestaurants;
    private final RestaurantPostListeners restaurantPostListener;
    private final Context context;

    public RestaurantPostAdapter(List<RestaurantModel> foodRestaurants, RestaurantPostListeners restaurantPostListener, Context context) {
        this.foodRestaurants = foodRestaurants;
        this.restaurantPostListener = restaurantPostListener;
        this.context = context;
    }

    @NonNull
    @Override
    public RestaurantPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RestaurantPostViewHolder(FoodRestaurantItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantPostViewHolder holder, int position) {
        holder.setData(foodRestaurants.get(position));
    }


    @Override
    public int getItemCount() {
        return foodRestaurants != null ? foodRestaurants.size() : 0;
    }

    class RestaurantPostViewHolder extends RecyclerView.ViewHolder {

        private FoodRestaurantItemContainerBinding binding;

        public RestaurantPostViewHolder(@NonNull FoodRestaurantItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(RestaurantModel foodRestaurant) {
            if (foodRestaurant.getPic().size() > 0) {
                Glide.with(context).load(foodRestaurant.getPic().get(0)).thumbnail(0.3f).error(R.drawable.icon_tasty).into(binding.imageViewRestaurant);
            }
            if (foodRestaurant.getPromotionRes().size() > 0) {
                binding.textViewRestaurantName.setText(String.format("%s: %s", foodRestaurant.getName(), foodRestaurant.getPromotionRes().get(0).getName()));
            } else {
                binding.textViewRestaurantName.setText(foodRestaurant.getName());
            }

            binding.textViewPromotion.setText(foodRestaurant.getPromotion());

            binding.ratingBar.setRating(Float.valueOf(foodRestaurant.getRateTotal()));
            binding.textViewCountReview.setText(foodRestaurant.getCountRate());

            binding.textViewRestaurantAddress.setText(String.format("%s %s %s", foodRestaurant.getLine(), foodRestaurant.getDistrict(), foodRestaurant.getCity()));
            binding.getRoot().setOnClickListener(v -> {
                restaurantPostListener.onRestaurantPostClicked(foodRestaurant);
            });
        }
    }

    public interface RestaurantPostListeners {
        void onRestaurantPostClicked(RestaurantModel foodRestaurant);
    }
}

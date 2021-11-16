package com.psteam.foodlocation.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.databinding.FoodRestaurantItemContainerBinding;

import java.util.List;

public class RestaurantPostAdapter extends RecyclerView.Adapter<RestaurantPostAdapter.RestaurantPostViewHolder> {

    private final List<FoodRestaurant> foodRestaurants;
    private final RestaurantPostListeners restaurantPostListener;

    public RestaurantPostAdapter(List<FoodRestaurant> foodRestaurants, RestaurantPostListeners restaurantPostListener) {
        this.foodRestaurants = foodRestaurants;
        this.restaurantPostListener = restaurantPostListener;
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

        public void setData(FoodRestaurant foodRestaurant) {
            binding.imageViewRestaurant.setImageResource(foodRestaurant.getImageRestaurant());
            binding.textViewRestaurantName.setText(foodRestaurant.getNameRestaurant());
            binding.ratingBar.setRating((float) foodRestaurant.getRating());
            binding.textViewCountReview.setText(String.valueOf(foodRestaurant.getTotalReview()));
            binding.textViewRestaurantAddress.setText(foodRestaurant.getAddress());
            binding.textViewPromotion.setText(foodRestaurant.getDiscount());
            binding.getRoot().setOnClickListener(v -> {
                restaurantPostListener.onRestaurantPostClicked(foodRestaurant);
            });
        }
    }

    public interface RestaurantPostListeners {
        void onRestaurantPostClicked(FoodRestaurant foodRestaurant);
    }

    public static class FoodRestaurant {
        private int imageRestaurant;
        private String nameRestaurant;
        private double rating;
        private int totalReview;
        private String address;
        private String discount;

        public FoodRestaurant(int imageRestaurant, String nameRestaurant, double rating, int totalReview, String address, String discount) {
            this.imageRestaurant = imageRestaurant;
            this.nameRestaurant = nameRestaurant;
            this.rating = rating;
            this.totalReview = totalReview;
            this.address = address;
            this.discount = discount;
        }

        public int getImageRestaurant() {
            return imageRestaurant;
        }

        public void setImageRestaurant(int imageRestaurant) {
            this.imageRestaurant = imageRestaurant;
        }

        public String getNameRestaurant() {
            return nameRestaurant;
        }

        public void setNameRestaurant(String nameRestaurant) {
            this.nameRestaurant = nameRestaurant;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public int getTotalReview() {
            return totalReview;
        }

        public void setTotalReview(int totalReview) {
            this.totalReview = totalReview;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }
    }
}

package com.psteam.foodlocation.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.databinding.RestaurantItemContainerBinding;
import com.psteam.foodlocation.models.RestaurantModel;

import java.util.List;

public class SearchRestaurantAdapter extends RecyclerView.Adapter<SearchRestaurantAdapter.SearchRestaurantViewHolder> {
    private final List<RestaurantModel> restaurantModels;

    public SearchRestaurantAdapter(List<RestaurantModel> restaurantModels) {
        this.restaurantModels = restaurantModels;
    }

    @NonNull
    @Override
    public SearchRestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchRestaurantViewHolder(RestaurantItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRestaurantViewHolder holder, int position) {
        holder.setData(restaurantModels.get(position));
    }

    @Override
    public int getItemCount() {
        return restaurantModels != null ? restaurantModels.size() : 0;
    }

    class SearchRestaurantViewHolder extends RecyclerView.ViewHolder {
        RestaurantItemContainerBinding binding;

        public SearchRestaurantViewHolder(@NonNull RestaurantItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(RestaurantModel restaurantModel) {
            binding.imageViewRestaurant.setBackgroundResource(restaurantModel.getImage());
            binding.textViewRestaurantAddress.setText(restaurantModel.getAddress());
            binding.textviewDistance.setText(restaurantModel.getDistance() + "km");
            binding.textViewRestaurantName.setText(restaurantModel.getName());
        }
    }
}

package com.psteam.foodlocation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.databinding.LayoutRestaurantRecentItemContainerBinding;
import com.psteam.lib.modeluser.RestaurantModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RestaurantRecentAdapter extends RecyclerView.Adapter<RestaurantRecentAdapter.RestaurantRecentViewHolder> {

    private final List<RestaurantModel> restaurantModels;
    private final RestaurantRecentListeners restaurantRecentListeners;

    public RestaurantRecentAdapter(List<RestaurantModel> restaurantModels, RestaurantRecentListeners restaurantRecentListeners) {
        this.restaurantModels = restaurantModels;
        this.restaurantRecentListeners = restaurantRecentListeners;
    }

    @NonNull
    @Override
    public RestaurantRecentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RestaurantRecentViewHolder(LayoutRestaurantRecentItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantRecentViewHolder holder, int position) {
        holder.setData(restaurantModels.get(position));
    }

    @Override
    public int getItemCount() {
        return restaurantRecentListeners != null ? restaurantModels.size() : 0;
    }


    class RestaurantRecentViewHolder extends RecyclerView.ViewHolder {

        final LayoutRestaurantRecentItemContainerBinding binding;

        public RestaurantRecentViewHolder(@NonNull LayoutRestaurantRecentItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(RestaurantModel restaurantModel) {
            if (restaurantModel.getPromotionRes() != null && restaurantModel.getPromotionRes().size() > 0) {
                binding.textViewPromotion.setText(String.format("-%s%%", restaurantModel.getPromotionRes().get(0).getValue()));
                binding.textViewResName.setText(String.format("%s: %s", restaurantModel.getName(), restaurantModel.getPromotionRes().get(0).getName().toLowerCase()));
            } else {
                binding.textViewPromotion.setText(String.format("%s", "Đặt bàn"));
                binding.textViewResName.setText(String.format("%s", restaurantModel.getName()));
            }
            Picasso.get().load(restaurantModel.getMainPic()).into(binding.imageView);
            binding.getRoot().setOnClickListener(v -> {
                restaurantRecentListeners.onClick(restaurantModel);
            });
        }
    }

    public interface RestaurantRecentListeners {
        void onClick(RestaurantModel restaurantModel);
    }
}

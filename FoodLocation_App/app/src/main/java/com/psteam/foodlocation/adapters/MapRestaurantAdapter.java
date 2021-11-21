package com.psteam.foodlocation.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.databinding.MapRestaurantItemContainerBinding;
import com.psteam.foodlocation.listeners.MapRestaurantListener;
import com.psteam.foodlocation.models.RestaurantModel;

import java.util.List;

public class MapRestaurantAdapter extends RecyclerView.Adapter<MapRestaurantAdapter.MapRestaurantViewHolder> {
    private final List<RestaurantModel> restaurantModels;
    private final MapRestaurantListener mapRestaurantListener;

    public MapRestaurantAdapter(List<RestaurantModel> restaurantModels, MapRestaurantListener mapRestaurantListener) {
        this.restaurantModels = restaurantModels;
        this.mapRestaurantListener = mapRestaurantListener;
    }

    @NonNull
    @Override
    public MapRestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MapRestaurantViewHolder(MapRestaurantItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull MapRestaurantViewHolder holder, int position) {
        holder.setData(restaurantModels.get(position));
    }

    @Override
    public int getItemCount() {
        return restaurantModels != null ? restaurantModels.size() : 0;
    }

    class MapRestaurantViewHolder extends RecyclerView.ViewHolder {
        MapRestaurantItemContainerBinding binding;

        public MapRestaurantViewHolder(@NonNull MapRestaurantItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(RestaurantModel restaurantModel) {
            binding.imageViewRestaurant.setBackgroundResource(restaurantModel.getImage());
            binding.textViewRestaurantAddress.setText(restaurantModel.getAddress());
            binding.textviewDistance.setText(restaurantModel.getDistance() + "km");
            binding.textViewRestaurantName.setText(restaurantModel.getName());

            binding.textViewGuide.setOnClickListener(v -> {
                mapRestaurantListener.onRestaurantGuideClicked(restaurantModel);
            });

            binding.getRoot().setOnClickListener(v->{
                mapRestaurantListener.onRestaurantClicked(restaurantModel);
            });


        }
    }
}

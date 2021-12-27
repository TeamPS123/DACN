package com.psteam.foodlocation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.MapRestaurantItemContainerBinding;
import com.psteam.foodlocation.listeners.MapRestaurantListener;
import com.psteam.lib.modeluser.RestaurantModel;

import java.util.List;

public class MapRestaurantAdapter extends RecyclerView.Adapter<MapRestaurantAdapter.MapRestaurantViewHolder> {
    private final List<RestaurantModel> restaurantModels;
    private final MapRestaurantListener mapRestaurantListener;
    private final Context context;


    public MapRestaurantAdapter(List<RestaurantModel> restaurantModels, MapRestaurantListener mapRestaurantListener, Context context) {
        this.restaurantModels = restaurantModels;
        this.mapRestaurantListener = mapRestaurantListener;
        this.context = context;
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
            Glide.with(context).load(restaurantModel.getMainPic()).error(R.drawable.icon_tasty).into(binding.imageViewRestaurant);
            binding.textViewRestaurantAddress.setText(restaurantModel.getAddress());
            binding.textviewDistance.setText(Math.round(Double.parseDouble(restaurantModel.getDistance())) + "km");
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

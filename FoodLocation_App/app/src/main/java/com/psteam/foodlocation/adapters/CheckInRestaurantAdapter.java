package com.psteam.foodlocation.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.databinding.LayoutRestaurantCheckInItemContainerBinding;
import com.psteam.lib.modeluser.RestaurantModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CheckInRestaurantAdapter extends RecyclerView.Adapter<CheckInRestaurantAdapter.CheckInRestaurantViewHolder> {
    private final CheckInRestaurantListeners checkInRestaurantListeners;
    private final List<RestaurantModel> restaurantModels;

    public CheckInRestaurantAdapter(CheckInRestaurantListeners checkInRestaurantListeners, List<RestaurantModel> restaurantModels) {
        this.checkInRestaurantListeners = checkInRestaurantListeners;
        this.restaurantModels = restaurantModels;
    }

    @NonNull
    @Override
    public CheckInRestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CheckInRestaurantViewHolder(LayoutRestaurantCheckInItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull CheckInRestaurantViewHolder holder, int position) {
        holder.setData(restaurantModels.get(position));
    }

    @Override
    public int getItemCount() {
        return restaurantModels != null ? restaurantModels.size() : 0;
    }

    class CheckInRestaurantViewHolder extends RecyclerView.ViewHolder {

        private final LayoutRestaurantCheckInItemContainerBinding binding;


        public CheckInRestaurantViewHolder(@NonNull LayoutRestaurantCheckInItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(RestaurantModel restaurantModel) {
            Picasso.get().load(restaurantModel.getMainPic()).into(binding.imageLogoRestaurant);
            binding.textViewName.setText(String.format("%s - %s", restaurantModel.getName(), restaurantModel.getLine()));
            binding.textViewAddress.setText(restaurantModel.getAddress());
            binding.textViewDistance.setText(String.valueOf(Math.round(Double.parseDouble(restaurantModel.getDistance())))+"km");
            binding.getRoot().setOnClickListener(v -> {
                checkInRestaurantListeners.onClick(restaurantModel);
            });
        }
    }

    public interface CheckInRestaurantListeners {
        void onClick(RestaurantModel restaurantModel);
    }
}

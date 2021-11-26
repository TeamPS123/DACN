package com.psteam.foodlocation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.RestaurantItemContainerBinding;
import com.psteam.lib.modeluser.RestaurantModel;

import java.time.LocalTime;
import java.util.List;

public class SearchRestaurantAdapter extends RecyclerView.Adapter<SearchRestaurantAdapter.SearchRestaurantViewHolder> {
    private final List<RestaurantModel> restaurantModels;
    private final Context context;
    private final SearchRestaurantListeners searchRestaurantListeners;

    public SearchRestaurantAdapter(List<RestaurantModel> restaurantModels, Context context, SearchRestaurantListeners searchRestaurantListeners) {
        this.restaurantModels = restaurantModels;
        this.context = context;
        this.searchRestaurantListeners = searchRestaurantListeners;
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
            Glide.with(context).load(restaurantModel.getMainPic()).thumbnail(0.3f).into(binding.imageViewRestaurant);

            binding.textViewRestaurantAddress.setText(restaurantModel.getAddress());
            binding.textviewDistance.setText(String.valueOf(Math.round(Double.parseDouble(restaurantModel.getDistance()))));
            binding.textViewRestaurantName.setText(restaurantModel.getName());

            LocalTime openTime = LocalTime.parse(restaurantModel.getOpenTime());
            LocalTime now=LocalTime.now();
            if(openTime.isAfter(now)){
                binding.textViewStatus.setText("Mở cửa");
                binding.textViewTime.setText(String.format(" cho đến %s",restaurantModel.getCloseTime()));
            }else {
                binding.textViewStatus.setText("Đóng cửa");
                binding.textViewStatus.setTextColor(context.getColor(R.color.ColorButtonReserve));
                binding.textViewTime.setTextColor(context.getColor(R.color.ColorButtonReserve));
                binding.textViewTime.setTextColor(context.getColor(R.color.ColorButtonReserve));
                binding.textViewTime.setText(String.format(" mở lúc %s",restaurantModel.getOpenTime()));
            }

            binding.getRoot().setOnClickListener(v->{
                searchRestaurantListeners.onRestaurantClicked(restaurantModel);
            });
        }
    }

    public interface SearchRestaurantListeners{
        void onRestaurantClicked(RestaurantModel restaurantModel);
    }
}

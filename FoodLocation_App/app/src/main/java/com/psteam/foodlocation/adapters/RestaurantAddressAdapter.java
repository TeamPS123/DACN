package com.psteam.foodlocation.adapters;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.psteam.foodlocation.databinding.ChooseRestaurantItemContainerBinding;

import java.util.List;

public class RestaurantAddressAdapter extends RecyclerView.Adapter<RestaurantAddressAdapter.RestaurantAddressViewHolder> {

    private final List<AddressRestaurant> addressRestaurantList;
    private final RestaurantAddressListener restaurantAddressListener;
    private static int selectedPos;

    public RestaurantAddressAdapter(List<AddressRestaurant> addressRestaurantList, RestaurantAddressListener restaurantAddressListener) {
        this.addressRestaurantList = addressRestaurantList;
        this.restaurantAddressListener = restaurantAddressListener;
    }

    @NonNull
    @Override
    public RestaurantAddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RestaurantAddressViewHolder(ChooseRestaurantItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantAddressViewHolder holder, int position) {
        holder.setData(addressRestaurantList.get(position));
        if(selectedPos==position){
           holder.setSelected(true);
        }else {
            holder.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return addressRestaurantList != null ? addressRestaurantList.size() : 0;
    }

    class RestaurantAddressViewHolder extends ViewHolder {

        public final ChooseRestaurantItemContainerBinding binding;

        public RestaurantAddressViewHolder(@NonNull ChooseRestaurantItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(AddressRestaurant addressRestaurant) {
            binding.textViewAddress.setText(addressRestaurant.getAddress());
            binding.textDistance.setText(addressRestaurant.getDistance());
            binding.textViewNameStreet.setText(addressRestaurant.getStreet());
            binding.getRoot().setOnClickListener(v -> {
                restaurantAddressListener.onRestaurantAddressClicked(addressRestaurant);
                notifyItemChanged(selectedPos);
                selectedPos = getLayoutPosition();
                notifyItemChanged(selectedPos);
            });
        }

        public void setSelected(Boolean selected){
            if(selected){
                binding.imageSelected.setVisibility(View.VISIBLE);
            }else {
                binding.imageSelected.setVisibility(View.GONE);
            }
        }
    }

    public interface RestaurantAddressListener {
        void onRestaurantAddressClicked(AddressRestaurant addressRestaurant);
    }

    public static class AddressRestaurant {

        private String street;
        private String address;
        private String distance;

        public AddressRestaurant(String street, String address, String distance) {
            this.street = street;
            this.address = address;
            this.distance = distance;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }
    }
}

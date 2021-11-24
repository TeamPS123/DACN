package com.psteam.foodlocation.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.databinding.LayoutUserReserveTableItemContainerBinding;

import java.util.List;

public class UserReserveTableAdapter extends RecyclerView.Adapter<UserReserveTableAdapter.UserReserveTableViewHolder> {

    private final List<ReserveTable> reserveTables;
    private final UserReserveTableListeners userReserveTableListeners;

    public UserReserveTableAdapter(List<ReserveTable> reserveTables, UserReserveTableListeners userReserveTableListeners) {
        this.reserveTables = reserveTables;
        this.userReserveTableListeners = userReserveTableListeners;
    }

    @NonNull
    @Override
    public UserReserveTableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserReserveTableViewHolder(LayoutUserReserveTableItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull UserReserveTableViewHolder holder, int position) {
        holder.setData(reserveTables.get(position));
    }

    @Override
    public int getItemCount() {
        return reserveTables != null ? reserveTables.size() : 0;
    }

    class UserReserveTableViewHolder extends RecyclerView.ViewHolder {

        final LayoutUserReserveTableItemContainerBinding binding;

        public UserReserveTableViewHolder(@NonNull LayoutUserReserveTableItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(ReserveTable reserveTable) {
            binding.textViewRestaurantName.setText(reserveTable.getRestaurantName());
            binding.textViewStatus.setText(String.format("Trạng thái chưa xác nhận",reserveTable.getStatus()));
            binding.textViewNumberPeople.setText(String.format("Đặt chỗ cho %s người",reserveTable.getNumberPeople()));
            binding.getRoot().setOnClickListener(v -> {
                userReserveTableListeners.onUserReserveTableClicked(reserveTable);
            });
        }
    }

    public interface UserReserveTableListeners {
        void onUserReserveTableClicked(ReserveTable reserveTable);
    }


    public static class ReserveTable {
        private String restaurantName;
        private String date;
        private int numberPeople;
        private int status;

        public ReserveTable(String restaurantName, String date, int numberPeople, int status) {
            this.restaurantName = restaurantName;
            this.date = date;
            this.numberPeople = numberPeople;
            this.status = status;
        }

        public String getRestaurantName() {
            return restaurantName;
        }

        public void setRestaurantName(String restaurantName) {
            this.restaurantName = restaurantName;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getNumberPeople() {
            return numberPeople;
        }

        public void setNumberPeople(int numberPeople) {
            this.numberPeople = numberPeople;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}

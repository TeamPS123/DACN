package com.psteam.foodlocation.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.databinding.LayoutUserReserveTableItemContainerBinding;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.lib.Models.reserveTableDetail.reserveTable;
import com.psteam.lib.modeluser.GetUserReserveTableModel;
import com.psteam.lib.modeluser.ReserveTable;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserReserveTableAdapter extends RecyclerView.Adapter<UserReserveTableAdapter.UserReserveTableViewHolder> {

    private final List<reserveTable> reserveTables;
    private final UserReserveTableListeners userReserveTableListeners;

    public UserReserveTableAdapter(List<reserveTable> reserveTables, UserReserveTableListeners userReserveTableListeners) {
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

        public void setData(reserveTable reserveTable) {
            binding.textViewRestaurantName.setText(reserveTable.getRestaurant().getName());
            if (reserveTable.getStatus() == 0) {
                binding.textViewStatus.setText(String.format("Trạng thái chưa xác nhận"));
            } else if (reserveTable.getStatus() == 1) {
                binding.textViewStatus.setText(String.format("Trạng thái đã được duyệt"));
            } else if (reserveTable.getStatus() == 2) {
                binding.textViewStatus.setText(String.format("Trạng thái bị huỷ"));
            } else if (reserveTable.getStatus() == 3) {
                binding.textViewStatus.setText(String.format("Quá hạn"));
            } else {
                binding.textViewStatus.setText(String.format("Hoàn tất"));
            }
            binding.textViewNumberPeople.setText(String.format("Đặt chỗ cho %s người", reserveTable.getQuantity()));

            Picasso.get().load(reserveTable.getRestaurant().getMainPic()).into(binding.imageViewRestaurant);

            Date date= Constants.coverStringToDate(reserveTable.getTime());
            binding.textViewTime.setText(new SimpleDateFormat("hh:mm").format(date));
            binding.textViewAMPM.setText(new SimpleDateFormat("a").format(date).equals("AM")?"Sáng":"Chiều");

            binding.textViewDay.setText(new SimpleDateFormat("dd").format(date));
            binding.textViewMonth.setText(new SimpleDateFormat("MMM").format(date).replace("t","T"));

            binding.getRoot().setOnClickListener(v -> {
                userReserveTableListeners.onUserReserveTableClicked(reserveTable);
            });
        }
    }

    public interface UserReserveTableListeners {
        void onUserReserveTableClicked(reserveTable reserveTable);
    }
}

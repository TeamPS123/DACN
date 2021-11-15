package com.psteam.foodlocation.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.databinding.TimeReserveItemContainerBinding;

import java.util.List;

public class TimeBookTableAdapter extends RecyclerView.Adapter<TimeBookTableAdapter.TimeBookTableViewHolder> {

    private final List<TimeBook> timeBooks;

    public TimeBookTableAdapter(List<TimeBook> timeBooks) {
        this.timeBooks = timeBooks;
    }

    @NonNull
    @Override
    public TimeBookTableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TimeBookTableViewHolder(TimeReserveItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull TimeBookTableViewHolder holder, int position) {
        holder.setData(timeBooks.get(position));
    }

    @Override
    public int getItemCount() {
        return timeBooks != null ? timeBooks.size() : 0;
    }

    class TimeBookTableViewHolder extends RecyclerView.ViewHolder {

        private TimeReserveItemContainerBinding binding;

        public TimeBookTableViewHolder(@NonNull TimeReserveItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(TimeBook timeBook) {
            binding.textViewTime.setText(timeBook.getTime());
            binding.textViewPercentDiscount.setText(timeBook.getDiscount());
        }
    }

    public static class TimeBook {
        private String time;
        private String discount;

        public TimeBook(String time, String discount) {
            this.time = time;
            this.discount = discount;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }
    }
}

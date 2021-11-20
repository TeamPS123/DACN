package com.psteam.foodlocation.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.databinding.ManagerFoodItemContainerBinding;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class ManagerFoodAdapter extends RecyclerView.Adapter<ManagerFoodAdapter.ManagerFoodViewHolder> {

    private final List<Food> foodList;

    public ManagerFoodAdapter(List<Food> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public ManagerFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ManagerFoodViewHolder(ManagerFoodItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ManagerFoodViewHolder holder, int position) {
        holder.setData(foodList.get(position));
    }

    @Override
    public int getItemCount() {
        return foodList != null ? foodList.size() : 0;
    }

    class ManagerFoodViewHolder extends RecyclerView.ViewHolder {

        private final ManagerFoodItemContainerBinding binding;

        public ManagerFoodViewHolder(@NonNull ManagerFoodItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(Food food) {
            binding.imageViewFood.setImageResource(food.getImage());
            binding.textViewFoodName.setText(food.getName());
            binding.textViewPrice.setText(DecimalFormat.getCurrencyInstance(new Locale("vi", "VN")).format(food.getPrice()));
            binding.textViewFoodInfo.setText(food.getInfo());
        }
    }

    public static class Food {

        private int image;
        private String name;
        private double price;
        private String info;


        public Food(int image, String name, double price, String info) {
            this.image = image;
            this.name = name;
            this.price = price;
            this.info = info;
        }

        public int getImage() {
            return image;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }
}
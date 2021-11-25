package com.psteam.foodlocationbusiness.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.databinding.ManagerFoodItemContainerBinding;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class ManagerFoodAdapter extends RecyclerView.Adapter<ManagerFoodAdapter.ManagerFoodViewHolder> {

    private final List<Food> foodList;
    private final Context context;

    public ManagerFoodAdapter(List<Food> foodList, Context context) {
        this.foodList = foodList;
        this.context = context;
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
            binding.imageViewFood.setImageBitmap(food.getImage().get(0));
            binding.textViewFoodName.setText(food.getName());
            binding.textViewPrice.setText(DecimalFormat.getCurrencyInstance(new Locale("vi", "VN")).format(food.getPrice()));
            binding.textViewFoodInfo.setText(food.getInfo());

            binding.buttonStatus.setOnClickListener(v -> {
                if( binding.buttonStatus.getTag().equals("off")) {
                    binding.buttonStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_toggle_on_24, 0, 0, 0);
                    binding.buttonStatus.setBackgroundColor(Color.parseColor("#2196F3"));
                    binding.buttonStatus.setTag("on");
                }else {
                    binding.buttonStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_toggle_off_24, 0, 0, 0);
                    binding.buttonStatus.setBackgroundColor(Color.parseColor("#454545"));
                    binding.buttonStatus.setTag("off");
                }
            });
        }
    }

    public static class Food {

        private List<Bitmap> image;
        private String name;
        private double price;
        private String info;


        public Food(List<Bitmap> image, String name, double price, String info) {
            this.image = image;
            this.name = name;
            this.price = price;
            this.info = info;
        }

        public List<Bitmap> getImage() {
            return image;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public void setImage(List<Bitmap> image) {
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
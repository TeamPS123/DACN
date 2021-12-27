package com.psteam.foodlocation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.FoodReserveItemContainerBinding;
import com.psteam.lib.modeluser.FoodModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FoodReserveAdapter extends RecyclerView.Adapter<FoodReserveAdapter.FoodReserveViewHolder> {

    private final List<FoodModel> foods;
    private final FoodReserveListeners foodReserveListeners;
    public final Context context;

    public FoodReserveAdapter(List<FoodModel> foods, FoodReserveListeners foodReserveListeners, Context context) {
        this.foods = foods;
        this.foodReserveListeners = foodReserveListeners;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodReserveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodReserveViewHolder(FoodReserveItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodReserveViewHolder holder, int position) {
        holder.setData(foods.get(position));

    }

    @Override
    public int getItemCount() {
        return foods != null ? foods.size() : 0;
    }

    class FoodReserveViewHolder extends RecyclerView.ViewHolder {

        public final FoodReserveItemContainerBinding binding;

        public FoodReserveViewHolder(@NonNull FoodReserveItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(FoodModel food) {
            if (food.getPic().size() > 0) {
                Glide.with(context).load(food.getPic().get(0)).thumbnail(0.3f).error(R.drawable.icon_tasty).into(binding.imageViewFood);
            }
            binding.textViewFoodName.setText(food.getName());
            binding.textViewPrice.setText(DecimalFormat.getCurrencyInstance(new Locale("vi", "VN")).format(Double.parseDouble(food.getPrice())));
            binding.textViewUnit.setText(food.getUnit());
            binding.textViewCategory.setText(food.getCategoryName());
            binding.textViewCount.setText(String.valueOf(food.getCount()));
            binding.textViewAdd.setOnClickListener(v -> {
                food.setCount(food.getCount() + 1);
                binding.textViewCount.setText(String.valueOf(food.getCount()));
                foodReserveListeners.onAddFoodReserveClick(food);
            });

            binding.textViewMinus.setOnClickListener(v -> {
                int count = food.getCount() - 1;
                food.setCount(count);
                if (count <= 0) {
                    double price = Double.parseDouble(food.getPrice());
                    foodReserveListeners.onRemoveFoodReserveClick(food, 1, price);
                    return;
                } else {
                    binding.textViewCount.setText(String.valueOf(count));
                }
                foodReserveListeners.onMinusFoodReserveClick(food);
            });

            binding.imageViewRemove.setOnClickListener(v -> {
                int count = food.getCount();
                food.setCount(0);
                double price = Double.parseDouble(food.getPrice());
                foodReserveListeners.onRemoveFoodReserveClick(food, count, price);
            });
        }
    }

    public interface FoodReserveListeners {
        void onAddFoodReserveClick(FoodModel food);

        void onMinusFoodReserveClick(FoodModel food);

        void onRemoveFoodReserveClick(FoodModel food, int count, double price);

        void onFoodClick(FoodModel food);
    }

    public static class FoodReserve {

        private ArrayList<String> image;
        private String name;
        private double price;
        private String info;
        private int count;
        private String id;


        public FoodReserve(ArrayList<String> image, String name, double price, String info, int count, String id) {
            this.image = image;
            this.name = name;
            this.price = price;
            this.info = info;
            this.count = count;
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public ArrayList<String> getImage() {
            return image;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public void setImage(ArrayList<String> image) {
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

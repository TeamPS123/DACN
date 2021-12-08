package com.psteam.foodlocation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.FoodItemContainerBinding;
import com.psteam.lib.modeluser.FoodModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder>  {

    private List<FoodModel> foodList;
    private final FoodListeners foodListeners;
    private List<FoodModel> oldFoodList;
    private final Context context;

    public FoodAdapter(List<FoodModel> foodList, FoodListeners foodListeners, Context context) {
        this.foodList = foodList;
        this.foodListeners = foodListeners;
        oldFoodList=foodList;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodViewHolder(FoodItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        holder.setData(foodList.get(position));
    }

    @Override
    public int getItemCount() {
        return foodList != null ? foodList.size() : 0;
    }

    class FoodViewHolder extends RecyclerView.ViewHolder {

        private final FoodItemContainerBinding binding;

        public FoodViewHolder(@NonNull FoodItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(FoodModel food) {
            if (food.getPic().size()>0){
                Glide.with(context).load(food.getPic().get(0)).thumbnail(0.3f).into(binding.imageViewFood);
            }
            binding.textViewFoodName.setText(food.getName());
            binding.textViewPrice.setText(DecimalFormat.getCurrencyInstance(new Locale("vi", "VN")).format(Double.parseDouble(food.getPrice())));
            binding.textViewUnit.setText(food.getUnit());
            binding.textViewCategory.setText(food.getCategoryName());

            binding.textViewAddFood.setOnClickListener(v -> {
                if(binding.textViewAddFood.getText().equals("Thêm món")) {
                    foodListeners.onAddFoodClick(food);
                    binding.textViewAddFood.setText("Xoá món");
                    binding.textViewAddFood.setBackgroundColor(context.getColor(R.color.ColorButtonReserve));
                }else {
                    foodListeners.onRemoveFoodClick(food);
                    binding.textViewAddFood.setText("Thêm món");
                    binding.textViewAddFood.setBackgroundColor(context.getColor(R.color.duskYellow));
                }
            });
            if(food.getCount()>0){
                binding.textViewAddFood.setText("Xoá món");
                binding.textViewAddFood.setBackgroundColor(context.getColor(R.color.ColorButtonReserve));
            }else {
                binding.textViewAddFood.setText("Thêm món");
                binding.textViewAddFood.setBackgroundColor(context.getColor(R.color.duskYellow));
            }
            binding.getRoot().setOnClickListener(v -> {
                foodListeners.onFoodClick(food);
            });
        }
    }

    public interface FoodListeners {
        void onAddFoodClick(FoodModel food);
        void onRemoveFoodClick(FoodModel foodModel);
        void onFoodClick(FoodModel food);
    }


}

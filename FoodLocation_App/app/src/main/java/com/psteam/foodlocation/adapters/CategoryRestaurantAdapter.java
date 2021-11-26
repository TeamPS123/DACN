package com.psteam.foodlocation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.LayoutCategoryRestaurantItemContainerBinding;
import com.psteam.lib.modeluser.CategoryRes;

import java.util.List;

public class CategoryRestaurantAdapter extends RecyclerView.Adapter<CategoryRestaurantAdapter.CategoryRestaurantViewHolder> {
    private final List<CategoryRes> categoryRestaurants;
    private final Context context;
    private final CategoryRestaurantListeners categoryRestaurantListeners;

    public CategoryRestaurantAdapter(List<CategoryRes> categoryRestaurants, Context context, CategoryRestaurantListeners categoryRestaurantListeners) {
        this.categoryRestaurants = categoryRestaurants;
        this.context = context;
        this.categoryRestaurantListeners = categoryRestaurantListeners;
    }

    @NonNull
    @Override
    public CategoryRestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryRestaurantViewHolder(LayoutCategoryRestaurantItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRestaurantViewHolder holder, int position) {
        holder.setData(categoryRestaurants.get(position));

    }

    @Override
    public int getItemCount() {
        return categoryRestaurants != null ? categoryRestaurants.size() : 0;
    }

    class CategoryRestaurantViewHolder extends RecyclerView.ViewHolder {

        private LayoutCategoryRestaurantItemContainerBinding binding;

        public CategoryRestaurantViewHolder(@NonNull LayoutCategoryRestaurantItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(CategoryRes categoryRestaurant) {
            binding.textViewName.setText(categoryRestaurant.getName());
            Glide.with(context).load(categoryRestaurant.getIcon()).thumbnail(0.3f).into(binding.imageViewIconCategoryRestaurant);

            binding.getRoot().setOnClickListener(v -> {
                isSelectedItem(categoryRestaurant);
            });

            if(categoryRestaurant.isSelected()){
                binding.imageViewIconCategoryRestaurant.setBackground(context.getDrawable(R.drawable.layout_category_restaurant_selected));
                binding.imageViewIconCategoryRestaurant.setTag("Selected");
               // categoryRestaurantListeners.onCategoryRestaurantClicked(categoryRestaurant,getAdapterPosition(),true,binding.imageViewIconCategoryRestaurant);
            }else {
                binding.imageViewIconCategoryRestaurant.setBackground(context.getDrawable(R.drawable.layout_category_restaurant));
                binding.imageViewIconCategoryRestaurant.setTag("unSelected");
              //  categoryRestaurantListeners.onCategoryRestaurantClicked(categoryRestaurant,getAdapterPosition(),false,binding.imageViewIconCategoryRestaurant);
            }
        }

        public void isSelectedItem(CategoryRes categoryRestaurant){
            if(binding.imageViewIconCategoryRestaurant.getTag().equals("unSelected")){
                binding.imageViewIconCategoryRestaurant.setBackground(context.getDrawable(R.drawable.layout_category_restaurant_selected));
                binding.imageViewIconCategoryRestaurant.setTag("Selected");
                categoryRestaurantListeners.onCategoryRestaurantClicked(categoryRestaurant,getAdapterPosition(),true,binding.imageViewIconCategoryRestaurant);
            }else {
                binding.imageViewIconCategoryRestaurant.setBackground(context.getDrawable(R.drawable.layout_category_restaurant));
                binding.imageViewIconCategoryRestaurant.setTag("unSelected");
                categoryRestaurantListeners.onCategoryRestaurantClicked(categoryRestaurant,getAdapterPosition(),false,binding.imageViewIconCategoryRestaurant);
            }


        }
    }

    public interface CategoryRestaurantListeners{
        void onCategoryRestaurantClicked(CategoryRes categoryRestaurant, int position, boolean isSelected, ImageView imageView);
    }


}

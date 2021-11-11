package com.psteam.foodlocation.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.databinding.CategoryItemContainerBinding;
import com.psteam.foodlocation.listeners.CategoryListener;
import com.psteam.foodlocation.models.CategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final List<CategoryModel> categoryModelList;
    private final CategoryListener categoryListener;

    public CategoryAdapter(List<CategoryModel> categoryModelList, CategoryListener categoryListener) {
        this.categoryModelList = categoryModelList;
        this.categoryListener = categoryListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(CategoryItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.setData(categoryModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryModelList != null ? categoryModelList.size() : 0;
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        final CategoryItemContainerBinding binding;

        public CategoryViewHolder(@NonNull CategoryItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(CategoryModel categoryModel) {
            binding.imageViewCategory.setBackgroundResource(categoryModel.getImage());
            binding.textViewName.setText(categoryModel.getName());
        }
    }
}

package com.psteam.foodlocation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.psteam.foodlocation.databinding.CategoryItemContainerBinding;
import com.psteam.foodlocation.listeners.CategoryListener;
import com.psteam.foodlocation.models.CategoryModel;
import com.psteam.lib.modeluser.CategoryRes;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final List<CategoryRes> categoryModelList;
    private final CategoryListener categoryListener;
    private final Context context;

    public CategoryAdapter(List<CategoryRes> categoryModelList, CategoryListener categoryListener, Context context) {
        this.categoryModelList = categoryModelList;
        this.categoryListener = categoryListener;
        this.context = context;
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

        public void setData(CategoryRes categoryModel) {

            Glide.with(context).load(categoryModel.getIcon()).into(binding.imageViewCategory);

            binding.textViewName.setText(categoryModel.getName());

            binding.getRoot().setOnClickListener(v -> {
                categoryListener.onCategoryClick(categoryModel);
            });
        }
    }
}

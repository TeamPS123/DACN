package com.psteam.foodlocation.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.databinding.ManagerCategoryItemContainerBinding;

import java.util.List;

public class ManagerCategoryAdapter extends RecyclerView.Adapter<ManagerCategoryAdapter.ManagerCategoryViewHolder> {

    private final List<Category> categories;

    public ManagerCategoryAdapter(List<Category> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public ManagerCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ManagerCategoryViewHolder(ManagerCategoryItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ManagerCategoryViewHolder holder, int position) {
        holder.setDat(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

    class ManagerCategoryViewHolder extends RecyclerView.ViewHolder {

        final ManagerCategoryItemContainerBinding binding;

        public ManagerCategoryViewHolder(@NonNull ManagerCategoryItemContainerBinding itemView) {
            super(itemView.getRoot());

            binding = itemView;
        }

        public void setDat(Category category) {
            binding.textViewCategoryName.setText(category.getName());
        }
    }

    public interface ManagerCategoryListeners {
        void onConfirmedClicked(Category category);

        void onBackClicked(Category category);
    }

    public static class Category {

        private String name;
        private String Id;
        private boolean status;

        public Category(String name, String id, boolean status) {
            this.name = name;
            Id = id;
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}

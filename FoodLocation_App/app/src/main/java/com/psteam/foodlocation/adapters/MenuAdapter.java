package com.psteam.foodlocation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.MenuItemContainerBinding;
import com.psteam.foodlocation.ultilities.DividerItemDecorator;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private final List<Menu> menus;
    private final Context context;
    private final FoodAdapter.FoodListeners foodListeners;

    public MenuAdapter(List<Menu> menus, Context context, FoodAdapter.FoodListeners foodListeners) {
        this.menus = menus;
        this.context = context;
        this.foodListeners = foodListeners;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MenuViewHolder(MenuItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        holder.setData(menus.get(position));
    }

    @Override
    public int getItemCount() {
        return menus != null ? menus.size() : 0;
    }

    class MenuViewHolder extends RecyclerView.ViewHolder {

        private final MenuItemContainerBinding binding;

        public MenuViewHolder(@NonNull MenuItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(Menu menu) {
            binding.textViewNameMenu.setText(menu.getName());

            FoodAdapter foodAdapter = new FoodAdapter(menu.foodList, foodListeners);
            binding.recycleViewMenuFood.setAdapter(foodAdapter);

            RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(context, R.drawable.divider));
            binding.recycleViewMenuFood.addItemDecoration(dividerItemDecoration);

        }
    }

    public static class Menu {

        private String name;
        private List<FoodAdapter.Food> foodList;

        public Menu(String name, List<FoodAdapter.Food> foodList) {
            this.name = name;
            this.foodList = foodList;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<FoodAdapter.Food> getFoodList() {
            return foodList;
        }

        public void setFoodList(List<FoodAdapter.Food> foodList) {
            this.foodList = foodList;
        }
    }
}

package com.psteam.foodlocation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.MenuItemContainerBinding;
import com.psteam.foodlocation.ultilities.DividerItemDecorator;
import com.psteam.lib.modeluser.FoodModel;
import com.psteam.lib.modeluser.MenuModel;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> implements Filterable {

    private List<MenuModel> menus;
    private List<MenuModel> oldMenus;
    private final Context context;
    private final FoodAdapter.FoodListeners foodListeners;

    public MenuAdapter(List<MenuModel> menus, Context context, FoodAdapter.FoodListeners foodListeners) {
        this.menus = menus;
        this.context = context;
        this.foodListeners = foodListeners;
        oldMenus = menus;
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                //String charString = VNCharacterUtils.removeAccent(charSequence.toString().trim().toLowerCase());
                String charStr=Normalizer.normalize(charSequence.toString().toLowerCase(Locale.ROOT), Normalizer.Form.NFD).replaceAll("\\p{M}", "");

                //String charString = charSequence.toString().trim().toLowerCase();
                if (charStr.isEmpty()) {
                    menus = oldMenus;
                } else {
                    List<MenuModel> filteredList = new ArrayList<>();
                    for (MenuModel row : oldMenus) {
                        ArrayList<FoodModel> filteredFoodList = new ArrayList<>();
                        for (FoodModel food : row.getFoodList()) {
                            if (/*row.getMenuId()==food.getMenuId() &&*/ (Normalizer.normalize(food.getName().toLowerCase(Locale.ROOT), Normalizer.Form.NFD).replaceAll("\\p{M}", "")).contains(charStr)) {
                                filteredFoodList.add(food);
                            }
                        }
                        if(filteredFoodList.size()>0){
                            filteredList.add(new MenuModel(filteredFoodList,row.getName(),row.getMenuId()));
                        }
                    }
                    menus = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = menus;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                menus = (ArrayList<MenuModel>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
              //  foodAdapter.notifyDataSetChanged();

            }
        };
    }

    public FoodAdapter foodAdapter;

    class MenuViewHolder extends RecyclerView.ViewHolder {

        private final MenuItemContainerBinding binding;

        public MenuViewHolder(@NonNull MenuItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(MenuModel menu) {
            binding.textViewNameMenu.setText(menu.getName());
            foodAdapter = new FoodAdapter(menu.getFoodList(), foodListeners, context);
            binding.recycleViewMenuFood.setAdapter(foodAdapter);

            RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(context, R.drawable.divider));
            binding.recycleViewMenuFood.addItemDecoration(dividerItemDecoration);

        }
    }

}

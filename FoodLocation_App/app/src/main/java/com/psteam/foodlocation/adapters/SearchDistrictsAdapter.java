package com.psteam.foodlocation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.LayoutDistrictsSearchItemContainerBinding;
import com.psteam.foodlocation.models.DistrictModel;

import java.util.List;

public class SearchDistrictsAdapter extends RecyclerView.Adapter<SearchDistrictsAdapter.SearchDistrictsViewHolder> {

    private final List<DistrictModel> districtModelList;
    private final Context context;
    private final SearchDistrictsListeners searchDistrictsListeners;

    public SearchDistrictsAdapter(List<DistrictModel> districtModelList, Context context, SearchDistrictsListeners searchDistrictsListeners) {
        this.districtModelList = districtModelList;
        this.context = context;
        this.searchDistrictsListeners = searchDistrictsListeners;
    }


    @NonNull
    @Override
    public SearchDistrictsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchDistrictsViewHolder(LayoutDistrictsSearchItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchDistrictsViewHolder holder, int position) {
        holder.setData(districtModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return districtModelList != null ? districtModelList.size() : 0;
    }

    class SearchDistrictsViewHolder extends RecyclerView.ViewHolder {

        private LayoutDistrictsSearchItemContainerBinding binding;

        public SearchDistrictsViewHolder(@NonNull LayoutDistrictsSearchItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(DistrictModel districtModel) {
            binding.textViewNameDistrict.setText(districtModel.getName());

            binding.getRoot().setOnClickListener(v -> {
                if (binding.textViewNameDistrict.getTag().equals("UnSelected")) {
                    binding.getRoot().setBackground(context.getDrawable(R.drawable.background_districts_search_selected));
                    binding.textViewNameDistrict.setTag("Selected");
                    binding.textViewNameDistrict.setTextColor(context.getColor(R.color.ColorTextSelected));
                    searchDistrictsListeners.onDistrictClicked(districtModel, binding.getRoot(), true);
                } else {
                    binding.getRoot().setBackground(context.getDrawable(R.drawable.background_districts_search));
                    binding.textViewNameDistrict.setTag("UnSelected");
                    binding.textViewNameDistrict.setTextColor(context.getColor(R.color.ColorTextUnSelected));
                    searchDistrictsListeners.onDistrictClicked(districtModel, binding.getRoot(), false);
                }
            });
        }
    }

    public interface SearchDistrictsListeners {
        void onDistrictClicked(DistrictModel districtModel, View view, boolean isSelected);
    }
}

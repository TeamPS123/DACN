package com.psteam.foodlocation.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.psteam.foodlocation.databinding.SliderItemContainerBinding;
import com.psteam.foodlocation.models.SliderItem;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SlideViewHolder> {
    private final List<SliderItem> sliderItemList;
    private final ViewPager2 viewPager2;

    public SliderAdapter(List<SliderItem> sliderItemList, ViewPager2 viewPager2) {
        this.sliderItemList = sliderItemList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SlideViewHolder(SliderItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        holder.setData(sliderItemList.get(position));
        if (position==sliderItemList.size()-2){
            viewPager2.post(sliderRunnable);
        }
    }

    @Override
    public int getItemCount() {
        return sliderItemList != null ? sliderItemList.size() : 0;
    }


    class SlideViewHolder extends RecyclerView.ViewHolder {

        final SliderItemContainerBinding binding;

        public SlideViewHolder(@NonNull SliderItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(SliderItem sliderItem) {
            binding.imageSlide.setBackgroundResource(sliderItem.getImage());
        }
    }

    private Runnable sliderRunnable=new Runnable() {
        @Override
        public void run() {
            sliderItemList.addAll(sliderItemList);
            notifyDataSetChanged();
        }
    };


}

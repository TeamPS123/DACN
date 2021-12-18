package com.psteam.foodlocation.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.psteam.foodlocation.databinding.LayoutPhotoReiviewItemContainerBinding;
import com.psteam.foodlocation.databinding.LayoutPhotoReviewBinding;
import com.psteam.foodlocation.databinding.SliderItemContainerBinding;
import com.psteam.foodlocation.models.SliderItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotoReviewAdapter extends RecyclerView.Adapter<PhotoReviewAdapter.PhotoReviewViewHolder> {
    private final List<String> sliderItemList;
    private final ViewPager2 viewPager2;

    public PhotoReviewAdapter(List<String> sliderItemList, ViewPager2 viewPager2) {
        this.sliderItemList = sliderItemList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public PhotoReviewAdapter.PhotoReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoReviewViewHolder(LayoutPhotoReiviewItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoReviewAdapter.PhotoReviewViewHolder holder, int position) {
        holder.setData(sliderItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return sliderItemList != null ? sliderItemList.size() : 0;
    }


    class PhotoReviewViewHolder extends RecyclerView.ViewHolder {

        final LayoutPhotoReiviewItemContainerBinding binding;

        public PhotoReviewViewHolder(@NonNull LayoutPhotoReiviewItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(String s) {
            Picasso.get().load(s).into(binding.imageView);
        }
    }

}

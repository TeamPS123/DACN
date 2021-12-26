package com.psteam.foodlocation.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.databinding.LayoutPhotoResMapItemContainerBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageResMapAdapter extends RecyclerView.Adapter<ImageResMapAdapter.ImageResMapViewHolder> {
    private final List<String> sliderItemList;


    public ImageResMapAdapter(List<String> sliderItemList) {
        this.sliderItemList = sliderItemList;
    }

    @NonNull
    @Override
    public ImageResMapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageResMapViewHolder(LayoutPhotoResMapItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageResMapViewHolder holder, int position) {
        holder.setData(sliderItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return sliderItemList != null ? sliderItemList.size() : 0;
    }


    class ImageResMapViewHolder extends RecyclerView.ViewHolder {

        final LayoutPhotoResMapItemContainerBinding binding;

        public ImageResMapViewHolder(@NonNull LayoutPhotoResMapItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(String s) {
            Picasso.get().load(s).into(binding.imagePhoto);
        }
    }

}


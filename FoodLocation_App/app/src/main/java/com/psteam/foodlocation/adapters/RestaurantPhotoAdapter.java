package com.psteam.foodlocation.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.databinding.RestaurantPhotoItemContainerBinding;

import java.util.List;

public class RestaurantPhotoAdapter extends RecyclerView.Adapter<RestaurantPhotoAdapter.RestaurantPhotoViewHolder> {

    private final List<Photo> photoList;

    public RestaurantPhotoAdapter(List<Photo> photoList) {
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public RestaurantPhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RestaurantPhotoViewHolder(RestaurantPhotoItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantPhotoViewHolder holder, int position) {
        holder.setData(photoList.get(position));
    }

    @Override
    public int getItemCount() {
        return photoList != null ? photoList.size() : 0;
    }

    class RestaurantPhotoViewHolder extends RecyclerView.ViewHolder {

        private RestaurantPhotoItemContainerBinding binding;

        public RestaurantPhotoViewHolder(@NonNull RestaurantPhotoItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(Photo photo) {
            binding.imageView.setImageResource(photo.getPhoto());
        }
    }


    public static class Photo {
        public int photo;

        public Photo(int photo) {
            this.photo = photo;
        }

        public int getPhoto() {
            return photo;
        }

        public void setPhoto(int photo) {
            this.photo = photo;
        }
    }
}

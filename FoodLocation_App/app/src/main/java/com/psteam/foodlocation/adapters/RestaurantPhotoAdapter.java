package com.psteam.foodlocation.adapters;

import static com.squareup.picasso.Picasso.*;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.psteam.foodlocation.databinding.RestaurantPhotoItemContainerBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RestaurantPhotoAdapter extends RecyclerView.Adapter<RestaurantPhotoAdapter.RestaurantPhotoViewHolder> {

    private final List<String> photoList;
    public final Context context;

    public RestaurantPhotoAdapter(List<String> photoList, Context context) {
        this.photoList = photoList;
        this.context = context;
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

        public void setData(String photo) {
            Picasso.get().load(photo).into(binding.imageView);
           // Glide.with(context).load("https://cdn.tgdd.vn/Files/2020/12/29/1316941/cach-cai-hinh-nen-doi-theo-ngay-dem-tren-iphone-d-1.jpg").thumbnail(0.3f).into(binding.getRoot());
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

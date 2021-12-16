package com.psteam.foodlocation.adapters;

import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.databinding.ContainerItemImageRestaurantBinding;

import java.util.ArrayList;

public class ImageRestaurantAdapter extends RecyclerView.Adapter<ImageRestaurantAdapter.ImageRestaurantViewHolder> {
    private final ArrayList<Uri> bitmaps;
    private final ImageRestaurantListeners imageRestaurantListeners;

    public ImageRestaurantAdapter(ArrayList<Uri> bitmaps, ImageRestaurantListeners imageRestaurantListeners) {
        this.bitmaps = bitmaps;

        this.imageRestaurantListeners = imageRestaurantListeners;
    }

    @NonNull
    @Override
    public ImageRestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageRestaurantViewHolder(ContainerItemImageRestaurantBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageRestaurantViewHolder holder, int position) {
        holder.setData(bitmaps.get(position));
    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
    }


    class ImageRestaurantViewHolder extends RecyclerView.ViewHolder {

        private final ContainerItemImageRestaurantBinding binding;

        public ImageRestaurantViewHolder(@NonNull ContainerItemImageRestaurantBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(Uri bitmap) {
            if (bitmap == null) {
                if (getAdapterPosition() < 5) {
                    binding.layoutPhoto.setVisibility(View.GONE);
                    binding.layoutAddPhoto.setVisibility(View.VISIBLE);
                    binding.layoutAddPhoto.setOnClickListener(v -> {
                        imageRestaurantListeners.onAddPhotoClick(getAdapterPosition());
                    });
                }else {
                    binding.layoutPhoto.setVisibility(View.GONE);
                }

                return;
            }else {
                binding.layoutAddPhoto.setVisibility(View.GONE);
                binding.layoutPhoto.setVisibility(View.VISIBLE);
                binding.imageViewRestaurant.setImageURI(bitmap);

                binding.imageRemove.setOnClickListener(v -> {
                    removeImage(getAdapterPosition());
                    imageRestaurantListeners.onRemovePhotoClick(bitmap, getAdapterPosition(), binding.layoutAddPhoto);
                });
            }
        }
    }

    public void removeImage(int position) {
        bitmaps.remove(position);
        notifyDataSetChanged();
    }

    public interface ImageRestaurantListeners {
        void onAddPhotoClick(int position);

        void onRemovePhotoClick(Uri uri, int position, View view);
    }
}

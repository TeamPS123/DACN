package com.psteam.foodlocation.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.databinding.LayoutPhotoReviewBinding;

import java.util.List;

public class ImageReviewAdapter extends RecyclerView.Adapter<ImageReviewAdapter.ImageReviewViewHolder> {

    private final List<Uri> uris;
    private final ImageReviewListeners imageReviewListeners;

    public ImageReviewAdapter(List<Uri> uris, ImageReviewListeners imageReviewListeners) {
        this.uris = uris;
        this.imageReviewListeners = imageReviewListeners;
    }

    @NonNull
    @Override
    public ImageReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageReviewViewHolder(LayoutPhotoReviewBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageReviewViewHolder holder, int position) {
        holder.setData(uris.get(position));
    }

    @Override
    public int getItemCount() {
        return uris != null ? uris.size() : 0;
    }


    class ImageReviewViewHolder extends RecyclerView.ViewHolder {

        final LayoutPhotoReviewBinding binding;

        public ImageReviewViewHolder(@NonNull LayoutPhotoReviewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(Uri uri) {
            binding.imagePhoto.setImageURI(uri);
            binding.iconRemove.setOnClickListener(v -> {
                uris.remove(uri);
                notifyItemChanged(getAdapterPosition());
                imageReviewListeners.onRemoveClick(uri);
            });
        }
    }

    public interface ImageReviewListeners {
        void onRemoveClick(Uri uri);
    }
}

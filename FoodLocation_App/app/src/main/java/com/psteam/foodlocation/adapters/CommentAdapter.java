package com.psteam.foodlocation.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.databinding.LayoutCommentItemContainerBinding;
import com.psteam.lib.modeluser.CommentModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final List<CommentModel> comments;

    public CommentAdapter(List<CommentModel> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutCommentItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.setData(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments != null ? comments.size() : 0;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        final LayoutCommentItemContainerBinding binding;

        public CommentViewHolder(@NonNull LayoutCommentItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(CommentModel comment) {
            binding.textViewUserName.setText(comment.getName());
            Picasso.get().load(comment.getImgUser()).into(binding.imageViewUser);
            binding.textViewContent.setText(comment.getContent());
            binding.textViewDate.setText(comment.getDate());
        }
    }


}

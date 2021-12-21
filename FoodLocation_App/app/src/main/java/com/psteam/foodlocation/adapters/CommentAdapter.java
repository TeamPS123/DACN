package com.psteam.foodlocation.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.databinding.LayoutCommentItemContainerBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final List<Comment> comments;

    public CommentAdapter(List<Comment> comments) {
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

        public void setData(Comment comment) {
            binding.textViewUserName.setText(comment.getFullName());
            Picasso.get().load(comment.getUserIcon()).into(binding.imageViewUser);
            binding.textViewContent.setText(comment.getContent());
            binding.textViewDate.setText(comment.getDate());
        }
    }

    public static class Comment {
        private String id;
        private String userId;
        private String fullName;
        private String content;
        private String reviewId;
        private String userIcon;
        private String date;

        public Comment(String id, String userId, String fullName, String content, String reviewId, String userIcon, String date) {
            this.id = id;
            this.userId = userId;
            this.fullName = fullName;
            this.content = content;
            this.reviewId = reviewId;
            this.userIcon = userIcon;
            this.date = date;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getReviewId() {
            return reviewId;
        }

        public void setReviewId(String reviewId) {
            this.reviewId = reviewId;
        }

        public String getUserIcon() {
            return userIcon;
        }

        public void setUserIcon(String userIcon) {
            this.userIcon = userIcon;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}

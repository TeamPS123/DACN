package com.psteam.foodlocation.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.databinding.NotificationItemContainerBinding;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final List<Notification> notifications;

    private final NotificationListeners notificationListeners;

    public NotificationAdapter(List<Notification> notifications, NotificationListeners notificationListeners) {
        this.notifications = notifications;
        this.notificationListeners = notificationListeners;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationViewHolder(NotificationItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.setData(notifications.get(position));
    }

    @Override
    public int getItemCount() {
        return notifications != null ? notifications.size() : 0;
    }


    class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final NotificationItemContainerBinding binding;

        public NotificationViewHolder(@NonNull NotificationItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(Notification notification) {
            binding.textViewNotificationContent.setText(notification.getContent());

            binding.textViewNotificationContent.setOnClickListener(v -> {
                notificationListeners.onClicked(notification, getAdapterPosition());
            });
        }
    }

    public interface NotificationListeners{
        void onClicked(Notification notification, int position);
    }

    public static class Notification {
        private String content;
        private String type;

        public Notification(String content, String type) {
            this.content = content;
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}

package com.psteam.foodlocation.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.databinding.LayoutNotificationItemContainerBinding;
import com.psteam.lib.Models.reserveTableDetail.reserveTable;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
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
        return new NotificationViewHolder(LayoutNotificationItemContainerBinding.inflate(
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
        private final LayoutNotificationItemContainerBinding binding;

        public NotificationViewHolder(@NonNull LayoutNotificationItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(Notification notification) {

            Picasso.get().load(notification.icon).into(binding.imageView);
            String status=String.format("Yêu cầu đặt bàn ở %s",notification.reserveTable.getRestaurant().getName());
            if(notification.getType().equals("1")){
                status+=" đã được xác nhận.";
            }else if(notification.getType().equals("4")){
                status+=" đã hoàn thành";
                binding.textViewContent.setText("Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi. Hãy chia sẻ trải nghiệm của bạn về đơn hàng để giúp những Khách hàng khác có thể tham khảo nhé!");
            }else if(notification.getType().equals("2")){
                status+=" đã bị huỷ";
                binding.textViewContent.setText("Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi. Hãy nhấn vào để xem chi tiết!");
            }
            binding.textViewTitle.setText(status);
            binding.textViewDate.setText(notification.getDate());
        }
    }

    public interface NotificationListeners{
        void onClicked(Notification notification, int position);
    }

    public static class Notification implements Serializable {
        private String type;
        private String icon;
        private String date;
        private reserveTable reserveTable;

        public Notification(String type, String icon, String date, com.psteam.lib.Models.reserveTableDetail.reserveTable reserveTable) {
            this.type = type;
            this.icon = icon;
            this.date = date;
            this.reserveTable = reserveTable;
        }

        public com.psteam.lib.Models.reserveTableDetail.reserveTable getReserveTable() {
            return reserveTable;
        }

        public void setReserveTable(com.psteam.lib.Models.reserveTableDetail.reserveTable reserveTable) {
            this.reserveTable = reserveTable;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}

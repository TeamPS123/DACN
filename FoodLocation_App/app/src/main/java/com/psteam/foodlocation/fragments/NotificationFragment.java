package com.psteam.foodlocation.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.activities.UserReserveTableDetailsActivity;
import com.psteam.foodlocation.adapters.NotificationAdapter;
import com.psteam.foodlocation.databinding.FragmentNotificationBinding;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.foodlocation.ultilities.DividerItemDecorator;
import com.psteam.foodlocation.ultilities.PreferenceManager;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {

    private static PreferenceManager preferenceManager;
    private static ArrayList<NotificationAdapter.Notification> notifications;
    private static ArrayList<NotificationAdapter.Notification> tempNotifications;
    private static NotificationAdapter notificationAdapter;
    private static Activity context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private static FragmentNotificationBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        init();
        context = this.getActivity();
        setListeners();
        return binding.getRoot();
    }

    private void init() {
        loading(true);
        preferenceManager = new PreferenceManager(getContext());
        notifications = new ArrayList<>();
        tempNotifications = new ArrayList<>();
        initNotification();
    }

    private void initNotification() {
        notifications = preferenceManager.getListNotification(Constants.TAG_NOTIFICATION);
        tempNotifications = notifications;

        if (notifications != null && notifications.size() > 0) {
            binding.textViewEmptyNotification.setVisibility(View.GONE);
        } else {
            binding.textViewEmptyNotification.setVisibility(View.VISIBLE);
        }

        notificationAdapter = new NotificationAdapter(notifications, new NotificationAdapter.NotificationListeners() {
            @Override
            public void onClicked(NotificationAdapter.Notification notification, int position) {
                Intent intent = new Intent(getContext(), UserReserveTableDetailsActivity.class);
                intent.putExtra("reserveTable", notification.getReserveTable());
                startActivity(intent);
            }
        });

        binding.recycleView.setAdapter(notificationAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(binding.recycleView);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecorator(getContext().getDrawable(R.drawable.divider));
        binding.recycleView.addItemDecoration(itemDecoration);
        loading(false);
    }

    public static void addNotification(NotificationAdapter.Notification notification) {
        if (notifications != null && notification != null) {
            notifications.add(0, notification);
            tempNotifications.add(0, notification);
            notificationAdapter.notifyItemInserted(0);
        } else {
            notifications = new ArrayList<>();
            if (preferenceManager == null) return;
            notifications = preferenceManager.getListNotification(Constants.TAG_NOTIFICATION);
            tempNotifications = notifications;
            notificationAdapter = new NotificationAdapter(notifications, new NotificationAdapter.NotificationListeners() {
                @Override
                public void onClicked(NotificationAdapter.Notification notification, int position) {
                    Intent intent = new Intent(context, UserReserveTableDetailsActivity.class);
                    intent.putExtra("reserveTable", notification.getReserveTable());
                    context.startActivity(intent);
                }
            });

            binding.recycleView.setAdapter(notificationAdapter);
        }
        checkNotification();
    }

    public static void checkNotification() {
        ArrayList<NotificationAdapter.Notification> check = new ArrayList<>();
        check = preferenceManager.getListNotification(Constants.TAG_NOTIFICATION);
        if (check != null) {
            if (check.size() != 0) {
                binding.textViewEmptyNotification.setVisibility(View.GONE);
                binding.recycleView.setVisibility(View.VISIBLE);
            } else {
                binding.textViewEmptyNotification.setVisibility(View.VISIBLE);
                binding.recycleView.setVisibility(View.GONE);
            }
        } else {
            binding.textViewEmptyNotification.setVisibility(View.VISIBLE);
            binding.recycleView.setVisibility(View.GONE);
        }
    }

    private void setListeners() {
        binding.buttonClearAll.setOnClickListener(v -> {
            preferenceManager.clearNotification();
            if (notifications != null) {
                notifications.clear();
                tempNotifications.clear();
                notificationAdapter.notifyDataSetChanged();
                checkNotification();
            }
        });
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            int position = viewHolder.getAdapterPosition();
            preferenceManager.removeNotification(position);
            notifications.remove(position);
            tempNotifications.remove(position);
            notificationAdapter.notifyItemRemoved(position);
            checkNotification();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        notifications = preferenceManager.getListNotification(Constants.TAG_NOTIFICATION);
        if (notifications != null && tempNotifications != null && notifications.size() != tempNotifications.size()) {
            Toast.makeText(getContext(), "onResume", Toast.LENGTH_SHORT).show();
            notificationAdapter.notifyDataSetChanged();
        }
    }

    private void loading(boolean Loading) {
        if (Loading) {
            binding.recycleView.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.recycleView.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        }
    }

}
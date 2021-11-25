package com.psteam.foodlocationbusiness.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.activites.ReserveTableDetailsActivity;
import com.psteam.foodlocationbusiness.adapters.ReserveTableAdapter;
import com.psteam.foodlocationbusiness.databinding.FragmentPendingReservedTableBinding;
import com.psteam.foodlocationbusiness.socket.models.MessageSenderFromRes;
import com.psteam.foodlocationbusiness.socket.setupSocket;
import com.psteam.foodlocationbusiness.ultilities.DividerItemDecorator;

import java.util.ArrayList;


public class PendingReservedTableFragment extends Fragment {

    private FragmentPendingReservedTableBinding binding;

    private ReserveTableAdapter reserveTableAdapter;
    private ArrayList<ReserveTableAdapter.ReserveTable> reserveTables;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPendingReservedTableBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        init();
        return view;
    }

    private void init(){
        initReserveTable();
    }

    private void initReserveTable() {
        reserveTables=new ArrayList<>();

        reserveTables.add(new ReserveTableAdapter.ReserveTable("Lê Tiểu Phàm","0123456789","11:03 SA, 11/19/2021",2));
        reserveTables.add(new ReserveTableAdapter.ReserveTable("Lê Tiểu Phàm","0123456789","11:03 SA, 11/19/2021",2));
        reserveTables.add(new ReserveTableAdapter.ReserveTable("Lê Tiểu Phàm","0123456789","11:03 SA, 11/19/2021",2));
        reserveTables.add(new ReserveTableAdapter.ReserveTable("Lê Tiểu Phàm","0123456789","11:03 SA, 11/19/2021",2));
        reserveTables.add(new ReserveTableAdapter.ReserveTable("Lê Tiểu Phàm","0123456789","11:03 SA, 11/19/2021",2));

        reserveTableAdapter=new ReserveTableAdapter(reserveTables, new ReserveTableAdapter.ReserveTableListeners() {
            @Override
            public void onConfirmClicked(ReserveTableAdapter.ReserveTable reserveTable, int position) {
                MessageSenderFromRes message = new MessageSenderFromRes("restaurant", "user", "thông báo", "Nhà hàng đã xác nhận đơn của bạn");
                setupSocket.reserveTable(message);
            }

            @Override
            public void onDenyClicked(ReserveTableAdapter.ReserveTable reserveTable, int position) {
                MessageSenderFromRes message = new MessageSenderFromRes("restaurant", "user", "thông báo", "Nhà hàng đã từ chối đơn của bạn");
                setupSocket.reserveTable(message);
            }

            @Override
            public void onClicked(ReserveTableAdapter.ReserveTable reserveTable, int position) {
                startActivity(new Intent(getContext(), ReserveTableDetailsActivity.class));
            }
        });

        binding.recycleView.setAdapter(reserveTableAdapter);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        binding.recycleView.addItemDecoration(dividerItemDecoration);
    }
}
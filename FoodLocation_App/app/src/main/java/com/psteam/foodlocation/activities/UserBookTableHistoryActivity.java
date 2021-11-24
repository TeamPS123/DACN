package com.psteam.foodlocation.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.UserReserveTableAdapter;
import com.psteam.foodlocation.databinding.ActivityUserBookTableHistoryBinding;
import com.psteam.foodlocation.ultilities.DividerItemDecorator;

import java.util.ArrayList;

public class UserBookTableHistoryActivity extends AppCompatActivity {

    private ActivityUserBookTableHistoryBinding binding;
    private ArrayList<UserReserveTableAdapter.ReserveTable> reserveTables;
    private UserReserveTableAdapter userReserveTableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBookTableHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
    }

    private void init() {
        setFullScreen();
        initUserBookTableAdapter();
    }

    private void initUserBookTableAdapter() {
        reserveTables=new ArrayList<>();
        reserveTables.add(new UserReserveTableAdapter.ReserveTable("Nhà hàng 1","Thứ 3 ngày 27 thg 10 2021",2,1));
        reserveTables.add(new UserReserveTableAdapter.ReserveTable("Nhà hàng 1","Thứ 3 ngày 27 thg 10 2021",2,1));
        reserveTables.add(new UserReserveTableAdapter.ReserveTable("Nhà hàng 1","Thứ 3 ngày 27 thg 10 2021",2,1));
        reserveTables.add(new UserReserveTableAdapter.ReserveTable("Nhà hàng 1","Thứ 3 ngày 27 thg 10 2021",2,1));
        reserveTables.add(new UserReserveTableAdapter.ReserveTable("Nhà hàng 1","Thứ 3 ngày 27 thg 10 2021",2,1));

        userReserveTableAdapter=new UserReserveTableAdapter(reserveTables, new UserReserveTableAdapter.UserReserveTableListeners() {
            @Override
            public void onUserReserveTableClicked(UserReserveTableAdapter.ReserveTable reserveTable) {
                startActivity(new Intent(getApplicationContext(),UserReserveTableDetailsActivity.class));
            }
        });

        binding.recycleView.setAdapter(userReserveTableAdapter);

    }

    private void setListeners() {

    }

    private void setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            getWindow().setStatusBarColor(ContextCompat.getColor(UserBookTableHistoryActivity.this, R.color.white));// set status background white
        }
    }
}
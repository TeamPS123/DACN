package com.psteam.foodlocation.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.ActivityUserReserveTableDetailsBinding;
import com.psteam.foodlocation.socket.models.BodySenderFromRes;
import com.psteam.lib.modeluser.ReserveTable;


public class UserReserveTableDetailsActivity extends AppCompatActivity {

    ActivityUserReserveTableDetailsBinding binding;
    private BodySenderFromRes response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserReserveTableDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ReserveTable reserveTable = (ReserveTable) bundle.getSerializable("reserveTable");

        binding.textViewRestaurantName.setText(reserveTable.getRestaurant().getName());
        binding.textViewCountPeople.setText(String.format("%s người", reserveTable.getQuantity()));
        binding.textViewRestaurantAddress.setText(reserveTable.getRestaurant().getAddress());
        if (reserveTable.getRestaurant().getPromotionRes().size() > 0) {
            binding.textViewPromotion.setText(String.format("-%s%%", reserveTable.getRestaurant().getPromotionRes().get(0).getValue()));
        } else {
            binding.text15.setVisibility(View.GONE);
            binding.image6.setVisibility(View.GONE);
            binding.textViewPromotion.setVisibility(View.GONE);
            binding.line6.setVisibility(View.GONE);
        }
        if (reserveTable.getStatus().equals("0")) {
            binding.textViewStatus.setText(String.format("Đang chờ xác nhận"));
        } else if (reserveTable.getStatus().equals("1")) {
            binding.textViewStatus.setText(String.format("Trạng thái đã được duyệt"));
        } else if (reserveTable.getStatus().equals("2")) {
            binding.textViewStatus.setText(String.format("Trạng thái bị huỷ"));
        } else if (reserveTable.getStatus().equals("3")) {
            binding.textViewStatus.setText(String.format("Quá hạn"));
        } else {
            binding.textViewStatus.setText(String.format("Hoàn tất"));
        }

        binding.textPhoneRestaurant.setText(reserveTable.getRestaurant().getPhoneRes());

        binding.textViewUserName.setText(reserveTable.getName());
        binding.textViewTimeReserve.setText(reserveTable.getTime());
        binding.textViewPhoneNumber.setText(reserveTable.getPhone());

        if (reserveTable.getStatus().equals("2")) {
            binding.text20.setVisibility(View.GONE);
            binding.inputNote.setVisibility(View.GONE);
        }

    }

    private void setListeners() {
        binding.imageViewClose.setOnClickListener(v -> {
            finish();
        });
    }

    private void setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            getWindow().setStatusBarColor(ContextCompat.getColor(UserReserveTableDetailsActivity.this, R.color.white));// set status background white
        }
    }

    private void init() {
        setFullScreen();
        initData();

        if (getIntent().getExtras() != null) {
            getDataFromNoti();
            setBinding();
        }
    }

    private void getDataFromNoti() {
        if (getIntent().getExtras().getString("notification") != null) {
            response = new BodySenderFromRes();
            response.setNotification(getIntent().getExtras().getString("notification"));
            response.setReserveTableId(getIntent().getExtras().getString("reserveTableId"));
        } else {
            response = (BodySenderFromRes) getIntent().getSerializableExtra("response");
        }
    }

    private void setBinding() {
        binding.inputNote.setText(response.getNotification());
    }
}
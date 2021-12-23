package com.psteam.foodlocation.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.ReportResAdapter;
import com.psteam.foodlocation.databinding.ActivityReportRestaurantBinding;
import com.psteam.foodlocation.databinding.LayoutNotificationDialogBinding;
import com.psteam.foodlocation.ultilities.CustomToast;
import com.psteam.foodlocation.ultilities.DividerItemDecorator;

import java.util.ArrayList;

public class ReportRestaurantActivity extends AppCompatActivity {

    private ActivityReportRestaurantBinding binding;
    private ReportResAdapter reportResAdapter;
    private ArrayList<String> strings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportRestaurantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListeners();
    }

    private void init() {
        setFullScreen();
        strings = new ArrayList<>();
        strings.add("Cửa hàng đã chuyển / đóng cửa");
        strings.add("Sai địa chỉ bản đồ");
        strings.add("Sai số điện thoại");
        strings.add("Sai điều kiện áp dụng khuyến mãi");
        strings.add("Khuyến mãi đã dừng");
        strings.add("Không được áp dụng đúng theo chương trình");
        strings.add("Cửa hàng từ chối áp dụng khuyến mãi");
        strings.add("Khác...");

        reportResAdapter = new ReportResAdapter(new ReportResAdapter.ReportResViewListeners() {
            @Override
            public void onClick(String s) {
                binding.recycleView.setVisibility(View.GONE);
                binding.radioButton.setText(s);
                binding.radioButton.setChecked(true);
                binding.radioButton.setEnabled(false);
                binding.layoutMore.setVisibility(View.VISIBLE);
                binding.buttonSendReport.setVisibility(View.VISIBLE);
            }
        }, strings);

        binding.recycleView.setAdapter(reportResAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecorator(getDrawable(R.drawable.divider));
        binding.recycleView.addItemDecoration(itemDecoration);

    }

    private void setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            w.setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void setListeners() {
        binding.textViewClose.setOnClickListener(v -> {
            finish();
        });

        binding.buttonSendReport.setOnClickListener(v -> {
            binding.inputContent.clearFocus();
            if (!binding.inputContent.getText().toString().trim().isEmpty())
                openDialog();
            else {
                CustomToast.makeText(getApplicationContext(), "Vui lòng nhập thông tin thêm", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            }
        });
    }

    AlertDialog alertDialog;

    public void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReportRestaurantActivity.this);
        LayoutNotificationDialogBinding layoutNotificationDialogBinding = LayoutNotificationDialogBinding.inflate(LayoutInflater.from(ReportRestaurantActivity.this));
        builder.setView(layoutNotificationDialogBinding.getRoot());
        builder.setCancelable(false);
        alertDialog = builder.create();

        layoutNotificationDialogBinding.textViewTitle.setText("Báo sai thông tin");
        layoutNotificationDialogBinding.textViewContent.setText("Nội dung bạn báo cáo sẽ được chúng tôi kiểm duyệt & xử lý");

        layoutNotificationDialogBinding.textViewAccept.setOnClickListener(v -> {
            alertDialog.dismiss();
            CustomToast.makeText(getApplicationContext(), "Đã gửi", CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
            finish();

        });

        layoutNotificationDialogBinding.textViewSkip.setOnClickListener(v -> {
            alertDialog.dismiss();
        });
        alertDialog.show();
    }
}
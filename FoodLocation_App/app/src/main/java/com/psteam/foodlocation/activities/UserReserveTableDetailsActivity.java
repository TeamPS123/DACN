package com.psteam.foodlocation.activities;

import static com.psteam.lib.RetrofitClient.getRetrofit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.ActivityUserReserveTableDetailsBinding;
import com.psteam.foodlocation.socket.models.BodySenderFromRes;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.foodlocation.ultilities.CustomToast;
import com.psteam.foodlocation.ultilities.PreferenceManager;
import com.psteam.foodlocation.ultilities.Token;
import com.psteam.lib.Models.reserveTableDetail.messageReserveTable;
import com.psteam.lib.Models.reserveTableDetail.reserveTable;
import com.psteam.lib.Services.ServiceAPI;
import com.psteam.lib.modeluser.GetReserveTableInput;
import com.psteam.lib.modeluser.GetReserveTableSinge;
import com.psteam.lib.modeluser.GetUserReserveTableModel;
import com.psteam.lib.modeluser.ReserveTable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserReserveTableDetailsActivity extends AppCompatActivity {

    ActivityUserReserveTableDetailsBinding binding;
    private BodySenderFromRes response;
    private Token token;
    private PreferenceManager preferenceManager;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserReserveTableDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        token = new Token(getApplicationContext());
        preferenceManager = new PreferenceManager(getApplicationContext());
        userId = preferenceManager.getString(Constants.USER_ID);
        init();
        setListeners();
    }

    private com.psteam.lib.Models.reserveTableDetail.reserveTable reserveTable;

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
//        reserveTable = (reserveTable) bundle.getSerializable("reserveTable");
        reserveTable = (reserveTable) intent.getSerializableExtra("reserveTable");
        loadData();

    }

    private void loadData() {
        if (reserveTable != null) {

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
            if (reserveTable.getStatus() == 0) {
                binding.textViewStatus.setText(String.format("Đang chờ xác nhận"));
            } else if (reserveTable.getStatus() == 1) {
                binding.textViewStatus.setText(String.format("Trạng thái đã được duyệt"));
            } else if (reserveTable.getStatus() == 2) {
                binding.textViewStatus.setText(String.format("Trạng thái bị huỷ"));
            } else if (reserveTable.getStatus() == 3) {
                binding.textViewStatus.setText(String.format("Quá hạn"));
            } else {
                binding.textViewStatus.setText(String.format("Hoàn tất"));
                binding.layoutReview.setVisibility(View.VISIBLE);
            }

            binding.textPhoneRestaurant.setText(reserveTable.getRestaurant().getPhoneRes());

            binding.textViewUserName.setText(reserveTable.getName());
            binding.textViewTimeReserve.setText(formatToYesterdayOrToday(Constants.coverStringToDate(reserveTable.getTime())));
            binding.textViewPhoneNumber.setText(reserveTable.getPhone());

            if (reserveTable.getStatus() != 2) {
                binding.text20.setVisibility(View.GONE);
                binding.inputNote.setVisibility(View.GONE);
            }
        }
    }

    public static String formatToYesterdayOrToday(Date dateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, 1);
        DateFormat timeFormatter = new SimpleDateFormat("dd MMM yyyy");

        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return new SimpleDateFormat("hh:mm").format(dateTime) + ", Hôm nay " + timeFormatter.format(dateTime);
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return new SimpleDateFormat("hh:mm").format(dateTime) + ", Ngày mai " + timeFormatter.format(dateTime);
        } else {
            return new SimpleDateFormat("hh:mm, EEEE dd MMM yyyy").format(dateTime);
        }
    }

    private void setListeners() {
        binding.imageViewClose.setOnClickListener(v -> {
            finish();
        });

        binding.buttonCallPhone.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(
                    getApplicationContext(), Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        UserReserveTableDetailsActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        REQUEST_CODE_PHONE_PERMISSION
                );
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + reserveTable.getRestaurant().getPhoneRes()));
                startActivity(intent);
            }
        });

        binding.buttonDirection.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RestaurantMapActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("getLongLat", reserveTable.getRestaurant().getLongLat());
            intent.putExtras(bundle);
            startActivity(intent);
        });

        binding.buttonReview.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),ReviewActivity.class);
            Bundle bundle =new Bundle();
            bundle.putSerializable("getRestaurant",reserveTable.getRestaurant());
            bundle.putSerializable("getRestaurantId",reserveTable.getRestaurant().getRestaurantId());
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private static final int REQUEST_CODE_PHONE_PERMISSION = 9;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PHONE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + reserveTable.getRestaurant().getPhoneRes()));
                startActivity(intent);
            } else {
                CustomToast.makeText(getApplicationContext(), "Permission denied", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            }
        }
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
            if (response != null)
                GetUserReserveTableModel(userId, response.getReserveTableId());
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
        if (response != null)
            binding.inputNote.setText(response.getNotification());
    }

    private void GetUserReserveTableModel(String userId, String reserveTableId) {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<messageReserveTable> call = serviceAPI.getAllFoodByReserveTableId(token.getToken(), userId, reserveTableId);
        call.enqueue(new Callback<messageReserveTable>() {
            @Override
            public void onResponse(Call<messageReserveTable> call, Response<messageReserveTable> response) {
                if (response.body() != null && response.body().getStatus() == 1) {
                    reserveTable = response.body().getReserveTable();
                    loadData();
                }
            }

            @Override
            public void onFailure(Call<messageReserveTable> call, Throwable t) {

            }
        });
    }
}
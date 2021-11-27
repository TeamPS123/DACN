package com.psteam.foodlocation.activities;

import static com.psteam.lib.RetrofitClient.getRetrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.UserReserveTableAdapter;
import com.psteam.foodlocation.databinding.ActivityUserBookTableHistoryBinding;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.foodlocation.ultilities.DividerItemDecorator;
import com.psteam.foodlocation.ultilities.Para;
import com.psteam.foodlocation.ultilities.PreferenceManager;
import com.psteam.foodlocation.ultilities.Token;
import com.psteam.lib.Services.ServiceAPI;
import com.psteam.lib.modeluser.GetReserveTableInput;
import com.psteam.lib.modeluser.GetUserReserveTableModel;
import com.psteam.lib.modeluser.ReserveTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserBookTableHistoryActivity extends AppCompatActivity implements UserReserveTableAdapter.UserReserveTableListeners {

    private ActivityUserBookTableHistoryBinding binding;
    private ArrayList<ReserveTable> reserveTables;
    private UserReserveTableAdapter userReserveTableAdapter;

    private Token token;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBookTableHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        token = new Token(getApplicationContext());
        preferenceManager = new PreferenceManager(getApplicationContext());
        init();
        setListeners();
    }

    private void init() {
        setFullScreen();
        initUserBookTableAdapter();
        GetUserReserveTableModel(new GetReserveTableInput(preferenceManager.getString(Constants.USER_ID), Para.latitude, Para.longitude));
    }

    private void initUserBookTableAdapter() {
        reserveTables = new ArrayList<>();
        userReserveTableAdapter = new UserReserveTableAdapter(reserveTables, this::onUserReserveTableClicked);
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(UserBookTableHistoryActivity.this, R.anim.layout_animation_left_tp_right);
        binding.recycleView.setLayoutAnimation(layoutAnimationController);
        binding.recycleView.setAdapter(userReserveTableAdapter);
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
            getWindow().setStatusBarColor(ContextCompat.getColor(UserBookTableHistoryActivity.this, R.color.white));// set status background white
        }
    }

    private void GetUserReserveTableModel(GetReserveTableInput getReserveTableInput) {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<GetUserReserveTableModel> call = serviceAPI.GetUserReserveTableModel(token.getToken(), getReserveTableInput);
        call.enqueue(new Callback<GetUserReserveTableModel>() {
            @Override
            public void onResponse(Call<GetUserReserveTableModel> call, Response<GetUserReserveTableModel> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    reserveTables.addAll(response.body().getReserveTables());
                    Collections.reverse(reserveTables);
                    userReserveTableAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<GetUserReserveTableModel> call, Throwable t) {
                Log.d("Log:", t.getMessage());
            }
        });
    }

    @Override
    public void onUserReserveTableClicked(ReserveTable reserveTable) {
        Intent intent = new Intent(getApplicationContext(), UserReserveTableDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("reserveTable", reserveTable);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
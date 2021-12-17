package com.psteam.foodlocation.activities;

import static com.psteam.lib.RetrofitClient.getRetrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.psteam.foodlocation.adapters.CheckInRestaurantAdapter;
import com.psteam.foodlocation.databinding.ActivityCheckInBinding;
import com.psteam.foodlocation.ultilities.Para;
import com.psteam.lib.Services.ServiceAPI;
import com.psteam.lib.modeluser.GetRestaurantBySearch;
import com.psteam.lib.modeluser.GetRestaurantModel;
import com.psteam.lib.modeluser.RestaurantModel;
import com.psteam.lib.modeluser.SearchCheckInModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckInActivity extends AppCompatActivity {

    private ActivityCheckInBinding binding;
    public static final int ResultCodeCheckInActivity = 2310;

    private CheckInRestaurantAdapter checkInRestaurantAdapter;
    private ArrayList<RestaurantModel> restaurantModels;

    private static String AfterTextChange = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
    }

    private void init() {
        restaurantModels = new ArrayList<>();
        initRestaurantCheckInAdapter();
        runThread("", 500);
    }

    private void initRestaurantCheckInAdapter() {
        checkInRestaurantAdapter = new CheckInRestaurantAdapter(new CheckInRestaurantAdapter.CheckInRestaurantListeners() {
            @Override
            public void onClick(RestaurantModel restaurantModel) {
                Intent returnIntent = new Intent();
                Bundle bundle=new Bundle();
                bundle.putSerializable("restaurantModel",restaurantModel);
                returnIntent.putExtras(bundle);
                setResult(ResultCodeCheckInActivity, returnIntent);
                finish();
            }
        }, restaurantModels);
        binding.recycleView.setAdapter(checkInRestaurantAdapter);
    }

    private void setListeners() {
        binding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loading(true);
                runThread(s.toString().trim(), 1000);
            }

            @Override
            public void afterTextChanged(Editable s) {
                AfterTextChange = s.toString().trim();
            }
        });
    }

    private void runThread(String textChange, long millis) {
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(millis);
                    if (textChange.equals(AfterTextChange)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(getApplicationContext(), textChange, Toast.LENGTH_SHORT).show();
                                loading(true);
                                // getRestaurantBySearch(new GetRestaurantBySearch(district, category, textChange, String.valueOf(Para.longitude), String.valueOf(Para.latitude)));
                                getRestaurant(new SearchCheckInModel(textChange, String.valueOf(Para.latitude), String.valueOf(Para.longitude)));
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void getRestaurant(SearchCheckInModel searchCheckInModel) {

        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<GetRestaurantModel> call = serviceAPI.GetResBySearchCheckIn(searchCheckInModel);
        call.enqueue(new Callback<GetRestaurantModel>() {
            @Override
            public void onResponse(Call<GetRestaurantModel> call, Response<GetRestaurantModel> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    if (response.body().getResList().size() > 0) {
                        restaurantModels.clear();
                        restaurantModels.addAll(response.body().getResList());
                        checkInRestaurantAdapter.notifyDataSetChanged();
                    }
                }
                loading(false);
            }

            @Override
            public void onFailure(Call<GetRestaurantModel> call, Throwable t) {
                Log.d("Tag", t.getMessage());
                loading(false);
            }
        });
    }

    public void loading(Boolean Loading) {
        if (Loading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }

}
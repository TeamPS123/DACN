package com.psteam.lib.Service;

import com.psteam.lib.Models.Get.messagePromotion;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.psteam.lib.RetrofitServer.getRetrofit_lib;

public interface ServiceAPI_lib {
    //Header("Authorization") String token,@Query("user") String user
    @GET("getAllPromotion")
    Call<messagePromotion> getAllPromotion();

    @GET("getAllRestaurant")
    Call<messagePromotion> getAllRestaurant();

    @GET("getRestaurant")
    Call<messagePromotion> getRestaurant(@Query("restaurantId") String id);

//    private void get(){
//        ServiceAPI_lib serviceAPI = getRetrofit_lib().create(ServiceAPI_lib.class);
//        Call<String> call = serviceAPI.GetProvinces();
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });
//    }
}


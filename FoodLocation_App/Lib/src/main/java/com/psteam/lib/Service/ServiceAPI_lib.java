package com.psteam.lib.Service;

import com.psteam.lib.Models.Get.messResDetail;
import com.psteam.lib.Models.Get.messagePromotion;
import com.psteam.lib.Models.Get.messageRestaurant;
import com.psteam.lib.Models.Input.confirmTable;
import com.psteam.lib.Models.Input.signIn;
import com.psteam.lib.Models.Insert.insertFood;
import com.psteam.lib.Models.Insert.insertMenu;
import com.psteam.lib.Models.Insert.insertPromotion;
import com.psteam.lib.Models.Insert.insertRestaurant;
import com.psteam.lib.Models.Insert.reserveFood;
import com.psteam.lib.Models.Insert.reserveTable;
import com.psteam.lib.Models.Insert.signUp;
import com.psteam.lib.Models.message;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceAPI_lib {
    @POST("signup")
    Call<message> signup(@Body signUp user);

    @POST("signin")
    Call<message> signin(@Body signIn user);

    @GET("getAllPromotion")
    Call<messagePromotion> getAllPromotion();

    @GET("getAllRestaurant")
    Call<messageRestaurant> getAllRestaurant();

    @GET("getRestaurant")
    Call<messResDetail> getRestaurant(@Query("restaurantId") String id);

    @GET("getResWithPromotion")
    Call<messResDetail> getResWithPromotion(@Query("promotionId") String id);

    @POST("addRestaurant")
    Call<message> addRestaurant(@Header("Authorization") String token, @Body insertRestaurant res);

    @POST("addMenu")
    Call<message> addMenu(@Header("Authorization") String token,@Body insertMenu menu);

    @POST("addFood")
    Call<message> addFood_lib(@Header("Authorization") String token,@Body insertFood foodList);

    @POST("addPromotion")
    Call<message> addPromotion(@Header("Authorization") String token,@Body insertPromotion foodList);

    @POST("reserveTable")
    Call<message> reserveTable(@Header("Authorization") String token,@Body reserveTable table);

    @POST("reserveFood")
    Call<message> reserveFood(@Header("Authorization") String token,@Body reserveFood foodList);

    @POST("confirmTable")
    Call<message> confirmTable(@Header("Authorization") String token,@Body confirmTable input);



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


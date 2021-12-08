package com.psteam.lib.Service;

import com.psteam.lib.Models.Get.messResDetail;
import com.psteam.lib.Models.Get.messageAllCategory;
import com.psteam.lib.Models.Get.messageAllMenu;
import com.psteam.lib.Models.Get.messageAllReserveTable;
import com.psteam.lib.Models.Get.messagePromotion;
import com.psteam.lib.Models.Get.messageRestaurant;
import com.psteam.lib.Models.Input.confirmTable;
import com.psteam.lib.Models.Input.signIn;
import com.psteam.lib.Models.Insert.insertCategory;
import com.psteam.lib.Models.Insert.insertFood;
import com.psteam.lib.Models.Insert.insertFoods;
import com.psteam.lib.Models.Insert.insertMenu;
import com.psteam.lib.Models.Insert.insertPromotion;
import com.psteam.lib.Models.Insert.insertRestaurant;
import com.psteam.lib.Models.Insert.reserveFood;
import com.psteam.lib.Models.Insert.reserveTable;
import com.psteam.lib.Models.Insert.signUp;
import com.psteam.lib.Models.message;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ServiceAPI_lib {
    //Header("Authorization") String token,@Query("user") String user
    @POST("signup")
    Call<message> signup(@Body signUp user);

    @POST("signin")
    Call<message> signin(@Body signIn user);

    @GET("getAllPromotion")
    Call<messagePromotion> getAllPromotion();

    @GET("getRestaurantId")
    Call<message> getRestaurantId(@Header("Authorization") String token, @Query("userId") String userId);

    @GET("getAllMenuByResId")
    Call<messageAllMenu> getAllMenu(@Header("Authorization") String token, @Query("userId") String userId, @Query("restaurantId") String restaurantId);

    @GET("getAllCategory")
    Call<messageAllCategory> getAllCategory(@Header("Authorization") String token, @Query("userId") String userId, @Query("restaurantId") String restaurantId);

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

    @POST("addFoods")
    Call<message> addFood_lib(@Header("Authorization") String token,@Body insertFoods foodList);

    @POST("addFood")
    Call<message> addFood(@Header("Authorization") String token,@Body insertFood foodList);

    @POST("addPromotion")
    Call<message> addPromotion(@Header("Authorization") String token,@Body insertPromotion promotion);

    @POST("addCategory")
    Call<message> addCategory(@Header("Authorization") String token,@Body insertCategory category);

    @POST("reserveTable")
    Call<message> reserveTable(@Header("Authorization") String token,@Body reserveTable table);

    @POST("reserveFood")
    Call<message> reserveFood(@Header("Authorization") String token,@Body reserveFood foodList);

    @POST("confirmTable")
    Call<message> confirmTable(@Header("Authorization") String token,@Body confirmTable input);

    @Multipart
    @POST("upImageOfRes")
    Call<message> addImgRes(@Header("Authorization") String token, @Part List<MultipartBody.Part> photo, @Query("userId") String userId, @Query("restaurantId") String restaurantId);

    @Multipart
    @POST("upImageOfFood")
    Call<message> addImgFood(@Header("Authorization") String token, @Part List<MultipartBody.Part> photo, @Query("userId") String userId, @Query("restaurantId") String restaurantId, @Query("foodId") String foodId);

    @GET("getAllReserveTableByRestaurantId")
    Call<messageAllReserveTable> getAllReserveTables(@Header("Authorization") String token, @Query("userId") String userId, @Query("restaurantId") String restaurantId, @Query("code") int code);

    @GET("updateReserveTable")
    Call<message> updateReserveTable(@Header("Authorization") String token, @Query("userId") String userId, @Query("reserveTableId") String reserveTableId, @Query("code") int code);

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

package com.psteam.lib.Services;

import com.psteam.lib.Models.reserveTableDetail.messageReserveTable;
import com.psteam.lib.modeluser.CreateReviewModel;
import com.psteam.lib.modeluser.GetCategoryResModel;
import com.psteam.lib.modeluser.GetMenuResModel;
import com.psteam.lib.modeluser.GetResInfo;
import com.psteam.lib.modeluser.GetReserveTableInput;
import com.psteam.lib.modeluser.GetReserveTableSinge;
import com.psteam.lib.modeluser.GetRestaurantByDistance;
import com.psteam.lib.modeluser.GetRestaurantBySearch;
import com.psteam.lib.modeluser.GetRestaurantModel;
import com.psteam.lib.modeluser.GetReviewModel;
import com.psteam.lib.modeluser.GetReviewRestaurantModel;
import com.psteam.lib.modeluser.GetUserReserveTableModel;
import com.psteam.lib.modeluser.InputSuggestRes;
import com.psteam.lib.modeluser.InsertRateModel;
import com.psteam.lib.modeluser.InsertReserveFoodModel;
import com.psteam.lib.modeluser.InsertReserveTableModel;

import com.psteam.lib.modeluser.ChangeInfoModel;
import com.psteam.lib.modeluser.GetInfoUser;
import com.psteam.lib.modeluser.LogUpModel;

import com.psteam.lib.modeluser.LoginModel;
import com.psteam.lib.modeluser.SearchCheckInModel;
import com.psteam.lib.modeluser.message;


import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

import retrofit2.http.Part;
import retrofit2.http.Path;

import retrofit2.http.Query;

public interface ServiceAPI {

    @POST("api/signin")
    Call<message> SignIn(@Body LoginModel loginModel);

    @GET("api/getAllCategoryRes")
    Call<GetCategoryResModel> GetCategoryRes();

    @POST("api/getRes_Distance")
    Call<GetRestaurantModel> GetResByDistance(@Body GetRestaurantByDistance getRestaurantByDistance);

    @POST("api/getResWithSupperSearch")
    Call<GetRestaurantModel> GetResBySearch(@Body GetRestaurantBySearch getRestaurantBySearch);

    @POST("api/getResWithNameOrAdd")
    Call<GetRestaurantModel> GetResBySearchCheckIn(@Body SearchCheckInModel searchCheckInModel);

    @POST("api/reserveTable")
    Call<message> ReserveTable(@Header("Authorization") String token, @Body InsertReserveTableModel insertReserveTableModel);

    @POST("api/reserveFood")
    Call<message> ReserveFood(@Header("Authorization") String token, @Body InsertReserveFoodModel insertReserveFoodModel);

    @GET("api/getAllMenuWithRes")
    Call<GetMenuResModel> GetMenuRes(@Query("restaurantId") String restaurantId);

    @POST("api/signup")
    Call<message> SignUp(@Body LogUpModel logUpModel);

    @GET("api/getInfo")
    Call<GetInfoUser> GetDetailUser(@Header("Authorization") String token, @Query("userId") String userId);

    @POST("api/updateInfo")
    Call<message> ChangeInfoUser(@Header("Authorization") String token, @Body ChangeInfoModel user);

    @POST("api/getAllReserverTable")
    Call<GetUserReserveTableModel> GetUserReserveTableModel(@Header("Authorization") String token, @Body GetReserveTableInput getReserveTableInput);

    @POST("api/getAllFoodByReserveTableId")
    Call<GetReserveTableSinge> GetReserveTableSinge(@Header("Authorization") String token, @Query("userId") String userId, @Query("reserveTableId") String reserveTableId);

    @GET("api/getAllFoodByReserveTableId")
    Call<messageReserveTable> getAllFoodByReserveTableId(@Header("Authorization") String token, @Query("userId") String userId, @Query("reserveTableId") String reserveTableId);

    @GET("api/getAllRateRes")
    Call<GetReviewModel> getReview(@Query("restaurantId") String restaurantId, @Query("value") int value, @Query("skip") int skip, @Query("take") int take);

    @POST("api/ratingRes")
    Call<message> insertRate(@Header("Authorization") String token, @Body InsertRateModel insertRateModel);

    @Multipart
    @POST("api/upImageOfRate")
    Call<message> addImgRate(@Header("Authorization") String token, @Part List<MultipartBody.Part> photo, @Query("userId") String userId, @Query("rateId") String rateId);

    @POST("api/review")
    Call<message> insertReview(@Header("Authorization") String token, @Body CreateReviewModel createReviewModel);

    @GET("api/getAllReviewRes")
    Call<GetReviewRestaurantModel> GetReviewRestaurant(@Query("skip") int skip, @Query("take") int take);

    @Multipart
    @POST("api/upImageOfReview")
    Call<message> addImgReview(@Header("Authorization") String token, @Part List<MultipartBody.Part> photo, @Query("userId") String userId, @Query("reviewId") String reviewId);

    @GET("api/getRestaurant")
    Call<GetResInfo> GetResInfo(@Query("restaurantId") String restaurantId);

    @Multipart
    @POST("api/upImageOfUser")
    Call<message> addImgUser(@Header("Authorization") String token, @Part MultipartBody.Part photo, @Query("userId") String userId);

    @GET("api/checkPhone")
    Call<message> checkPhoneNumber(@Query("phone") String phone);

    @GET("api/addComment")
    Call<message> insertComment(@Header("Authorization") String token, @Query("userId") String userId, @Query("reviewId") String reviewId, @Query("content") String content, @Query("date") String date);

    @GET("api/likeReview")
    Call<message> likeOrDislike(@Header("Authorization") String token, @Query("userId") String userId, @Query("reviewId") String reviewId);

    @POST("api/getRes_Suggest")
    Call<GetRestaurantModel> GetSuggestRes(@Body InputSuggestRes inputSuggestRes);

}

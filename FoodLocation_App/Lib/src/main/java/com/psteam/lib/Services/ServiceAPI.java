package com.psteam.lib.Services;

import com.psteam.lib.modeluser.ChangeInfoModel;
import com.psteam.lib.modeluser.GetInfoUser;
import com.psteam.lib.modeluser.LogUpModel;
import com.psteam.lib.modeluser.LoginModel;
import com.psteam.lib.modeluser.message;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceAPI {

    @POST("api/signin")
    Call<message> SignIn(@Body LoginModel loginModel);

    @POST("api/signup")
    Call<message> SignUp(@Body LogUpModel logUpModel);

    @GET("api/getInfo")
    Call<GetInfoUser> GetDetailUser(@Header("Authorization") String token, @Query("userId") String userId);
    @POST("api/updateInfo")
    Call<message> ChangeInfoUser(@Header("Authorization") String token,@Body ChangeInfoModel user);
}

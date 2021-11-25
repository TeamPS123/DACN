package com.psteam.lib.Services;

import com.psteam.lib.modeluser.LoginModel;
import com.psteam.lib.modeluser.message;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceAPI {

    @POST("api/signin")
    Call<message> SignIn(@Body LoginModel loginModel);

    @POST("api/signup")
    Call<message> SignUp(@Body LoginModel loginModel);

}

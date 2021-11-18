package com.psteam.lib.Services;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceAPI {

    @GET("api/p/")
    Call<ArrayList<String>> GetProvinces();


}

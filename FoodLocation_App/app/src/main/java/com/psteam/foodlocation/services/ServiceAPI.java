package com.psteam.foodlocation.services;

import com.psteam.foodlocation.models.DistrictModel;
import com.psteam.foodlocation.models.ProvinceModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ServiceAPI {
    @GET("api/p/")
    Call<ArrayList<ProvinceModel>> GetProvinces();

    @GET("api/p/{code}?depth=2")
    Call<ProvinceModel> GetDistricts(@Path("code") String code);

    @GET("api/d/{code}?depth=2")
    Call<DistrictModel> GetWards(@Path("code") String code);
}

package com.example.services;

import com.example.objects.Message;

import io.reactivex.Observable;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadApi {
    String BASE_Service = "https://api.covid21tsp.space/api/";

    @Multipart
    @POST("uploadFile")
    Observable<Message> Upload(@Part MultipartBody.Part photo, @Part("des") RequestBody des);
}

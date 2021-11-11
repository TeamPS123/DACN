package com.example.services;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private static Retrofit retrofit = null;

    public static  Retrofit getApi(){
//        OkHttpClient client = new OkHttpClient.Builder().addInterceptor((chain) ->{
//                    Request originalRequest = chain.request();
//                    Request.Builder builder = originalRequest.newBuilder().header
//                            ("Authorization", Credentials.basic("acc2", "123"));
//                    Request newRequest = builder.build();
//                    return chain.proceed(newRequest);
//                }).build();
//        retrofit = new Retrofit.Builder()
//                .baseUrl("https://api.covid21tsp.space/api/uploadFile")
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
//                .build();
//        return retrofit;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.covid21tsp.space/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }
}

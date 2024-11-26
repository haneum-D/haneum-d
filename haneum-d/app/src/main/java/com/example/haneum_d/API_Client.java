package com.example.haneum_d;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/* API Client */
public class API_Client {

    private static Retrofit retrofit = null;


    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build();


    static Retrofit getClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL) // local.properties
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}


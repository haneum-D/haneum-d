package com.example.haneum_d;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/* API Client */
public class API_Client {

    private static Retrofit retrofit = null;

    static Retrofit getClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl("API_URL")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}

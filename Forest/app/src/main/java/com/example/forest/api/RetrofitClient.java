package com.example.forest.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitClient {
    private static Retrofit retrofit=null;
    public static ForestApi getApi()
    {
        if (retrofit==null){
            retrofit=new Retrofit.Builder()
                    .baseUrl("https://forestbackend.onrender.com/").addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit.create(ForestApi.class);
    }
}

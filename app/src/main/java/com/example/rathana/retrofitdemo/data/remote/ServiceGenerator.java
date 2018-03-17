package com.example.rathana.retrofitdemo.data.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RATHANA on 3/11/2018.
 */

public class ServiceGenerator {
    private static final String BASE_API_URL="http://api-ams.me";
    private static Retrofit builder = new Retrofit.Builder()
                                    .baseUrl(BASE_API_URL)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

    public static  <S> S generateService(Class<S> service){
        return builder.create(service);
    }
}

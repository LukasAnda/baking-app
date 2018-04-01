package com.lukasanda.bakingapp.api;

import android.provider.SyncStateContract;

import com.google.gson.GsonBuilder;
import com.lukasanda.bakingapp.utils.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lukas on 3/13/2018.
 */

public class ApiClient {
    private static Retrofit retrofit = null;
    
    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder().build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                    .build();
        }
        return retrofit;
    }
}

package com.lukasanda.bakingapp.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.lukasanda.bakingapp.api.ApiClient;
import com.lukasanda.bakingapp.api.ApiInterface;
import com.lukasanda.bakingapp.data.Recipe;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lukas on 3/13/2018.
 */

public class NetworkUtils {
    public interface RecipesRequestCallback {
        void onRecipesDownloaded(List<Recipe> recipes);
    }
    
    public interface ErrorCallback {
        void onError();
    }
    
    public static void fetchRecipes(final RecipesRequestCallback callback,
                                     final ErrorCallback errorCallback) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        final Call<List<Recipe>> recipesCall = apiInterface.getRecipes();
        
        recipesCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull
                    Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    if (callback != null)
                        callback.onRecipesDownloaded(response.body());
                } else if (errorCallback != null) errorCallback.onError();
                
            }
            
            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                if (errorCallback != null) errorCallback.onError();
            }
        });
    }
    
    public static boolean isImageURL(String url){
        if(TextUtils.isEmpty(url)) return false;
        //For the sake of simplicity, let's just expect .jpg .png and .jpeg
        String end = url.substring(url.lastIndexOf(".") + 1);
        return end.equalsIgnoreCase("jpg") || end.equalsIgnoreCase("png") || end.equalsIgnoreCase("jpeg");
    }
    
    public static boolean isVideoURL(String url){
        if(TextUtils.isEmpty(url)) return false;
        //I expect only .mp4 for video, but this could be done much more nicely
        String end = url.substring(url.lastIndexOf(".") + 1);
        return end.equalsIgnoreCase("mp4");
    }
}

package com.lukasanda.bakingapp.api;

import com.lukasanda.bakingapp.data.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by lukas on 3/13/2018.
 */

public interface ApiInterface {
    
    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();
}

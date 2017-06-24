package com.bamideleoguntuga.bakingapp.api;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by delaroy on 6/16/17.
 */
public interface Service {

    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<JsonArray> readRecipeArray();

}

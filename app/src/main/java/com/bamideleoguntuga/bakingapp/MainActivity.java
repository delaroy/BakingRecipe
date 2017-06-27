package com.bamideleoguntuga.bakingapp;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;


import com.bamideleoguntuga.bakingapp.adapter.RecipeAdapter;
import com.bamideleoguntuga.bakingapp.adapter.TestAdapter;
import com.bamideleoguntuga.bakingapp.api.Client;
import com.bamideleoguntuga.bakingapp.api.Service;
import com.bamideleoguntuga.bakingapp.model.Recipe;
import com.bamideleoguntuga.bakingapp.sync.RecipeSyncUtils;
import com.bamideleoguntuga.bakingapp.utilities.JsonUtils;
import com.bamideleoguntuga.bakingapp.utilities.RecipeTestDownloader;
import com.bamideleoguntuga.bakingapp.utilities.SimpleIdlingResource;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements  RecipeTestDownloader.DelayerCallback  {

    private List<Recipe> recipes = new ArrayList<>();
    private static Context mContext;
    private RecyclerView recyclerView;

    @Nullable
    private SimpleIdlingResource mIdlingResource;


    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        setTitle(R.string.app_name);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        boolean isPhone = getResources().getBoolean(R.bool.is_phone);

        if (isPhone) {

            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
        } else {

            recyclerView.setHasFixedSize(true);
            if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            }

        }


        RecipeSyncUtils.initialize(this);
        loadJSON();

        mContext = this;
        RecipeSyncUtils.initialize(this);
        getIdlingResource();

        //For testing the recipe collection sorting alphabetically
        TestAdapter testAdapter = new TestAdapter(LayoutInflater.from(this));
        recyclerView.setAdapter(testAdapter);
        testAdapter.setRecipe(recipes);

    }

    public Activity getActivity(){
        Context context = this;
        while (context instanceof ContextWrapper){
            if (context instanceof Activity){
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;

    }

    private void loadJSON(){

        Service serviceAPI = Client.getClient();
        Call<JsonArray> loadRecipeCall = serviceAPI.readRecipeArray();

        loadRecipeCall.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                try {

                    String recipeString = response.body().toString();

                    Type listType = new TypeToken<List<Recipe>>() {}.getType();
                    recipes = JsonUtils.getRecipeListFromJson(recipeString, listType);
                    recyclerView.setAdapter(new RecipeAdapter(getApplicationContext(), recipes));

                    //sorting recipe in alphabetical order which UI test was done upon
                    Collections.sort(recipes, Recipe.BY_NAME_ALPHABETICAL);


                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

                Log.d("onFailure", t.toString());

            }


        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        RecipeTestDownloader.downloadRecipe(this, MainActivity.this, mIdlingResource);
        loadJSON();
    }

    @Override
    public void onDone(ArrayList<Recipe> recipes) {

    }
}

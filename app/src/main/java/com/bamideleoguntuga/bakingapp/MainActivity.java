package com.bamideleoguntuga.bakingapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.bamideleoguntuga.bakingapp.adapter.RecipeAdapter;
import com.bamideleoguntuga.bakingapp.api.Client;
import com.bamideleoguntuga.bakingapp.api.Service;
import com.bamideleoguntuga.bakingapp.data.Contract;
import com.bamideleoguntuga.bakingapp.data.RecipeDBHelper;
import com.bamideleoguntuga.bakingapp.model.Recipe;
import com.bamideleoguntuga.bakingapp.recipewidget.RecipeWidget;
import com.bamideleoguntuga.bakingapp.utility.SimpleIdlingResource;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    ProgressDialog pd;
    List<Recipe> recipeList;
    private static Context mContext;

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

        initViews();
        mContext = this;
        getIdlingResource();
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

    private void initViews(){

        boolean isPhone = getResources().getBoolean(R.bool.is_phone);

        if (isPhone) {

            recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
        } else {

            recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

            recyclerView.setHasFixedSize(true);
            if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            }

        }


        loadJSON();

    }

    private void loadJSON(){

        Service serviceAPI = Client.getClient();
        Call<JsonArray> loadRecipeCall = serviceAPI.readRecipeArray();

        loadRecipeCall.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                try {

                    String recipeString = response.body().toString();
                   // Log.i("onResponse", recipeString);
                    Type listType = new TypeToken<List<Recipe>>() {}.getType();
                    recipeList = new Gson().fromJson(recipeString, listType);
                   // Log.i("onResponse", recipeList.toString());
                    for(int i = 0; i < recipeList.size(); i++){
                        recipeList.get(i).getName();
                    }

                    recyclerView.setAdapter(new RecipeAdapter(getApplicationContext(), recipeList));
                    recyclerView.smoothScrollToPosition(0);

                    for(Recipe recipe : recipeList) {
                        addRecipe(recipe.getName());
                        System.out.println(recipe.getName().toString());
                       // Log.d("Recipe :", recipe.getName());
                    }
                    RecipeWidget.sendRefreshBroadcast(mContext);



                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            public void addRecipe(final String s) {

                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {

                        ContentValues values = new ContentValues();
                        values.put(Contract.COLUMN_RECIPENAME, s);

                        final Uri uri = mContext.getContentResolver().insert(Contract.PATH_RECIPE_URI, values);

                        MainActivity a = (MainActivity) mContext;
                        if(uri != null) {
                            a.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecipeWidget.sendRefreshBroadcast(mContext);
                                }
                            });
                        } else {
                            a.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext, "Something went wrong, task cannot be created.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        return null;

                    }
                }.execute();
            }


            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

                Log.d("onFailure", t.toString());

            }


        });
    }


}

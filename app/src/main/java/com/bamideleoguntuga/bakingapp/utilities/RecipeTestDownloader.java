package com.bamideleoguntuga.bakingapp.utilities;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.bamideleoguntuga.bakingapp.R;
import com.bamideleoguntuga.bakingapp.model.Ingredient;
import com.bamideleoguntuga.bakingapp.model.Recipe;
import com.bamideleoguntuga.bakingapp.model.Step;

import java.util.ArrayList;



public class RecipeTestDownloader {

    private static final int DELAY_MILLIS = 3000;

    // Create an ArrayList of mTeas
    final static ArrayList<Recipe> mRecipes = new ArrayList<>();

    public interface DelayerCallback {
        void onDone(ArrayList<Recipe> recipes);
    }


    public static void downloadRecipe(Context context, final DelayerCallback callback,
                                      @Nullable final SimpleIdlingResource idlingResource) {

        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }


        String text = context.getString(R.string.loading_msg);
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        Recipe recipe;
        Ingredient ingredient;
        Step step;
        ArrayList<Ingredient> ingredientList = new ArrayList<>();
        ArrayList<Step> stepList = new ArrayList<>();


        for (int i = 1; i < 3; i++) {
            recipe = new Recipe(i, "Recipe Name" + i, 8, "Image" + i);
            step = new Step(i, "Short Desc" + i, "Desc" + i, "video", "");
            ingredient = new Ingredient((double) (i / 5), "Measure", "Ingredient");
            ingredientList.add(ingredient);
            stepList.add(step);
            recipe.setSteps(stepList);
            recipe.setIngredients(ingredientList);
            mRecipes.add(recipe);
        }


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onDone(mRecipes);
                    if (idlingResource != null) {
                        idlingResource.setIdleState(true);
                    }
                }
            }
        }, DELAY_MILLIS);
    }
}

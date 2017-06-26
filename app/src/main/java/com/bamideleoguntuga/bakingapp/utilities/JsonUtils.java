package com.bamideleoguntuga.bakingapp.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.bamideleoguntuga.bakingapp.model.Ingredient;
import com.bamideleoguntuga.bakingapp.model.Recipe;
import com.bamideleoguntuga.bakingapp.model.RecipeContentValues;
import com.bamideleoguntuga.bakingapp.model.Step;
import com.bamideleoguntuga.bakingapp.provider.RecipeContract;
import com.bamideleoguntuga.bakingapp.provider.RecipeProvider;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



public final class JsonUtils {

    public static <T> List<T> getRecipeListFromJson(String jsonString, Type type) {
        if (!isValid(jsonString)) {
            return null;
        }
        return new Gson().fromJson(jsonString, type);
    }


    public static boolean isValid(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonSyntaxException jse) {
            return false;
        }
    }

    public static RecipeContentValues getRecipeContentValues(List<Recipe> recipes) {

        ContentValues[] recipeValues = new ContentValues[recipes.size()];

        ArrayList<ContentValues> stepList = new ArrayList<>();
        ArrayList<ContentValues> ingredientList = new ArrayList<>();

        ContentValues values;
        Integer recipeId;

        for (int i = 0; i < recipes.size(); i++) {

            recipeId = recipes.get(i).getId();

            values = new ContentValues();
            values.put(RecipeContract.RecipeEntry.COLUMN_ID, recipeId);
            values.put(RecipeContract.RecipeEntry.COLUMN_NAME, recipes.get(i).getName());
            values.put(RecipeContract.RecipeEntry.COLUMN_IMAGE, recipes.get(i).getImage());
            values.put(RecipeContract.RecipeEntry.COLUMN_SERVINGS, recipes.get(i).getServings());

            recipeValues[i] = values;


            List<Step> steps = recipes.get(i).getSteps();

            for (int j = 0; j < steps.size(); j++) {
                values = new ContentValues();
                values.put(RecipeContract.StepEntry.COLUMN_RECIPE_ID, recipeId);
                values.put(RecipeContract.StepEntry.COLUMN_SHORT_DESC, steps.get(j).getShortDescription());
                values.put(RecipeContract.StepEntry.COLUMN_DESC, steps.get(j).getDescription());
                values.put(RecipeContract.StepEntry.COLUMN_VIDEO, steps.get(j).getVideoURL());
                values.put(RecipeContract.StepEntry.COLUMN_THUMBNAIL, steps.get(j).getThumbnailURL());
                stepList.add(values);
            }

            List<Ingredient> ingredients = recipes.get(i).getIngredients();

            for (int k = 0; k < ingredients.size(); k++) {
                values = new ContentValues();
                values.put(RecipeContract.IngredientEntry.COLUMN_RECIPE_ID, recipeId);
                values.put(RecipeContract.IngredientEntry.COLUMN_INGREDIENT, ingredients.get(k).getIngredient());
                values.put(RecipeContract.IngredientEntry.COLUMN_MEASURE, ingredients.get(k).getMeasure());
                values.put(RecipeContract.IngredientEntry.COLUMN_QUANTITY, ingredients.get(k).getQuantity());
                ingredientList.add(values);
            }
        }

        ContentValues[] stepValues = new ContentValues[stepList.size()];
        stepValues = stepList.toArray(stepValues);

        ContentValues[] ingredientValues = new ContentValues[ingredientList.size()];
        ingredientValues = ingredientList.toArray(ingredientValues);

        RecipeContentValues rcv = new RecipeContentValues();
        rcv.setRecipes(recipeValues);
        rcv.setIngredients(ingredientValues);
        rcv.setSteps(stepValues);

        return rcv;
    }

    public static ArrayList<Recipe> getRecipeListFromCursor(Cursor recipeCursor, Context context) {

        ArrayList<Recipe> recipes = new ArrayList<>();

        int idIndex = recipeCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_ID);
        int imageIndex = recipeCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_IMAGE);
        int nameIndex = recipeCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_NAME);
        int servingIndex = recipeCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_SERVINGS);

        Recipe recipe;

        while (recipeCursor.moveToNext()) {
            recipe = new Recipe(
                    recipeCursor.getInt(idIndex),
                    recipeCursor.getString(nameIndex),
                    recipeCursor.getInt(servingIndex),
                    recipeCursor.getString(imageIndex)
            );

            Cursor ingredientCursor = context.getContentResolver().query(
                    RecipeProvider.RecipeIngredients.withId(recipe.getId()),
                    null,
                    null,
                    null,
                    RecipeContract.IngredientEntry.COLUMN_ID
            );

            ArrayList<Ingredient> ingredientListFromCursor = getIngredientListFromCursor(ingredientCursor);
            recipe.setIngredients(ingredientListFromCursor);

            Cursor stepCursor = context.getContentResolver().query(
                    RecipeProvider.RecipeSteps.withId(recipe.getId()),
                    null,
                    null,
                    null,
                    RecipeContract.IngredientEntry.COLUMN_ID
            );

            ArrayList<Step> stepArrayListFromCursor = getStepListFromCursor(stepCursor);
            recipe.setSteps(stepArrayListFromCursor);


            recipes.add(recipe);
        }
        recipeCursor.close();
        return recipes;

    }


    public static ArrayList<Step> getStepListFromCursor(Cursor stepCursor) {

        ArrayList<Step> steps = new ArrayList<>();

        int idIndex = stepCursor.getColumnIndex(RecipeContract.StepEntry.COLUMN_ID);
        int shortDescIndex = stepCursor.getColumnIndex(RecipeContract.StepEntry.COLUMN_SHORT_DESC);
        int descIndex = stepCursor.getColumnIndex(RecipeContract.StepEntry.COLUMN_DESC);
        int videoIndex = stepCursor.getColumnIndex(RecipeContract.StepEntry.COLUMN_VIDEO);
        int thumbnailIndex = stepCursor.getColumnIndex(RecipeContract.StepEntry.COLUMN_THUMBNAIL);

        Step step;

        while (stepCursor.moveToNext()) {
            step = new Step(
                    stepCursor.getInt(idIndex),
                    stepCursor.getString(shortDescIndex),
                    stepCursor.getString(descIndex),
                    stepCursor.getString(videoIndex),
                    stepCursor.getString(thumbnailIndex)
            );

            steps.add(step);
        }
        stepCursor.close();
        return steps;
    }

    public static ArrayList<Ingredient> getIngredientListFromCursor(Cursor ingredientCursor) {

        ArrayList<Ingredient> ingredients = new ArrayList<>();

        int ingredientIndex = ingredientCursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_INGREDIENT);
        int measureIndex = ingredientCursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_MEASURE);
        int quantityIndex = ingredientCursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_QUANTITY);

        Ingredient ingredient;

        while (ingredientCursor.moveToNext()) {
            ingredient = new Ingredient(
                    ingredientCursor.getDouble(quantityIndex),
                    ingredientCursor.getString(measureIndex),
                    ingredientCursor.getString(ingredientIndex)
            );

            ingredients.add(ingredient);
        }
        ingredientCursor.close();
        return ingredients;
    }

}


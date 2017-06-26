package com.bamideleoguntuga.bakingapp.provider;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;


@ContentProvider(
        authority = RecipeProvider.AUTHORITY,
        database = RecipeDatabase.class)
public class RecipeProvider {
    public static final String AUTHORITY = "com.bamideleoguntuga.bakingapp.provider.provider";

    @TableEndpoint(table = RecipeDatabase.RECIPE_INGREDIENTS)
    public static class RecipeIngredients {

        @ContentUri(
                path = "ingredients",
                type = "vnd.android.cursor.dir/ingredients",
                defaultSort = RecipeContract.IngredientEntry.COLUMN_ID + " DESC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/ingredients");

        @InexactContentUri(
                path = "ingredients" + "/#",
                name = "INGREDIENT_RECIPE_ID",
                type = "vnd.android.cursor.item/ingredients",
                whereColumn = RecipeContract.IngredientEntry.COLUMN_RECIPE_ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return Uri.parse("content://" + AUTHORITY + "/ingredients/" + id);
        }

    }

    @TableEndpoint(table = RecipeDatabase.RECIPE_STEPS)
    public static class RecipeSteps {

        @ContentUri(
                path = "steps",
                type = "vnd.android.cursor.dir/steps",
                defaultSort = RecipeContract.StepEntry.COLUMN_ID + " DESC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/steps");

        @InexactContentUri(
                path = "steps" + "/#",
                name = "STEP_RECIPE_ID",
                type = "vnd.android.cursor.item/steps",
                whereColumn = RecipeContract.StepEntry.COLUMN_RECIPE_ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return Uri.parse("content://" + AUTHORITY + "/steps/" + id);
        }
    }

    @TableEndpoint(table = RecipeDatabase.RECIPES)
    public static class Recipes {

        @ContentUri(
                path = "recipes",
                type = "vnd.android.cursor.dir/recipes",
                defaultSort = RecipeContract.RecipeEntry.COLUMN_ID + " DESC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/recipes");


        @InexactContentUri(
                path = "recipes" + "/#",
                name = "RECIPE_ID",
                type = "vnd.android.cursor.item/recipes",
                whereColumn = RecipeContract.RecipeEntry.COLUMN_ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return Uri.parse("content://" + AUTHORITY + "/recipes/" + id);
        }
    }
}


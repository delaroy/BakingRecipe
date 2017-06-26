package com.bamideleoguntuga.bakingapp.provider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;


@Database(version = RecipeDatabase.VERSION)
public class RecipeDatabase {

    public static final int VERSION = 1;

    @Table(RecipeContract.IngredientEntry.class)
    public static final String RECIPE_INGREDIENTS = "ingredients";

    @Table(RecipeContract.StepEntry.class)
    public static final String RECIPE_STEPS = "steps";

    @Table(RecipeContract.RecipeEntry.class)
    public static final String RECIPES = "recipes";
}
package com.bamideleoguntuga.bakingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by delaroy on 6/24/17.
 */
public class Contract implements BaseColumns {

    public static final String DATABASE_NAME = "RECIPE.DB";

    public static final String TABLE_NAME = "RECIPES";
    public static final String COLUMN_RECIPENAME = "RECIPENAME";
    public static final String COL_RECIPE_TEXT = "RECIPE";

    public static final String SCHEMA = "content://";
    public static final String AUTHORITY = "com.bamideleoguntuga.bakingapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEMA + AUTHORITY);
    public static final String PATH_TORECIPE = "recipespath";
    public static final Uri PATH_RECIPE_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TORECIPE).build();

}

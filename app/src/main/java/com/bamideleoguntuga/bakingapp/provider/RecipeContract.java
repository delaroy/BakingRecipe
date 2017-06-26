package com.bamideleoguntuga.bakingapp.provider;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;



public class RecipeContract {

    public static final class IngredientEntry {

        @DataType(DataType.Type.INTEGER)
        @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
        @AutoIncrement
        public static final String COLUMN_ID = "_id";

        @DataType(DataType.Type.INTEGER)
        @NotNull
        public static final String COLUMN_RECIPE_ID = "recipe_id";

        @DataType(DataType.Type.REAL)
        @NotNull
        public static final String COLUMN_QUANTITY = "quantity";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String COLUMN_MEASURE = "measure";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String COLUMN_INGREDIENT = "ingredient";

    }

    public static final class StepEntry {

        @DataType(DataType.Type.INTEGER)
        @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
        @AutoIncrement
        public static final String COLUMN_ID = "_id";

        @DataType(DataType.Type.INTEGER)
        @NotNull
        public static final String COLUMN_RECIPE_ID = "recipe_id";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String COLUMN_SHORT_DESC = "short_desc";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String COLUMN_DESC = "desc";

        @DataType(DataType.Type.TEXT)
        public static final String COLUMN_VIDEO = "video_url";

        @DataType(DataType.Type.TEXT)
        public static final String COLUMN_THUMBNAIL = "thumb_url";

    }

    public static final class RecipeEntry {

        @DataType(DataType.Type.INTEGER)
        @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
        public static final String COLUMN_ID = "_id";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String COLUMN_NAME = "recipe_name";

        @DataType(DataType.Type.INTEGER)
        @NotNull
        public static final String COLUMN_SERVINGS = "servings";

        @DataType(DataType.Type.TEXT)
        public static final String COLUMN_IMAGE = "image";
    }


}
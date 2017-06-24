package com.bamideleoguntuga.bakingapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by delaroy on 6/24/17.
 */
public class RecipeDBHelper extends SQLiteOpenHelper {

   // private static String columnid = "ID";
   // private static String columnname = "recipeName";
    //private static String tableName = "Recipes";

   // private static final String DATABASE_NAME = "RECIPE.DB";
    private static final int DATABASE_VERSION = 1;
    SQLiteDatabase db = this.getWritableDatabase();

   // public static final String CREATE_QUERY =
           // "CREATE TABLE " + tableName + " ( " + columnid + " INTEGER PRIMARY KEY, " + columnname + " TEXT NOT NULL" + ");";

    public RecipeDBHelper(Context context) {
        super(context, Contract.DATABASE_NAME, null, DATABASE_VERSION);
        Log.e("Database operations", "Database created or opened...");
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_QUERY =
                "CREATE TABLE " + Contract.TABLE_NAME + "(" + Contract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Contract.COLUMN_RECIPENAME + " TEXT NOT NULL UNIQUE" + ");";


        sqLiteDatabase.execSQL(CREATE_QUERY);
        Log.e("Database operations", "Database created...");

    }

    public void addRecipe( String recipe, SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.COLUMN_RECIPENAME, recipe);
        db.insertWithOnConflict(Contract.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_ABORT);
        Log.e("Database operations", "One row inserted ...");
    }

    public Cursor getRecipe(SQLiteDatabase db) {
        Cursor cursor;
        String[] projections = {Contract._ID,Contract.COLUMN_RECIPENAME};
        cursor = db.query(
                Contract.TABLE_NAME,
                projections,
                null,
                null,
                null,
                null,
                null
        );

        return cursor;
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

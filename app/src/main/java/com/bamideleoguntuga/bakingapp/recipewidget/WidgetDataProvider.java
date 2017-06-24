package com.bamideleoguntuga.bakingapp.recipewidget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bamideleoguntuga.bakingapp.R;
import com.bamideleoguntuga.bakingapp.data.Contract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by delaroy on 6/22/17.
 */
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = "WidgetDataProvider";

    List<String> mCollection = new ArrayList<>();
    private Context mContext;
    private Cursor mCursor;

    public WidgetDataProvider(Context applicationContext, Intent intent) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }


    @Override
    public void onDataSetChanged() {
        if (mCursor != null) {
            mCursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();
        Uri uri = Contract.PATH_RECIPE_URI;
        mCursor = mContext.getContentResolver().query(uri,
                null,
                null,
                null,
                Contract._ID + " DESC");

        Binder.restoreCallingIdentity(identityToken);
    }

   /* public Cursor getInformations(SQLiteDatabase db) {
        Cursor cursor;
        String[] projections = {column1,column2};
        cursor = db.query(
                tableName,
                projections,
                null,
                null,
                null,
                null,
                null
        );

        return cursor;
    }*/


    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                mCursor == null || !mCursor.moveToPosition(position)) {
            return null;
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget_list_item);
        rv.setTextViewText(R.id.widgetItemTaskNameLabel, mCursor.getString(1));

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(RecipeWidget.EXTRA_LABEL, mCursor.getString(1));
        rv.setOnClickFillInIntent(R.id.widgetItemContainer, fillInIntent);

        return rv;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return mCursor.moveToPosition(position) ? mCursor.getLong(0) : position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
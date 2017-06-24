package com.bamideleoguntuga.bakingapp.recipewidget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.bamideleoguntuga.bakingapp.recipewidget.WidgetDataProvider;

/**
 * Created by delaroy on 6/22/17.
 */
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetDataProvider(this, intent);
    }
}
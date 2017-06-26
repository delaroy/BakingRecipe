package com.bamideleoguntuga.bakingapp.sync;

import android.app.IntentService;
import android.content.Intent;



public class RecipeIntentService extends IntentService {

    public RecipeIntentService() {
        super("RecipeIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        RecipeSyncTask.syncRecipe(this);
    }
}
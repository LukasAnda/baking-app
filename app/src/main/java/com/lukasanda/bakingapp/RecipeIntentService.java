package com.lukasanda.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.lukasanda.bakingapp.data.Recipe;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by lukas on 3/30/2018.
 */

public class RecipeIntentService extends IntentService {
    
    private static final String ACTION_UPDATE = "com.lukasanda.bakingapp.action.UPDATE_WIDGETS";
    
    private static final String NEXT_RECIPE = "nextRecipe";
    
    private static final String PREVIOUS_RECIPE = "previousRecipe";
    
    private static final String CURRENT = "currentRecipe";
    
    private static final String TAG = RecipeIntentService.class.getSimpleName();
    
    public RecipeIntentService() {
        super("RecipeIntentService");
    }
    
    public static Intent getNextRecipe(Context context) {
        Intent intent = new Intent(context, RecipeIntentService.class);
        intent.setAction(NEXT_RECIPE);
        return intent;
    }
    
    public static Intent getPreviousRecipe(Context context) {
        Intent intent = new Intent(context, RecipeIntentService.class);
        intent.setAction(PREVIOUS_RECIPE);
        return intent;
    }
    
    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdateWidget(Context context) {
        Intent intent = new Intent(context, RecipeIntentService.class);
        intent.setAction(ACTION_UPDATE);
        try {
            context.startService(intent);
        } catch (IllegalStateException e) {
            Log.d(TAG, "Cannot start service intent while app is in the background");
        }
    }
    
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE.equals(action)) {
                handleActionUpdateWidgets();
            } else if (PREVIOUS_RECIPE.equals(action)) {
                handleGetPreviousRecipe();
            } else if (NEXT_RECIPE.equals(action)) {
                handleGetNextRecipe();
            }
        }
    }
    
    private void handleGetNextRecipe() {
        RecipeWidgetProvider.setRecipeIndex(RecipeWidgetProvider.getsRecipeIndex() + 1);
        handleActionUpdateWidgets();
    }
    
    private void handleGetPreviousRecipe() {
        RecipeWidgetProvider.setRecipeIndex(RecipeWidgetProvider.getsRecipeIndex() - 1);
        handleActionUpdateWidgets();
    }
    
    private void handleActionUpdateWidgets() {
        Realm realm = Realm.getDefaultInstance();
        final List<Recipe> recipes = realm.where(Recipe.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                //We retrieve an unmanaged copy from realm, because we are switching threads
                List<Recipe> recipes1 = realm.copyFromRealm(recipes);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(RecipeIntentService.this);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(RecipeIntentService.this, RecipeWidgetProvider.class));
                RecipeWidgetProvider.setRecipes(recipes1);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.recipe_widget_container);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.recipe_ingredients_list);
                RecipeWidgetProvider.updateRecipeWidgets(RecipeIntentService.this, appWidgetManager, appWidgetIds);
            }
        });
        realm.close();
    }
    
}

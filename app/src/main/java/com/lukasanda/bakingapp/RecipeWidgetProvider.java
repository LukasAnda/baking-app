package com.lukasanda.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.lukasanda.bakingapp.data.Recipe;
import com.lukasanda.bakingapp.data.RecipeIngredient;
import com.lukasanda.bakingapp.ui.activities.RecipeActivity;
import com.lukasanda.bakingapp.ui.activities.RecipeListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukas on 3/30/2018.
 */

public class RecipeWidgetProvider extends AppWidgetProvider {
    
    private static int sRecipeIndex = 0;
    
    private static List<Recipe> sRecipes = new ArrayList<>();
    
    public static void setRecipes(List<Recipe> recipes) {
        sRecipes = recipes;
    }
    
    public static int getsRecipeIndex() {
        return sRecipeIndex;
    }
    
    public static void setRecipeIndex(int index) {
        if (index == -1) {
            if (sRecipes != null && sRecipes.size() != 0) {
                sRecipeIndex = sRecipes.size() - 1;
            }
        } else if (sRecipes != null && index == sRecipes.size()) {
            // reset the value
            sRecipeIndex = 0;
        } else {
            sRecipeIndex = index;
        }
    }
    
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetIt) {
        
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_list);
        
        Intent intent;
        
        try {
            Recipe recipe = sRecipes.get(sRecipeIndex);
            intent = new Intent(context, RecipeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(RecipeActivity.RECIPE_KEY, recipe);
            bundle.putBoolean(RecipeActivity.FROM_WIDGET_KEY, true);
            intent.putExtras(bundle);
        } catch (Exception e) {
            Log.d("RecipeWidgetProvider", "Invalid attempt to get item from list");
            intent = new Intent(context, RecipeListActivity.class);
        }
        
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.recipe_info_container, pendingIntent);
        
        // previous button
        Intent previousIntent = RecipeIntentService.getPreviousRecipe(context);
        PendingIntent previousPending = PendingIntent.getService(context, 0, previousIntent, 0);
        views.setOnClickPendingIntent(R.id.prev_button, previousPending);
        
        // next button
        Intent nextIntent = RecipeIntentService.getNextRecipe(context);
        PendingIntent nextPending = PendingIntent.getService(context, 0, nextIntent, 0);
        views.setOnClickPendingIntent(R.id.next_button, nextPending);
        
        Recipe recipe = getCurrentRecipe();
        if (recipe != null) {
            views.setTextViewText(R.id.recipe_name_tv, recipe.getName());
            
            StringBuilder sb = new StringBuilder();
            for (RecipeIngredient ingredient : recipe.getIngredients()) {
                sb.append(ingredient).append("\n");
            }
            views.setTextViewText(R.id.recipe_ingredients_list, sb.toString());
        }
        
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIt, R.id.recipe_widget_container);
        appWidgetManager.updateAppWidget(appWidgetIt, views);
    }
    
    public static Recipe getCurrentRecipe() {
        if (sRecipes == null) {
            return null;
        }
        
        if (sRecipeIndex > sRecipes.size() || sRecipeIndex < 0) {
            sRecipeIndex = 0;
        }
        
        return sRecipes.get(sRecipeIndex);
    }
    
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] recipeIds) {
        // There may be multiple widgets active, so update all of them
        RecipeIntentService.startActionUpdateWidget(context);
    }
    
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }
    
    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    
    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}



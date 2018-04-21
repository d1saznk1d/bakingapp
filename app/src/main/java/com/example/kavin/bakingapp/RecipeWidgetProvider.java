package com.example.kavin.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.example.kavin.bakingapp.Data.BakingAppsDataModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {
    public static final String RECIPE_ITEM = "com.example.bakingapp.RECIPE_ITEM";
    public String baking_lists;
    public String recipeTitle;
    private BakingAppsDataModel bakingAppsDataModel = new BakingAppsDataModel();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent (context, RecipeWidgetRemoteViewsService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.listview_widget_layout);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            Gson gson = new Gson();
            baking_lists = preferences.getString("save_ingredients", "");
            bakingAppsDataModel = gson.fromJson(baking_lists, new TypeToken<BakingAppsDataModel>(){}.getType());
            if (bakingAppsDataModel != null) {
                recipeTitle = bakingAppsDataModel.getName();
            }

            rv.setRemoteAdapter(R.id.widget_list_view, intent);
            rv.setEmptyView(R.id.widget_list_view, R.id.empty_view);
            rv.setTextViewText(R.id.widget_name, recipeTitle.toString());

            appWidgetManager.updateAppWidget(appWidgetId, rv);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list_view);
            }
            super.onUpdate(context, appWidgetManager, appWidgetIds);
        }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
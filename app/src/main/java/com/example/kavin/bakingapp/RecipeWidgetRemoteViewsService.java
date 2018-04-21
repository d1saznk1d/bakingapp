package com.example.kavin.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.kavin.bakingapp.Data.BakingAppsDataModel;
import com.example.kavin.bakingapp.Data.IngredientsDataModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by kavin on 3/12/2018.
 */

public class RecipeWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    Context context;
    String ingredient_list;
    private BakingAppsDataModel bakingAppsDataModel = new BakingAppsDataModel();
    List<IngredientsDataModel> ingredientsDataModels;

    public ListViewRemoteViewsFactory(Context applicationContext) {
        this.context = applicationContext;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        ingredient_list = preferences.getString("save_ingredients", "");
        bakingAppsDataModel = gson.fromJson(ingredient_list, new TypeToken<BakingAppsDataModel>(){}.getType());
        if (bakingAppsDataModel != null) {
            ingredientsDataModels = bakingAppsDataModel.getIngredients();
        }
    }

    @Override
    public void onDestroy() {
        ingredientsDataModels.clear();
    }

    @Override
    public int getCount() {
        if (ingredientsDataModels != null) {
            return ingredientsDataModels.size();
        } else {
            return 0;
        }
    }


    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.listview_widget_item);
        IngredientsDataModel ingredientsDataModel = ingredientsDataModels.get(position);
        views.setTextViewText(R.id.widget_measure, ingredientsDataModel.getMeasure().toString());
        views.setTextViewText(R.id.widget_ingredient, ingredientsDataModel.getIngredient().toString());
        views.setTextViewText(R.id.widget_ingredientQuantity, Double.toString(ingredientsDataModel.getQuantity()));

        Bundle extras = new Bundle();
        extras.putInt(RecipeWidgetProvider.RECIPE_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("short_ingredients", ingredientsDataModel);
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.widget_measure, fillInIntent);
        views.setOnClickFillInIntent(R.id.widget_ingredient, fillInIntent);
        views.setOnClickFillInIntent(R.id.widget_ingredientQuantity, fillInIntent);

        return views;
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
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}


package com.example.kavin.bakingapp.BakingAppsAdapter;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.BinderThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kavin.bakingapp.Data.BakingAppsDataModel;
import com.example.kavin.bakingapp.R;
import com.example.kavin.bakingapp.RecipeWidgetProvider;
import com.example.kavin.bakingapp.RecipeWidgetRemoteViewsService;
import com.example.kavin.bakingapp.ui.DetailActivityFragment;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kavin on 2/19/2018.
 */

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.MyViewHolder> {
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView nameMa;
        @BindView(R.id.imageURL)
        ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Intent intent = new Intent(view.getContext(), DetailActivityFragment.class);
                    intent.putExtra("baking_app", mbakingAppsDataModels.get(position));

                    //Using SharedPreferences to send then specific Recipe to the widget
                    Gson gson = new Gson();
                    String json = gson.toJson(mbakingAppsDataModels.get(position));

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("save_ingredients", json);
                    editor.apply();

                    //Send Broadcast from Adapter once the recipe is selected
                    Intent intentToWidget = new Intent(context, RecipeWidgetProvider.class);
                    int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, RecipeWidgetProvider.class));
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    context.sendBroadcast(intentToWidget);

                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    ArrayList<BakingAppsDataModel> mbakingAppsDataModels = new ArrayList<>();

    public MainActivityAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_recipecard, parent, false);
        MyViewHolder vh = new MyViewHolder(itemView);
        return vh;
    }

    public void updateLists(ArrayList<BakingAppsDataModel> bakingAppsDataModels) {
        mbakingAppsDataModels = bakingAppsDataModels;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BakingAppsDataModel bakingAppsDataModel = mbakingAppsDataModels.get(position);
        holder.nameMa.setText(bakingAppsDataModel.getName().toString());

        if (bakingAppsDataModel.getImage().toString().equalsIgnoreCase("")) {
            } else {
            Picasso.with(context).load(bakingAppsDataModel.getImage().toString())
                    .error(R.drawable.ic_error_placeholder)
                    .into(holder.imageView);
            }
    }

    @Override
    public int getItemCount() {
        //condition ? if : else
        return (mbakingAppsDataModels != null) ? mbakingAppsDataModels.size(): 0;
    }
}
package com.example.kavin.bakingapp.BakingAppsAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kavin.bakingapp.Data.BakingAppsDataModel;
import com.example.kavin.bakingapp.Data.IngredientsDataModel;
import com.example.kavin.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kavin on 2/25/2018.
 */

public class IngredientsListAdapter extends RecyclerView.Adapter<IngredientsListAdapter.ViewHolder> {
    private ArrayList<IngredientsDataModel> ingredientsDataModels;
    private Context mContext;
    private LayoutInflater inflater;

    public IngredientsListAdapter(Context mContext, ArrayList<IngredientsDataModel> ingredientsDataModels) {
        this.ingredientsDataModels = ingredientsDataModels;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipes_steps_fragment, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    IngredientsDataModel ingredientsDataModel = ingredientsDataModels.get(position);
        final float quantity = ingredientsDataModel.getQuantity();
        holder.quantity.setText(String.valueOf(quantity));
        holder.measure.setText(ingredientsDataModel.getMeasure().toString());
        holder.ingredient.setText(ingredientsDataModel.getIngredient().toString());
        holder.ingredients_title.setText(mContext.getResources().getString(R.string.ingredient, position));
    }

    @Override
    public int getItemCount() {
        if (ingredientsDataModels == null || ingredientsDataModels.isEmpty()) {
            return 0;
        } else {
            return ingredientsDataModels.size();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void updateIngredientsList(List<IngredientsDataModel> data) {
        ingredientsDataModels.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.quantity) TextView quantity;
        @BindView(R.id.measure) TextView measure;
        @BindView(R.id.ingredient) TextView ingredient;
        @BindView(R.id.ingredients_title) TextView ingredients_title;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

package com.example.kavin.bakingapp.BakingAppsAdapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kavin.bakingapp.Data.StepDataModel;
import com.example.kavin.bakingapp.R;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kavin on 2/25/2018.
 */

public class StepsListAdapter extends RecyclerView.Adapter<StepsListAdapter.ViewHolder> {
    private ArrayList<StepDataModel> stepDataModels;

    public StepsListAdapter(ArrayList<StepDataModel> stepDataModels) {
        this.stepDataModels = stepDataModels;
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
        StepDataModel stepDataModel = stepDataModels.get(position);
        holder.shortDescription.setText(stepDataModel.getShortDescription().toString());
}

    @Override
    public int getItemCount() {
        if (stepDataModels == null || stepDataModels.isEmpty()) {
            return 0;
        } else {
            return stepDataModels.size();
        }
    }


    public void updateStepsList(List<StepDataModel> data) {
        stepDataModels.clear();
        stepDataModels.addAll(data);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.shortDescription) TextView shortDescription;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}


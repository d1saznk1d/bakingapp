package com.example.kavin.bakingapp.ui;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.kavin.bakingapp.BakingAppsAdapter.IngredientsListAdapter;
import com.example.kavin.bakingapp.BakingAppsAdapter.StepsListAdapter;
import com.example.kavin.bakingapp.Data.BakingAppsDataModel;
import com.example.kavin.bakingapp.Data.IngredientsDataModel;
import com.example.kavin.bakingapp.Data.StepDataModel;
import com.example.kavin.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by kavin on 2/23/2018.
 */

public class StepsIngredientsFragment extends Fragment {
    public StepsListAdapter stepsListAdapter;
    public IngredientsListAdapter ingredientsListAdapter;
    @BindView(R.id.list_steps)
    RecyclerView stepsListView;
    @BindView(R.id.list_ingredients)
    RecyclerView listIngredientsView;
    OnImageClickListener mCallback;
    final static String ARG_POSITION = "position";
    private static Bundle mBundleRecyclerViewState;
    private final String KEY_RECYCLER_STATE = "recycler_state";

    public StepsIngredientsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle saveInstanceState) {
        Log.d(TAG, "pass onCreateView");
        final View rootView  = inflater.inflate(R.layout.listview_fragment_main, parent, false);
        ButterKnife.bind(this, rootView);
        LinearLayoutManager listVerticalLayout =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager stepsVerticalLayout =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        stepsListAdapter = new StepsListAdapter(new ArrayList<StepDataModel>());
        ingredientsListAdapter = new IngredientsListAdapter(this.getActivity(), new ArrayList<IngredientsDataModel>());
        stepsListView.setLayoutManager(listVerticalLayout);
        stepsListView.getLayoutManager().setMeasurementCacheEnabled(false);
        listIngredientsView.setLayoutManager(stepsVerticalLayout);
        listIngredientsView.getLayoutManager().setMeasurementCacheEnabled(false);

        stepsListView.setAdapter(stepsListAdapter);
        listIngredientsView.setAdapter(ingredientsListAdapter);
        stepsListView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        listIngredientsView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));


        stepsListView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        mCallback.onImageSelected(position);
                    }
                })
        );
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mBundleRecyclerViewState = new Bundle();
        Parcelable stepsListState = stepsListView.getLayoutManager().onSaveInstanceState();
        Parcelable ingredientsListState = listIngredientsView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, stepsListState);
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, ingredientsListState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = this.getArguments();
        BakingAppsDataModel bakingAppsDataModels = args.getParcelable("fragment1");
        if (args != null) {
            stepsListAdapter.updateStepsList(bakingAppsDataModels.getSteps());
            ingredientsListAdapter.updateIngredientsList(bakingAppsDataModels.getIngredients());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBundleRecyclerViewState != null) {
            Parcelable stepsListState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            Parcelable ingredientsListState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            stepsListView.getLayoutManager().onRestoreInstanceState(stepsListState);
            listIngredientsView.getLayoutManager().onRestoreInstanceState(ingredientsListState);
        }
    }


    public interface OnImageClickListener {
        void onImageSelected(int i);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnImageClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListner");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "pass inside onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }
}


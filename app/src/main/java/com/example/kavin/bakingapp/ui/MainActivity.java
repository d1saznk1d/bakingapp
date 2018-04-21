package com.example.kavin.bakingapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.kavin.bakingapp.BakingAppsAdapter.MainActivityAdapter;
import com.example.kavin.bakingapp.Data.BakingAppsDataModel;
import com.example.kavin.bakingapp.Data.remote.ApiUtils;
import com.example.kavin.bakingapp.Data.remote.MyApiEndpointInterface;
import com.example.kavin.bakingapp.R;
import com.example.kavin.bakingapp.SimpleIdling.BakingIdlingResource;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {
    MainActivityAdapter mainActivityAdapter;
    private MyApiEndpointInterface myApiEndpointInterface;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    private static Bundle mBundleRecyclerViewState;
    private final String KEY_RECYCLER_STATE = "recycler_state";

    @Nullable
    private BakingIdlingResource bakingIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (bakingIdlingResource == null) {
            bakingIdlingResource = new BakingIdlingResource();
        }
        return bakingIdlingResource;
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        myApiEndpointInterface = ApiUtils.getAPIService();
        ButterKnife.bind(this);
        Resources res = getResources();
        int rows = res.getInteger(R.integer.num_columns);
        //calling mainActivityAdapter

        mainActivityAdapter = new MainActivityAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, rows));
        mRecyclerView.setAdapter(mainActivityAdapter);

        MainActivityDisplay();
        //Get the IdleResource instance
        getIdlingResource();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Save list state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBundleRecyclerViewState != null) {
            Parcelable liststate = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(liststate);
        }
    }

    public void MainActivityDisplay() {
        if (bakingIdlingResource != null) {
            bakingIdlingResource.setIdleState(false);
        }
        myApiEndpointInterface.getBakingData().enqueue(new Callback<ArrayList<BakingAppsDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<BakingAppsDataModel>> call, Response<ArrayList<BakingAppsDataModel>> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    final ArrayList<BakingAppsDataModel> bakingAppsDataModels = response.body();
                    mainActivityAdapter.updateLists(bakingAppsDataModels);
                    Log.d("Correct response code: ", String.valueOf(status));

                    if (bakingIdlingResource != null) {
                        bakingIdlingResource.setIdleState(true);
                    }
                } else {
                    Log.d("Wrong response code: ", String.valueOf(status));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BakingAppsDataModel>> call, Throwable t) {
                Log.d("MainActivity ", "error loading from API");
            }
        });
    }
}

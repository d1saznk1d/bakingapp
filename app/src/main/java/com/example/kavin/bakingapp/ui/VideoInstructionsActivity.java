package com.example.kavin.bakingapp.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.kavin.bakingapp.Data.BakingAppsDataModel;
import com.example.kavin.bakingapp.Data.StepDataModel;
import com.example.kavin.bakingapp.R;
import com.example.kavin.bakingapp.SimpleIdling.BakingIdlingResource;

import java.util.ArrayList;

/**
 * Created by kavin on 3/2/2018.
 */

public class VideoInstructionsActivity extends AppCompatActivity implements VideosInstructionsFragment.onItemListener {
    ArrayList<StepDataModel> stepDataModels = new ArrayList<>();
    BakingAppsDataModel bakingAppsDataModels =  new BakingAppsDataModel();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_main);
        getSupportActionBar();

            if(savedInstanceState == null) {
                int indexPosition = getIntent().getIntExtra("videoFragmentIndex", 0);
                bakingAppsDataModels = getIntent().getParcelableExtra("steps_list");

                stepDataModels = new ArrayList<>(bakingAppsDataModels.getSteps());
                VideosInstructionsFragment videosInstructionsFragment = VideosInstructionsFragment.newInstance(stepDataModels);

                videosInstructionsFragment.setListIndex(indexPosition);

                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, videosInstructionsFragment)
                        .commit();
            }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case android.R.id.home:
                    NavUtils.navigateUpFromSameTask(this);
                    final Intent intent = new Intent(this, DetailActivityFragment.class);
                    intent.putExtra("baking_app", bakingAppsDataModels);

                    startActivity(intent);
                    return true;
            }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onItemSelected(int i) {
    }
}

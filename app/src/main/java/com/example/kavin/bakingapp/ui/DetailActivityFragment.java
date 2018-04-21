package com.example.kavin.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.kavin.bakingapp.Data.BakingAppsDataModel;
import com.example.kavin.bakingapp.Data.StepDataModel;
import com.example.kavin.bakingapp.R;



import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;



/**4
 * Created by kavin on 2/19/2018.
 */

public class DetailActivityFragment extends AppCompatActivity implements StepsIngredientsFragment.OnImageClickListener,
                                                                    VideosInstructionsFragment.onItemListener {
    StepsIngredientsFragment stepsIngredientsFragment = new StepsIngredientsFragment();
    VideosInstructionsFragment videosInstructionsFragment = new VideosInstructionsFragment();
    ArrayList<StepDataModel> stepDataModels = new ArrayList<>();
    public int videoFragmentIndex;
    @BindView(R.id.shortDescription)
    TextView shortDescription;
    BakingAppsDataModel bakingAppsDataModels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_fragment_main);
        stepsIngredientsFragment = new StepsIngredientsFragment();
        videosInstructionsFragment = new VideosInstructionsFragment();


        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle args = getIntent().getExtras();
        if (savedInstanceState == null) {
            bakingAppsDataModels = args.getParcelable("baking_app");
        } else {
            bakingAppsDataModels = args.getParcelable("baking_app");
        }

        Bundle bundle = new Bundle();
        bundle.putParcelable("fragment1", bakingAppsDataModels);

        boolean tableSize = getResources().getBoolean(R.bool.isTablet);


        if (findViewById(R.id.fragment_container) != null) {
                  if (tableSize && savedInstanceState == null) {
                    stepsIngredientsFragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, stepsIngredientsFragment)
                            .commit();

                    stepDataModels = new ArrayList<>(bakingAppsDataModels.getSteps());
                    VideosInstructionsFragment videosInstructionsFragment = VideosInstructionsFragment.newInstance(stepDataModels);
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_containerRight, videosInstructionsFragment)
                            .commit();
                }  else {
                    if (savedInstanceState == null) {
                        stepsIngredientsFragment.setArguments(bundle);
                        fragmentManager.beginTransaction()
                                .add(R.id.fragment_container, stepsIngredientsFragment)
                                .commit();
                    }
                }
            }
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }


    @Override
    public void onImageSelected(final int position) {
        ButterKnife.bind(this);
        int listIndex = position;
        boolean tableSize = getResources().getBoolean(R.bool.isTablet);
        if (tableSize) {
            stepDataModels = new ArrayList<>(bakingAppsDataModels.getSteps());
            VideosInstructionsFragment videosInstructionsFragment = VideosInstructionsFragment.newInstance(stepDataModels);
            videosInstructionsFragment.setListIndex(position);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_containerRight, videosInstructionsFragment)
                    .commit();
        } else {
            videoFragmentIndex = listIndex;
            Bundle b = new Bundle();
            b.putInt("videoFragmentIndex", videoFragmentIndex);
            final Intent intent = new Intent(this, VideoInstructionsActivity.class);
            intent.putExtras(b);
            intent.putExtra("steps_list", bakingAppsDataModels);
            startActivity(intent);

            shortDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }
    }

    @Override
    public void onItemSelected(int position) {
    }
}

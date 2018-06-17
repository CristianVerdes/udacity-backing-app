package com.example.cristianverdes.bakingapp.ui.step;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.cristianverdes.bakingapp.R;
import com.example.cristianverdes.bakingapp.ui.BaseActivity;

/**
 * Created by cristian.verdes on 16.03.2018.
 */

public class StepActivity extends BaseActivity {
    private static final String RECIPE_ID = "recipeId";
    private static final String STEP_ID = "stepId";
    private static final String STEPS_SIZE = "stepsSize";
    private static final String RECIPE_NAME = "recipeName";

    public static void start(Context context, int recipeId, int stepId, int stepsSize, String recipeName) {
        Intent starter = new Intent(context, StepActivity.class);
        starter.putExtra(RECIPE_ID, recipeId);
        starter.putExtra(STEP_ID, stepId);
        starter.putExtra(STEPS_SIZE, stepsSize);
        starter.putExtra(RECIPE_NAME, recipeName);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        setCustomActionBar();
        // We check savedInstanceState because we don't want to create a new fragment when the screen rotates
        if (savedInstanceState == null) {
            // Get data from previous Activity
            int recipeId = getIntent().getIntExtra(RECIPE_ID, 0);
            int stepId = getIntent().getIntExtra(STEP_ID, 0);
            int stepsSize = getIntent().getIntExtra(STEPS_SIZE, 0);

            // Create Fragment instance
            StepFragment stepFragment = new StepFragment();

            // Send data to fragment
            Bundle bundle = new Bundle();
            bundle.putInt(RECIPE_ID, recipeId);
            bundle.putInt(STEP_ID, stepId);

            bundle.putInt(STEPS_SIZE, stepsSize);
            stepFragment.setArguments(bundle);

            // Get Fragment Manager
            FragmentManager fragmentManager = getSupportFragmentManager();

            // Begin transition
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, stepFragment)
                    .commit();
        }
    }

    private void setCustomActionBar() {
        getSupportActionBar().setTitle(getIntent().getStringExtra(RECIPE_NAME));
    }
}

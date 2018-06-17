package com.example.cristianverdes.bakingapp.ui.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.cristianverdes.bakingapp.R;
import com.example.cristianverdes.bakingapp.ui.step.StepFragment;

/**
 * Created by cristian.verdes on 15.03.2018.
 */

public class RecipeActivity extends AppCompatActivity{
    private static final String RECIPE_ID = "recipeId";
    private static final String RECIPE_NAME = "recipeName";
    private static final String STEP_ID = "stepId";
    private static final String IS_TABLET = "isTablet";

    private boolean isTablet;

    public static void start(Context context, int recipeId, String recipeName) {
        Intent starter = new Intent(context, RecipeActivity.class);
        starter.putExtra(RECIPE_ID, recipeId);
        starter.putExtra(RECIPE_NAME, recipeName);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        setCustomActionBar();

        isTablet = getResources().getBoolean(R.bool.is_tablet);

        // We check savedInstanceState because we don't want to create a new fragment when the screen rotates
        if (savedInstanceState == null) {
            // Get recipeId from Intent
            int recipeId = getIntent().getIntExtra(RECIPE_ID, 0);

            if (isTablet) {
                // TABLET layout
                // Create first fragment: RecipeFragment - (Master)
                createRecipeFragment(R.id.fl_steps_container, recipeId);

                // Create second fragment: Step fragment - (Detail)
                // This is the first time the activity is lunched so the Step fragment must by the first one ( 0 ).
                // After this the savedInstanceState will be != null so the Steps Adapter will be responsible to instantiate another Step fragment.
                createStepFragment(recipeId);

            } else {
                // PHONE layout
                createRecipeFragment(R.id.fragment_container, recipeId);
            }
        }
    }

    private void createRecipeFragment(int fragmentContainerId, int recipeId) {
        RecipeFragment recipeFragment = new RecipeFragment();

        // Send ID to fragment
        Bundle bundle = new Bundle();
        bundle.putInt(RECIPE_ID, recipeId);
        bundle.putBoolean(IS_TABLET, isTablet);
        bundle.putString(RECIPE_NAME, getIntent().getStringExtra(RECIPE_NAME));
        recipeFragment.setArguments(bundle);

        // Use Fragment manager and transition to add the fragment to the screen
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Begin transition
        fragmentManager.beginTransaction()
                .replace(fragmentContainerId, recipeFragment)
                .commit();
    }


    // Used only in Tablet Layout
    private void createStepFragment(int recipeId) {

        // Create fragment
        StepFragment stepFragment = new StepFragment();

        // Send data to fragment
        Bundle bundle = new Bundle();
        bundle.putInt(RECIPE_ID, recipeId);
        bundle.putInt(STEP_ID, 0);
        bundle.putBoolean(IS_TABLET, true);
        stepFragment.setArguments(bundle);

        // Get Fragment Manager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Begin transition
        fragmentManager.beginTransaction()
                .replace(R.id.fl_step_container, stepFragment)
                .commit();
    }

    private void setCustomActionBar() {
        getSupportActionBar().setTitle(getIntent().getStringExtra(RECIPE_NAME));
    }
}

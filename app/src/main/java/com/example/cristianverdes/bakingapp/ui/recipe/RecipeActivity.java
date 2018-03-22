package com.example.cristianverdes.bakingapp.ui.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.cristianverdes.bakingapp.R;

/**
 * Created by cristian.verdes on 15.03.2018.
 */

public class RecipeActivity extends AppCompatActivity{
    private static final String RECIPE_ID = "recipeId";
    private static final String RECIPE_NAME = "recipeName";
    private static final String TWO_PANE = "twoPane";

    private boolean twoPane = false;

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

        if (findViewById(R.id.fl_steps_container) != null) {
            // TABLET layout
            twoPane = true;

            RecipeFragment recipeFragment = new RecipeFragment();

            // Send ID to fragment
            Bundle bundle = new Bundle();
            int recipeId = getIntent().getIntExtra(RECIPE_ID, 0);
            bundle.putInt(RECIPE_ID, recipeId);
            bundle.putBoolean(TWO_PANE, twoPane);
            bundle.putString(RECIPE_NAME, getIntent().getStringExtra(RECIPE_NAME));
            recipeFragment.setArguments(bundle);

            // Use Fragment manager and transition to add the fragment to the screen
            FragmentManager fragmentManager = getSupportFragmentManager();

            // Begin transition
            fragmentManager.beginTransaction()
                    .replace(R.id.fl_steps_container, recipeFragment)
                    .commit();

        } else {
            // PHONE layout
            twoPane = false;

            RecipeFragment recipeFragment = new RecipeFragment();

            // Send ID to fragment
            Bundle bundle = new Bundle();
            int recipeId = getIntent().getIntExtra(RECIPE_ID, 0);
            bundle.putInt(RECIPE_ID, recipeId);
            bundle.putString(RECIPE_NAME, getIntent().getStringExtra(RECIPE_NAME));
            recipeFragment.setArguments(bundle);

            // Use Fragment manager and transition to add the fragment to the screen
            FragmentManager fragmentManager = getSupportFragmentManager();

            // Begin transition
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, recipeFragment)
                    .commit();
        }
    }

    private void setCustomActionBar() {
        getSupportActionBar().setTitle(getIntent().getStringExtra(RECIPE_NAME));
    }
}

package com.example.cristianverdes.bakingapp.ui.recipe;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cristianverdes.bakingapp.R;
import com.example.cristianverdes.bakingapp.data.model.Recipe;
import com.example.cristianverdes.bakingapp.data.model.Step;
import com.example.cristianverdes.bakingapp.ui.ingredients.IngredientsActivity;
import com.example.cristianverdes.bakingapp.ui.listrecipes.RecipesViewModel;
import com.example.cristianverdes.bakingapp.ui.step.StepFragment;
import com.example.cristianverdes.bakingapp.utils.Injection;

import java.util.List;

/**
 * Created by cristian.verdes on 15.03.2018.
 */

public class RecipeFragment extends Fragment{
    private static final String RECIPE_ID = "recipeId";
    private static final String RECIPE_NAME = "recipeName";
    private static final String STEP_ID = "stepId";
    private static final String KEY_SCROLL_INDEX = "key_scroll_index";
    private static final String IS_TABLET = "isTablet";

    private static final String TAG = RecipeFragment.class.getSimpleName();
    private static int recipeId;
    private View rootView;

    private RecyclerView stepsList;
    private RecipesViewModel recipesViewModel;
    private StepsAdapter stepsAdapter;
    private TextView ingredients;

    private LinearLayoutManager linearLayoutManager;

    private int listScrollIndex = -1;
    private int stepId = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Get Extra
        recipeId = getArguments().getInt(RECIPE_ID, 0);

        // Inflate View
        rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        // RecyclerView
        stepsList = rootView.findViewById(R.id.rv_steps);
        stepsAdapter = new StepsAdapter(recipeId);

        stepsAdapter.setIsTablet(getArguments().getBoolean(IS_TABLET));

        stepsList.setAdapter(stepsAdapter);
        linearLayoutManager = new LinearLayoutManager(container.getContext());
        stepsList.setLayoutManager(linearLayoutManager);

        // Set ingredients listener
        setIngredientsListener(rootView);

        // Create View Model
        createViewModel();

        // Here we subscribe to the Observable stream from ViewModel
        subscribeToDataStreams();

        // TABLET layout
        if (getArguments().getBoolean("isTablet")) {
            stepsAdapter.getChangeFragmentObservable().observe(this, new Observer<ChangeStepFragmentData>() {
                @Override
                public void onChanged(@Nullable ChangeStepFragmentData changeStepFragmentData) {
                    // Set new Step Fragment
                    createStepFragment(changeStepFragmentData.getRecipeId(), changeStepFragmentData.getStepId());
                }
            });
        }

        return rootView;

    }

    // Save and restore state
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        int scrollIndex = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
        outState.putInt(KEY_SCROLL_INDEX, scrollIndex);
        outState.putInt(STEP_ID, stepId);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            listScrollIndex = savedInstanceState.getInt(KEY_SCROLL_INDEX);
            stepId = savedInstanceState.getInt(STEP_ID);
        }
    }

    // Used only in Tablet Layout
    private void createStepFragment(int recipeId, int stepId) {
        this.stepId = stepId;

        // Create fragment
        StepFragment stepFragment = new StepFragment();

        // Send data to fragment
        Bundle bundle = new Bundle();
        bundle.putInt(RECIPE_ID, recipeId);
        bundle.putInt(STEP_ID, stepId);
        bundle.putBoolean(IS_TABLET, true);
        stepFragment.setArguments(bundle);

        // Get Fragment Manager
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        // Begin transition
        fragmentManager.beginTransaction()
                .replace(R.id.fl_step_container, stepFragment)
                .commit();
    }

    private void setIngredientsListener(final View rootView) {
        ingredients = rootView.findViewById(R.id.tv_ingredients);
        ingredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent starter = new Intent(rootView.getContext(), IngredientsActivity.class);
                starter.putExtra(RECIPE_ID, recipeId);
                starter.putExtra(RECIPE_NAME, RECIPE_NAME);
                startActivityForResult(starter, 0);
            }
        });
    }

    private void subscribeToDataStreams() {
        recipesViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes != null && recipes.size() != 0) {
                    List<Step> steps = recipes.get(recipeId - 1).getSteps();
                    if (steps != null) {
                        // Hide progressbar and show data
                        hideProgressbar();
                        stepsAdapter.setRecipeName(recipes.get(recipeId - 1).getName());
                        stepsAdapter.setSteps(steps);
                        // Scroll list if needed
                        if (listScrollIndex != -1) {
                            linearLayoutManager.scrollToPositionWithOffset(listScrollIndex, 0);
                        }
                    } else {
                        Log.e(TAG, "Error: No Steps");
                    }
                } else {
                    Log.e(TAG, "Error: No recipes");
                }
            }
        });
    }

    private void createViewModel() {
        recipesViewModel = new RecipesViewModel(Injection.provideRecipesRepository(this.getContext()));
    }

    public void hideProgressbar() {
        ProgressBar progressBar = rootView.findViewById(R.id.pb_recipe);
        progressBar.setVisibility(View.INVISIBLE);
        stepsList.setVisibility(View.VISIBLE);
        TextView stepsListTitle = rootView.findViewById(R.id.tv_recipe_steps);
        stepsListTitle.setVisibility(View.VISIBLE);
        CardView cardView = rootView.findViewById(R.id.cv_ingredients);
        cardView.setVisibility(View.VISIBLE);
    }
}

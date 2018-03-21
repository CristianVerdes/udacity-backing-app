package com.example.cristianverdes.bakingapp.ui.recipe;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
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

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.Observable;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by cristian.verdes on 15.03.2018.
 */

public class RecipeFragment extends Fragment{
    private static final String RECIPE_ID = "recipeId";
    private static final String STEP_ID = "stepId";

    private static final String TAG = RecipeFragment.class.getSimpleName();
    private int recipeId;
    private View rootView;

    private RecyclerView stepsList;
    private RecipesViewModel recipesViewModel;
    private StepsAdapter stepsAdapter;
    private TextView ingredients;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Get Extra
        recipeId = getArguments().getInt("recipeId");

        // Inflate View
        rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        // RecyclerView
        stepsList = rootView.findViewById(R.id.rv_steps);
        stepsAdapter = new StepsAdapter(recipeId);

        stepsAdapter.setTwoPane(getArguments().getBoolean("twoPane"));

        stepsList.setAdapter(stepsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        stepsList.setLayoutManager(linearLayoutManager);

        // Set ingredients listener
        setIngredientsListener(rootView);

        // Create View Model
        createViewModel();

        // Here we subscribe to the Observable stream from ViewModel
        subscribeToDataStreams();

        // TwoPane
        if (getArguments().getBoolean("twoPane")) {
            // Set fragment at fragment startup
            createStepFragment(recipeId, 0);
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

    private void createStepFragment(int recipeId, int stepId) {
        // Create fragment
        StepFragment stepFragment = new StepFragment();

        // Send data to fragment
        Bundle bundle = new Bundle();
        bundle.putInt(RECIPE_ID, recipeId);
        bundle.putInt(STEP_ID, stepId);
        bundle.putBoolean("twoPane", true);
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
                IngredientsActivity.start(rootView.getContext(), recipeId);
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

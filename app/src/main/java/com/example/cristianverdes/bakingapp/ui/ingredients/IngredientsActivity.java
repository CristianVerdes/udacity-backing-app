package com.example.cristianverdes.bakingapp.ui.ingredients;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.cristianverdes.bakingapp.R;
import com.example.cristianverdes.bakingapp.data.model.Ingredient;
import com.example.cristianverdes.bakingapp.data.model.Recipe;
import com.example.cristianverdes.bakingapp.data.model.Step;
import com.example.cristianverdes.bakingapp.ui.listrecipes.RecipesViewModel;

import java.util.List;

/**
 * Created by cristian.verdes on 16.03.2018.
 */

public class IngredientsActivity extends AppCompatActivity {
    private static final String TAG = IngredientsActivity.class.getSimpleName();
    private static final String RECIPE_ID = "recipeId";
    private int recipeId;
    private RecyclerView ingredientsList;
    private RecipesViewModel recipesViewModel;
    private IngredientsAdapter ingredientsAdapter;

    public static void start(Context context, int recipeId) {
        Intent starter = new Intent(context, IngredientsActivity.class);
        starter.putExtra(RECIPE_ID, recipeId);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        // Get data from Intent
        recipeId = getIntent().getIntExtra(RECIPE_ID, 0);

        ingredientsList = findViewById(R.id.rv_ingredients);
        ingredientsAdapter = new IngredientsAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        ingredientsList.setLayoutManager(linearLayoutManager);
        ingredientsList.setAdapter(ingredientsAdapter);

        // Create ViewModel
        createViewModel();

        // Subscribe to data steam
        subscribeToDataStream();
    }

    private void createViewModel() {
        recipesViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);
    }

    private void subscribeToDataStream() {
        recipesViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes != null) {
                    List<Ingredient> ingredients = recipes.get(recipeId - 1).getIngredients();
                    if (ingredients != null) {
                        // Hide progressbar and show data
                        hideProgressbar();
                        ingredientsAdapter.setIngredients(ingredients);
                    } else {
                        Log.e(TAG, "Error: No ingredients");
                    }
                } else {
                    Log.e(TAG, "Error: No recipes");
                }

            }
        });
    }

    public void hideProgressbar() {
        ProgressBar progressBar = findViewById(R.id.pb_ingredients);
        progressBar.setVisibility(View.INVISIBLE);
        ingredientsList.setVisibility(View.VISIBLE);
    }
}

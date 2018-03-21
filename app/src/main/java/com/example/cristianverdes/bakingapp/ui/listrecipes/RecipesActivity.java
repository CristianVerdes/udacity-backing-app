package com.example.cristianverdes.bakingapp.ui.listrecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.cristianverdes.bakingapp.R;
import com.example.cristianverdes.bakingapp.data.model.Recipe;
import com.example.cristianverdes.bakingapp.ui.recipe.RecipeActivity;
import com.example.cristianverdes.bakingapp.utils.Injection;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesActivity extends AppCompatActivity {
    private static final String TAG = RecipeActivity.class.getSimpleName();

    private RecipesViewModel recipesViewModel;
    private RecipesAdapter recipesAdapter;
    private RecyclerView recipesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        // Set Layout Manager
        if (findViewById(R.id.rv_recipes_grid) != null) {
            // Tablet
            recipesList = findViewById(R.id.rv_recipes_grid);
            recipesList.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            // Phone
            recipesList = findViewById(R.id.rv_recipes);
            recipesList.setLayoutManager(new LinearLayoutManager(this));
        }

        setAdapterToRecyclerView();

        instantiateViewModel();

        subscribeToDataStream();
    }

    private void instantiateViewModel() {
        recipesViewModel = new RecipesViewModel(Injection.provideRecipesRepository(this));
    }

    private void subscribeToDataStream() {
        recipesViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes != null) {
                    // Hide Progressbar and show data
                    hideProgressBar();
                    recipesAdapter.setRecipes(recipes);
                } else {
                    Log.e(TAG, "Error: No recipes");
                }
            }
        });
    }

    private void setAdapterToRecyclerView() {
        recipesAdapter = new RecipesAdapter();
        recipesList.setAdapter(recipesAdapter);
    }

    public void hideProgressBar(){
        ProgressBar progressBar = findViewById(R.id.pb_recipes);
        progressBar.setVisibility(View.INVISIBLE);
        recipesList.setVisibility(View.VISIBLE);
    }
}

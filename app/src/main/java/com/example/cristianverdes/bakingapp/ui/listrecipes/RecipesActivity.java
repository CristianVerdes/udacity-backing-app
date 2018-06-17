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
    private static final String KEY_SCROLL_INDEX = "key_scroll_index";

    private RecipesViewModel recipesViewModel;
    private RecipesAdapter recipesAdapter;
    private RecyclerView recipesList;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;

    private int listScrollIndex = -1;
    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        isTablet = getResources().getBoolean(R.bool.is_tablet);
        // Set Layout Manager
        if (isTablet) {
            // Tablet
            recipesList = findViewById(R.id.rv_recipes_grid);
            gridLayoutManager = new GridLayoutManager(this, 3);
            recipesList.setLayoutManager(gridLayoutManager);
        } else {
            // Phone
            recipesList = findViewById(R.id.rv_recipes);
            linearLayoutManager = new LinearLayoutManager(this);
            recipesList.setLayoutManager(linearLayoutManager);
        }

        setAdapterToRecyclerView();

        instantiateViewModel();

        subscribeToDataStream();
    }

    // Save and restore state
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int scrollIndex;

        if (isTablet) {
            // Tablet
            scrollIndex = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
        } else {
            // Phone
            scrollIndex = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
        }

        outState.putInt(KEY_SCROLL_INDEX, scrollIndex);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            listScrollIndex = savedInstanceState.getInt(KEY_SCROLL_INDEX);
        }
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
                    if (listScrollIndex != -1) {
                        if (isTablet) {
                            // Tablet
                            gridLayoutManager.scrollToPositionWithOffset(listScrollIndex, 0);
                        } else {
                            // Phone
                            linearLayoutManager.scrollToPositionWithOffset(listScrollIndex, 0);
                        }
                    }
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

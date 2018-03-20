package com.example.cristianverdes.bakingapp.ui.listrecipes;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.cristianverdes.bakingapp.data.model.Recipe;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by cristian.verdes on 14.03.2018.
 */

public class RecipesViewModel extends ViewModel {
    private GetRecipesUseCase getRecipesUseCase;
    private MutableLiveData<List<Recipe>> recipes = new MutableLiveData<>();

    // Constructor
    public RecipesViewModel(){
        this.getRecipesUseCase = new GetRecipesUseCase();
        loadRecipes();
    }

    // Getters
    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    public void loadRecipes() {
        this.getRecipesUseCase.get()
        .subscribe(new Consumer<List<Recipe>>() {
            @Override
            public void accept(List<Recipe> recipes) throws Exception {
                RecipesViewModel.this.recipes.setValue(recipes);
            }
        });
    }
}

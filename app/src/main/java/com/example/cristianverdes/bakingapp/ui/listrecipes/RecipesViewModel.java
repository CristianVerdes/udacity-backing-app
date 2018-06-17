package com.example.cristianverdes.bakingapp.ui.listrecipes;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.cristianverdes.bakingapp.data.model.Recipe;
import com.example.cristianverdes.bakingapp.data.repository.RecipesRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by cristian.verdes on 14.03.2018.
 */

public class RecipesViewModel extends ViewModel {
    private RecipesRepository recipesRepository;
    private MutableLiveData<List<Recipe>> recipes = new MutableLiveData<>();

    // Constructor
    public RecipesViewModel(RecipesRepository recipesRepository){
        this.recipesRepository = recipesRepository;
        loadRecipes();
    }

    // Getters
    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    public void loadRecipes() {
        recipesRepository.loadRecipes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Recipe>>() {
                        @Override
                        public void accept(List<Recipe> recipes) throws Exception {
                            RecipesViewModel.this.recipes.setValue(recipes);
                        }
        });
    }
}

package com.example.cristianverdes.bakingapp.data.repositories;

import com.example.cristianverdes.bakingapp.data.model.Recipe;
import com.example.cristianverdes.bakingapp.data.remote.RecipesApiClient;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by cristian.verdes on 14.03.2018.
 */

public class RecipesRepository {
    private static RecipesRepository recipesRepository;

    public static RecipesRepository getInstance() {
        if (recipesRepository != null) {
            return recipesRepository;
        } else {
            recipesRepository = new RecipesRepository();
            return recipesRepository;
        }
    }

    private RecipesRepository(){}

    public Observable<List<Recipe>> loadRecipes() {
        return RecipesApiClient.getRecipesApiService().getRecipes();
    }

}

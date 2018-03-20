package com.example.cristianverdes.bakingapp.ui.listrecipes;

import com.example.cristianverdes.bakingapp.data.model.Recipe;
import com.example.cristianverdes.bakingapp.data.repositories.RecipesRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by cristian.verdes on 15.03.2018.
 */

public class GetRecipesUseCase {
    private RecipesRepository recipesRepository;

    public GetRecipesUseCase() {
        this.recipesRepository = RecipesRepository.getInstance();
    }

    public Observable<List<Recipe>> get() {
        return recipesRepository.loadRecipes().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

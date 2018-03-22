package com.example.cristianverdes.bakingapp.data.repository;

import android.os.AsyncTask;

import com.example.cristianverdes.bakingapp.data.local.AppDatabase;
import com.example.cristianverdes.bakingapp.data.model.Recipe;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by cristian.verdes on 22.03.2018.
 */

public class GetDataFromDbTask extends AsyncTask<Void, Void, List<Recipe>> {
    private PublishSubject<List<Recipe>> recipesObservable = PublishSubject.create();
    private WeakReference<AppDatabase> appDatabaseWeakReference;

    // Class
    public GetDataFromDbTask(AppDatabase appDatabase){
        appDatabaseWeakReference = new WeakReference<>(appDatabase);
    }

    public Observable<List<Recipe>> getRecipesObservable() {
        return recipesObservable;
    }

    // EXTENDED: AsyncTask
    @Override
    protected List<Recipe> doInBackground(Void... voids) {
        // List Container
        List<Recipe> recipes = new ArrayList<>();

        // Get Data from DB
        for (Recipe recipe: appDatabaseWeakReference.get().recipeDao().getAll()){
            recipe.setIngredients(appDatabaseWeakReference.get().ingredientDao().getIngredientsByRecipeId(recipe.getId()));
            recipe.setSteps(appDatabaseWeakReference.get().stepDao().getAllByRecipeId(recipe.getId()));
            recipes.add(recipe);
        }
        return recipes;
    }

    @Override
    protected void onPostExecute(List<Recipe> recipes) {
        this.recipesObservable.onNext(recipes);
    }
}
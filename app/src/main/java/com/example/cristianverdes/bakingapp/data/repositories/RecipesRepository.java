package com.example.cristianverdes.bakingapp.data.repositories;

import android.content.Context;
import android.os.AsyncTask;

import com.example.cristianverdes.bakingapp.data.local.AppDatabase;
import com.example.cristianverdes.bakingapp.data.model.Ingredient;
import com.example.cristianverdes.bakingapp.data.model.Recipe;
import com.example.cristianverdes.bakingapp.data.model.Step;
import com.example.cristianverdes.bakingapp.data.remote.RecipesApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import static com.example.cristianverdes.bakingapp.utils.Preconditions.checkNotNull;

/**
 * Created by cristian.verdes on 14.03.2018.
 */

public class RecipesRepository {
    private static RecipesRepository recipesRepository;
    private boolean dataLoadedFromInternet = false;
    private AppDatabase appDatabase;

    private PublishSubject<List<Recipe>> recipesPublishSubject = PublishSubject.create() ;

    public static RecipesRepository getInstance(Context context) {
        checkNotNull(context);
        if (recipesRepository != null) {
            return recipesRepository;
        } else {
            recipesRepository = new RecipesRepository(context);
            return recipesRepository;
        }
    }

    // Constructors
    private RecipesRepository(){}

    private RecipesRepository(Context context) {
        this.appDatabase = AppDatabase.getAppDatabase(context);
    }

    // Object Functions
    public Observable<List<Recipe>> loadRecipes() {
        if (dataLoadedFromInternet) {
            // GET DATA FROM DATABASE
            GetDataFromDbTask getDataFromDbTask = new GetDataFromDbTask();
            getDataFromDbTask.getRecipesObservable().subscribe(new Consumer<List<Recipe>>() {
                @Override
                public void accept(List<Recipe> recipes) throws Exception {
                    recipesPublishSubject.onNext(recipes);
                }
            });
            getDataFromDbTask.execute();

            // Create Rx Observable and return it;
            return recipesPublishSubject;
        } else {
            // GET DATA FROM INTERNET
            Observable<List<Recipe>> recipesObservable =  RecipesApiClient.getRecipesApiService().getRecipes();

            // Add data in database
            recipesObservable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<List<Recipe>>() {
                    @Override
                    public void accept(List<Recipe> recipes) throws Exception {
                        // We could add here a timer so that every 30 minutes we'll get new data to cache.
                        // But since the API gives the same data we'll ignore this aspect.
                        if (appDatabase.recipeDao().getAll().size() == 0) {
                            for (Recipe recipe : recipes) {
                                for (Step step : recipe.getSteps()) {
                                    step.setRecipeId(recipe.getId());
                                    appDatabase.stepDao().insertAll(step);
                                }
                                for (Ingredient ingredient : recipe.getIngredients()) {
                                    ingredient.setRecipeId(recipe.getId());
                                    appDatabase.ingredientDao().insertAll(ingredient);
                                }
                                appDatabase.recipeDao().insertAll(recipe);
                            }
                        }
                    }
                }
            );

            // Data loaded from internet and saved in database
            dataLoadedFromInternet = true;

            return recipesObservable;
        }
    }

    private class GetDataFromDbTask extends AsyncTask<Void, Void, List<Recipe>> {
        private PublishSubject<List<Recipe>> recipesObservable = PublishSubject.create();

        // Class
        public GetDataFromDbTask(){}

        public Observable<List<Recipe>> getRecipesObservable() {
            return recipesObservable;
        }

        // EXTENDED: AsyncTask
        @Override
        protected List<Recipe> doInBackground(Void... voids) {
            // List Container
            List<Recipe> recipes = new ArrayList<>();

            // Get Data from DB
            for (Recipe recipe: appDatabase.recipeDao().getAll()){
                recipe.setIngredients(appDatabase.ingredientDao().getIngredientsByRecipeId(recipe.getId()));
                recipe.setSteps(appDatabase.stepDao().getAllByRecipeId(recipe.getId()));
                recipes.add(recipe);
            }
            return recipes;
        }

        @Override
        protected void onPostExecute(List<Recipe> recipes) {
            this.recipesObservable.onNext(recipes);
        }
    }
}

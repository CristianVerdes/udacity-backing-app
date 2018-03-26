package com.example.cristianverdes.bakingapp.data.repository;

import android.content.Context;

import com.example.cristianverdes.bakingapp.data.local.AppDatabase;
import com.example.cristianverdes.bakingapp.data.model.Ingredient;
import com.example.cristianverdes.bakingapp.data.model.Recipe;
import com.example.cristianverdes.bakingapp.data.model.Step;
import com.example.cristianverdes.bakingapp.data.model.Timer;
import com.example.cristianverdes.bakingapp.data.remote.RecipesApiClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
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
    private boolean cachedTrigger;

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
    private RecipesRepository(Context context) {
        this.appDatabase = AppDatabase.getAppDatabase(context);
    }

    // Object Functions
    public Observable<List<Recipe>> loadRecipes() {

        // Access Database and verify if is data cached
        GetTimerFromDbTask getTimerFromDbTask = new GetTimerFromDbTask(appDatabase);
        getTimerFromDbTask.execute();
        getTimerFromDbTask.getIsDataCached().subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean isDataCached) throws Exception {
                RecipesRepository.this.cachedTrigger = isDataCached;
                if (isDataCached) {
                    // GET DATA FROM DATABASE
                    GetDataFromDbTask getDataFromDbTask = new GetDataFromDbTask(appDatabase);
                    getDataFromDbTask.getRecipesObservable().subscribe(new Consumer<List<Recipe>>() {
                        @Override
                        public void accept(List<Recipe> recipes) throws Exception {
                            // Notify Observable
                            recipesPublishSubject.onNext(recipes);
                        }
                    });
                    getDataFromDbTask.execute();
                } else {
                    // GET DATA FROM INTERNET
                    Observable<List<Recipe>> recipesObservable =  RecipesApiClient.getRecipesApiService().getRecipes();

                    // Insert data in database
                    recipesObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Consumer<List<Recipe>>() {
                               @Override
                               public void accept(List<Recipe> recipes) throws Exception {
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

                                   // Notify Observable
                                   recipesPublishSubject.onNext(recipes);
                               }
                        });

                    // Data loaded from internet and saved in database
                    // Saving timer
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    Date currentDate = new Date();
                    String currentTime = sdf.format(currentDate);
                    appDatabase.timerDao().insertAll(new Timer(currentTime));

                }
            }
        });

        // Create Rx Observable and return it;
        return recipesPublishSubject;
    }


}

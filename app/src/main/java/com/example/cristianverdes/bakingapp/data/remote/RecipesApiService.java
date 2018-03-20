package com.example.cristianverdes.bakingapp.data.remote;

import com.example.cristianverdes.bakingapp.data.model.Recipe;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by cristian.verdes on 14.03.2018.
 */

public interface RecipesApiService {
    @GET("2017/May/59121517_baking/baking.json")
    Observable<List<Recipe>> getRecipes();
}

package com.example.cristianverdes.bakingapp.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cristian.verdes on 14.03.2018.
 */

public class RecipesApiClient {
    private static Retrofit retrofit = null;
    private static RecipesApiService recipesApiService;

    private static void createClient() {
        Gson gson = new GsonBuilder().create();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkConstants.bakingUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
    }

    public static RecipesApiService getRecipesApiService() {
        createClient();
        if (recipesApiService == null) {
            recipesApiService = retrofit.create(RecipesApiService.class);
        }

        return recipesApiService;
    }
}

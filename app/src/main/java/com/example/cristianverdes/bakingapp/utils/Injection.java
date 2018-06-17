package com.example.cristianverdes.bakingapp.utils;

import android.content.Context;

import com.example.cristianverdes.bakingapp.data.repository.RecipesRepository;

import static com.example.cristianverdes.bakingapp.utils.Preconditions.checkNotNull;

/**
 * Created by cristian.verdes on 21.03.2018.
 */

public class Injection {
    public static RecipesRepository provideRecipesRepository(Context context) {
        checkNotNull(context);
        return RecipesRepository.getInstance(context);
    }
}

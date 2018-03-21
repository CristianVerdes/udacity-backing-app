package com.example.cristianverdes.bakingapp.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.cristianverdes.bakingapp.data.model.Ingredient;

import java.util.List;

/**
 * Created by cristian.verdes on 20.03.2018.
 */

@Dao
public interface IngredientDao {

    @Query("SELECT * FROM INGREDIENTS WHERE recipe_id = :recipeId")
    List<Ingredient> getIngredientsByRecipeId(int recipeId);

    @Query("SELECT COUNT(*) FROM INGREDIENTS")
    int countIngredients();

    @Insert
    void insertAll(Ingredient... ingredients);

    @Delete
    void delete(Ingredient ingredient);
}

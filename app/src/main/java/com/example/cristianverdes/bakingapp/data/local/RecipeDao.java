package com.example.cristianverdes.bakingapp.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.cristianverdes.bakingapp.data.model.Recipe;

import java.util.List;

/**
 * Created by cristian.verdes on 20.03.2018.
 */

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipes")
    List<Recipe> getAll();

    @Query("SELECT * FROM recipes WHERE id = :id")
    Recipe getRecipeById(int id);

    @Query("SELECT COUNT(*) FROM recipes")
    int countRecipes();

    @Insert
    void insertAll(Recipe... recipes);

    @Delete
    void delete(Recipe recipe);
}

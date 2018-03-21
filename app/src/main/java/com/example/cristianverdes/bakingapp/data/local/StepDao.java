package com.example.cristianverdes.bakingapp.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.cristianverdes.bakingapp.data.model.Step;

import java.util.List;

/**
 * Created by cristian.verdes on 20.03.2018.
 */

@Dao
public interface StepDao {

    @Query("SELECT * FROM STEPS WHERE recipe_id = :recipeId")
    List<Step> getAllByRecipeId(int recipeId);

    @Query("SELECT * FROM STEPS WHERE recipe_id = :recipeId AND stepId = :stepId")
    Step getStepById(int recipeId, int stepId);

    @Query("SELECT COUNT(*) FROM STEPS")
    int countRecipes();

    @Insert
    void insertAll(Step... steps);

    @Delete
    void delete(Step step);
}

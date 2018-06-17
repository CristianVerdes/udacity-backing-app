package com.example.cristianverdes.bakingapp.data.local;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.cristianverdes.bakingapp.data.model.Ingredient;
import com.example.cristianverdes.bakingapp.data.model.Recipe;
import com.example.cristianverdes.bakingapp.data.model.Step;
import com.example.cristianverdes.bakingapp.data.model.Timer;

/**
 * Created by cristian.verdes on 20.03.2018.
 */

@Database(entities = {Recipe.class, Step.class, Ingredient.class, Timer.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase databaseInstance;

    public abstract RecipeDao recipeDao();
    public abstract StepDao stepDao();
    public abstract IngredientDao ingredientDao();
    public abstract TimerDao timerDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "recipes-database")
                    .build();
        }

        return databaseInstance;
    }

    public static void destroyInstance() {
        databaseInstance = null;
    }

    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }
}

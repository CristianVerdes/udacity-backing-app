package com.example.cristianverdes.bakingapp.ui.recipe;

import java.util.Observable;

/**
 * Created by cristian.verdes on 19.03.2018.
 */

public class ChangeStepFragmentData extends Observable {
    private int recipeId;
    private int stepId;

    // Constructor
    public ChangeStepFragmentData() {
        this.recipeId = 1;
        this.stepId = 0;
    }

    // Getters
    public int getRecipeId() {
        return recipeId;
    }

    public int getStepId() {
        return stepId;
    }

    // Setters
    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }
}

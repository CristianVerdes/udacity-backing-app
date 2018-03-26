package com.example.cristianverdes.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.example.cristianverdes.bakingapp.data.model.Recipe;
import com.example.cristianverdes.bakingapp.ui.listrecipes.RecipesActivity;
import com.example.cristianverdes.bakingapp.ui.recipe.RecipeActivity;

import android.support.test.espresso.contrib.RecyclerViewActions;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;


/**
 * Created by cristian.verdes on 18.03.2018.
 */
@RunWith(AndroidJUnit4.class)
public class RecipesActivityTest {
    // TESTING:
    // 1. Find the View
    // 2. Perform action on the View
    // 3. Check if the view does what you expected

    @Rule public ActivityTestRule<RecipesActivity> activityActivityTestRule
            = new ActivityTestRule<>(RecipesActivity.class);

    @Test
    public void scrollRecyclerView() {
        // Wait for data to load
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Get total items from RecyclerView
        RecyclerView recipesRv = activityActivityTestRule.getActivity().findViewById(R.id.rv_recipes);
        int itemCount = recipesRv.getAdapter().getItemCount();

        // Test Scroll RecyclerView ( Unfortunately There are only 4 recipes :( )
        onView(withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.scrollToPosition(itemCount - 1));
    }

    @Test
    public void clickRecyclerView() {
        // Test click on RecyclerView
        onView(withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
    }
}

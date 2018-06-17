package com.example.cristianverdes.bakingapp.widgets;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Spinner;

import com.example.cristianverdes.bakingapp.R;
import com.example.cristianverdes.bakingapp.data.local.AppDatabase;
import com.example.cristianverdes.bakingapp.data.model.Recipe;
import com.example.cristianverdes.bakingapp.data.repository.GetDataFromDbTask;
import com.example.cristianverdes.bakingapp.data.repository.RecipesRepository;
import com.example.cristianverdes.bakingapp.utils.Injection;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by cristian.verdes on 22.03.2018.
 */

public class IngredientsWidgetConfAct extends Activity {
    public List<String> recipesNames = new ArrayList<>();
    private int appWidgetId;
    private AppWidgetManager appWidgetManager;
    private Context context = this;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_activity_ingredients);
        setResult(RESULT_CANCELED);

        // Set WidgetId from the Widget that started this Activity;
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // Get an instance of the AppWidgetManager:
        appWidgetManager = AppWidgetManager.getInstance(this);
        appWidgetManager.updateAppWidget(appWidgetId, null);

        // Instantiate Repository
        RecipesRepository recipesRepository = Injection.provideRecipesRepository(this);

        // Subscribe to Repository
        // (Data may come from the internet or from Database, depends if we have data cached at that moment)
        recipesRepository.loadRecipes().subscribe(new Consumer<List<Recipe>>() {
            @Override
            public void accept(List<Recipe> recipes) throws Exception {
                for (Recipe recipe : recipes) {
                    recipesNames.add(recipe.getName());
                    createSpinnerAdapter();
                }
            }
        });


    }

    private void createSpinnerAdapter() {
        // Get Spinner
        final Spinner recipesSpinner = findViewById(R.id.sp_recipes);

        // create adapter with Recipes Names
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, recipesNames.toArray(new String[recipesNames.size()]));

        // Set Names to spinner
        recipesSpinner.setAdapter(spinnerAdapter);

        Button doneButton = findViewById(R.id.bt_done);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int recipeId = recipesSpinner.getSelectedItemPosition() + 1;

                // Create a new RemoteViews object with the widget layout resource
                RemoteViews views = new RemoteViews(context.getPackageName(),
                        R.layout.widget_ingredients);

                // Trigger new IngredientsRemoteViewFactory for every widget
                appWidgetManager.updateAppWidget(appWidgetId, views);

                // Set IngredientsWidgetService intent to act as the adapter for the ListView
                Intent intent = new Intent(context, IngredientsWidgetService.class);
                intent.setData(Uri.fromParts("content", String.valueOf(recipeId), null));
                views.setRemoteAdapter(R.id.widget_list_view, intent);

                // Set title to widget
                CharSequence widgetText = recipesNames.get(recipesSpinner.getSelectedItemPosition());
                views.setTextViewText(R.id.appwidget_recipe_title, widgetText);

                // Update the app widget with the widget ID and the new remote view:
                appWidgetManager.updateAppWidget(appWidgetId, views);

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        appWidgetId);
                setResult(RESULT_OK, resultValue);

                finish();

            }

        });
    }
}

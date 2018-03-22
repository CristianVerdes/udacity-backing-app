package com.example.cristianverdes.bakingapp.widgets;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.cristianverdes.bakingapp.R;
import com.example.cristianverdes.bakingapp.data.local.AppDatabase;
import com.example.cristianverdes.bakingapp.data.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

class IngredientsRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    Context context;
    private List<Ingredient> ingredients = new ArrayList<>();

    public IngredientsRemoteViewFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        ingredients = AppDatabase.getAppDatabase(context).ingredientDao().getIngredientsByRecipeId(1);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        // Get Ingredient from list
        Ingredient ingredient = ingredients.get(position);

        // Get Ingredient title
        String ingredientTitle = ingredient.getIngredient();

        // Get Widget layout
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients);

        // Set Widget title
        views.setTextViewText(R.id.appwidget_recipe_title, ingredientTitle);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}

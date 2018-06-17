package com.example.cristianverdes.bakingapp.widgets;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.cristianverdes.bakingapp.R;
import com.example.cristianverdes.bakingapp.data.local.AppDatabase;
import com.example.cristianverdes.bakingapp.data.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

class IngredientsRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private int recipeId;
    Context context;
    private List<Ingredient> ingredients = new ArrayList<>();

    public IngredientsRemoteViewFactory(Context context, int recipeId) {
        this.context = context;
        this.recipeId = recipeId;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        ingredients = AppDatabase.getAppDatabase(context).ingredientDao().getIngredientsByRecipeId(recipeId);
    }

    @Override
    public void onDestroy() {
        AppDatabase.destroyInstance();
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
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);

        // Set Widget title
        views.setTextViewText(R.id.tv_recipe_title, ingredientTitle);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

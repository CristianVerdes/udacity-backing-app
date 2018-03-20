package com.example.cristianverdes.bakingapp.ui.listrecipes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cristianverdes.bakingapp.R;
import com.example.cristianverdes.bakingapp.data.model.Recipe;
import com.example.cristianverdes.bakingapp.ui.recipe.RecipeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cristian.verdes on 14.03.2018.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder>{
    private List<Recipe> recipes;

    public RecipesAdapter(){
        this.recipes = new ArrayList<>();
    }

    @Override
    public RecipesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_item, parent, false);
        return new RecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipesViewHolder holder, int position) {
        holder.bind(recipes.get(position));
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class RecipesViewHolder extends RecyclerView.ViewHolder {

        public RecipesViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(final Recipe recipe) {
            // We'll use the itemView from the super class
            TextView recipeTitle = itemView.findViewById(R.id.tv_recipe_title);
            recipeTitle.setText(recipe.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecipeActivity.start(itemView.getContext(), recipe.getId(), recipe.getName());
                }
            });
        }
    }
}

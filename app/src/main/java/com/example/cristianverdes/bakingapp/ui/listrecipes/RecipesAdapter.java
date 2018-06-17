package com.example.cristianverdes.bakingapp.ui.listrecipes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cristianverdes.bakingapp.R;
import com.example.cristianverdes.bakingapp.data.model.Recipe;
import com.example.cristianverdes.bakingapp.ui.recipe.RecipeActivity;
import com.squareup.picasso.Picasso;

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
            // TITLE
            TextView recipeTitle = itemView.findViewById(R.id.tv_recipe_title);
            recipeTitle.setText(recipe.getName());

            // IMAGE
            ImageView recipeImage = itemView.findViewById(R.id.iv_recipe_image);
            // Verify if there is an image Url in the json
            if (recipe.getImage() != null && !TextUtils.isEmpty(recipe.getImage())) {
                // Load image
                Picasso.get().load(recipe.getImage()).into(recipeImage);
            }

            // CLICK LISTENER
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecipeActivity.start(itemView.getContext(), recipe.getId(), recipe.getName());
                }
            });
        }
    }
}

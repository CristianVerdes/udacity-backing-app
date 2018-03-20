package com.example.cristianverdes.bakingapp.ui.ingredients;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cristianverdes.bakingapp.R;
import com.example.cristianverdes.bakingapp.data.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cristian.verdes on 17.03.2018.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {
    private List<Ingredient> ingredients;

    public IngredientsAdapter() {
        this.ingredients = new ArrayList<>();
    }

    @Override
    public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_ingredients, parent, false);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsViewHolder holder, int position) {
        holder.bind(ingredients.get(position));
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder{
        public IngredientsViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(Ingredient ingredient) {
            TextView ingredientTitle = itemView.findViewById(R.id.tv_ingredient_title);
            TextView measure = itemView.findViewById(R.id.tv_measure);
            TextView quantity = itemView.findViewById(R.id.tv_quantity);

            ingredientTitle.setText(ingredient.getIngredient());
            measure.setText(ingredient.getMeasure());
            quantity.setText(String.valueOf(ingredient.getQuantity()));
        }
    }
}

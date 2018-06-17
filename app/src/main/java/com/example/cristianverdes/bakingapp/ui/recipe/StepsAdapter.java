package com.example.cristianverdes.bakingapp.ui.recipe;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cristianverdes.bakingapp.R;
import com.example.cristianverdes.bakingapp.data.model.Step;
import com.example.cristianverdes.bakingapp.ui.step.StepActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cristian.verdes on 15.03.2018.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {
    private int recipeId;
    private String recipeName;
    private List<Step> steps;
    private boolean isTablet = false;

    private ChangeStepFragmentData changeStepFragmentData = new ChangeStepFragmentData();
    private MutableLiveData<ChangeStepFragmentData> changeFragmentObservable = new MutableLiveData<>();

    public StepsAdapter(int recipeId) {
        this.steps = new ArrayList<>();
        this.recipeId = recipeId;
    }

    @Override
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_item, parent, false);

        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder, int position) {
        holder.bind(steps.get(position), position, steps.size());
    }

    // Setters
    public void setSteps(List<Step> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public void setIsTablet(@Nullable boolean tablet) {
        this.isTablet = tablet;
    }

    // Getters
    public MutableLiveData<ChangeStepFragmentData> getChangeFragmentObservable() {
        return changeFragmentObservable;
    }

    // CLASS: View Holder
    public class StepsViewHolder extends RecyclerView.ViewHolder {

        public StepsViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(final Step step, final int stepId, final int stepsSize) {
            TextView title = itemView.findViewById(R.id.tv_recipe_title);
            title.setText(step.getShortDescription());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isTablet) {
                        changeStepFragmentData.setRecipeId(recipeId);
                        changeStepFragmentData.setStepId(stepId);
                        changeFragmentObservable.setValue(changeStepFragmentData);

                    } else {
                        StepActivity.start(itemView.getContext(), recipeId, stepId, stepsSize, recipeName);
                    }
                }
            });
        }
    }
}

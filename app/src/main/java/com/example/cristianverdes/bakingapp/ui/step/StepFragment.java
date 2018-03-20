package com.example.cristianverdes.bakingapp.ui.step;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cristianverdes.bakingapp.R;
import com.example.cristianverdes.bakingapp.data.model.Recipe;
import com.example.cristianverdes.bakingapp.data.model.Step;
import com.example.cristianverdes.bakingapp.ui.listrecipes.RecipesViewModel;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

/**
 * Created by cristian.verdes on 16.03.2018.
 */

public class StepFragment extends Fragment {
    private static final String RECIPE_ID = "recipeId";
    private static final String STEP_ID = "stepId";
    private static final String STEPS_SIZE = "stepsSize";

    private static final String TAG = StepFragment.class.getSimpleName();
    private RecipesViewModel recipesViewModel;
    private int recipeId;
    private int stepId;
    private int stepsSize;
    private View rootView;
    private ImageView noVideoIv;
    private TextView noVideoTv;

    // ExoPlayer
    private SimpleExoPlayer player;

    // Views
    private SimpleExoPlayerView playerView;
    private TextView instructions;
    private Button previous;
    private Button next;

    // Initialize and Release Player
    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // View
        rootView = inflater.inflate(R.layout.fragment_step, container, false);

        playerView = rootView.findViewById(R.id.exo_video_step);
        instructions = rootView.findViewById(R.id.tv_instructions_text);
        previous = rootView.findViewById(R.id.bt_previous);
        next = rootView.findViewById(R.id.bt_next);
        noVideoIv = rootView.findViewById(R.id.iv_no_video);
        noVideoTv = rootView.findViewById(R.id.tv_no_video);

        // Get data from Bundle
        recipeId = getArguments().getInt("recipeId");
        stepId = getArguments().getInt("stepId");
        stepsSize = getArguments().getInt("stepsSize");

        // Set previous / next buttons listeners
        setPreviousNextButtonsListeners();

        // Hide Previous / Next Buttons if needed
        if (stepId == 0) {
            previous.setVisibility(View.INVISIBLE);
        }
        if (stepId == stepsSize - 1) {
            next.setVisibility(View.INVISIBLE);
        }

        // Create View Model
        createViewModel();

        // Subscribe to Observable stream from ViewModel
        subscribeToDataStreams();

        return rootView;
    }


    private void setPreviousNextButtonsListeners() {
        if (getArguments().getBoolean("twoPane")) {
            previous.setVisibility(View.INVISIBLE);
            next.setVisibility(View.INVISIBLE);
        } else {
            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StepFragment stepFragment = new StepFragment();
                    // Create Bundle
                    int stepIdArg = stepId - 1;
                    Bundle bundle = new Bundle();
                    bundle.putInt(RECIPE_ID, recipeId);
                    bundle.putInt(STEP_ID, stepIdArg);
                    bundle.putInt(STEPS_SIZE, stepsSize);
                    stepFragment.setArguments(bundle);

                    stepFragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, stepFragment)
                            .commit();
                }
            });

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StepFragment stepFragment = new StepFragment();
                    // Create Bundle
                    int stepIdArg = stepId + 1;
                    Bundle bundle = new Bundle();
                    bundle.putInt(RECIPE_ID, recipeId);
                    bundle.putInt(STEP_ID, stepIdArg);
                    bundle.putInt(STEPS_SIZE, stepsSize);
                    stepFragment.setArguments(bundle);

                    stepFragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, stepFragment)
                            .commit();
                }
            });
        }
    }

    private void createViewModel() {
        recipesViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);
    }

    private void subscribeToDataStreams() {
        recipesViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes != null) {
                    List<Step> steps = recipes.get(recipeId - 1).getSteps();
                    if (steps != null) {
                        // Hide progressbar and show data
                        hideProgressbar();
                        Step step = steps.get(stepId);
                        instructions.setText(step.getDescription());

                        // Video data
                        if (!step.getVideoURL().equals("")) {
                            noVideoIv.setVisibility(View.INVISIBLE);
                            noVideoTv.setVisibility(View.INVISIBLE);
                            Uri uri = Uri.parse(step.getVideoURL());
                            MediaSource mediaSource = buildMediaSource(uri);
                            player.prepare(mediaSource, true, false);
                        } else {
                            // Hide video player and show no video available
                            playerView.setVisibility(View.INVISIBLE);
                            noVideoIv.setVisibility(View.VISIBLE);
                            noVideoTv.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.e(TAG, "Error: No Steps");
                    }
                } else {
                    Log.e(TAG, "Error: No recipes");
                }
            }
        });
    }

    public void hideProgressbar() {
        ProgressBar progressBar = rootView.findViewById(R.id.pb_step);
        progressBar.setVisibility(View.INVISIBLE);

        playerView.setVisibility(View.VISIBLE);
        TextView instructionsTitle = rootView.findViewById(R.id.tv_instructions_title);
        instructionsTitle.setVisibility(View.VISIBLE);
        instructions.setVisibility(View.VISIBLE);
    }

    // ExoPlayer support functions
    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());
        playerView.setPlayer(player);
        player.setPlayWhenReady(true);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer"))
                .createMediaSource(uri);
    }
}

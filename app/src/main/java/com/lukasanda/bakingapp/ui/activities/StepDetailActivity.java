package com.lukasanda.bakingapp.ui.activities;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lukasanda.bakingapp.R;
import com.lukasanda.bakingapp.data.Recipe;
import com.lukasanda.bakingapp.data.RecipeStep;
import com.lukasanda.bakingapp.ui.fragments.StepDetailFragment;
import com.lukasanda.bakingapp.ui.fragments.StepVideoFragment;
import com.lukasanda.bakingapp.utils.NetworkUtils;

import java.util.List;

/**
 * Created by lukas on 3/13/2018.
 */

public class StepDetailActivity extends AppCompatActivity {
    
    public static final String RECIPE_STEP_KEY = "recipeStep";
    
    public static final String RECIPE_KEY = "recipeKey";
    
    private static final String TAG = StepDetailActivity.class.getSimpleName();
    
    private RecipeStep currentStep;
    
    private Recipe recipe;
    
    private StepVideoFragment videoFragment;
    
    private StepDetailFragment detailFragment;
    
    private long position = 0;
    
    private boolean isPlaying = true;
    
    
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);
        
        if (savedInstanceState == null) {
            try {
                currentStep = getIntent().getExtras().getParcelable(RECIPE_STEP_KEY);
                recipe = getIntent().getExtras().getParcelable(RECIPE_KEY);
            } catch (NullPointerException e) {
                Log.d(TAG, "Recipe from intent was null: " + e.getMessage());
                finish();
                return;
            }
        } else {
            currentStep = savedInstanceState.getParcelable(RECIPE_STEP_KEY);
            recipe = savedInstanceState.getParcelable(RECIPE_KEY);
            position = savedInstanceState.getLong(StepVideoFragment.SAVE_POSITION,
                    StepVideoFragment.INVALID);
            isPlaying = savedInstanceState.getBoolean(StepVideoFragment.VIDEO_PLAYING, true);
        }
        
        if (currentStep == null) {
            Toast.makeText(this, "Recipe was null... exiting", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // landscape mode == video fragment
        // portrait mode == video + details + next_button
        videoFragment = (StepVideoFragment) getFragmentManager().findFragmentById(R.id
                .video_fragment);
        
        detailFragment = (StepDetailFragment) getFragmentManager()
                .findFragmentById(R.id.step_detail_fragment);
        
        if (detailFragment != null) {
            // populate information
            loadData(currentStep, true);
            findViewById(R.id.next_step_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // check if this currentStep is the last currentStep in the steps array
                    List<RecipeStep> steps = recipe.getSteps();
                    if (currentStep.getId() == steps.size() - 1) {
                        finish();
                        return;
                    }
                    
                    
                    RecipeStep nextStep = null;
                    for (RecipeStep step : steps) {
                        if (step.getId() == currentStep.getId() + 1) {
                            nextStep = step;
                            break;
                        }
                    }
                    currentStep = nextStep;
                    loadData(currentStep, true);
                }
            });
        } else {
            loadData(currentStep, false);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }
    
    public void loadData(RecipeStep step, boolean showDetail) {
        if (showDetail) detailFragment.setText(step.getDescription());
        handleStepUrl();
    }
    
    private void handleStepUrl() {
        if (!TextUtils.isEmpty(currentStep.getVideoURL())) {
            if (NetworkUtils.isVideoURL(currentStep.getVideoURL())) {
                videoFragment.initPlayer(Uri.parse(currentStep.getVideoURL()), position, isPlaying);
            } else {
                showImage();
            }
        } else if (!TextUtils.isEmpty(currentStep.getThumbnailURL())) {
            showImage();
        } else {
            videoFragment.stopPlayer();
            videoFragment.reset();
            Log.d(TAG, "URL and thumbnailURL were null");
        }
    }
    
    private void showImage() {
        videoFragment.stopPlayer();
        if (!NetworkUtils.isImageURL(currentStep.getThumbnailURL())) {
            videoFragment.reset();
            return;
        }
        videoFragment.showImage(Uri.parse(currentStep.getThumbnailURL()));
    }
    
    @SuppressLint("MissingSuperCall")
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RECIPE_STEP_KEY, currentStep);
        outState.putParcelable(RECIPE_KEY, recipe);
        videoFragment.onSaveInstanceState(outState);
        position = outState.getLong(StepVideoFragment.SAVE_POSITION, -1);
        isPlaying = outState.getBoolean(StepVideoFragment.VIDEO_PLAYING, false);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    
}

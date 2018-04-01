package com.lukasanda.bakingapp.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.lukasanda.bakingapp.R;
import com.lukasanda.bakingapp.data.Recipe;
import com.lukasanda.bakingapp.data.RecipeStep;
import com.lukasanda.bakingapp.ui.adapters.RecipeStepAdapter;
import com.lukasanda.bakingapp.ui.fragments.RecipeStepsFragment;
import com.lukasanda.bakingapp.ui.fragments.StepDetailFragment;
import com.lukasanda.bakingapp.ui.fragments.StepVideoFragment;
import com.lukasanda.bakingapp.utils.NetworkUtils;

import java.net.MalformedURLException;

/**
 * Created by lukas on 3/13/2018.
 */

public class RecipeActivity extends AppCompatActivity implements RecipeStepAdapter
        .RecipeStepCallback {
    
    private static final String TAG = RecipeActivity.class.getSimpleName();
    
    public static final String RECIPE_KEY = "recipeInfo";
    
    public static final String FROM_WIDGET_KEY = "fromWidgetKey";
    
    private static final String SAVE_RECIPE_KEY = "saveRecipe";
    
    private static final String SAVE_FROM_WIDGET = "saveFromWidget";
    
    private static final String SAVE_RV_POSITION = "saveRvPosition";
    
    private static final String SAVE_STEP = "saveRecipeStep";
    
    private Recipe recipe;
    
    private RecipeStepsFragment detailFragment;
    
    private StepVideoFragment videoFragment;
    
    private StepDetailFragment stepDetailFragment;
    
    private boolean fromWidget;
    
    private Parcelable state;
    
    private long position;
    
    private boolean isPlaying = true;
    
    private RecipeStep currentStep = null;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        
        if (savedInstanceState == null) {
            try {
                recipe = getIntent().getExtras().getParcelable(RECIPE_KEY);
                fromWidget = getIntent().getExtras().getBoolean(FROM_WIDGET_KEY, false);
                if (recipe == null) {
                    Log.d(TAG, "Recipe from intent was null");
                    finish();
                    return;
                }
                currentStep = recipe.getSteps().first();
                setupView();
            } catch (NullPointerException e) {
                Log.d(TAG, "Recipe from intent was null: " + e.getMessage());
                finish();
            }
        } else {
            recipe = savedInstanceState.getParcelable(SAVE_RECIPE_KEY);
            fromWidget = getIntent().getExtras().getBoolean(FROM_WIDGET_KEY, false);
            state = savedInstanceState.getParcelable(SAVE_RV_POSITION);
            position = savedInstanceState.getLong(StepVideoFragment.SAVE_POSITION,
                    StepVideoFragment.INVALID);
            isPlaying = savedInstanceState.getBoolean(StepVideoFragment.VIDEO_PLAYING, true);
            currentStep = savedInstanceState.getParcelable(SAVE_STEP);
            setupView();
        }
        handleStepUrl();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
    }
    
    @SuppressLint("MissingSuperCall")
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVE_RECIPE_KEY, recipe);
        outState.putBoolean(SAVE_FROM_WIDGET, fromWidget);
        outState.putParcelable(SAVE_RV_POSITION, detailFragment.getSavedLayout());
        if (videoFragment != null) {
            videoFragment.onSaveInstanceState(outState);
            position = outState.getLong(StepVideoFragment.SAVE_POSITION, -1);
            isPlaying = outState.getBoolean(StepVideoFragment.VIDEO_PLAYING, false);
        }
        outState.putParcelable(SAVE_STEP, currentStep);
    }
    
    private void handleStepUrl() {
        if (currentStep != null && videoFragment != null) {
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
    }
    
    private void showImage() {
        videoFragment.stopPlayer();
        if (!NetworkUtils.isImageURL(currentStep.getThumbnailURL())) {
            videoFragment.reset();
            return;
        }
        videoFragment.showImage(Uri.parse(currentStep.getThumbnailURL()));
    }
    
    @Override
    public void onStepClicked(RecipeStep step, boolean reset) {
        currentStep = step;
        if (reset) {
            position = -1;  // reset
        }
        if (isTabletMode()) {
            stepDetailFragment.setText(step.getDescription());
            handleStepUrl();
        } else {
            Intent detailIntent = new Intent(this, StepDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(StepDetailActivity.RECIPE_STEP_KEY, step);
            bundle.putParcelable(StepDetailActivity.RECIPE_KEY, recipe);
            detailIntent.putExtras(bundle);
            startActivity(detailIntent);
        }
    }
    
    @Override
    public void onIngredientsClicked() {
        // launch activity of ingredients list
        Intent intent = new Intent(this, IngredientsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(IngredientsActivity.RECIPE_KEY, recipe);
        bundle.putString(IngredientsActivity.IMAGE_KEY, recipe.getImage());
        intent.putExtras(bundle);
        startActivity(intent);
    }
    
    private boolean isTabletMode() {
        return stepDetailFragment != null;
    }
    
    private void setupView() {
        detailFragment = (RecipeStepsFragment) getFragmentManager().findFragmentById(R.id
                .recipe_detail_fragment);
        detailFragment.setRecipeSteps(recipe.getSteps());
        detailFragment.setCallback(this);
        detailFragment.setSavedState(state);
        
        videoFragment = (StepVideoFragment) getFragmentManager().findFragmentById(R.id
                .recipe_video_fragment_tablet);
        stepDetailFragment = (StepDetailFragment) getFragmentManager().findFragmentById(R.id
                .recipe_step_detail_fragment_tablet);
        
        if (isTabletMode() && currentStep != null) {
            onStepClicked(currentStep, false);
        }
    }
    
    @Override
    public void onBackPressed() {
        if (fromWidget) {
            // no ListActivity on the back stack so go to it explicitly
            Intent intent = new Intent(this, RecipeListActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
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

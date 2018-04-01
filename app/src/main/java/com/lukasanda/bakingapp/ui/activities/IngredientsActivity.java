package com.lukasanda.bakingapp.ui.activities;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lukasanda.bakingapp.R;
import com.lukasanda.bakingapp.data.Recipe;
import com.lukasanda.bakingapp.ui.adapters.IngredientsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lukas on 3/13/2018.
 */

public class IngredientsActivity extends AppCompatActivity {
    
    @BindView(R.id.ingredients_rv)
    RecyclerView ingredientsRecyclerView;
    
    @BindView(R.id.image)
    ImageView recipeImageView;
    
    public static final String RECIPE_KEY = "recipeKey";
    
    public static final String IMAGE_KEY = "imageKey";
    
    private static final String SAVE_RV = "saveRvIngredients";
    
    private Recipe recipe;
    
    private IngredientsAdapter adapter;
    
    private String recipeImage;
    
    private Parcelable savedState;
    
    private RecyclerView.LayoutManager manager;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_ingredients);
        ButterKnife.bind(this);
        
        if (savedInstanceState == null) {
            recipe = getIntent().getExtras().getParcelable(RECIPE_KEY);
            recipeImage = getIntent().getExtras().getString(IMAGE_KEY);
        } else {
            recipe = savedInstanceState.getParcelable(RECIPE_KEY);
            recipeImage = getIntent().getExtras().getString(IMAGE_KEY);
            savedState = savedInstanceState.getParcelable(SAVE_RV);
        }
        
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        
        if (savedState != null) {
            manager.onRestoreInstanceState(savedState);
        }
        
        ingredientsRecyclerView.setLayoutManager(manager);
        
        if (!TextUtils.isEmpty(recipeImage)) {
            Glide.with(this)
                    .load(Uri.parse(recipeImage))
                    .into(recipeImageView);
        }
        
        adapter = new IngredientsAdapter();
        
        ingredientsRecyclerView.setAdapter(adapter);
        
        adapter.setItems(recipe.getIngredients());
        
    }
    
    @SuppressLint("MissingSuperCall")
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RECIPE_KEY, recipe);
        outState.putString(IMAGE_KEY, recipeImage);
        outState.putParcelable(SAVE_RV, manager.onSaveInstanceState());
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

package com.lukasanda.bakingapp.ui.activities;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lukasanda.bakingapp.R;
import com.lukasanda.bakingapp.data.Recipe;
import com.lukasanda.bakingapp.idlingResource.SimpleIdlingResource;
import com.lukasanda.bakingapp.ui.adapters.RecipesAdapter;
import com.lukasanda.bakingapp.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by lukas on 3/13/2018.
 */

public class RecipeListActivity extends AppCompatActivity implements RecipesAdapter.RecipeListener{
    
    private List<Recipe> recipes;
    
    private RecipesAdapter adapter;
    
    private Realm realm;
    
    private static final String RECIPE_KEY = "recipeKey";
    
    private static final String NETWORK_CALL_KEY = "networkKey";
    
    private static final int NUM_COL_PORTRAIT = 1;
    
    private static final int NUM_COL_LANDSCAPE = 3;
    
    private boolean hasMadeNetworkCall = false;
    
    
    @BindView(R.id.recipes_recycler_view)
    RecyclerView recyclerView;
    
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    
    @Nullable
    private SimpleIdlingResource mIdlingResource;
    
    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);
        
        realm = Realm.getDefaultInstance();
        
        int numColumns;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            numColumns = NUM_COL_LANDSCAPE;
        } else {
            numColumns = NUM_COL_PORTRAIT;
        }
        
        adapter = new RecipesAdapter(realm.where(Recipe.class).findAllAsync(),this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numColumns, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        
        if (savedInstanceState != null) {
            recipes = savedInstanceState.getParcelableArrayList(RECIPE_KEY);
            hasMadeNetworkCall = savedInstanceState.getBoolean(NETWORK_CALL_KEY, false);
        }
        
        if(realm.where(Recipe.class).findAll().size() == 0){
            getRecipesFromAPI();
        }
        
        // by default, try to retrieve recipes from the DB first
//        if (recipes == null) {
//            getRecipesFromDb();
//        } else {
//            setData();
//        }
        
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(realm!=null){
            realm.close();
            realm = null;
        }
    }
    
    @SuppressLint("StaticFieldLeak")
    private void getRecipesFromAPI() {
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }

        showProgress();
        NetworkUtils.fetchRecipes(new NetworkUtils.RecipesRequestCallback() {
            @Override
            public void onRecipesDownloaded(final List<Recipe> recipes) {
                hasMadeNetworkCall = true;
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.insertOrUpdate(recipes);
                        //                    RecipeIntentService.startActionUpdateWidget(RecipeListActivity.this);
    
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        if (mIdlingResource != null) {
                            mIdlingResource.setIdleState(true);
                        }
                        //no need to set data to adapter as our adapter autofetches its data
                        hideProgress();
                    }
                });
            }
        }, new NetworkUtils.ErrorCallback() {
            @Override
            public void onError() {
                //Show some info to user that the download failed
                Toast.makeText(RecipeListActivity.this, R.string.download_failed, Toast.LENGTH_SHORT).show();
                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);
                }
            }
        });
    }
    
    
    
    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }
    
    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (recipes != null) {
            outState.putParcelableArrayList(RECIPE_KEY, new ArrayList<Parcelable>(recipes));
            outState.putBoolean(NETWORK_CALL_KEY, hasMadeNetworkCall);
        }
    }
    
    @Override
    public void onRecipeClicked(Recipe recipe) {
                    // launch recipe details page
            Intent detailIntent = new Intent(this, RecipeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(RecipeActivity.RECIPE_KEY, recipe);
            bundle.putBoolean(RecipeActivity.FROM_WIDGET_KEY, false);
            detailIntent.putExtras(bundle);
            startActivity(detailIntent);
    }
}

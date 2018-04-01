package com.lukasanda.bakingapp.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lukasanda.bakingapp.R;
import com.lukasanda.bakingapp.data.RecipeStep;
import com.lukasanda.bakingapp.ui.adapters.RecipeStepAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lukas on 3/13/2018.
 */

public class RecipeStepsFragment extends Fragment {
    private RecyclerView recyclerView;
    
    private RecipeStepAdapter adapter;
    
    private RecyclerView.LayoutManager manager;
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        
        recyclerView = view.findViewById(R.id.recipe_steps_rv);
        
        manager = new LinearLayoutManager(inflater.getContext(), LinearLayoutManager.VERTICAL, false);
        
        recyclerView.setLayoutManager(manager);
        
        adapter = new RecipeStepAdapter(getActivity());
        
        recyclerView.setAdapter(adapter);
        
        return view;
    }
    
    public Parcelable getSavedLayout() {
        return manager.onSaveInstanceState();
    }
    
    public void setRecipeSteps(List<RecipeStep> steps) {
        adapter.setSteps(steps);
    }
    
    public void setCallback(RecipeStepAdapter.RecipeStepCallback callback) {
        adapter.setCallback(callback);
    }
    
    public void setSavedState(Parcelable savedState) {
        if (savedState != null) {
            manager.onRestoreInstanceState(savedState);
            recyclerView.setLayoutManager(manager);
        }
    }
}

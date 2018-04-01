package com.lukasanda.bakingapp.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lukasanda.bakingapp.R;
import com.lukasanda.bakingapp.data.RecipeIngredient;

import java.util.List;

/**
 * Created by lukas on 3/13/2018.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
    
    private List<RecipeIngredient> ingredients;
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecipeIngredient ingredient = ingredients.get(position);
        holder.textView.setText(ingredient.toString());
    }
    
    @Override
    public int getItemCount() {
        return ingredients == null ? 0 : ingredients.size();
    }
    
    public void setItems(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }
    
    class ViewHolder extends RecyclerView.ViewHolder {
        
        private TextView textView;
        
        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.recipe_card_tv);
        }
    }
    
}
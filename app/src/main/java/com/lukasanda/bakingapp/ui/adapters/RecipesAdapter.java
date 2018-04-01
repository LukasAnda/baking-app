package com.lukasanda.bakingapp.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lukasanda.bakingapp.R;
import com.lukasanda.bakingapp.data.Recipe;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by lukas on 3/13/2018.
 */

public class RecipesAdapter extends RealmRecyclerViewAdapter<Recipe, RecipesAdapter.ViewHolder> {
    
    
    private RecipeListener listener;
    
    public RecipesAdapter(OrderedRealmCollection<Recipe> recipes, RecipeListener listener) {
        super(recipes, true);
        this.listener = listener;
        setHasStableIds(true);
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card,
                parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(getData() == null) return;
        holder.textView.setText(getData().get(position).getName());
    }
    
    
    @Override
    public int getItemCount() {
        return getData() == null ? 0 : getData().size();
    }
    
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        
        private TextView textView;
        
        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            textView = view.findViewById(R.id.recipe_card_tv);
        }
        
        @Override
        public void onClick(View view) {
            if(getData() == null) return;
            Recipe recipe = getData().get(getAdapterPosition());
            listener.onRecipeClicked(recipe);
        }
    }
    
    public interface RecipeListener {
        void onRecipeClicked(Recipe recipe);
    }
}

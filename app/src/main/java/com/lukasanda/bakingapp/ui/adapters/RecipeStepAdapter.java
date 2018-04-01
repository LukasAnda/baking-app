package com.lukasanda.bakingapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lukasanda.bakingapp.R;
import com.lukasanda.bakingapp.data.RecipeStep;

import java.util.List;

/**
 * Created by lukas on 3/13/2018.
 */

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.ViewHolder> {
    
    private final Context context;
    
    private List<RecipeStep> steps;
    
    private RecipeStepCallback callback;
    
    public RecipeStepAdapter(Context context) {
        this.context = context;
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == 0) {
            holder.textView.setText(context.getResources().getString(R.string.recipe_ingredients));
        } else {
            holder.textView.setText(steps.get(position - 1).getShortDescription());
        }
    }
    
    @Override
    public int getItemCount() {
        return steps == null ? 0 : steps.size() + 1;
    }
    
    public void setSteps(List<RecipeStep> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }
    
    public void setCallback(RecipeStepCallback callback) {
        this.callback = callback;
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
            if (getAdapterPosition() == 0) {
                callback.onIngredientsClicked();
            } else {
                callback.onStepClicked(steps.get(getAdapterPosition() - 1), true);
            }
        }
    }
    
    public interface RecipeStepCallback {
        
        void onStepClicked(RecipeStep step, boolean reset);
        
        void onIngredientsClicked();
    }
    
}

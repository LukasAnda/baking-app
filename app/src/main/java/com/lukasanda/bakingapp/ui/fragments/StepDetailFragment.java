package com.lukasanda.bakingapp.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lukasanda.bakingapp.R;

/**
 * Created by lukas on 3/13/2018.
 */

public class StepDetailFragment extends Fragment {
    
    private TextView textView;
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.recipe_step_card, container, false);
        textView = view.findViewById(R.id.description_tv);
        return view;
    }
    
    public void setText(String text) {
        textView.setText(text);
    }
    
}

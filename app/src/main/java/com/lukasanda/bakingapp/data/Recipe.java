package com.lukasanda.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by lukas on 2/27/2018.
 */

public class Recipe extends RealmObject implements Parcelable {
    @PrimaryKey
    private long id;
    
    private String name;
    
    private RealmList<RecipeIngredient> ingredients;
    
    private RealmList<RecipeStep> steps;
    
    private double servings;
    
    private String image;
    
    public Recipe() {
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public RealmList<RecipeIngredient> getIngredients() {
        return ingredients;
    }
    
    public void setIngredients(RealmList<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }
    
    public RealmList<RecipeStep> getSteps() {
        return steps;
    }
    
    public void setSteps(RealmList<RecipeStep> steps) {
        this.steps = steps;
    }
    
    public double getServings() {
        return servings;
    }
    
    public void setServings(double servings) {
        this.servings = servings;
    }
    
    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }
    
    protected Recipe(Parcel in){
        this.id = in.readLong();
        this.name = in.readString();
        this.ingredients = new RealmList<>();
        this.ingredients.addAll(in.createTypedArrayList(RecipeIngredient.CREATOR));
        this.steps = new RealmList<>();
        this.steps.addAll(in.createTypedArrayList(RecipeStep.CREATOR));
        this.servings = in.readDouble();
        this.image = in.readString();
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeTypedList(this.ingredients);
        dest.writeTypedList(this.steps);
        dest.writeDouble(this.servings);
        dest.writeString(this.image);
    }
    
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }
        
        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}

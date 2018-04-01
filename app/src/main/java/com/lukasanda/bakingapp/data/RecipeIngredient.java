package com.lukasanda.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by lukas on 3/13/2018.
 */

public class RecipeIngredient extends RealmObject implements Parcelable {
    private double quantity;
    
    private String measure;
    
    private String ingredient;
    
    public RecipeIngredient() {
    }
    
    public double getQuantity() {
        return quantity;
    }
    
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    
    public String getMeasure() {
        return measure;
    }
    
    public void setMeasure(String measure) {
        this.measure = measure;
    }
    
    public String getIngredient() {
        return ingredient;
    }
    
    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
    
    @Override
    public String toString() {
        return quantity + " " + measure + " " + ingredient;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.quantity);
        dest.writeString(this.measure);
        dest.writeString(this.ingredient);
    }
    
    protected RecipeIngredient(Parcel in) {
        this.quantity = in.readDouble();
        this.measure = in.readString();
        this.ingredient = in.readString();
    }
    
    public static final Parcelable.Creator<RecipeIngredient> CREATOR = new Parcelable
            .Creator<RecipeIngredient>() {
        @Override
        public RecipeIngredient createFromParcel(Parcel source) {
            return new RecipeIngredient(source);
        }
        
        @Override
        public RecipeIngredient[] newArray(int size) {
            return new RecipeIngredient[size];
        }
    };
}

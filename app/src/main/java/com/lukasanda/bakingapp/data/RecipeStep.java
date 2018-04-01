package com.lukasanda.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by lukas on 3/13/2018.
 */

public class RecipeStep extends RealmObject implements Parcelable {
    private long id;
    
    private String shortDescription;
    
    private String description;
    
    private String videoURL;
    
    private String thumbnailURL;
    
    public RecipeStep() {
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getShortDescription() {
        return shortDescription;
    }
    
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getVideoURL() {
        return videoURL;
    }
    
    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }
    
    public String getThumbnailURL() {
        return thumbnailURL;
    }
    
    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.shortDescription);
        dest.writeString(this.description);
        dest.writeString(this.videoURL);
        dest.writeString(this.thumbnailURL);
    }
    
    protected RecipeStep(Parcel in){
        this.id = in.readLong();
        this.shortDescription = in.readString();
        this.description = in.readString();
        this.videoURL = in.readString();
        this.thumbnailURL = in.readString();
    }
    
    public static final Parcelable.Creator<RecipeStep> CREATOR = new Parcelable.Creator<RecipeStep>() {
        @Override
        public RecipeStep createFromParcel(Parcel source) {
            return new RecipeStep(source);
        }
        
        @Override
        public RecipeStep[] newArray(int size) {
            return new RecipeStep[size];
        }
    };
}

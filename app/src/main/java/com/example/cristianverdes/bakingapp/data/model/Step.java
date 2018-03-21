package com.example.cristianverdes.bakingapp.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "steps")
public class Step {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "primaryKey")
    private int primaryKey;

    @ColumnInfo(name = "stepId")
    @SerializedName("id")
    @Expose
    private Integer stepId;

    @ColumnInfo(name = "short_description")
    @SerializedName("shortDescription")
    @Expose
    private String shortDescription;

    @ColumnInfo(name = "description")
    @SerializedName("description")
    @Expose
    private String description;

    @ColumnInfo(name = "video_url")
    @SerializedName("videoURL")
    @Expose
    private String videoURL;

    @ColumnInfo(name = "thumbnail_url")
    @SerializedName("thumbnailURL")
    @Expose
    private String thumbnailURL;

    // Database attributes with setters and getters
    @ColumnInfo(name = "recipe_id")
    private int recipeId;

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getRecipeId() {
        return recipeId;
    }

        // Primary key
    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    // Getters and setters
    public Integer getStepId() {
        return recipeId;
    }

    public void setStepId(Integer stepId) {
        this.stepId = stepId;
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

}

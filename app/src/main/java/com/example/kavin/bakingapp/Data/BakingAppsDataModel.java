package com.example.kavin.bakingapp.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BakingAppsDataModel implements Parcelable {

    public BakingAppsDataModel(Integer id, String name, List<IngredientsDataModel> ingredients, List<StepDataModel> steps, Integer servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ingredients")
    @Expose
    private List<IngredientsDataModel> ingredients = null;
    @SerializedName("steps")
    @Expose
    private List<StepDataModel> steps = null;
    @SerializedName("servings")
    @Expose
    private Integer servings;
    @SerializedName("image")
    @Expose
    private String image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IngredientsDataModel> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientsDataModel> ingredients) {
        this.ingredients = ingredients;
    }

    public List<StepDataModel> getSteps() {
        return steps;
    }

    public void setSteps(List<StepDataModel> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeTypedList(this.ingredients);
        dest.writeTypedList(this.steps);
        dest.writeValue(this.servings);
        dest.writeString(this.image);
    }

    public BakingAppsDataModel() {
    }

    protected BakingAppsDataModel(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.ingredients = in.createTypedArrayList(IngredientsDataModel.CREATOR);
        this.steps = in.createTypedArrayList(StepDataModel.CREATOR);
        this.servings = (Integer) in.readValue(Integer.class.getClassLoader());
        this.image = in.readString();
    }

    public static final Parcelable.Creator<BakingAppsDataModel> CREATOR = new Parcelable.Creator<BakingAppsDataModel>() {
        @Override
        public BakingAppsDataModel createFromParcel(Parcel source) {
            return new BakingAppsDataModel(source);
        }

        @Override
        public BakingAppsDataModel[] newArray(int size) {
            return new BakingAppsDataModel[size];
        }
    };
}
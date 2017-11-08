/*
 * Copyright (C) 2017 Eric Afenyo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.eric.bakingrecipes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by eric on 06/11/2017.
 *  The POJO Schema for the RecipesModel JsonResponse
 */

public class RecipesModel implements Parcelable{
    private int id;
    private String name;
    private int servings;
    private List<Ingredients> ingredients;
    private List<Steps> steps;

    //Constructors
    public RecipesModel() {
    }

    public RecipesModel(int id, String name, int servings, List<Ingredients> ingredients, List<Steps> steps) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    //RecipesModel Parcelable
    protected RecipesModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        servings = in.readInt();
    }

    public static final Creator<RecipesModel> CREATOR = new Creator<RecipesModel>() {
        @Override
        public RecipesModel createFromParcel(Parcel in) {
            return new RecipesModel(in);
        }

        @Override
        public RecipesModel[] newArray(int size) {
            return new RecipesModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //Adding RecipesModel
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(servings);

        //Adding InnerClasses
        parcel.writeParcelable((Parcelable) ingredients,i);
        parcel.writeParcelable((Parcelable) steps,i);

    } //End of RecipesModel Parcelable

    /**Ingredients InnerClass*/
    public static class Ingredients implements Parcelable{
        private double quantity;
        private String measure;
        private String ingredient;

        //Ingredients Parcelable
        protected Ingredients(Parcel in) {
            quantity = in.readDouble();
            measure = in.readString();
            ingredient = in.readString();
        }

        public static final Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
            @Override
            public Ingredients createFromParcel(Parcel in) {
                return new Ingredients(in);
            }

            @Override
            public Ingredients[] newArray(int size) {
                return new Ingredients[size];
            }
        };


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeDouble(quantity);
            parcel.writeString(measure);
            parcel.writeString(ingredient);
        } //End of Ingredients Parcelable

        //Ingredients Getters
        public double getQuantity() {
            return quantity;
        }

        public String getMeasure() {
            return measure;
        }

        public String getIngredient() {
            return ingredient;
        }
    }

    /**Steps InnerClass*/
    public static class Steps implements Parcelable{
        private int id;
        private String shortDescription;
        private String description;
        private String videoURL;
        private String thumbnailURL;

        //Steps Parcelable
        protected Steps(Parcel in) {
            id = in.readInt();
            shortDescription = in.readString();
            description = in.readString();
            videoURL = in.readString();
            thumbnailURL = in.readString();
        }

        public static final Creator<Steps> CREATOR = new Creator<Steps>() {
            @Override
            public Steps createFromParcel(Parcel in) {
                return new Steps(in);
            }

            @Override
            public Steps[] newArray(int size) {
                return new Steps[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(id);
            parcel.writeString(shortDescription);
            parcel.writeString(description);
            parcel.writeString(videoURL);
            parcel.writeString(thumbnailURL);
        }//End of Steps Parcelable

        //Steps Getters
        public int getId() {
            return id;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public String getDescription() {
            return description;
        }

        public String getVideoURL() {
            return videoURL;
        }

        public String getThumbnailURL() {
            return thumbnailURL;
        }
    }

    //RecipesModel Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getServings() {
        return servings;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public List<Steps> getSteps() {
        return steps;
    }
}
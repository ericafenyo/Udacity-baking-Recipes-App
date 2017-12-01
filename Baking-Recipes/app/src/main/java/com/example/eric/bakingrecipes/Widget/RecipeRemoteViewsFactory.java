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

package com.example.eric.bakingrecipes.Widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.eric.bakingrecipes.BuildConfig;
import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.Utils.Data.RecipesModel;
import com.example.eric.bakingrecipes.Utils.N;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by eric on 24/11/2017.
 */

public class RecipeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    private Context mContext;
    private List<RecipesModel.Ingredients> mData;

    public RecipeRemoteViewsFactory(Context mContext, Intent intent) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        //network call
        try {
            URL url = new URL(BuildConfig.JsonUrl);
           mData = parseJson(N.getHttpResponse(url));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Error:" ,e.toString());
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.item_appwidget_shopping_list);
        RecipesModel.Ingredients data = mData.get(position);
        view.setTextViewText(R.id.text_view_shopping_list_ingredients,data.getIngredient());
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * uses Google's Gson to parse the json "response"
     *
     * @param response the JsonArray from the network call
     * @return list of Recipes using a schema from the RecipeModel class
     */
    private List<RecipesModel.Ingredients> parseJson(String response) {
        List<RecipesModel> recipesList;
        Gson gson = new Gson();
        //gets the Type of data to be returned
        Type type = new TypeToken<List<RecipesModel>>() {
        }.getType();
        //deserialization
        recipesList = gson.fromJson(String.valueOf(response), type);
        return recipesList.get(N.getSLPreferences(mContext,"keyValue",0)).getIngredients();
    }
}

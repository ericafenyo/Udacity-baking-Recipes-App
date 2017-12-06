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

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.eric.bakingrecipes.BuildConfig;
import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.Utils.Data.RecipesModel;
import com.example.eric.bakingrecipes.Utils.MySingleton;
import com.example.eric.bakingrecipes.Utils.N;
import com.example.eric.bakingrecipes.Utils.ParseJson;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppWidgetConfigurationActivity extends AppCompatActivity implements
        AppWidgetConfigurationAdapter.OnItemClickLister {

    private List<RecipesModel> mRecipes;
    private AppWidgetConfigurationAdapter mAdapter;
    private int mAppWidgetId;

    @BindView(R.id.list_view_ingredient_picker)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_widget_configuration);
        ButterKnife.bind(this);

        //get the App Widget Id from the Intent that launched the Configuration Activity:
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        //makes network call
        loadJson();
    }

    /**
     * request for recipes json data using Google's Volley Library.
     * the response is parsed using a {@link #parseJson(JSONArray)} method
     */
    private void loadJson() {

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET,
                BuildConfig.JsonUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                //deserialization
                Type type = new TypeToken<List<RecipesModel>>() {}.getType();
                mRecipes  =  ParseJson.deSerializeList(String.valueOf(response),type);

                mAdapter = new AppWidgetConfigurationAdapter(
                        AppWidgetConfigurationActivity.this,
                        AppWidgetConfigurationActivity.this);
                mAdapter.setData(mRecipes);
                listView.setAdapter(mAdapter);
            }

            //handles error if any in the process of the network request
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError || error instanceof TimeoutError) {
                    Log.e("NoConnectionError", error.toString());
                } else if (error instanceof ServerError) {
                    Log.e("ServerError", error.toString());
                }
            }
        });

        //add arrayRequest to RequestQueue
        MySingleton.getInstance(this).addToRequestQueue(arrayRequest);
    }

    /**
     * uses Google's Gson to parse the json "response"
     *
     * @param response the JsonArray from the network call
     * @return list of Recipes using a schema from the RecipeModel class
     */
    private List<RecipesModel> parseJson(JSONArray response) {

        List<RecipesModel> recipesList;
        Gson gson = new Gson();
        //gets the Type of data to be returned
        Type type = new TypeToken<List<RecipesModel>>() {
        }.getType();
        //deserialization
        recipesList = gson.fromJson(String.valueOf(response), type);
        return recipesList;
    }

    @Override
    public void onClick(int position, List<RecipesModel> data) {
        //        stores int value used to get correct ingredients at the correct positions
        N.storeSLPreferences(this, "keyValue", position);
        N.toast(getApplicationContext(),mAppWidgetId);

        //gets an instance of the AppWidgetManager by calling getInstance(Context):
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        //Update the App Widget
        RecipeAppWidgetProvider.updateAppWidget(this, appWidgetManager, mAppWidgetId);

        //creates return Intent and finish the Activity:
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}












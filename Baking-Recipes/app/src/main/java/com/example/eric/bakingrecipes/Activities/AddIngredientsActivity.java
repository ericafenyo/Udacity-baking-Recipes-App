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

package com.example.eric.bakingrecipes.Activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.eric.bakingrecipes.Adapters.IngredientsPagerAdapter;
import com.example.eric.bakingrecipes.BuildConfig;
import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.Utils.Data.RecipesModel;
import com.example.eric.bakingrecipes.Utils.MySingleton;
import com.example.eric.bakingrecipes.Utils.ParseJson;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddIngredientsActivity extends AppCompatActivity {

    private static final String BUNDLED_RECIPES = "BUNDLE_RECIPES";

    private List<RecipesModel> mRecipes;
    private IngredientsPagerAdapter mAdapter;


    @BindView(R.id.view_pager_add_ingredients)
    ViewPager vPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);
        ButterKnife.bind(this);

        //sets toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_add_ingredients);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.toolbar_ingredients_title);
        }
        //make network call only if recipe data is null;
        if (savedInstanceState != null) {
            mRecipes = savedInstanceState.getParcelableArrayList(BUNDLED_RECIPES);
            mAdapter = new IngredientsPagerAdapter(getSupportFragmentManager(), mRecipes);
            vPager.setAdapter(mAdapter);
        } else {
            //requests and parse Recipes Json from the net
            loadJson();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(BUNDLED_RECIPES, (ArrayList<? extends Parcelable>) mRecipes);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    /**
     * make network calls using Google's Volley Library.
     * {@link #parseJson(JSONArray)} parses the network response
     */
    private void loadJson() {
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET,
                BuildConfig.JsonUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                //deserialization
                Type type = new TypeToken<List<RecipesModel>>() {}.getType();
                mRecipes  =  ParseJson.deSerializeList(String.valueOf(response),type);

                //set ViewPager adapter
                mAdapter = new IngredientsPagerAdapter(getSupportFragmentManager(), mRecipes);
                vPager.setAdapter(mAdapter);

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
}

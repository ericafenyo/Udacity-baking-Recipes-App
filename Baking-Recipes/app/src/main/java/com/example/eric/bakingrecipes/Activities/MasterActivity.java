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
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.eric.bakingrecipes.Adapters.MasterAdapter;
import com.example.eric.bakingrecipes.BuildConfig;
import com.example.eric.bakingrecipes.Fragments.MasterFragment;
import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.Utils.Data.RecipesModel;
import com.example.eric.bakingrecipes.Utils.MySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MasterActivity extends AppCompatActivity {
    private static final String EXTRA_INGREDIENTS = "EXTRA_INGREDIENTS";
    private static final String EXTRA_STEPS = "EXTRA_STEPS";
    private static final String BUNDLED_RECIPES = "BUNDLE_RECIPES";

    private LinearLayoutManager mLayoutManager;
    private MasterAdapter mAdapter;
    private List<RecipesModel> mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        Toolbar toolbar = findViewById(R.id.toolbar_master);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }



        //checks to see if recipes data is saved into bundle before making network call. This minimizes internet usage
        if (savedInstanceState != null) {
            mRecipes = savedInstanceState.getParcelableArrayList(BUNDLED_RECIPES);
            //fragment initiation using a custom factory method declared in the fragment
            MasterFragment fragment = MasterFragment.newFragment(mRecipes);

            //inflates the fragment in the activity
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.frame_master_container, fragment)
                    .commit();
        } else {
            //requests and parse Recipes Json from the Net
            loadJson();
        }//end of onCreate

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(BUNDLED_RECIPES, (ArrayList<? extends Parcelable>) mRecipes);
    }


    /**
     * request for recipes json data using Google's Volley Library.
     * the response is parsed using a custom parseJson() method
     */
    private void loadJson() {

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET,
                BuildConfig.JsonUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                //returns JsonArrayList of RecipesModel
                mRecipes = parseJson(response);

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(BUNDLED_RECIPES, (ArrayList<? extends Parcelable>) mRecipes);

                //fragment initiation using a custom factory method declared in the fragment
                MasterFragment fragment = MasterFragment.newFragment(mRecipes);
                //inflates the fragment in the activity
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction()
                        .add(R.id.frame_master_container, fragment)
                        .commit();
            }

            //handles error if any in the process of the network request
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO:Handle Error here

            }
        });

        //add arrayRequest to RequestQueue
        MySingleton.getInstance(this).addToRequestQueue(arrayRequest);

    }

    /**
     * uses Google Gson to convert the "response" to objects from their corresponding key:value pairs
     *
     * @param response the recipes JsonArray results from the network call
     * @return List of Recipes using a schema from the RecipeModel
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

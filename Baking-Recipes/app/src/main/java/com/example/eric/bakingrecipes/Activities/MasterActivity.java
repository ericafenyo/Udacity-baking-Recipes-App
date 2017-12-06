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

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.eric.bakingrecipes.Adapters.MasterAdapter;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MasterActivity extends AppCompatActivity implements MasterAdapter.onItemSelectListener {

    private static final String EXTRA_INGREDIENTS = "EXTRA_INGREDIENTS";
    private static final String EXTRA_STEPS = "EXTRA_STEPS";
    private static final String BUNDLED_RECIPES = "BUNDLE_RECIPES";
    private static final String POSITION = "POSITION";
    private static final String RECIPE_NAME = "RECIPE_NAME";
    private static final String SERVINGS = "SERVINGS";

//    private MasterAdapter mAdapter;
    private List<RecipesModel> mRecipes;

    @BindView(R.id.recyclerView_fragment_master_list)
    RecyclerView recyclerView;
    @BindView(R.id.text_view_network_info)
    TextView textViewNetworkInfo;
    @BindView(R.id.progress_bar_master)
    ProgressBar progressBar;
    @BindView(R.id.button_retry)
    Button buttonRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        ButterKnife.bind(this);

        //sets toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_master);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        //make network call only if recipe data is null;
        if (savedInstanceState != null) {
            mRecipes = savedInstanceState.getParcelableArrayList(BUNDLED_RECIPES);
            progressBar.setVisibility(View.INVISIBLE);
            setAdapter(recyclerView);
            setLayoutManager(recyclerView);
            if (mRecipes == null) {
                hideData();
            }
        } else {
            //requests and parse Recipes Json from the net
            loadJson();
        }
    }//end of onCreate()

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(BUNDLED_RECIPES, (ArrayList<? extends Parcelable>) mRecipes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.shopping_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.action_shopping_list:
                Intent intent = new Intent(getApplicationContext(), ShoppingListActivity.class);
                for (int i = 0; i < mRecipes.size();i++){
                    List<RecipesModel.Ingredients> mIngredients = mRecipes.get(i).getIngredients();
                    intent.putParcelableArrayListExtra(EXTRA_INGREDIENTS, (ArrayList<? extends Parcelable>) mIngredients);
                }
                startActivity(intent);
                break;
        }
        return true;
    }

    //inner methods

    /**
     * make network calls using Google's Volley Library.
     * {@link #parseJson(JSONArray)} parses the network response
     */
    private void loadJson() {
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET,
                BuildConfig.JsonUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                showData();//hide progress bar

                //deserialization
                Type type = new TypeToken<List<RecipesModel>>() {}.getType();
                mRecipes  =  ParseJson.deSerializeList(String.valueOf(response),type);

                setAdapter(recyclerView);
                setLayoutManager(recyclerView);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError || error instanceof TimeoutError) {
                    Log.e("NoConnectionError", error.toString());
                    hideData();
                } else if (error instanceof ServerError) {
                    Log.e("ServerError", error.toString());
                    textViewNetworkInfo.setText(R.string.server_info);
                    textViewNetworkInfo.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    buttonRetry.setVisibility(View.INVISIBLE);
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

    /**
     * specifies an Adapter for the RecyclerView
     *
     * @param recyclerView RecyclerView
     */
    private void setAdapter(RecyclerView recyclerView) {
        MasterAdapter adapter = new MasterAdapter(this, mRecipes, this);
        recyclerView.setAdapter(adapter);
        //improve performance
        recyclerView.setHasFixedSize(true);
    }

    /**
     * specifies a LayoutManager based on the type of device(a Tablet or a Handset) and it's orientation mode
     *
     * @param recyclerView RecyclerView
     */
    private void setLayoutManager(RecyclerView recyclerView) {
        //verify type of device (returns true for a Tablet and false for a Handset)
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);

        //LayoutManager Configuration for Handset devices
        if (!isTablet) {
            if (getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_PORTRAIT) {
                //use a LinearLayoutManager for portrait orientation mode
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            } else if (getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_LANDSCAPE) {
                //use a GridLayoutManager for landscape orientation mode
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            }
        } else {
            //LayoutManager Configuration for Tablet devices
            if (getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_PORTRAIT) {
                //use a LinearLayoutManager for portrait orientation mode
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            } else if (getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_LANDSCAPE) {
                //use a GridLayoutManager for landscape orientation mode
                recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            }
        }
    }

    @Override
    public void onItemClick(int position, List<RecipesModel> recipes) {
        //get objects from their respective positions within the JsonArray
        RecipesModel models = recipes.get(position);
        List<RecipesModel.Ingredients> ingredients = models.getIngredients();
        List<RecipesModel.Steps> steps = models.getSteps();
        String recipeName = models.getName();
        int servings = models.getServings();

        //Send data to DetailsActivity
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_INGREDIENTS, (ArrayList<? extends Parcelable>) ingredients);
        intent.putParcelableArrayListExtra(EXTRA_STEPS, (ArrayList<? extends Parcelable>) steps);
        intent.putExtra(RECIPE_NAME, recipeName);
        intent.putExtra(SERVINGS, servings);
        intent.putExtra(POSITION, position);
        startActivity(intent);
    }

    /**
     * refreshes the activity
     */
    public void onRetry(View view) {
        if (N.checkConnectivity(getApplicationContext())) {
            finish();
            startActivity(getIntent());
        } else {
            N.toast(getApplicationContext(), "not connected");
        }
    }

    /**
     * displays data if all conditions are favorable
     */
    private void showData() {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        textViewNetworkInfo.setVisibility(View.INVISIBLE);
        buttonRetry.setVisibility(View.INVISIBLE);
    }

    /**
     * hides the recyclerView
     */
    private void hideData() {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        textViewNetworkInfo.setVisibility(View.VISIBLE);
        buttonRetry.setVisibility(View.VISIBLE);
    }
}
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

package com.example.eric.bakingrecipes.Ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.example.eric.bakingrecipes.Adapters.RecipesAdapter;
import com.example.eric.bakingrecipes.BuildConfig;
import com.example.eric.bakingrecipes.Utils.MySingleton;
import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.RecipesModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.onItemSelectListener {

    LinearLayoutManager layoutManager;
    RecipesAdapter adapter;
    Gson gson;

    List<RecipesModel>  recipes = new ArrayList<>();
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

//    @BindView(R.id.image_test)
    ImageView image;
    private String EXTRA_INGREDIENTS = "EXTRA_INGREDIENTS";
    private String EXTRA_STEPS = "EXTRA_STEPS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        loadJson();
        Log.v("Res2",recipes.toString());

    }

    private void loadJson() {

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, BuildConfig.JsonUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                recipes = parseJson(response);
                Log.v("Res",recipes.toString());

                layoutManager = new LinearLayoutManager(MainActivity.this);
                adapter = new RecipesAdapter(recipes,MainActivity.this, MainActivity.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("JsonError",error.toString());
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(arrayRequest);

    }

    private List<RecipesModel> parseJson(JSONArray response) {

        List<RecipesModel> recipesList = new ArrayList<>();
        Gson gson = new Gson();

        Type type = new TypeToken<List<RecipesModel>>(){}.getType();
        recipesList  = gson.fromJson(String.valueOf(response),type);
        Log.v("JsonError",recipesList.toString());

        return recipesList;

    }

    @Override
    public void onItemClick(int position, List<RecipesModel> recipes) {
        RecipesModel models = recipes.get(position);
        List<RecipesModel.Ingredients> ingredients = models.getIngredients();
        List<RecipesModel.Steps> steps = models.getSteps();
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_INGREDIENTS, (ArrayList<? extends Parcelable>) ingredients);
        intent.putParcelableArrayListExtra(EXTRA_STEPS, (ArrayList<? extends Parcelable>) steps);
        startActivity(intent);
    }

    @Override
    public void onViewClick(int position, List<RecipesModel> recipes, View view) {
//        Log.v("JsonError", String.valueOf(view.getId()));
    }



}

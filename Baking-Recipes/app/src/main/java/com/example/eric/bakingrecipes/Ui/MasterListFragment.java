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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.eric.bakingrecipes.Adapters.MasterListAdapter;
import com.example.eric.bakingrecipes.BuildConfig;
import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.RecipesModel;
import com.example.eric.bakingrecipes.Utils.L;
import com.example.eric.bakingrecipes.Utils.MySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eric on 08/11/2017.
 * overall network request is handled here
 */

public class MasterListFragment extends Fragment{

    private static final String EXTRA_INGREDIENTS = "EXTRA_INGREDIENTS";
    private static final String EXTRA_STEPS = "EXTRA_STEPS";

    private LinearLayoutManager mLayoutManager;
    private MasterListAdapter mAdapter;
    private Context mContext = getActivity();
    private List<RecipesModel> mRecipes = new ArrayList<>();

    //cast the corresponding views.
    @BindView(R.id.recyclerView_fragment_master_list)RecyclerView recyclerView;

    //Constructor
    public MasterListFragment() {
        //should be empty
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_master_list,container,false);

        //Bind views
        ButterKnife.bind(this,view);

        //requests and parse Recipes Json response
        loadJson();

        //return method(-> View)
        return view;

    } //end of onCreateView

    //Inner Methods

    /**
     * request for recipes json data using Google's Volley Library
     * */
    private void loadJson() {
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, BuildConfig.JsonUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                //sets the value of "mRecipes" to parsed Recipes JSONArray response
                mRecipes = parseJson(response);
                Log.v("Res", mRecipes.toString());

                //using a LinearLayoutManager
                mLayoutManager = new LinearLayoutManager(mContext);
                recyclerView.setLayoutManager(mLayoutManager);

                //specifying an Adapter
                mAdapter = new MasterListAdapter(mContext, mRecipes,itemSelectListener);
                recyclerView.setAdapter(mAdapter);

                //improve performance
                recyclerView.setHasFixedSize(true);

            }

        //handles error if any in the process of data request
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO:Handle Error here

            }
        });

        //adds arrayRequest to RequestQueue
        MySingleton.getInstance(getContext()).addToRequestQueue(arrayRequest);
    }

    /**
     * TODO:Comment on this
     * */
    private List<RecipesModel> parseJson(JSONArray response) {

        List<RecipesModel> recipesList;
        Gson gson = new Gson();

        //
        Type type = new TypeToken<List<RecipesModel>>(){}.getType();
        recipesList  = gson.fromJson(String.valueOf(response),type);
        return recipesList;
    }

    /**
     * TODO:Comment on this
     * */
  MasterListAdapter.onItemSelectListener itemSelectListener = new MasterListAdapter.onItemSelectListener() {
      @Override
      public void onItemClick(int position, List<RecipesModel> recipes) {

          RecipesModel models = recipes.get(position);

          //gets Lists of Recipe Steps and Steps from there respective position in the Recipes JsonArray
          List<RecipesModel.Ingredients> ingredients = models.getIngredients();
          List<RecipesModel.Steps> steps = models.getSteps();

          //Sends Intent data to DetailActivity
          Intent intent = new Intent(getContext(), DetailActivity.class);
          intent.putParcelableArrayListExtra(EXTRA_INGREDIENTS, (ArrayList<? extends Parcelable>) ingredients);
          intent.putParcelableArrayListExtra(EXTRA_STEPS, (ArrayList<? extends Parcelable>) steps);
          if (getActivity().findViewById(R.id.frame_detail_fragment) == null){
              startActivity(intent);
          }else {

              DetailListFragment detailListFragment = new DetailListFragment();
              FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
              fragmentManager.beginTransaction()
                      .replace(R.id.frame_detail_fragment,detailListFragment)
                      .commit();

              L.toast(getActivity(),String.valueOf(position));
          }


      }
  };
}

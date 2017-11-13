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

package com.example.eric.bakingrecipes.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eric.bakingrecipes.Activities.DetailsActivity;
import com.example.eric.bakingrecipes.Adapters.MasterAdapter;
import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.RecipesModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eric on 08/11/2017.
 * TODO: comment on this
 */

public class MasterFragment extends Fragment {

    private static final String EXTRA_INGREDIENTS = "EXTRA_INGREDIENTS";
    private static final String EXTRA_STEPS = "EXTRA_STEPS";
    private static final String BUNDLED_RECIPES = "BUNDLE_RECIPES";

    private LinearLayoutManager mLayoutManager;
    private MasterAdapter mAdapter;
    private Context mContext = getActivity();
    private List<RecipesModel> mRecipes = new ArrayList<>();

    @BindView(R.id.recyclerView_fragment_master_list)
    RecyclerView recyclerView;

    /**
     * creates new MasterFragment which makes data available within the fragment through bundle
     * @param recipeData List of recipes data from the jsonArray response
     * @return new Instance of  MasterFragment
     */
    public static MasterFragment newFragment(List<RecipesModel> recipeData) {
        MasterFragment fragment = new MasterFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(BUNDLED_RECIPES, (ArrayList<? extends Parcelable>) recipeData);
        fragment.setArguments(args);
        return fragment;
    }

    //Constructor
    public MasterFragment() {
        //should be empty
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_master_list, container, false);

        //Bind views
        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        if (args != null) {
            //retrieves data from the bundle
            mRecipes = args.getParcelableArrayList(BUNDLED_RECIPES);
            //specifying an Adapter
            mAdapter = new MasterAdapter(mContext, mRecipes, itemSelectListener);
            recyclerView.setAdapter(mAdapter);
            //using a LinearLayoutManager
            mLayoutManager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(mLayoutManager);
            //improve performance
            recyclerView.setHasFixedSize(true);
        }

        //return method(-> View)
        return view;

    } //end of onCreateView

    //Inner Methods
    /**
     * TODO:Comment on this
     */
    MasterAdapter.onItemSelectListener itemSelectListener =
            new MasterAdapter.onItemSelectListener() {
                @Override
                public void onItemClick(int position, List<RecipesModel> recipes) {

                    RecipesModel models = recipes.get(position);

                    //gets Lists of Recipe Steps and Steps from there respective position in the Recipes JsonArray
                    List<RecipesModel.Ingredients> ingredients = models.getIngredients();
                    List<RecipesModel.Steps> steps = models.getSteps();

                    //Sends Intent data to DetailsActivity
                    Intent intent = new Intent(getContext(), DetailsActivity.class);
                    intent.putParcelableArrayListExtra(EXTRA_INGREDIENTS,
                            (ArrayList<? extends Parcelable>) ingredients);
                    intent.putParcelableArrayListExtra(EXTRA_STEPS, (ArrayList<? extends Parcelable>) steps);
//          if (getActivity().findViewById(R.id.frame_detail_fragment) == null){
                    startActivity(intent);
//          }else {
//
//              DetailsFragment detailListFragment = new DetailsFragment();
//              FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//              fragmentManager.beginTransaction()
////                      .replace(R.id.frame_detail_fragment,detailListFragment)
//                      .commit();
//
////              L.toast(getActivity(),String.valueOf(position));
//          }


                }
            };
}

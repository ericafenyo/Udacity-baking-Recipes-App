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

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eric.bakingrecipes.Activities.ShoppingListActivity;
import com.example.eric.bakingrecipes.Adapters.IngredientsAdapter;
import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.Utils.Data.RecipesModel;
import com.example.eric.bakingrecipes.Utils.N;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.eric.bakingrecipes.Utils.Data.ShoppingListContentProvider.ContentUris.ALL_ITEMS;
import static com.example.eric.bakingrecipes.Utils.Data.ShoppingListContract.ShoppingListColumns.INGREDIENT;

/**
 * Created by eric on 08/11/2017.
 */

public class IngredientsFragment extends Fragment implements IngredientsAdapter.onItemSelectListener {

    private static final String EXTRA_INGREDIENTS = "EXTRA_INGREDIENTS";

    private IngredientsAdapter mAdapter;
    private List<RecipesModel.Ingredients> mIngredients = new ArrayList<>();

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    /**
     * @param ingredientsData list of recipe ingredients
     * @return instance of IngredientsFragment
     */
    public static IngredientsFragment newFragment(List<RecipesModel.Ingredients> ingredientsData) {
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_INGREDIENTS, (ArrayList<? extends Parcelable>) ingredientsData);
        fragment.setArguments(args);
        return fragment;
    }

    public IngredientsFragment() {
        //should be empty
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle;
        if (getArguments() != null) {
            bundle = getArguments();
        } else {
            bundle = getActivity().getIntent().getExtras();
        }

        if (bundle != null) {
            mIngredients = bundle.getParcelableArrayList(EXTRA_INGREDIENTS);
            assert mIngredients != null;

            //specifying an Adapter and LayoutManager
            mAdapter = new IngredientsAdapter(getActivity(), mIngredients, this);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        //return method(-> View)
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter = new IngredientsAdapter(getActivity(), mIngredients, this);
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * add ingredient to shopping list(database)
     *
     * @param data recipe ingredients
     */
    public void addData(String data) {
        try {
            ContentValues values = new ContentValues();
            values.put(INGREDIENT, data);
            Uri uri = getActivity().getContentResolver().insert(ALL_ITEMS, values);
            if (uri != null) {
                getActivity().getContentResolver().notifyChange(uri, null);
                N.storeSLPreferences(getActivity(), data, 1);
                //snackBar
                N.makeSnackAction(getView(), getString(R.string.add_to_list), getString(R.string.view_list_action), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ShoppingListActivity.class);
                        intent.putParcelableArrayListExtra(EXTRA_INGREDIENTS, (ArrayList<? extends Parcelable>) mIngredients);
                        startActivity(intent);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Insertion Error:", "Data could not be inserted " + e);
        }
    }

    /**
     * remove ingredient from shopping list(database)
     *
     * @param data recipe ingredients
     */
    private void deleteData(String data) {
        int fb = getActivity().getContentResolver().delete(ALL_ITEMS, INGREDIENT + "=" + "'" + data + "'", null);
        if (fb > 0) {
            getActivity().getContentResolver().notifyChange(ALL_ITEMS, null);
            N.makeSnackAction(getView(), getString(R.string.remove_from_list), getString(R.string.view_list_action), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ShoppingListActivity.class);
                    intent.putParcelableArrayListExtra(EXTRA_INGREDIENTS, (ArrayList<? extends Parcelable>) mIngredients);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onItemClick(int position, List<RecipesModel.Ingredients> ingredients, ImageView addToCart) {
        String data = ingredients.get(position).getIngredient();
        int inFb = N.getSLPreferences(getActivity(), ingredients.get(position).getIngredient(), 0);
        if (inFb == 0) {
            addToCart.setImageDrawable(getActivity().getDrawable(R.drawable.ic_remove));
            N.storeSLPreferences(getActivity(), data, 1);
            addData(data);
        } else if (inFb == 1) {
            addToCart.setImageDrawable(getActivity().getDrawable(R.drawable.ic_add));
            N.storeSLPreferences(getActivity(), data, 0);
            deleteData(data);
        }
    }
}
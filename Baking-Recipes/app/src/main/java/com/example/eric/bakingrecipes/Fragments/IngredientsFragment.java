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
import android.widget.TextView;

import com.example.eric.bakingrecipes.Adapters.IngredientsAdapter;
import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.RecipesAppWidget;
import com.example.eric.bakingrecipes.Utils.Data.RecipesModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.eric.bakingrecipes.Utils.Data.ShoppingListContract.ShoppingListColumns.INGREDIENT;
import static com.example.eric.bakingrecipes.Utils.Data.ShoppingListProvider.ContentUris.ALL_ITEMS;

/**
 * Created by eric on 08/11/2017.
 */

public class IngredientsFragment extends Fragment {

    private static final String BUNDLED_INGREDIENTS = "BUNDLED_INGREDIENTS";
    private static final String EXTRA_INGREDIENTS = "EXTRA_INGREDIENTS";

    private IngredientsAdapter mAdapter;
    List<RecipesModel.Ingredients> ingredients = new ArrayList<>();
    @BindView(R.id.recycler_view_ingredients)
    RecyclerView recyclerView;
    @BindView(R.id.text_view_ingredients_details_info)
    TextView textIngredientInfo;
    @BindView(R.id.text_view_ingredients_add_to_shopping_list_button)
    TextView buttonAddAllIngredient;

    /**
     * @param ingredientsData v
     * @return
     */
    public static IngredientsFragment newFragment(List<RecipesModel.Ingredients> ingredientsData) {
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(BUNDLED_INGREDIENTS, (ArrayList<? extends Parcelable>) ingredientsData);
        fragment.setArguments(args);
        return fragment;
    }

    public IngredientsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_ingredients_list, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            ingredients = bundle.getParcelableArrayList(EXTRA_INGREDIENTS);
            textIngredientInfo.setText(String.format("Number of Items: %s", String.valueOf(ingredients.size())));

            mAdapter = new IngredientsAdapter(getActivity(), ingredients, onClickListener);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            //return method(-> View)


            buttonAddAllIngredient.setOnClickListener(addAllOnClickListener);
            // this will send the broadcast to update the appwidget

        }
        return view;
    }

    IngredientsAdapter.onIngredientItemClickListener onClickListener = new IngredientsAdapter.onIngredientItemClickListener() {

        @Override
        public void onItemClick(int position, List<RecipesModel.Ingredients> ingredients) {

        }
    };

    View.OnClickListener addAllOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String allIngredient;

            try {
                for (int i = 0; i < ingredients.size(); i++){
                    allIngredient = ingredients.get(i).getIngredient();
                    ContentValues values = new ContentValues();

                    values.put(INGREDIENT,allIngredient);
                    Uri uri = getActivity().getContentResolver().insert(ALL_ITEMS,values);
                    if (uri != null){
                        getActivity().getContentResolver().notifyChange(uri,null);
                    }

                }
                RecipesAppWidget.sendRefreshBroadcast(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Insertion Error:","Data could not be inserted " + e );
            }
        }
    };
}

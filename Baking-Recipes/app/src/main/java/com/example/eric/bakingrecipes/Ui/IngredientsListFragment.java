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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eric.bakingrecipes.Adapters.IngredientsAdapter;
import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.RecipesModel;
import com.example.eric.bakingrecipes.Utils.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eric on 08/11/2017.
 */

public class IngredientsListFragment extends Fragment {

    private static final String EXTRA_INGREDIENTS = "EXTRA_INGREDIENTS";
    LinearLayoutManager layoutManager;

    private IngredientsAdapter mAdapter;
    @BindView(R.id.recyclerView_fragment_ingredients_list)
    RecyclerView mRecyclerView;

    public IngredientsListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_ingredients_list, container, false);
        ButterKnife.bind(this,view);

        List<RecipesModel.Ingredients> ingredients = new ArrayList<>();
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            ingredients = bundle.getParcelableArrayList(EXTRA_INGREDIENTS);
            L.log("From Ingredient " + String.valueOf(ingredients));

        }

        mAdapter = new IngredientsAdapter(getActivity(), ingredients, onclickListener);
        mRecyclerView.setAdapter(mAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        return view;
    }

    IngredientsAdapter.onIngredientSelect onclickListener = new IngredientsAdapter.onIngredientSelect() {
        @Override
        public void onItemClick(int position, List<RecipesModel.Ingredients> ingredients) {
            L.log(String.valueOf(position));
        }
    };
}

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
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eric.bakingrecipes.Adapters.StepsAdapter;
import com.example.eric.bakingrecipes.PlayerActivity;
import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.RecipesModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eric on 08/11/2017.
 *
 */

public class DetailListFragment extends Fragment {
    private static final String EXTRA_STEPS = "EXTRA_STEPS";
    private static final String EXTRA_INGREDIENTS = "EXTRA_INGREDIENTS";
    private static final String VIDEO_URL = "VIDEO_URL";
    private static final String SHORT_DESCRIPTION = "SHORT_DESCRIPTION";
    private static final String DESCRIPTION = "DESCRIPTION";

    LinearLayoutManager layoutManager;
    StepsAdapter adapter;

    @BindView(R.id.card_ingredients_button_wrapper)CardView ingredientButton;
    @BindView(R.id.recyclerView_detail_steps)RecyclerView recyclerView;

    //Constructor
    public DetailListFragment() {
        //should be empty
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_list,container,false);

        //Bind views
        ButterKnife.bind(this,view);

        ingredientButton.setOnClickListener(clickListener);

        getParcelableExtra();
        return view;
    }

    //Inner Methods



    StepsAdapter.onItemSelectListener  itemSelectListener= new StepsAdapter.onItemSelectListener() {
        @Override
        public void onItemClick(int position, List<RecipesModel.Steps> steps) {
            RecipesModel.Steps step = steps.get(position);

            String shortDescription = step.getShortDescription();
            String description = step.getDescription();
            String videoURL = step.getVideoURL();

                Intent intent = new Intent(getContext(),PlayerActivity.class);

                intent.putExtra(SHORT_DESCRIPTION,shortDescription);
                intent.putExtra(DESCRIPTION,shortDescription);

            if (description != null) {
                intent.putExtra(VIDEO_URL,videoURL);
            }

            startActivity(intent);
            }

    };

    // Ingredient Button Click Listener
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            List<RecipesModel.Ingredients> ingredients;
            Bundle bundle = getActivity().getIntent().getExtras();
            if (bundle != null) {
                ingredients = bundle.getParcelableArrayList(EXTRA_INGREDIENTS);
                Intent intent = new Intent(getContext(),IngredientsActivity.class);
                intent.putParcelableArrayListExtra(EXTRA_INGREDIENTS, (ArrayList<? extends Parcelable>) ingredients);
                startActivity(intent);
            }
        }
    };

    /**
     * gets intent data from MasterListFragment
     * */
    private void getParcelableExtra() {

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null){
            List<RecipesModel.Steps> steps = bundle.getParcelableArrayList(EXTRA_STEPS);
            adapter = new StepsAdapter(getContext(),steps,itemSelectListener);
        }

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}

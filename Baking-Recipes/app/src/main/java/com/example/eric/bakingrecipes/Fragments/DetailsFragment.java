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

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eric.bakingrecipes.Activities.IngredientsActivity;
import com.example.eric.bakingrecipes.Adapters.StepsAdapter;
import com.example.eric.bakingrecipes.PlayerActivityPhone;
import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.Utils.Data.RecipesModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eric on 08/11/2017.
 */

public class DetailsFragment extends Fragment {
    private static final String EXTRA_STEPS = "EXTRA_STEPS";
    private static final String EXTRA_INGREDIENTS = "EXTRA_INGREDIENTS";
    private static final String VIDEO_URL = "VIDEO_URL";
    private static final String SHORT_DESCRIPTION = "SHORT_DESCRIPTION";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String BUNDLED_STEPS = "BUNDLED_STEPS";
    private static final String BUNDLED_INGREDIENTS = "BUNDLED_INGREDIENTS";
    private static final String POSITION = "POSITION";

    @BindView(R.id.button_ingredients_list_launcher)
    TextView ingredientButton;
    @BindView(R.id.recyclerView_detail_steps)
    RecyclerView recyclerView;

    private StepsAdapter mAdapter;
    private List<RecipesModel.Steps> mSteps = new ArrayList<>();
    private List<RecipesModel.Ingredients> mIngredients = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;


    /**
     * creates new DetailsFragment which makes data available within the fragment through bundle
     * @param stepsData List of recipes steps data from the jsonArray response
     * @return new Instance of  DetailsFragment
     */
    public static DetailsFragment newFragment(List<RecipesModel.Steps> stepsData, List<RecipesModel.Ingredients> ingredientsData) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(BUNDLED_STEPS, (ArrayList<? extends Parcelable>) stepsData);
        args.putParcelableArrayList(BUNDLED_INGREDIENTS, (ArrayList<? extends Parcelable>) ingredientsData);
        fragment.setArguments(args);
        return fragment;
    }

    //Constructor
    public DetailsFragment() {
        //should be empty
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_list, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            //retrieves data from the bundle
            mSteps = bundle.getParcelableArrayList(EXTRA_STEPS);
            mIngredients = bundle.getParcelableArrayList(EXTRA_INGREDIENTS);

            //specifying an Adapter
            mAdapter = new StepsAdapter(getActivity(), mSteps, itemSelectListener);
            recyclerView.setAdapter(mAdapter);

            //using a LinearLayoutManager
            mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);

            //improve performance
            recyclerView.setHasFixedSize(true);
            recyclerView.setFocusable(false);

            //TODO add a custom one
            DividerItemDecoration dividerItemDecoration =
                    new DividerItemDecoration(recyclerView.getContext(),
                            mLayoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);
        }

        //launches IngredientsActivity
        ingredientButton.setOnClickListener(onButtonClick);

        //return method(-> View)
        return view;
    }

    //Inner Methods


    /**
     * TODO: comment and make name changes for more understanding
     */
    StepsAdapter.onItemSelectListener itemSelectListener = new StepsAdapter.onItemSelectListener() {
        @Override
        public void onItemClick(int position, List<RecipesModel.Steps> steps) {
            RecipesModel.Steps step = steps.get(position);

//            boolean isTablet = getResources().getBoolean(R.bool.isTablet);
            String shortDescription = step.getShortDescription();
            String description = step.getDescription();
            String videoURL = step.getVideoURL();

//            if (!isTablet) {
                Intent intent = new Intent(getContext(), PlayerActivityPhone.class);

                intent.putExtra(SHORT_DESCRIPTION, shortDescription);

                intent.putParcelableArrayListExtra(EXTRA_STEPS, (ArrayList<? extends Parcelable>) mSteps);

                intent.putExtra(DESCRIPTION, description);

                intent.putExtra(POSITION,position);

                intent.putExtra(VIDEO_URL, videoURL);

//            L.toast(getContext(),mSteps);
                startActivity(intent);

//            }
//            else {
//                PlayerFragment fragment = PlayerFragment.newFragment(shortDescription, description, videoURL);
//                FragmentManager manager = getFragmentManager();
//                manager.beginTransaction()
//                        .replace(R.id.frame_detail_container, fragment)
//                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
////                        .addToBackStack(null)
//                        .commit();
//            }
        }

    };

    //TODO comment on this and change name for better understanding
    View.OnClickListener onButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = IngredientsActivity.newIntent(getActivity(), mIngredients);
            startActivity(intent);
        }
    };

}



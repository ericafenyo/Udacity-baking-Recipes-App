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
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eric.bakingrecipes.Activities.IngredientsActivity;
import com.example.eric.bakingrecipes.Activities.PlayerActivityPhone;
import com.example.eric.bakingrecipes.Adapters.RecipeStepsAdapter;
import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.Utils.Data.RecipesModel;
import com.example.eric.bakingrecipes.Utils.DividerItemDecoration;
import com.example.eric.bakingrecipes.Utils.N;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eric on 08/11/2017
 */

public class DetailsFragment extends Fragment implements View.OnClickListener, RecipeStepsAdapter.onItemSelectListener {

    private static final String EXTRA_STEPS = "EXTRA_STEPS";
    private static final String EXTRA_INGREDIENTS = "EXTRA_INGREDIENTS";
    private static final String VIDEO_URL = "VIDEO_URL";
    private static final String SHORT_DESCRIPTION = "SHORT_DESCRIPTION";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String BUNDLED_STEPS = "BUNDLED_STEPS";
    private static final String BUNDLED_INGREDIENTS = "BUNDLED_INGREDIENTS";
    private static final String POSITION = "POSITION";
    private static final String RECIPE_NAME = "RECIPE_NAME";
    private static final String SERVINGS = "SERVINGS";
    private static final String TAG_INGREDIENT = "TAG_INGREDIENT";
    private static final String MST_KEY = "MST_KEY";

    private String shortDescription;
    private String description;
    private String videoURL;
    private List<RecipesModel.Steps> mSteps = new ArrayList<>();
    private List<RecipesModel.Ingredients> mIngredients = new ArrayList<>();

    @BindView(R.id.button_ingredients_list_launcher)
    TextView buttonLaunchIngredient;
    @BindView(R.id.recyclerView_detail_steps)
    RecyclerView recyclerView;
    @Nullable
    @BindView(R.id.text_view_detail_no_of_servings)
    TextView textViewServingsCount;
    @Nullable
    @BindView(R.id.text_view_detail_recipe_title)
    TextView textViewDetailRecipeTitle;
    @BindView(R.id.button_detail_share)
    ImageView buttonShare;

    /**
     * @param stepsData       list of recipe steps
     * @param ingredientsData list of recipe ingredients
     * @return instance of DetailsFragment
     */
    public static DetailsFragment newFragment(List<RecipesModel.Steps> stepsData,
                                              List<RecipesModel.Ingredients> ingredientsData) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(BUNDLED_STEPS, (ArrayList<? extends Parcelable>) stepsData);
        args.putParcelableArrayList(BUNDLED_INGREDIENTS,
                (ArrayList<? extends Parcelable>) ingredientsData);
        fragment.setArguments(args);
        return fragment;
    }

    //Constructor
    public DetailsFragment() {
        //should be empty
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_list, container, false);
        ButterKnife.bind(this, view);

        //verify type of device (returns true for a Tablet and false for a Handset)
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);

        //retrieve data from bundle
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            mSteps = bundle.getParcelableArrayList(EXTRA_STEPS);
            mIngredients = bundle.getParcelableArrayList(EXTRA_INGREDIENTS);
            textViewServingsCount.setText(String.format("Servings %s",
                    String.valueOf(bundle.getInt(SERVINGS))));
            textViewDetailRecipeTitle.setText(String.format(bundle.getString(RECIPE_NAME)));
            //specifying an Adapter
            RecipeStepsAdapter adapter = new RecipeStepsAdapter(getActivity(),
                    mSteps, this);
            recyclerView.setAdapter(adapter);

            //using a LinearLayoutManager
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);

            //improve performance
            recyclerView.setHasFixedSize(true);
            recyclerView.setFocusable(false);
            //custom RecyclerView ItemDecoration
            recyclerView.addItemDecoration(
                    new DividerItemDecoration(getActivity(),
                            getActivity().getDrawable(R.drawable.item_decorator)));
        }
        //open details view for the first item in the recyclerView when device is a tablet and in landscape mode
        if (isTablet) {
            if (getActivity().getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_LANDSCAPE) {
                int isFirstTime = N.getSLPreferences(getActivity(), MST_KEY, 0);
                if (isFirstTime == 0) {
                    new Handler().postDelayed(new Runnable() {
                        int pos = 0;

                        @Override
                        public void run() {
                            recyclerView.findViewHolderForAdapterPosition(pos).itemView.performClick();
                            N.storeSLPreferences(getActivity(), MST_KEY, 1);
                        }
                    }, 50);
                }
            }
        }
        //button to launch IngredientsActivity
        buttonLaunchIngredient.setOnClickListener(this);
        //share button
        buttonShare.setOnClickListener(this);

        //return method(-> View)
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (N.getSLPreferences(getActivity(), "MST_KEY", 0) == 1) {
            if (isTablet) {
                if (getActivity().getResources().getConfiguration().orientation ==
                        Configuration.ORIENTATION_LANDSCAPE) {

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            recyclerView.findViewHolderForAdapterPosition(N.getSLPreferences(getActivity(), "CLICK_POSITION_KEY", 0)).itemView.performClick();
                        }
                    }, 10);
                }
            }
        }
    }

    private void executeIntent(int position) {
        Intent intent = new Intent(getContext(), PlayerActivityPhone.class);
        intent.putParcelableArrayListExtra(EXTRA_STEPS, (ArrayList<? extends Parcelable>) mSteps);
        intent.putExtra(SHORT_DESCRIPTION, shortDescription);
        intent.putExtra(DESCRIPTION, description);
        intent.putExtra(POSITION, position);
        intent.putExtra(VIDEO_URL, videoURL);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_ingredients_list_launcher:

        //verify type of device (returns true for a Tablet and false for a Handset)
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (!isTablet) {
            Intent intent = IngredientsActivity.newIntent(getActivity(), mIngredients);
            startActivity(intent);
        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                Intent intent = IngredientsActivity.newIntent(getActivity(), mIngredients);
                startActivity(intent);
            } else {
                IngredientsFragment fragment = IngredientsFragment.newFragment(mIngredients);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.frame_player_container, fragment)
                        .addToBackStack(TAG_INGREDIENT)
                        .commit();
            }
        }

                break;
            case R.id.button_detail_share:

                StringBuilder sb = new StringBuilder();
                for (RecipesModel.Ingredients s : mIngredients)
                {
                    String string = s.getIngredient();
                    sb.append(string + "\n");
                }

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, (Serializable) sb);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            break;
        }
    }

    @Override
    public void onItemClick(int position, List<RecipesModel.Steps> steps) {
        //get recipe steps object at their respective position
        RecipesModel.Steps step = steps.get(position);
        //checks if the device is either a Tablet or a Handset
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        shortDescription = step.getShortDescription();
        description = step.getDescription();
        videoURL = step.getVideoURL();

        N.storeSLPreferences(getActivity(), "CLICK_POSITION_KEY", position);
        if (!isTablet) {
            //uses Intent to pass data to Player activity
            executeIntent(position);

        } else {
            Fragment playerFragment = PagerPlayerFragment.newFragment(shortDescription, description, videoURL);
            FragmentManager manager = getActivity().getSupportFragmentManager();

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                executeIntent(position);
            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                manager.beginTransaction()
                        .replace(R.id.frame_player_container, playerFragment, "TAG_PLAYER")
                        .commit();
            }
        }
    }
}
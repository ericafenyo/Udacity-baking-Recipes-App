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

package com.example.eric.bakingrecipes.Adapters;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.example.eric.bakingrecipes.Fragments.IngredientsFragment;
import com.example.eric.bakingrecipes.Utils.Data.RecipesModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 21/11/2017.
 */

public class IngredientsPagerAdapter extends FragmentStatePagerAdapter {

    private static final String EXTRA_INGREDIENTS = "EXTRA_INGREDIENTS";
    private List<RecipesModel> mRecipes;

    //constructor
    public IngredientsPagerAdapter(FragmentManager fm, List<RecipesModel> mRecipes) {
        super(fm);
        this.mRecipes = mRecipes;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new IngredientsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_INGREDIENTS, (ArrayList<? extends Parcelable>) mRecipes.get(position).getIngredients());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return mRecipes == null ? 0 : mRecipes.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
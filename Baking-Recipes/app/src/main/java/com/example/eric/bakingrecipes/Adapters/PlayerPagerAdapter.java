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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.example.eric.bakingrecipes.Fragments.PagerPlayerFragment;
import com.example.eric.bakingrecipes.Utils.Data.RecipesModel;

import java.util.List;

/**
 * Created by eric on 17/11/2017.
 */

public class PlayerPagerAdapter extends FragmentStatePagerAdapter {

    private static final String VIDEO_URL = "VIDEO_URL";
    private static final String SHORT_DESCRIPTION = "SHORT_DESCRIPTION";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String POSITION = "POSITION";
    private List<RecipesModel.Steps> mData;

    //constructor
    public PlayerPagerAdapter(FragmentManager fm, List<RecipesModel.Steps> mData) {
        super(fm);
        this.mData = mData;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new PagerPlayerFragment();
        Bundle args = new Bundle();
        String shortDescription = mData.get(position).getShortDescription();
        String description = mData.get(position).getDescription();
        String videoURL = mData.get(position).getVideoURL();
        args.putString(SHORT_DESCRIPTION, shortDescription);
        args.putString(DESCRIPTION, description);
        args.putString(VIDEO_URL, videoURL);
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}



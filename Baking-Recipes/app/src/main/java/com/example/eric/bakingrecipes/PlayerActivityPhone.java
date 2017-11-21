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

package com.example.eric.bakingrecipes;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.eric.bakingrecipes.Fragments.PlayerFragment;
import com.example.eric.bakingrecipes.Utils.Data.RecipesModel;
import com.example.eric.bakingrecipes.Utils.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerActivityPhone extends AppCompatActivity {

    private static final String EXTRA_STEPS = "EXTRA_STEPS";
    private static final String EXTRA_INGREDIENTS = "EXTRA_INGREDIENTS";
    private static final String VIDEO_URL = "VIDEO_URL";
    private static final String SHORT_DESCRIPTION = "SHORT_DESCRIPTION";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String BUNDLED_STEPS = "BUNDLED_STEPS";
    private static final String BUNDLED_INGREDIENTS = "BUNDLED_INGREDIENTS";
    private static final String POSITION = "POSITION";
    private int mPosition;
    private List<RecipesModel.Steps> mSteps;
    private int mPrevPosition;
    private int mNextPosition;
    private PlayerFragment fragment;
    FragmentManager manager;


    private PagerAdapter mPagerAdapter;

//    @BindView(R.id.button_prev)
//    TextView prevButton;
//    @BindView(R.id.button_next)
//    TextView nextButton;

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.button_prev)
    TextView prevButton;
    @BindView(R.id.button_next)
    TextView nextButton;


    SimpleExoPlayer mPlayer;
    //    @BindView(R.id.player_recipes)
//    SimpleExoPlayerView mPlayerView;
    Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_phone);
        ButterKnife.bind(this);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String shortDescription = bundle.getString(SHORT_DESCRIPTION);
            String description = bundle.getString(DESCRIPTION);
            String videoURL = bundle.getString(VIDEO_URL);
            mPosition = bundle.getInt(POSITION);
            mSteps = bundle.getParcelableArrayList(EXTRA_STEPS);
//            L.toast(getApplicationContext(), mSteps.get(mPosition).getShortDescription());


            mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), mSteps);
            viewPager.setAdapter(mPagerAdapter);
            viewPager.setCurrentItem(mPosition);
            viewPager.setOffscreenPageLimit(0);
            mPagerAdapter.notifyDataSetChanged();

        }

        prevButton.setOnClickListener(prevButtonOnclickListener);

        nextButton.setOnClickListener(nextButtonOnclickListener);

    }


    //inner methods
    //TODO: comment on previous button
    View.OnClickListener prevButtonOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mPrevPosition = viewPager.getCurrentItem() - 1;
            viewPager.setCurrentItem(mPrevPosition, true);

        }


    };

    View.OnClickListener nextButtonOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            mNextPosition = viewPager.getCurrentItem() + 1;
            viewPager.setCurrentItem(mNextPosition, true);
        }
    };


}

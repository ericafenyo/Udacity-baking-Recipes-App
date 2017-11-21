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

package com.example.eric.bakingrecipes.Activities;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.eric.bakingrecipes.Fragments.PlayerFragment;
import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.Utils.Data.RecipesModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlayerActivity extends AppCompatActivity {

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

    @BindView(R.id.button_prev)
    TextView prevButton;
    @BindView(R.id.button_next)
    TextView nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            fragment = new PlayerFragment();
            manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .add(R.id.frame_player_container, fragment)
                    .commit();
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String shortDescription = bundle.getString(SHORT_DESCRIPTION);
            String description = bundle.getString(DESCRIPTION);
            String videoURL = bundle.getString(VIDEO_URL);
            mPosition = bundle.getInt(POSITION);
            mSteps = bundle.getParcelableArrayList(EXTRA_STEPS);
//            L.toast(getApplicationContext(), steps.get(position).getShortDescription());

            if (mPosition == 0) {
                prevButton.setVisibility(View.INVISIBLE);
            } else if (mPosition == mSteps.size() - 1) {
                nextButton.setText("GO TO STEPS");
            }

        }

        prevButton.setOnClickListener(prevButtonOnclickListener);

        nextButton.setOnClickListener(nextButtonOnclickListener);
    }

    //inner methods
    //TODO: comment on previous button
    View.OnClickListener prevButtonOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mPrevPosition = mSteps.get(mPosition).getId() - 1;
            String prevShortDescription = mSteps.get(mPrevPosition).getShortDescription();
            String prevDescription = mSteps.get(mPrevPosition).getDescription();
            String prevVideoUrl = mSteps.get(mPrevPosition).getVideoURL();
//            L.toast(getApplicationContext(), prevVideoUrl);
            PlayerFragment prevFragment = PlayerFragment.newFragment(prevShortDescription,prevDescription,"http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
            manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.frame_player_container,prevFragment)
                    .commit();
        }
    };

    View.OnClickListener nextButtonOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            mNextPosition = mSteps.get(mPosition).getId() + 1;
            String nextShortDescription = mSteps.get(mNextPosition).getShortDescription();
            String nextDescription = mSteps.get(mNextPosition).getDescription();
            String nextVideoUrl = mSteps.get(mNextPosition).getVideoURL();
//            L.toast(getApplicationContext(), nextVideoUrl);

//            PagerPlayerFragment nextFragment = PagerPlayerFragment.newFragment(nextShortDescription,nextDescription,"http://www.html5videoplayer.net/videos/toystory.mp4");
//            manager = getSupportFragmentManager();
//            manager.beginTransaction()
//                    .replace(R.id.frame_player_container,nextFragment)
//                    .commit();

        }
    };
}

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

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.Utils.L;
import com.example.eric.bakingrecipes.Utils.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eric on 10/11/2017.
 */

public class PlayerFragment extends Fragment {

    private static final String VIDEO_URL = "VIDEO_URL";
    private static final String SHORT_DESCRIPTION = "SHORT_DESCRIPTION";
    private static final String DESCRIPTION = "DESCRIPTION";

    SimpleExoPlayer mPlayer;
    @BindView(R.id.player_recipes)
    SimpleExoPlayerView mPlayerView;

    Player player;

    public PlayerFragment() {
        //should be empty
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        //Bind views
        ButterKnife.bind(this, view);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            String shortDescription = bundle.getString(SHORT_DESCRIPTION);
            String description = bundle.getString(DESCRIPTION);


            String videoURL = bundle.getString(VIDEO_URL);
            if (videoURL.isEmpty()){
                mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.ph));

                mPlayerView.hideController();
                mPlayerView.setControllerVisibilityListener(new PlaybackControlView.VisibilityListener() {
                    @Override
                    public void onVisibilityChange(int i) {
                        if(i == 0) {
                            mPlayerView.hideController();
                        }
                    }
                });
            }
            L.log("From Player " + videoURL);
            player = new Player(getContext(), mPlayer, mPlayerView, Uri.parse(videoURL));
//
            player.initialize();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        player.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        player.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
    }
}
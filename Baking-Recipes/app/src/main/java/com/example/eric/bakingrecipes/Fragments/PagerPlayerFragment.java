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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.Utils.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eric on 10/11/2017
 */

public class PagerPlayerFragment extends Fragment {

    private static final String VIDEO_URL = "VIDEO_URL";
    private static final String SHORT_DESCRIPTION = "SHORT_DESCRIPTION";
    private static final String DESCRIPTION = "DESCRIPTION";

    @BindView(R.id.player_recipes)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.text_short_player_description)
    TextView text_view_step_short_description;
    @BindView(R.id.text_player_description)
    TextView text_view_step_description;
    @BindView(R.id.progress_bar_player)
    ProgressBar progressBar;

    SimpleExoPlayer simpleExoPlayer;
    Player player;
    private View view;

    /**
     * @param shortDescription recipe steps short descriptions
     * @param description      recipe main steps descriptions
     * @param videoURL         recipe steps video Url
     * @return instance of PagerPlayerFragment
     */
    public static PagerPlayerFragment newFragment(String shortDescription, String description,
                                                  String videoURL) {
        PagerPlayerFragment fragment = new PagerPlayerFragment();
        Bundle args = new Bundle();
        args.putString(SHORT_DESCRIPTION, shortDescription);
        args.putString(DESCRIPTION, description);
        args.putString(VIDEO_URL, videoURL);
        fragment.setArguments(args);
        return fragment;
    }

    public PagerPlayerFragment() {
        //should be empty
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_player, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();

        if (bundle != null) {
            String shortDescription = bundle.getString(SHORT_DESCRIPTION);
            String description = bundle.getString(DESCRIPTION);
            String videoURL = bundle.getString(VIDEO_URL);
            text_view_step_description.setText(description);
            text_view_step_short_description.setText(shortDescription);

            if (videoURL.isEmpty()) {
                progressBar.setVisibility(View.INVISIBLE);
                mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(),
                        R.drawable.video_error));
                mPlayerView.hideController();
                mPlayerView.setControllerVisibilityListener(new PlaybackControlView.VisibilityListener() {
                    @Override
                    public void onVisibilityChange(int i) {
                        if (i == 0) {
                            mPlayerView.hideController();
                        }
                    }
                });
            }
//            N.log(videoURL);
            player = new Player(getContext(), simpleExoPlayer, mPlayerView, Uri.parse(videoURL));
            player.initialize();
            player.checkState(progressBar);
        }
        return view;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && view != null) {
//            N.toast(getActivity(),"not visible");
            player.pause();
        } else if (isVisibleToUser && view != null) {
//            N.toast(getActivity(),"visible");
            player.initialize();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        player.pause();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }
}
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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eric.bakingrecipes.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eric on 10/11/2017
 */

public class PagerPlayerFragment extends Fragment implements Player.EventListener {

    private static final String VIDEO_URL = "VIDEO_URL";
    private static final String SHORT_DESCRIPTION = "SHORT_DESCRIPTION";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String CURRENT_POSITION = "CURRENT_POSITION";

    private SimpleExoPlayer mPlayer;
    private String videoURL;
    private TrackSelector trackSelector;
    private int resumeWindow;
    private long resumePosition;
    private View view;

    @BindView(R.id.player_recipes)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.text_short_player_description)
    TextView text_view_step_short_description;
    @BindView(R.id.text_player_description)
    TextView text_view_step_description;
    @BindView(R.id.progress_bar_player)
    ProgressBar progressBar;


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
            videoURL = bundle.getString(VIDEO_URL);
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
        }
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        resumePosition = C.TIME_UNSET;
        if (savedInstanceState != null) {
            //...your code...
            resumePosition = savedInstanceState.getLong(CURRENT_POSITION, C.TIME_UNSET);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initializePlayer();
        resumePosition = mPlayer.getContentPosition();
        mPlayer.seekTo(resumePosition);
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayer != null) {
            resumePosition = mPlayer.getCurrentPosition();
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPlayer != null) {
            resumePosition = mPlayer.getCurrentPosition();
            releasePlayer();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            resumePosition = mPlayer.getCurrentPosition();
            releasePlayer();
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(CURRENT_POSITION, resumePosition);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && view != null) {
            releasePlayer();

        } else if (isVisibleToUser && view != null) {
            initializePlayer();
        }
    }


    /**
     * Initialize ExoPlayer.
     */
    public void initializePlayer() {
        if (mPlayer == null) {
            // Create an instance of the ExoPlayer.
            trackSelector = new DefaultTrackSelector();
            mPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
            mPlayerView.setPlayer(mPlayer);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "ClassicalMusicQuiz");
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoURL),
                    new DefaultDataSourceFactory(getActivity(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            boolean haveResumePosition = resumeWindow != C.INDEX_UNSET;
            if (haveResumePosition) {
                mPlayer.seekTo(resumeWindow, resumePosition);
            }
            mPlayer.prepare(mediaSource);
        }
        mPlayer.addListener(this);
        mPlayer.setPlayWhenReady(false);
    }


    /**
     * Release Player.
     */
    public void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            trackSelector = null;
        }
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_READY) {
            progressBar.setVisibility(View.INVISIBLE);
        }else if (playbackState == Player.STATE_ENDED){
            progressBar.setVisibility(View.INVISIBLE);
            mPlayer.seekTo(0);
            mPlayer.setPlayWhenReady(false);
        }

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }
}
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

package com.example.eric.bakingrecipes.Utils;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by eric on 10/11/2017.
 */

public class Player {

    private Context context;
    private SimpleExoPlayer mPlayer;
    private SimpleExoPlayerView mPlayerView;
    private Uri videoUri;

    public Player(Context context, SimpleExoPlayer mPlayer, SimpleExoPlayerView mPlayerView, Uri videoUri) {
        this.context = context;
        this.mPlayer = mPlayer;
        this.mPlayerView = mPlayerView;
        this.videoUri = videoUri;
    }

    /**
     * Initialize ExoPlayer.
     */
    public void initialize() {

        if (mPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
            mPlayerView.setPlayer(mPlayer);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(context, "ClassicalMusicQuiz");

//            if (videoUri == null){
//                        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(context.getResources(), R.drawable.ph));
//            }
//            else {


            MediaSource mediaSource = new ExtractorMediaSource(videoUri, new DefaultDataSourceFactory(
                    context, userAgent), new DefaultExtractorsFactory(), null, null);
            mPlayer.prepare(mediaSource);}
                mPlayer.setPlayWhenReady(true);

        }
//    }

    /**
     * Release Player.
     */
    public void release() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    /**
     * pause Player.
     */
    public void pause() {
        if (mPlayer != null) {
            mPlayer.setPlayWhenReady(false);
            mPlayer.getPlaybackState();
        }
    }


    /**
     * resume Player.
     */
    public void start() {
        if (mPlayer != null) {
            mPlayer.getPlaybackState();
            mPlayer.setPlayWhenReady(true);
        }
    }
}

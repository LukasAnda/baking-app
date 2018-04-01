package com.lukasanda.bakingapp.ui.fragments;

import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.lukasanda.bakingapp.GlideApp;
import com.lukasanda.bakingapp.R;

/**
 * Created by lukas on 3/13/2018.
 */

public class StepVideoFragment extends Fragment implements ExoPlayer.EventListener {
    
    private SimpleExoPlayer mExoPlayer;
    
    private SimpleExoPlayerView mPlayerView;
    
    private ImageView mImageView;
    
    public static final String SAVE_POSITION = "savePosition";
    
    public static final String VIDEO_PLAYING = "videoPlaying";
    
    public static final int INVALID = -1;
    
    private long position;
    
    private boolean isPlaying = true;
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_exoplayer, container, false);
        
        mPlayerView = view.findViewById(R.id.playerView);
        
        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable
                .question_mark));
        
        mImageView = view.findViewById(R.id.image_view);
        
        return view;
    }
    
    public void reset() {
        mPlayerView.setVisibility(View.GONE);
        mImageView.setVisibility(View.VISIBLE);
        GlideApp.with(getActivity())
                .load(R.drawable.question_mark)
                //.error(getResources().getDrawable(android.R.drawable.sym_def_app_icon))
                .into(mImageView);
    }
    
    public void showImage(Uri uri) {
        mPlayerView.setVisibility(View.GONE);
        mImageView.setVisibility(View.VISIBLE);
        GlideApp.with(getActivity())
                .load(uri)
                //.error(getResources().getDrawable(android.R.drawable.sym_def_app_icon))
                .into(mImageView);
    }
    
    public void initPlayer(Uri uri, long position, boolean isPlaying) {
        mPlayerView.setVisibility(View.VISIBLE);
        mImageView.setVisibility(View.GONE);
        
        stopPlayer();
        
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector,
                    loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.setPlayWhenReady(isPlaying);
            mExoPlayer.addListener(this);
            if (position != INVALID) {
                mExoPlayer.seekTo(position);
            }
            
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "BakingApp");
            MediaSource mediaSource =
                    new ExtractorMediaSource(uri, new DefaultDataSourceFactory(getActivity(),
                            userAgent), new DefaultExtractorsFactory(), null,
                            null);
            mExoPlayer.prepare(mediaSource, position == INVALID, false);
        } else {
            mPlayerView.setVisibility(View.GONE);
        }
    }
    
    public void stopPlayer() {
        if (mExoPlayer != null) {
            position = mExoPlayer.getCurrentPosition();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
            mPlayerView.requestFocus();
        } else {
            position = -1;
        }
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(SAVE_POSITION, position);
        outState.putBoolean(VIDEO_PLAYING, isPlaying);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        stopPlayer();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPlayer();
    }
    
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
    
    }
    
    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    
    }
    
    @Override
    public void onLoadingChanged(boolean isLoading) {
    
    }
    
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        isPlaying = playWhenReady;
    }
    
    @Override
    public void onRepeatModeChanged(int repeatMode) {
    
    }
    
    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
    
    }
    
    @Override
    public void onPlayerError(ExoPlaybackException error) {
    
    }
    
    @Override
    public void onPositionDiscontinuity(int reason) {
    
    }
    
    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    
    }
    
    @Override
    public void onSeekProcessed() {
    
    }
}

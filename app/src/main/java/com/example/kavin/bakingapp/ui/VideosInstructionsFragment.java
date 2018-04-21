package com.example.kavin.bakingapp.ui;


import android.support.v4.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.kavin.bakingapp.Data.StepDataModel;
import com.example.kavin.bakingapp.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
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
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.net.Uri.parse;
import static com.example.kavin.bakingapp.ui.StepsIngredientsFragment.ARG_POSITION;
import static com.google.android.exoplayer2.ExoPlayer.STATE_READY;

/**
 * Created by kavin on 2/23/2018.
 */

public class VideosInstructionsFragment extends Fragment implements ExoPlayer.EventListener {
    @BindView(R.id.playerView)
    SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.RecipeInstructionsRP)
    TextView recipeInstructions;
    @BindView(R.id.previousButton)
    Button previousButton;
    @BindView(R.id.nextButton)
    Button nextButton;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.imagesThumbURL)
    ImageView imageView;
    private SimpleExoPlayer simpleExoPlayer;
    private MediaSessionCompat mediaSessionCompat;
    private PlaybackStateCompat.Builder mStateBuilder;
    private static final String TAG = VideosInstructionsFragment.class.getSimpleName();
    onItemListener onItemListener;
    private List<StepDataModel> stepDataModels;
    public int mListIndex;
    public int index_positionPlus;
    public int index_positionMinus;
    public String video_URL;
    public String image_URL;
    public long currentPosition;
    public boolean getPlayReady;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle saveInstanceState) {
        final View rootView  = inflater.inflate(R.layout.rightside_video_fragment, parent, false);
        ButterKnife.bind(this, rootView);
        currentPosition = C.TIME_UNSET;
        if (saveInstanceState != null) {
            stepDataModels = getArguments().getParcelableArrayList("steps_list");
            mListIndex = saveInstanceState.getInt(ARG_POSITION);
            currentPosition = saveInstanceState.getLong("selected_position", C.TIME_UNSET);
            getPlayReady = saveInstanceState.getBoolean("player_state");
            video_URL = stepDataModels.get(mListIndex).getVideoURL().toString();
            initializePlayer(parse(video_URL));
            final int[] position = saveInstanceState.getIntArray("ARTICLE_SCROLL_POSITION");
            if (position != null) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(position[0], position[1]);
                    }
                });
            }
        } else {
            stepDataModels = getArguments().getParcelableArrayList("steps_list");
        }

        try {
            image_URL = stepDataModels.get(mListIndex).getThumbnailURL().toString();
            recipeInstructions.setText(stepDataModels.get(mListIndex).getDescription().toString());
            video_URL = stepDataModels.get(mListIndex).getVideoURL().toString();
        } catch (Exception e) {
            Log.e("Index: ", "not in place");
        }

        if (image_URL.equalsIgnoreCase("")) {
            } else {
            Picasso.with(getContext()).load(image_URL)
                    .error(R.drawable.ic_error_placeholder)
                    .into(imageView);
        }

        if (video_URL.equalsIgnoreCase("")) {
            simpleExoPlayerView.setVisibility(View.INVISIBLE);
            simpleExoPlayerView.setVisibility(View.GONE);
        } else {
            initializePlayer(parse(video_URL));
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListIndex >= 0 && mListIndex < stepDataModels.size() -1) {
                    index_positionPlus = mListIndex + 1;
                    mListIndex = index_positionPlus;
                    recipeInstructions.setText(stepDataModels.get(index_positionPlus).getDescription().toString());
                    video_URL = stepDataModels.get(index_positionPlus).getVideoURL().toString();

                    releasePlayer();
                    if (video_URL.equalsIgnoreCase("")) {
                        simpleExoPlayerView.setVisibility(View.INVISIBLE);
                        simpleExoPlayerView.setVisibility(View.GONE);
                    } else {
                        initializePlayer(parse(video_URL));
                    }
                    simpleExoPlayer.seekTo(0);
                } else {
                    recipeInstructions.setText(stepDataModels.get(mListIndex).getDescription().toString());
                    video_URL = stepDataModels.get(mListIndex).getVideoURL().toString();
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListIndex > 0 && mListIndex < stepDataModels.size()) {
                    index_positionMinus = mListIndex - 1;
                    recipeInstructions.setText(stepDataModels.get(index_positionMinus).getDescription().toString());
                    video_URL = stepDataModels.get(index_positionMinus).getVideoURL().toString();
                    mListIndex = index_positionMinus;

                    if (video_URL.equalsIgnoreCase("")) {
                        simpleExoPlayerView.setVisibility(View.INVISIBLE);
                        simpleExoPlayerView.setVisibility(View.GONE);
                    } else {
                        initializePlayer(parse(video_URL));
                    }
                    simpleExoPlayer.seekTo(0);
                } else {
                    recipeInstructions.setText(stepDataModels.get(mListIndex).getDescription().toString());
                    video_URL = stepDataModels.get(mListIndex).getVideoURL().toString();
                }
            }
        });

        simpleExoPlayerView.requestFocus();
        initializeMediaSession();
        return rootView;
    }

    public static VideosInstructionsFragment newInstance(ArrayList<StepDataModel> stepDataModels) {
        VideosInstructionsFragment videosInstructionsFragment = new VideosInstructionsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("steps_list", stepDataModels);
        videosInstructionsFragment.setArguments(args);
        return videosInstructionsFragment;
    }


    public interface onItemListener { void onItemSelected(int i); }

    public void setListIndex(int index) { mListIndex = index; }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onItemListener = (onItemListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement onItemListener");
        }
    }

    private void initializePlayer(Uri mediaUri) {
        // Create an instance of the ExoPlayer
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this.getActivity(), trackSelector, loadControl);
        simpleExoPlayerView.setPlayer(simpleExoPlayer);

        // Set the ExoPlayer.EventListener to this activity
        simpleExoPlayer.addListener(this);

        // Prepare the MediaSource
        String userAgent = Util.getUserAgent(this.getActivity(), "BakingApp");
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                this.getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
        if (currentPosition != C.TIME_UNSET) simpleExoPlayer.seekTo(currentPosition);
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (simpleExoPlayer != null && simpleExoPlayerView != null) {
            startPlayer();
            simpleExoPlayer.setPlayWhenReady(getPlayReady);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null && simpleExoPlayerView != null) {
            currentPosition = simpleExoPlayer.getCurrentPosition();
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
        }
    }

    private void startPlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlayWhenReady(true);
            simpleExoPlayer.getPlaybackState();
        }
    }

    public boolean isPlayer() {
        return simpleExoPlayer.getPlayWhenReady();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (simpleExoPlayer != null) {
            outState.putLong("selected_position", currentPosition);
            outState.putBoolean("player_state", isPlayer());
        }
        outState.putIntArray("ARTICLE_SCROLL_POSITION", new int[] { scrollView.getScrollX(), scrollView.getScrollY()});
        outState.putInt(ARG_POSITION, mListIndex);
    }

    private void initializeMediaSession() {
        mediaSessionCompat = new MediaSessionCompat(this.getActivity(), TAG);
        mediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mediaSessionCompat.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSessionCompat.setPlaybackState(mStateBuilder.build());
        mediaSessionCompat.setCallback(new MySessionCallback());
        mediaSessionCompat.setActive(true);
    }

    private void showNotification(PlaybackStateCompat state) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getActivity());

        int icon;
        String play_pause;
        if(state.getState() == PlaybackStateCompat.STATE_PLAYING) {
            icon = R.drawable.exo_controls_pause;
            play_pause = getString(R.string.pause);
        } else {
            icon = R.drawable.exo_controls_play;
            play_pause = getString(R.string.play);
        }

        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(
                icon, play_pause, MediaButtonReceiver.buildMediaButtonPendingIntent(this.getActivity(),
                PlaybackStateCompat.ACTION_PLAY_PAUSE));

        NotificationCompat.Action restartAction = new android.support.v4.app.NotificationCompat.Action
                (R.drawable.exo_controls_previous, getString(R.string.restart),
                        MediaButtonReceiver.buildMediaButtonPendingIntent(this.getActivity(),
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        builder.setContentTitle(getString(R.string.guess))
                .setContentText(getString(R.string.notification_text))
                .setSmallIcon(R.drawable.ic_music_note)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(restartAction)
                .addAction(playPauseAction)
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSessionCompat.getSessionToken())
                        .setShowActionsInCompactView(0,1));
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
        if((playbackState == STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    simpleExoPlayer.getCurrentPosition(), 1f);
        } else if (playbackState == STATE_READY) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    simpleExoPlayer.getCurrentPosition(), 1f);
        }
        mediaSessionCompat.setPlaybackState(mStateBuilder.build());
        showNotification(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity() {
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            simpleExoPlayer.setPlayWhenReady(true);
        }
        @Override
        public void onPause() {
            simpleExoPlayer.setPlayWhenReady(false);
        }
        @Override
        public void onSkipToPrevious() {
            simpleExoPlayer.seekTo(0);
        }
    }

    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
        }
    }
}
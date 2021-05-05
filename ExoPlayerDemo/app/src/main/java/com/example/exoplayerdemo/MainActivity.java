package com.example.exoplayerdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    Button volumeControl;
    PlayerView playerView;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private SimpleExoPlayer player;
    TextView volumeStatus;

    int currentvolume;

    AspectRatioFrameLayout aspectRatioFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aspectRatioFrameLayout = findViewById(R.id.aspectRatioFL);
        playerView = findViewById(R.id.video_view);
        volumeControl = findViewById(R.id.volumeControl);
        volumeStatus = findViewById(R.id.volumeStatus);

        // This line sets the default aspect ratio of the ExoPlayer
        aspectRatioFrameLayout.setAspectRatio(16f/9f);

        volumeControl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(player.getVolume() == currentvolume){
                    player.setVolume(0f);
                    volumeStatus.setText(getResources().getString(R.string.volume_status_mute));
                    volumeControl.setBackground(ContextCompat
                            .getDrawable(getApplicationContext(),
                                    R.drawable.baseline_volume_off_black_24));
                } else {
                    player.setVolume(currentvolume);
                    volumeStatus.setText(getResources().getString(R.string.volume_status_on));
                    volumeControl.setBackground(ContextCompat
                            .getDrawable(getApplicationContext(),
                                    R.drawable.baseline_volume_up_black_24));
                }
            }
        });


    }

    private void initializePlayer() {
        if (player == null) {
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(this);
            trackSelector.setParameters(
                    trackSelector.buildUponParameters().setMaxVideoSizeSd());
            player = new SimpleExoPlayer.Builder(this)
                    .setTrackSelector(trackSelector)
                    .build();
        }

        playerView.setPlayer(player);

        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(getString(R.string.media_url_hls))
                .setMimeType(MimeTypes.APPLICATION_M3U8)
                .build();

        player.setMediaItem(mediaItem);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare();

        // This line gets the current volume of the stream
        // Use this upon upping muted volume
        currentvolume = (int) player.getVolume();

    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }


//    ````````vvvvvvvvvvvvvvvvvvvvvv ANDROID LIFE CYCLE vvvvvvvvvvvvvvvvvvvvvv'''''''''''

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            initializePlayer();
            Log.v(TAG, "onStart()...");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer();
            Log.v(TAG, "onResume()...");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
            Log.v(TAG, "onPause()...");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
            Log.v(TAG, "onStop()...");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy()...");
        releasePlayer();
    }

}
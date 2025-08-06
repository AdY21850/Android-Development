package com.example.watchit;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class media_player extends AppCompatActivity {

    private VideoView videoView;
    private ImageButton btnPlay, btnPause, btnFullscreen;
    private boolean isFullscreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        // Initialize UI components
        videoView = findViewById(R.id.videoView);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnFullscreen = findViewById(R.id.btnFullscreen);

        // Get video URL from the Intent
        Intent intent = getIntent();
        if (intent != null) {
            String videoUrl = intent.getStringExtra("videoUrl");

            if (videoUrl != null && !videoUrl.isEmpty()) {
                Log.d("movie URRRRRRRRl:--->",videoUrl);
                Uri videoUri = Uri.parse(videoUrl);
                videoView.setVideoURI(videoUri);
            }

            else {
                Toast.makeText(getApplicationContext(), "url de dengu", Toast.LENGTH_SHORT).show();
            }
        }

        btnPlay.setOnClickListener(v -> {
            videoView.start();
            btnPlay.setVisibility(View.GONE);
            btnPause.setVisibility(View.VISIBLE);
        });

        btnPause.setOnClickListener(v -> {
            videoView.pause();
            btnPlay.setVisibility(View.VISIBLE);
            btnPause.setVisibility(View.GONE);
        });

        btnFullscreen.setOnClickListener(v -> toggleFullscreen());
    }

    private void toggleFullscreen() {
        if (isFullscreen) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            btnFullscreen.setImageResource(R.drawable.ic_fullscreen);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            hideSystemUI();
        }
        isFullscreen = !isFullscreen;
    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    @Override
    public void onConfigurationChanged(@NonNull android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
            btnPlay.setVisibility(View.GONE);
            btnPause.setVisibility(View.GONE);
            btnFullscreen.setVisibility(View.GONE);
        } else {
            btnFullscreen.setVisibility(View.VISIBLE);
        }
    }
}
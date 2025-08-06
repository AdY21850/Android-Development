package com.example.cumealcopy2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class timming extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timming);
        ImageView imageViewGif = findViewById(R.id.imageViewGif);
        ImageView Tmenu = findViewById(R.id.tommorowing);
        TextView TM = findViewById(R.id.tommorowingtxt);
        ImageView time = findViewById(R.id.homing);
        TextView TB = findViewById(R.id.homeingtxt);

        Tmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j=new Intent(timming.this, tommorow.class);
                startActivity(j);
                finish();
            }
        });

        TM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(timming.this, tommorow.class);
                startActivity(i);
                finish();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(timming.this, MainActivity.class));
                finish();
            }
        });

        TB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(timming.this, MainActivity.class));
                finish();
            }
        });


        // Load and animate the GIF using Glide
        Glide.with(this)
                .asGif()
                .load(R.drawable.clock)
                .into(new ImageViewTarget<GifDrawable>(imageViewGif) {
                    @Override
                    protected void setResource(@Nullable GifDrawable resource) {
                        if (resource != null) {
                            // Start GIF animation
                            resource.start();
                        }
                        // Set the resource (GIF) to the ImageView
                        view.setImageDrawable(resource);
                    }
                });
    }
}

package com.example.watchit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class profile extends AppCompatActivity {
    LinearLayout navExplore, navLike, navCart, navProfile;
    TextView textExplore, textLike, textCart, textProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        navExplore = findViewById(R.id.nav_explore1);
        navLike = findViewById(R.id.nav_like1);
        navCart = findViewById(R.id.nav_cart1);
        navProfile = findViewById(R.id.nav_profile1);

        textExplore = findViewById(R.id.text_explore1);
        textLike = findViewById(R.id.text_like1);
        textCart = findViewById(R.id.text_cart1);
        textProfile = findViewById(R.id.text_profile1);

        updateNavSelection(textProfile);

        navExplore.setOnClickListener(v -> updateNavSelection(textExplore));
        navLike.setOnClickListener(v -> updateNavSelection(textLike));
        navCart.setOnClickListener(v -> updateNavSelection(textCart));
        navProfile.setOnClickListener(v -> {updateNavSelection(textProfile);
            });


       findViewById(R.id.backtohome).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(profile.this, dashboard.class));
               finish();
           }
       });



    }
    private void updateNavSelection(TextView selectedTextView) {
        textExplore.setVisibility(View.GONE);
        textLike.setVisibility(View.GONE);
        textCart.setVisibility(View.GONE);
        textProfile.setVisibility(View.GONE);
        selectedTextView.setVisibility(View.VISIBLE);
    }
}
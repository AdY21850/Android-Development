package com.example.cumealcopy2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class tommorow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tommorow);

        // Initialize TextViews
        TextView breakfastTextView = findViewById(R.id.breakfastt);
        TextView lunchTextView = findViewById(R.id.luncht);
        TextView snacksBoysTextView = findViewById(R.id.snacksbt);
        TextView snacksGirlsTextView = findViewById(R.id.snacksgt);
        TextView dinnerTextView = findViewById(R.id.dinnert);
        TextView southIndianDinnerTextView = findViewById(R.id.southindiandinnert);

        // Initialize ImageViews and TextViews
        ImageView homeImageView = findViewById(R.id.referhome);
        TextView homeTextView = findViewById(R.id.hometxtt);
        ImageView timeImageView = findViewById(R.id.timingbtnttime);
        TextView timeTextView = findViewById(R.id.timingtxtt);

        // Setting onClickListeners
        homeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(tommorow.this, MainActivity.class));
                finish();
            }
        });

        homeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(tommorow.this, MainActivity.class));
                finish();
            }
        });

        timeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(tommorow.this, timming.class));
                finish();
            }
        });

        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(tommorow.this, timming.class));
                finish();
            }
        });

        // Read data from the database for tomorrow
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1); // Add 1 to get tomorrow's date
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String tomorrowDate = String.valueOf(day);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(tomorrowDate);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String breakfast = dataSnapshot.child("breakfast").getValue(String.class);
                    String lunch = dataSnapshot.child("lunch").getValue(String.class);
                    String snacksBoys = dataSnapshot.child("snacksboys").getValue(String.class);
                    String snacksGirls = dataSnapshot.child("snacks").getValue(String.class);
                    String dinner = dataSnapshot.child("dinner").getValue(String.class);
                    String southDine = dataSnapshot.child("southindiandinner").getValue(String.class);

                    // Update TextViews with menu items for tomorrow
                    breakfastTextView.setText(breakfast);
                    lunchTextView.setText(lunch);
                    snacksBoysTextView.setText(snacksBoys);
                    snacksGirlsTextView.setText(snacksGirls);
                    dinnerTextView.setText(dinner);
                    southIndianDinnerTextView.setText(southDine);
                } else {
                    // Handle if menu data for tomorrow doesn't exist
                    Toast.makeText(tommorow.this, "Menu not available for tomorrow", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle errors
                Toast.makeText(tommorow.this, "Failed to read menu data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

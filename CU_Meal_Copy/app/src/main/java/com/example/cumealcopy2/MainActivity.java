package com.example.cumealcopy2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

import com.example.cumealcopy2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        TextView breakfastTextView = findViewById(R.id.breakfasttextview);
        TextView lunchTextView = findViewById(R.id.lunchtextview);
        TextView snacksBoysTextView = findViewById(R.id.snackbtextview);
        TextView snacksGirlsTextView = findViewById(R.id.snackgtextview);
        TextView dinnerTextView = findViewById(R.id.dinnertextview);
        TextView southIndianDinnerTextView = findViewById(R.id.southindiandinnertextview);

        // Initialize buttons and text
        ImageView referToTomorrowButton = findViewById(R.id.refertexttomorow);
        TextView bookTimeTextView = findViewById(R.id.booktexttimettt);
        ImageView timingButton = findViewById(R.id.timingbtnttime);
        TextView timingTextView = findViewById(R.id.timingtxt);

        // Set onClickListeners for buttons
        referToTomorrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, tommorow.class));
                finish();
            }
        });

        bookTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, tommorow.class));
                finish();
            }
        });

        timingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, timming.class));
                finish();
            }
        });

        timingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, timming.class));
                finish();
            }
        });

        // Read today's menu from the database
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatabaseReference menuRef = FirebaseDatabase.getInstance().getReference(String.valueOf(day));

        menuRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String breakfast = dataSnapshot.child("breakfast").getValue(String.class);
                    String lunch = dataSnapshot.child("lunch").getValue(String.class);
                    String snacksBoys = dataSnapshot.child("snacksboys").getValue(String.class);
                    String snacksGirls = dataSnapshot.child("snacks").getValue(String.class);
                    String dinner = dataSnapshot.child("dinner").getValue(String.class);
                    String southDine = dataSnapshot.child("southindiandinner").getValue(String.class);

                    // Update TextViews with menu items
                    breakfastTextView.setText(breakfast);
                    lunchTextView.setText(lunch);
                    snacksBoysTextView.setText(snacksBoys);
                    snacksGirlsTextView.setText(snacksGirls);
                    dinnerTextView.setText(dinner);
                    southIndianDinnerTextView.setText(southDine);
                } else {
                    // Handle if menu data for the day doesn't exist
                    Toast.makeText(MainActivity.this, "Menu not available for today", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle errors
                Toast.makeText(MainActivity.this, "Failed to read menu data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

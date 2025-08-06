package com.example.bikerentalcu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class notifications extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private ProgressBar progressBar;
    private TextView emptyCart;
    private NotificationAdapter adapter;
    private List<NotificationItem> notificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications); // Replace with your actual layout name

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        emptyCart = findViewById(R.id.emptycart);

        notificationList = new ArrayList<>();
        adapter = new NotificationAdapter(notificationList);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);

        // Simulate loading notification (real ones can come from FCM or internal broadcast)
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("body");

        if (title != null && message != null) {
            notificationList.add(new NotificationItem(title, message));
            adapter.notifyDataSetChanged();
            emptyCart.setVisibility(View.GONE);
        } else {
            emptyCart.setVisibility(View.VISIBLE);
        }

        progressBar.setVisibility(View.GONE);

        // Back button functionality
        findViewById(R.id.backButton).setOnClickListener(v -> onBackPressed());
    }
}

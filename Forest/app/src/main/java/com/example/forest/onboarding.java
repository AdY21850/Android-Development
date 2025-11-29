package com.example.forest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.forest.api.RetrofitClient;
import com.example.forest.model.OnboardingItem;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class onboarding extends AppCompatActivity {

    private ViewPager2 viewPager;
    private OnboardingAdapter adapter;

    // UI Elements for the Static Card
    private TextView tvTitle, tvDescription;

    // Store the list here so we can access it when swiping
    private List<OnboardingItem> onboardingItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.onboarding);

        // Initialize Views
        viewPager = findViewById(R.id.viewPager);
        DotsIndicator dotsIndicator = findViewById(R.id.dotsIndicator);
        TextView tvSkip = findViewById(R.id.tvSkip);
        Button btnNext = findViewById(R.id.btnNext);

        // Initialize Card Text Views
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);

        // Setup Adapter
        adapter = new OnboardingAdapter();
        viewPager.setAdapter(adapter);
        dotsIndicator.attachTo(viewPager);

        // Disable manual swiping (as per your previous request)
        viewPager.setUserInputEnabled(false);

        // --- PAGE CHANGE LISTENER ---
        // This updates the text when the page changes
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateCardText(position);
            }
        });

        // Skip Button
        tvSkip.setOnClickListener(v -> navigateToLogin());

        // Next Button
        btnNext.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            int totalItems = adapter.getItemCount();

            if (currentItem < totalItems - 1) {
                viewPager.setCurrentItem(currentItem + 1);
            } else {
                navigateToLogin();
            }
        });

        // Fetch Data
        fetchData(adapter);
    }

    private void updateCardText(int position) {
        // Ensure list is not empty and position is valid
        if (!onboardingItems.isEmpty() && position < onboardingItems.size()) {
            OnboardingItem item = onboardingItems.get(position);
            tvTitle.setText(item.getTitle());
            tvDescription.setText(item.getDescription());
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(onboarding.this, Login.class);
        startActivity(intent);
        finish();
    }

    private void fetchData(OnboardingAdapter adapter) {
        RetrofitClient.getApi().getOnboardingSlides().enqueue(new Callback<List<OnboardingItem>>() {
            @Override
            public void onResponse(Call<List<OnboardingItem>> call, Response<List<OnboardingItem>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    onboardingItems = response.body();


                    adapter.setItems(onboardingItems);


                    updateCardText(0);
                } else {
                    Toast.makeText(onboarding.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OnboardingItem>> call, Throwable t) {
                Toast.makeText(onboarding.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
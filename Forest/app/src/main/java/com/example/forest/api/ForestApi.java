package com.example.forest.api;

import com.example.forest.model.OnboardingItem;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
public interface ForestApi {
    @GET("api/v1/onboarding")
    Call<List<OnboardingItem>> getOnboardingSlides();
}

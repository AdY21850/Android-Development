package com.example.watchit;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

public class dashboard extends AppCompatActivity {

    LinearLayout navExplore, navLike, navCart, navProfile;
    TextView textExplore, textLike, textCart, textProfile;
    ViewPager2 viewPager;
    ProgressBar progressBar1, progressBar2, progressBar3, progressBar4;
    RecyclerView recyclerViewTopMovies, recyclerViewUpcomingMovies, recyclerViewRecommendations;
    TopMoviesAdapter topMoviesAdapter;
    MovieAdapter movieAdapter;
    RecommendationAdapter recommendationAdapter;
    upcoming_movie_adapter upcomingMovieAdapter;

    private List<Model_movie> bannerMovies;
    private List<Model_movie1> movieList1;
    private List<RecommendationModel> recommendationList;

    private Handler sliderHandler = new Handler();

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            int currentItem = viewPager.getCurrentItem();
            int nextItem = (currentItem + 1) % bannerMovies.size();
            viewPager.setCurrentItem(nextItem, true);
            sliderHandler.postDelayed(this, 3000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        navExplore = findViewById(R.id.nav_explore);
        navLike = findViewById(R.id.nav_like);
        navCart = findViewById(R.id.nav_cart);
        navProfile = findViewById(R.id.nav_profile);

        textExplore = findViewById(R.id.text_explore);
        textLike = findViewById(R.id.text_like);
        textCart = findViewById(R.id.text_cart);
        textProfile = findViewById(R.id.text_profile);

        navExplore.setOnClickListener(v -> {updateNavSelection(textExplore);
            Intent intent = new Intent(dashboard.this, explore.class); // Open ProfileActivity
            startActivity(intent);});
        navLike.setOnClickListener(v -> {updateNavSelection(textLike);
            Intent intent = new Intent(dashboard.this, wishlist.class); // Open ProfileActivity
            startActivity(intent);});
        navCart.setOnClickListener(v -> {updateNavSelection(textCart);
            Intent intent = new Intent(dashboard.this, downloads.class); // Open ProfileActivity
            startActivity(intent);});
        navProfile.setOnClickListener(v -> {updateNavSelection(textProfile);
                Intent intent = new Intent(dashboard.this, profile.class); // Open ProfileActivity
        startActivity(intent);});

        viewPager = findViewById(R.id.ViewPager2);
        progressBar1 = findViewById(R.id.progressBar);
        progressBar2 = findViewById(R.id.progressBarTopMovies);
        progressBar3 = findViewById(R.id.progressBarUpcomingMovies);
        progressBar4 = findViewById(R.id.progressBarrecomendations);

        bannerMovies = new ArrayList<>();
        bannerMovies.add(new Model_movie(
                "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742661917/imacculate_qkhol5.jpg?_s=public-apps",
                "https://res.cloudinary.com/dorc5p2jg/video/upload/v1742910601/Snapchat-1490782232_cv4baz.mp4",
                "Immaculate",
                "History | Thriller | Drama",
                "2023",
                "9.1",
                "A gripping tale of survival."
        ));

        bannerMovies.add(new Model_movie(
                "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742661915/godzillaxkong_ba94tb.jpg?_s=public-apps",
                "https://res.cloudinary.com/dorc5p2jg/video/upload/v1742910601/Snapchat-1490782232_cv4baz.mp4",
                "Godzilla x Kong",
                "Action | Sci-Fi",
                "2019",
                "8.4",
                "Epic titan battle."
        ));

        bannerMovies.add(new Model_movie(
                "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742661912/damaged_img_lgioen.jpg?_s=public-apps",
                "https://res.cloudinary.com/dorc5p2jg/video/upload/v1742910601/Snapchat-1490782232_cv4baz.mp4",
                "Damaged",
                "Sci-Fi | Thriller",
                "2010",
                "8.8",
                "Human enhancement & AI dominance."
        ));

        bannerMovies.add(new Model_movie(
                "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742661918/intro_logo_irbjv2.jpg?_s=public-apps",

                "https://res.cloudinary.com/dorc5p2jg/video/upload/v1742910601/Snapchat-1490782232_cv4baz.mp4","Three Musketeers",
                "Action | Crime",
                "2008",
                "9.0",
                "Modern retelling of a classic."
        ));
        bannerMovies.add(new Model_movie(
                "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742661928/wide2_dkpsid.jpg?_s=public-apps",

                "https://res.cloudinary.com/dorc5p2jg/video/upload/v1742910601/Snapchat-1490782232_cv4baz.mp4","Deadpool",
                "Action | comedy | thriller",
                "2008",
                "9.0",
                "Modern retelling of a classic."
        ));


        movieAdapter = new MovieAdapter(bannerMovies, viewPager, this, (position, movie) -> {
            Intent intent = new Intent(this, Movies_Details_Activity.class);
            intent.putExtra("movieTitle", movie.getTitle());
            intent.putExtra("movieGenre", movie.getGenre());
            intent.putExtra("movieYear", movie.getYear());
            intent.putExtra("movieRating", movie.getRating());
            intent.putExtra("movieImage", movie.getImageUrl());
            intent.putExtra("movielink", movie.getmovieurl());
            intent.putExtra("summary", movie.getSummary());
            startActivity(intent);
        });
        viewPager.setAdapter(movieAdapter);

        sliderHandler.postDelayed(sliderRunnable, 3000);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });

        progressBar1.setVisibility(View.GONE);

        recyclerViewTopMovies = findViewById(R.id.recyclerViewTopMovies);
        recyclerViewTopMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        movieList1 = new ArrayList<>();
        movieList1.add(new Model_movie1(
                "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742661915/godzillaxkong_ba94tb.jpg?_s=public-apps",
                "https://res.cloudinary.com/dorc5p2jg/video/upload/v1742910601/Snapchat-1490782232_cv4baz.mp4",
                "Godzilla x Kong",
                "Action, Sci-Fi",
                "2024",
                "8.2",
                "The battle continues as Godzilla and King Kong unite against a new monstrous threat."
        ));

        movieList1.add(new Model_movie1(
                "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742661918/kungfupanda_hfnkab.jpg?_s=public-apps",
                "https://res.cloudinary.com/dorc5p2jg/video/upload/v1742910601/Snapchat-1490782232_cv4baz.mp4",
                "Kung Fu Panda",
                "Animation, Action, Comedy",
                "2008",
                "7.6",
                "Po, an unlikely kung fu enthusiast, is chosen as the Dragon Warrior to protect the Valley of Peace."
        ));

        movieList1.add(new Model_movie1(
                "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742661912/damaged_img_lgioen.jpg?_s=public-apps",
                "https://res.cloudinary.com/dorc5p2jg/video/upload/v1742910601/Snapchat-1490782232_cv4baz.mp4",
                "Damaged",
                "Thriller, Crime",
                "2023",
                "6.9",
                "A detective is forced to confront his past while investigating a series of gruesome murders."
        ));

        movieList1.add(new Model_movie1(
                "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742661919/no_way_up_znivtr.jpg?_s=public-apps",
                "https://res.cloudinary.com/dorc5p2jg/video/upload/v1742910601/Snapchat-1490782232_cv4baz.mp4",
                "No Way Up",
                "Adventure, Survival",
                "2024",
                "7.4",
                "A group of passengers fight to survive after their plane crashes into the ocean."
        ));

        topMoviesAdapter = new TopMoviesAdapter(this, movieList1, (position, movie) -> {
            // Handle click event
            Intent intent = new Intent(this, Movies_Details_Activity.class);
            intent.putExtra("movieTitle", movie.getTitle());
            intent.putExtra("movieGenre", movie.getGenre());
            intent.putExtra("movieYear", movie.getYear());
            intent.putExtra("movieRating", movie.getRating());
            intent.putExtra("movieImage", movie.getImageUrl());
            Log.d("mila gaya url--->",movie.getmovieurl());
            intent.putExtra("movielink", movie.getmovieurl());
            intent.putExtra("summary", movie.getSummary());
            startActivity(intent);
        });

        recyclerViewTopMovies.setAdapter(topMoviesAdapter);
        progressBar2.setVisibility(View.GONE);


        recyclerViewUpcomingMovies = findViewById(R.id.recyclerViewUpcommingMovies);
        recyclerViewUpcomingMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<Model_movie1> upcomingMovies = new ArrayList<>();
        upcomingMovies.add(new Model_movie1(
                "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742661927/wide1_ycc9la.jpg?_s=public-apps",
                "https://res.cloudinary.com/dorc5p2jg/video/upload/v1742910601/Snapchat-1490782232_cv4baz.mp4",
                "Avengers",
                "Action, Sci-Fi",
                "2024",
                "8.2",
                "Earth's mightiest heroes must come together and learn to fight as a team if they are going to stop the mischievous Loki and his alien army from enslaving humanity."
        ));

        upcomingMovies.add(new Model_movie1(
                "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742661927/wide_ukg6ot.jpg?_s=public-apps",
                "https://res.cloudinary.com/dorc5p2jg/video/upload/v1742910601/Snapchat-1490782232_cv4baz.mp4",
                "1917",
                "Autobiography",
                "2008",
                "7.6",
                "April 6th, 1917. As an infantry battalion assembles to wage war deep in enemy territory, two soldiers are assigned to race against time and deliver a message that will stop 1,600 men from walking straight into a deadly trap."
        ));

        upcomingMovies.add(new Model_movie1(
                "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742661928/wide3_jcqd72.jpg?_s=public-apps",
                "https://res.cloudinary.com/dorc5p2jg/video/upload/v1742910601/Snapchat-1490782232_cv4baz.mp4",
                "Openhiemer",
                "Autobiography",
                "2023",
                "6.9",
                "During World War II, Lt. Gen. Leslie Groves Jr. appoints physicist J. Robert Oppenheimer to work on the top-secret Manhattan Project. Oppenheimer and a team of scientists spend years developing and designing the atomic bomb. Their work comes to fruition on July 16, 1945, as they witness the world's."
        ));

        upcomingMovies.add(new Model_movie1(
                "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742661920/rebel_moon_movie_poster_b5xklt.jpg?_s=public-apps",
                "https://res.cloudinary.com/dorc5p2jg/video/upload/v1742910601/Snapchat-1490782232_cv4baz.mp4",
                "Rebel Moon",
                "Adventure, Survival",
                "2024",
                "7.4",
                "When a colony on the edge of the galaxy finds itself threatened by the armies of the tyrannical Regent Balisarius, they dispatch a young woman with a mysterious past to seek out warriors from neighbouring planets to help them take a stand."
        ));

        upcomingMovieAdapter = new upcoming_movie_adapter(this, upcomingMovies, (position, movie) -> {
            // Handle click event

            Intent intent = new Intent(this, Movies_Details_Activity.class);
            intent.putExtra("movieTitle", movie.getTitle());
            intent.putExtra("movieGenre", movie.getGenre());
            intent.putExtra("movieYear", movie.getYear());
            intent.putExtra("movieRating", movie.getRating());
            intent.putExtra("movieImage", movie.getImageUrl());
            Log.d("mila gaya url..............",movie.getmovieurl());
            intent.putExtra("movielink", movie.getmovieurl());

            intent.putExtra("summary", movie.getSummary());
            startActivity(intent);
        });

        recyclerViewUpcomingMovies.setAdapter(upcomingMovieAdapter);
        progressBar3.setVisibility(View.GONE);

        // Recommendations RecyclerView
        recyclerViewRecommendations = findViewById(R.id.recyclerViewrecomendations);
        recyclerViewRecommendations.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        recommendationList = new ArrayList<>();
        recommendationList.add(new RecommendationModel(
                "Emakku Thozhil Romance",
                "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742670398/images_qiquuz.jpg?_s=public-apps",
                "https://res.cloudinary.com/dorc5p2jg/video/upload/v1742910601/Snapchat-1490782232_cv4baz.mp4",
                "Romance", "2024 |", "⭐8.4", "A heartwarming love story that transcends challenges."
        ));

        recommendationList.add(new RecommendationModel(
                "The Witch Revenge",
                "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742670416/images_dnn8vx.jpg?_s=public-apps",
                "https://res.cloudinary.com/dorc5p2jg/video/upload/v1742910601/Snapchat-1490782232_cv4baz.mp4",
                "Horror", "2023 |", "⭐8.7", "A gripping tale of dark magic and vengeance."
        ));

        recommendationList.add(new RecommendationModel(
                "Ouija Castle",
                "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742670383/images_gogtnz.jpg?_s=public-apps",
                "https://res.cloudinary.com/dorc5p2jg/video/upload/v1742910601/Snapchat-1490782232_cv4baz.mp4",
                "Horror", "2022 |", "⭐7.9", "An eerie castle hides terrifying secrets."
        ));

        recommendationList.add(new RecommendationModel(
                "Nosferatu",
                "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742670347/images_tb7amm.jpg?_s=public-apps",
                "https://res.cloudinary.com/dorc5p2jg/video/upload/v1742910601/Snapchat-1490782232_cv4baz.mp4",
                "Horror", "1922 |", "⭐8.2", "A silent film classic that redefined vampires."
        ));

        recommendationList.add(new RecommendationModel(
                "Pushpa 2",
                "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742670318/hekl4hx4hjxpz54zdpig.jpg?_s=public-apps",
                "https://res.cloudinary.com/dorc5p2jg/video/upload/v1742910601/Snapchat-1490782232_cv4baz.mp4",
                "Action", "2024 |", "⭐6.3", "The thrilling sequel to the blockbuster action film."
        ));

        recommendationList.add(new RecommendationModel(
                "Dhoom Dhaam",
                "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742670303/tzab5335mxoygalse4ck.jpg?_s=public-apps",
                "https://res.cloudinary.com/dorc5p2jg/video/upload/v1742910601/Snapchat-1490782232_cv4baz.mp4",
                "Comedy", "2023 |", "⭐4.5", "A lighthearted entertainer filled with laughter."
        ));

        recommendationList.add(new RecommendationModel(
                "Baby John",
                "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742670231/tbeiiyfnqdslip1p8sab.jpg?_s=public-apps",
                "https://res.cloudinary.com/dorc5p2jg/video/upload/v1742910601/Snapchat-1490782232_cv4baz.mp4",
                "Drama", "2024 |", "⭐9.2", "An emotional journey of self-discovery."
        ));

        recommendationList.add(new RecommendationModel(
                "Marked Men: Rule + Shaw",
                "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742674634/evzvh4d5svntdyjdglak.jpg?_s=public-apps",
                "https://res.cloudinary.com/dorc5p2jg/video/upload/v1742910601/Snapchat-1490782232_cv4baz.mp4",
                "Thriller", "|2024 |", "⭐9.2", "A high-stakes thriller with intense action."
        ));

        List<RecommendationModel> infiniteList = new ArrayList<>(recommendationList);
        infiniteList.addAll(recommendationList);

        recommendationAdapter = new RecommendationAdapter(this, infiniteList, (position, movie) -> {
            // Handle click event
            Intent intent = new Intent(this, Movies_Details_Activity.class);
            intent.putExtra("movieTitle", movie.getTitle());
            intent.putExtra("movieGenre", movie.getGenre());
            intent.putExtra("movieYear", movie.getYear());
            intent.putExtra("movieRating", movie.getRating());
            intent.putExtra("movieImage", movie.getPosterUrl());
            intent.putExtra("movielink", movie.getmovieurl());
            intent.putExtra("summary", movie.getSummary());
            startActivity(intent);
        });
        recyclerViewRecommendations.setAdapter(recommendationAdapter);
        recyclerViewRecommendations.scrollToPosition(recommendationList.size());
        progressBar4.setVisibility(View.GONE);
        //onclick on bottom bottons



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    private void updateNavSelection(TextView selectedTextView) {
        textExplore.setVisibility(View.GONE);
        textLike.setVisibility(View.GONE);
        textCart.setVisibility(View.GONE);
        textProfile.setVisibility(View.GONE);
        selectedTextView.setVisibility(View.VISIBLE);
    }
}
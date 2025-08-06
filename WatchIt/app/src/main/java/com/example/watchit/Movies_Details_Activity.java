package com.example.watchit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class Movies_Details_Activity extends AppCompatActivity {

    private TextView movieTitle, movieGenre, movieYear, movieRating, movieSummary;
    private ImageView movieImage;
    private Button watchButton; // Watch Button
    private Model_movie movie;

    private RecyclerView recyclerViewRecommendations, recyclerViewCast;
    private RecommendationAdapter recommendationAdapter;
    private List<RecommendationModel> recommendationList;

    private cast_Adapter castAdapter;
    private List<Cast> castList;

    private ProgressBar progressBar4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_movies_details);

        // Initialize UI elements
        movieTitle = findViewById(R.id.tvMovieTitle);
        movieGenre = findViewById(R.id.gener1);
        movieYear = findViewById(R.id.tvMovieYear);
        movieRating = findViewById(R.id.tvMovieRating);
        movieSummary = findViewById(R.id.tvMovieSummary);
        movieImage = findViewById(R.id.movie_details_banner_img1);
        watchButton = findViewById(R.id.btnWatchNow); // Initialize Watch Button

        recyclerViewCast = findViewById(R.id.rvCastList);
        recyclerViewRecommendations = findViewById(R.id.recyclerViewrecomendations);
        progressBar4 = findViewById(R.id.progressBarrecomendations);

        // Retrieve movie data from Intent
        Intent intent = getIntent();
        if (intent != null) {
            String imageRes = intent.getStringExtra("movieImage");
            String imageurl = intent.getStringExtra("movielink");
            Log.d("intent me mila url",imageurl);
            String title = intent.getStringExtra("movieTitle");
            String genre = intent.getStringExtra("movieGenre");
            String year = intent.getStringExtra("movieYear");
            String rating = intent.getStringExtra("movieRating");
            String summary = intent.getStringExtra("summary");

            movie = new Model_movie(imageRes, imageurl, title, genre, year, rating, summary);

            setMovieDetails();
        }

        // Set OnClickListener for Watch Button
        watchButton.setOnClickListener(v -> {
            Intent Intent = new Intent(Movies_Details_Activity.this, media_player.class);
            Intent.putExtra("videoUrl", movie.getmovieurl());
            // Sending URL to MediaPlayerActivity
            startActivity(Intent);
        });

        setupCastRecyclerView();
        setupRecommendationRecyclerView();
    }

    private void setMovieDetails() {
        movieTitle.setText(movie.getTitle());
        movieGenre.setText(movie.getGenre());
        movieYear.setText(movie.getYear());
        movieRating.setText(movie.getRating());
        movieSummary.setText(movie.getSummary());

        // Load image using Glide
        Glide.with(this)
                .load(movie.getImageUrl())
                .placeholder(R.drawable.search)
                .error(R.drawable.exclamation_mark)
                .into(movieImage);
    }

    private void setupCastRecyclerView() {
        castList = new ArrayList<>();

        // Dummy cast data
        castList.add(new Cast("Sydney Sweeney", "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742661925/sydney_sweeney_xneyx7.jpg?_s=public-apps"));
        castList.add(new Cast("Robert Downey Jr.", "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742665112/iaozlnauip5g9fkm5wsr.jpg?_s=public-apps"));
        castList.add(new Cast("Will Attenborough", "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742661928/will_attenborough_m5mo2b.jpg?_s=public-apps"));
        castList.add(new Cast("Laura Handdock", "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742661918/laura_haddock_qfe8m4.jpg?_s=public-apps"));
        castList.add(new Cast("Vincent Cassel", "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742661926/vincent_cassel_ichp06.jpg?_s=public-apps"));
        castList.add(new Cast("Sophie Lpjarg", "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742661925/sophie_lpjarg.jpg?_s=public-apps"));
        castList.add(new Cast("Sydney Sweeney", "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742661925/sydney_sweeney_xneyx7.jpg?_s=public-apps"));
        castList.add(new Cast("Robert Downey Jr.", "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742665112/iaozlnauip5g9fkm5wsr.jpg?_s=public-apps"));

        castAdapter = new cast_Adapter(this, castList);
        recyclerViewCast.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCast.setAdapter(castAdapter);
    }

    private void setupRecommendationRecyclerView() {
        recommendationList = new ArrayList<>();

        // Dummy recommended movies
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
                "Thriller", "2024 |", "⭐9.2", "A high-stakes thriller with intense action."
        ));

        recommendationAdapter = new RecommendationAdapter(this, recommendationList, (position, movie) -> {
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

        recyclerViewRecommendations.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewRecommendations.setAdapter(recommendationAdapter);
        recyclerViewRecommendations.scrollToPosition(recommendationList.size());
        progressBar4.setVisibility(View.GONE);
    }
}

package com.example.watchit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Model_movie> movieList;
    private ViewPager2 viewPager;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, Model_movie movie);
    }

    public MovieAdapter(List<Model_movie> movieList, ViewPager2 viewPager, Context context, OnItemClickListener listener) {
        this.movieList = movieList;
        this.viewPager = viewPager;
        this.context = context;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (movieList == null || movieList.isEmpty()) return;

        // **Use modulo to create an infinite loop**
        int realPosition = position % movieList.size();


        Model_movie movie = movieList.get(realPosition);

        // **Load Image Using Glide**
        Glide.with(context)
                .load(movie.getImageUrl())  // Load image from URL
                .placeholder(R.drawable.search) // Placeholder while loading
                .error(R.drawable.exclamation_mark) // Error image if loading fails
                .into(holder.movieImage);

        // **Alternative: Load Image Using Picasso**
        // Picasso.get().load(movie.getImageUrl()).placeholder(R.drawable.placeholder).error(R.drawable.error_image).into(holder.movieImage);

        holder.movieTitle.setText(movie.getTitle() != null ? movie.getTitle() : "Unknown Title");
        holder.movieGenre.setText(movie.getGenre() != null ? movie.getGenre() : "Unknown Genre");
        holder.movieYear.setText(movie.getYear() != null ? movie.getYear() : "N/A");
        holder.movieRating.setText(movie.getRating() != null ? movie.getRating() : "N/A");

        holder.itemView.setVisibility(View.VISIBLE);

        // **Set Click Listener**
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(realPosition, movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (movieList == null || movieList.isEmpty()) ? 0 : Integer.MAX_VALUE; // Simulate infinite items
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView movieImage;
        TextView movieTitle, movieGenre, movieYear, movieRating;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.movieImage);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            movieGenre = itemView.findViewById(R.id.movieGenre);
            movieYear = itemView.findViewById(R.id.movieYear);
            movieRating = itemView.findViewById(R.id.movieRating);
        }
    }
}

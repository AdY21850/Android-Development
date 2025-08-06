package com.example.watchit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class upcoming_movie_adapter extends RecyclerView.Adapter<upcoming_movie_adapter.MovieViewHolder> {
    private List<Model_movie1> movieList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    // Interface for handling click events
    public interface OnItemClickListener {
        void onItemClick(int position, Model_movie1 movie);
    }

    // Constructor
    public upcoming_movie_adapter(Context context, List<Model_movie1> movieList, OnItemClickListener listener) {
        this.context = context;
        this.movieList = movieList;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_top_movies_dashboard, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (movieList == null || movieList.isEmpty()) return;

        // Use modulo to simulate infinite scrolling
        int realPosition = position % movieList.size();
        Model_movie1 movie = movieList.get(realPosition);

        // Load Image Using Glide
        Glide.with(context)
                .load(movie.getImageUrl())  // Load image from URL
                .placeholder(R.drawable.search) // Placeholder while loading
                .error(R.drawable.exclamation_mark) // Error image if URL fails
                .into(holder.imageView);

        // **Alternative: Use Picasso (Uncomment to use)**
        // Picasso.get().load(movie.getImageUrl()).placeholder(R.drawable.placeholder).error(R.drawable.error_image).into(holder.imageView);

        holder.movieName.setText(movie.getTitle());

        // Handle Click Events
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(realPosition, movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE; // Simulating infinite scroll
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView movieName;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.movieImage1);
            movieName = itemView.findViewById(R.id.movieName);
        }
    }
}

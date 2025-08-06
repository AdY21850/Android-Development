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
import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

    public class cast_Adapter extends RecyclerView.Adapter<cast_Adapter.CastViewHolder> {

        private Context context;
        private List<Cast> castList;

        public cast_Adapter(Context context, List<Cast> castList) {
            this.context = context;
            this.castList = castList;
        }

        @NonNull
        @Override
        public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.cast_recycler, parent, false);
            return new CastViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {
            Cast cast = castList.get(position);
            holder.castName.setText(cast.getName());

            // Load image using Glide (make sure you add Glide dependency)

            Picasso.get()
                    .load(cast.getImageUrl())
                    .placeholder(R.drawable.exclamation_mark)  // While loading
                    .error(R.drawable.exclamation_mark)      // If error
                    .fit() // Ensure it scales correctly
                    .into(holder.castImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("Picasso", "Image loaded successfully: " + cast.getImageUrl());
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("Picasso", "Image failed to load: " + cast.getImageUrl(), e);
                        }
                    });
        }

        @Override
        public int getItemCount() {
            return castList.size();
        }

        public static class CastViewHolder extends RecyclerView.ViewHolder {
            ImageView castImage;
            TextView castName;

            public CastViewHolder(@NonNull View itemView) {
                super(itemView);
                castImage = itemView.findViewById(R.id.cast_image);
                castName = itemView.findViewById(R.id.cast_name);
            }
        }
    }



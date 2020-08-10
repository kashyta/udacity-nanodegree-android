package com.test.mymovieapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.test.mymovieapp.BuildConfig;
import com.test.mymovieapp.R;
import com.test.mymovieapp.viewHolder.TrailerViewHolder;
import com.test.mymovieapp.model.Trailer;


import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerViewHolder> {

    public static String YOUTUBE_VIDEO = BuildConfig.YOUTUBE_BASE_VIDEO_URL;
    public static String YOUTUBE_IMAGE = BuildConfig.YOUTUBE_BASE_IMAGE_URL;
    public static String YOUTUBE_IMAGE_EXTENSION = BuildConfig.YOUTUBE_IMAGE_EXTENSION;

    public static Context context;
    public static List<Trailer> trailers;

    public TrailerAdapter(Context context, List<Trailer> trailers) {
        this.context = context;
        this.trailers = trailers;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrailerViewHolder(LayoutInflater.from(context).inflate(R.layout.trailers_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        Trailer trailer = trailers.get(position);
        if (trailer.getSite().equalsIgnoreCase("youtube")) {
            Uri uri = Uri.parse(YOUTUBE_IMAGE + trailer.getKey() + YOUTUBE_IMAGE_EXTENSION);
            holder.trailerName.setText(trailer.getTitle());
            Picasso.Builder builder = new Picasso.Builder(context);
            builder.build()
                    .load(uri)
                    .placeholder((R.drawable.youtube))
                    .into(holder.trailerView);
        }
    }

    @Override
    public int getItemCount() {
        return trailers != null ? trailers.size() : 0;
    }



}

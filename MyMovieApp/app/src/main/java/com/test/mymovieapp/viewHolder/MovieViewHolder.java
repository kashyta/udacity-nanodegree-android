package com.test.mymovieapp.viewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.test.mymovieapp.BuildConfig;
import com.test.mymovieapp.R;
import com.test.mymovieapp.RecyclerViewClickListener;
import com.test.mymovieapp.model.Movie;


import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public static String IMAGEPATH = BuildConfig.IMAGE_BASE_URL;
    private Context movieContext;
    public RecyclerViewClickListener rvclickListener;

    @BindView(R.id.iv_movie_thumbnail)
    ImageView moviePoster;

    public MovieViewHolder(View itemView, RecyclerViewClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        movieContext = itemView.getContext();
        itemView.setOnClickListener(this);
        this.rvclickListener = listener;
    }

    public void bindMovie(Movie movie) {
        Picasso.Builder builder = new Picasso.Builder(movieContext);
        builder.downloader(new OkHttp3Downloader(movieContext));
        builder.build().load(IMAGEPATH + movie.getPosterPath())
                .placeholder((R.drawable.movie_poster_backdrop))
                .error(R.drawable.ic_launcher_background)
                .into(moviePoster);
    }

    @Override
    public void onClick(View v) {
        rvclickListener.onClick(v, getAdapterPosition());
    }
}
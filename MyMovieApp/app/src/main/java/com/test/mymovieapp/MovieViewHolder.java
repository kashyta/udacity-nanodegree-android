package com.test.mymovieapp;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.test.mymovieapp.MainActivity.movieImagePathBuilder;


public class MovieViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.cv_movie_list) CardView cv_MovieList;
    @BindView(R.id.iv_movie_thumbnail) ImageView iv_MoviePoster;


    public MovieViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(final Movie movie, final MovieClickListener movieClickListener) {
        cv_MovieList.setLayoutParams(new ViewGroup.LayoutParams(560,440));

        Picasso.get().load(movieImagePathBuilder(movie.getPosterPath())).placeholder(R.drawable.movie_poster_backdrop).fit().into(iv_MoviePoster);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                movieClickListener.onMovieClick(movie);
            }
        });
    }
}

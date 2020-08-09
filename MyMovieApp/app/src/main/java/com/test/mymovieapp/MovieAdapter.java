package com.test.mymovieapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

private final MovieClickListener movieClickListener;
private final List<Movie> movieList;

public MovieAdapter(List<Movie> movieList, MovieClickListener movieClickListener) {
        this.movieList = movieList;
        this.movieClickListener = movieClickListener;
        }

@Override
public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_content, parent, false);
        return new MovieViewHolder(view);
        }

@Override
public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = this.movieList.get(position);
        holder.bind(movie, movieClickListener);
        }

@Override
public int getItemCount() {
        return this.movieList.size();
        }

@Override
public void onViewRecycled(MovieViewHolder holder) {
        super.onViewRecycled(holder);
        }
}

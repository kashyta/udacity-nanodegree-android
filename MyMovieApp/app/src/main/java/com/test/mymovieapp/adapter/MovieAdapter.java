package com.test.mymovieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.test.mymovieapp.MovieDetailsActivity;
import com.test.mymovieapp.viewHolder.MovieViewHolder;
import com.test.mymovieapp.R;
import com.test.mymovieapp.RecyclerViewClickListener;
import com.test.mymovieapp.model.Movie;

import java.util.List;

import static com.test.mymovieapp.MainActivity.SELECTED_MOVIE;


public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> implements RecyclerViewClickListener {

    private List<Movie> movieList;
    private Context context;
    private RecyclerViewClickListener rvClickListener;

    public MovieAdapter(Context context, List<Movie> dataList, RecyclerView recyclerView) {
        this.context = context;
        this.movieList = dataList;
        this.rvClickListener = this;

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_content, parent, false);
        return new MovieViewHolder(view, rvClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bindMovie(movieList.get(position));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void setData(List<Movie> movies) {
        this.movieList = movies;
        notifyDataSetChanged();
    }

    public void clear() {
        this.movieList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Movie> movies) {
        this.movieList.addAll(movies);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view, int position) {
        Movie movie = movieList.get(position);
        Intent intent = new Intent(context, MovieDetailsActivity.class);
        intent.putExtra(SELECTED_MOVIE, movie);
        context.startActivity(intent);
    }
}

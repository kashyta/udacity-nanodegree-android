package com.test.mymovieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


import static com.test.mymovieapp.MainActivity.movieImagePathBuilder;

public class MovieDetailsActivity extends AppCompatActivity {

    @BindView(R.id.movie_details_poster)
    ImageView detailsMoviePoster;
    @BindView(R.id.movie_details_title)
    TextView detailsMovieTitle;
    @BindView(R.id.movie_details_release_date)
    TextView detailsMovieReleaseDate;
    @BindView(R.id.movie_details_rating)
    TextView detailsMovieRating;
    @BindView(R.id.movie_details_overview)
    TextView detailsMovieOverView;

    private Movie movieDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            movieDetails = (Movie) bundle.getSerializable("movie");
            assert movieDetails != null;
            populateActivity(movieDetails);
        }
        else {
            movieDetails = (Movie) savedInstanceState.getSerializable("movie");
            populateActivity(movieDetails);
        }
    }

    private void populateActivity(Movie movieDetails){
        Picasso.get().load(movieImagePathBuilder(movieDetails.getPosterPath())).into(detailsMoviePoster);
        detailsMovieTitle.setText("Movie Title: "+ movieDetails.getTitle());
        detailsMovieOverView.setText(movieDetails.getOverview());
        detailsMovieReleaseDate.setText("Release Date: " + movieDetails.getReleaseDate());
        String voteAverageText = String.valueOf(movieDetails.getVoteAverage()) + "/10";
        detailsMovieRating.setText("Vote Average: " + voteAverageText);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("movie", movieDetails);
    }

}
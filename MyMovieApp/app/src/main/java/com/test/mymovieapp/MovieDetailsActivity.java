package com.test.mymovieapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.test.mymovieapp.adapter.ReviewsAdapter;
import com.test.mymovieapp.adapter.TrailerAdapter;
import com.test.mymovieapp.database.MovieDatabase;
import com.test.mymovieapp.model.Movie;
import com.test.mymovieapp.model.Reviews_Results;
import com.test.mymovieapp.model.Reviews;
import com.test.mymovieapp.model.Trailer;
import com.test.mymovieapp.model.Trailer_Response;
import com.test.mymovieapp.viewModel.MovieDetailsActivityViewModel;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


import static com.test.mymovieapp.MainActivity.API_KEY;
import static com.test.mymovieapp.MainActivity.SELECTED_MOVIE;
import static com.test.mymovieapp.viewHolder.MovieViewHolder.IMAGEPATH;


public class MovieDetailsActivity extends AppCompatActivity {

    private static Retrofit retrofit;
    public List<Trailer> trailers;
    public List<Reviews> reviews;
    private boolean isFavorite;
    private int movieId;
    private Movie movie;

    @BindView(R.id.iv_details_moviePoster)
    ImageView moviePoster;

    @BindView(R.id.tv_details_MovieTitle)
    TextView movieTitle;

    @BindView(R.id.tv_details_plot)
    TextView moviePlot;

    @BindView(R.id.tv_details_releaseDate)
    TextView movieReleaseDate;

    @BindView(R.id.tv_details_voteAverage)
    TextView movieVoteAverage;

    @BindView(R.id.rv_trailer)
    public RecyclerView rvTrailer;

    @BindView(R.id.rv_reviews)
    public RecyclerView rvReviews;

    @BindView(R.id.tv_trailers_not_available)
    TextView trailersNotAvailable;

    @BindView(R.id.tv_reviews_not_available)
    TextView reviewsNotAvailable;

    @BindView(R.id.favorite_button)
    MaterialFavoriteButton favoriteButton;

    private MovieDetailsActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        ButterKnife.bind(this);
        MovieDataService movieService = Client.getRetrofitInstance().create(MovieDataService.class);
        viewModel = new ViewModelProvider(this).get(MovieDetailsActivityViewModel.class);

        favoriteButton.setOnClickListener(v -> onFavButtonClicked());

        if (getIntent() != null) {
            if (getIntent().hasExtra(SELECTED_MOVIE)) {
                movie = getIntent().getParcelableExtra(SELECTED_MOVIE);
                movieId = movie.getMovieId();
                AppExecutors.getExecutorInstance().getDiskIO().execute(() -> {
                    isFavorite = viewModel.isFavorite(movieId);
                    if (isFavorite) {
                        movie = MovieDatabase.getInstance(this).movieDao().getMovie(movieId);
                        runOnUiThread(() -> favoriteButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.star_blue)));
                    } else {
                        runOnUiThread(() -> favoriteButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.star_grey)));
                    }
                });
            }
        }

        getSelectedMovieDetails(movieService);

    }

    private void getSelectedMovieDetails(MovieDataService client) {
        if (movieId != 0) {
            Call<Movie> detailResultsCall = client.getMovieDetails(movieId, API_KEY);
            detailResultsCall.enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                    if (response.body() == null) {
                        return;
                    }

                    movieTitle.setText(response.body().getTitle());
                    if (response.body().getOverview() != null && !response.body().getOverview().isEmpty()) {
                        moviePlot.setText(response.body().getOverview());
                    } else {
                        moviePlot.setText(getResources().getString(R.string.plot_synopsis_not_available));
                    }
                    movieReleaseDate.setText(response.body().getReleaseDate());
                    String voteAverageText = String.valueOf(movie.getVoteAverage()) + "/10";
                    movieVoteAverage.setText(voteAverageText);

                    Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
                    builder.downloader(new OkHttp3Downloader(getApplicationContext()));
                    builder.build().load(IMAGEPATH + response.body().getBackdropPath())
                            .placeholder((R.drawable.movie_poster_backdrop))
                            .error(R.drawable.ic_launcher_background)
                            .into(moviePoster);
                }

                @Override
                public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                    if (movie != null) {
                        System.out.print("Movie already present");
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_Fetching_Data), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            getMovieTrailers(movieId);
            getMovieReviews(movieId);
        }
    }

    private void onFavButtonClicked() {
        AppExecutors.getExecutorInstance().getDiskIO().execute(() -> {
            boolean isFavorite = viewModel.isFavorite(movieId);
            if (isFavorite) {
                viewModel.removeMovieFromFavorites(movie);
                runOnUiThread(() -> {
                    favoriteButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.star_grey));
                    Toast.makeText(this, getResources().getString(R.string.Favorite_Removed), Toast.LENGTH_SHORT).show();
                });
            } else {
                viewModel.addMovieToFavorites(movie);
                runOnUiThread(() -> {
                    Toast.makeText(this, getResources().getString(R.string.Favorite_Added), Toast.LENGTH_SHORT).show();
                    favoriteButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.star_blue));
                });
            }
            viewModel.updateFavoriteMovie(movieId, !isFavorite);
            finish();
        });
    }

    private void getMovieReviews(Integer id) {
        if (MovieUtils.getInstance().isNetworkAvailable(this)) {
            if (retrofit == null) {
                retrofit = Client.getRetrofitInstance();
            }
            MovieDataService movieService = retrofit.create(MovieDataService.class);
            Call<Reviews_Results> call = movieService.getMovieReviews(id, API_KEY, 1);
            call.enqueue(new Callback<Reviews_Results>() {
                @Override
                public void onResponse(@NonNull Call<Reviews_Results> call, @NonNull Response<Reviews_Results> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        reviews = response.body().getReviewList();
                        if (reviews != null && !reviews.isEmpty()) {
                            rvReviews.setVisibility(View.VISIBLE);
                            reviewsNotAvailable.setVisibility(View.GONE);
                            generateReviewList(reviews);
                        } else {
                            rvReviews.setVisibility(View.GONE);
                            reviewsNotAvailable.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Reviews_Results> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, getResources().getString(R.string.Error_Fetching_Data), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(MovieDetailsActivity.this, getResources().getString(R.string.Network_Status_Not_Available), Toast.LENGTH_SHORT).show();
        }
    }

    private void getMovieTrailers(Integer id) {
        if (MovieUtils.getInstance().isNetworkAvailable(this)) {
            if (retrofit == null) {
                retrofit = Client.getRetrofitInstance();
            }
            MovieDataService movieService = retrofit.create(MovieDataService.class);
            Call<Trailer_Response> call = movieService.getMovieTrailers(id, API_KEY);
            call.enqueue(new Callback<Trailer_Response>() {
                @Override
                public void onResponse(@NonNull Call<Trailer_Response> call, @NonNull Response<Trailer_Response> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        trailers = response.body().getTrailers();
                        if (trailers != null && !trailers.isEmpty()) {
                            rvTrailer.setVisibility(View.VISIBLE);
                            trailersNotAvailable.setVisibility(View.GONE);
                            generateTrailerList(trailers);
                        } else {
                            rvTrailer.setVisibility(View.GONE);
                            trailersNotAvailable.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Trailer_Response> call, @NonNull Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, R.string.Error_Fetching_Data, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(MovieDetailsActivity.this, getResources().getString(R.string.Network_Status_Not_Available), Toast.LENGTH_SHORT).show();
        }
    }

    private void generateReviewList(final List<Reviews> reviews) {
        ReviewsAdapter adapter = new ReviewsAdapter(this, reviews);
        initReviewsAdapter(adapter);
    }

    private void generateTrailerList(final List<Trailer> trailers) {
        TrailerAdapter adapter = new TrailerAdapter(this, trailers);
        initTrailersAdapter(adapter);
    }

    private void initReviewsAdapter(ReviewsAdapter adapter) {
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setAdapter(adapter);
    }

    private void initTrailersAdapter(TrailerAdapter adapter) {
        rvTrailer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvTrailer.setAdapter(adapter);
    }
}

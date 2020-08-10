package com.test.mymovieapp;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.test.mymovieapp.adapter.MovieAdapter;
import com.test.mymovieapp.model.Movie;
import com.test.mymovieapp.model.Movie_Results;
import com.test.mymovieapp.viewModel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    public static String API_KEY = com.test.mymovieapp.BuildConfig.THE_MOVIE_DB_API_TOKEN;
    private int FIRST_PAGE = 1;
    public ArrayList<Movie> movieResults;

    private MainActivityViewModel viewModel;
    private MovieAdapter movieAdapter;
    private MovieDataService movieService;
    private static String actionBarTitle;
    private boolean mostPopularOptionSelected = true;
    private boolean topRatedOptionSelected = false;

    public static final String MOVIES_LIST = "MOVIES_LIST";
    public static final String RECYCLER_VIEW_LAYOUT_MANAGER_STATE = "RECYCLER_VIEW_LAYOUT_MANAGER_STATE";
    public static final String ACTIVITY_TITLE = "ACTIVITY_TITLE";
    public static final String SELECTED_MOVIE = "SELECTED_MOVIE";
    public static final String MOST_POPULAR_OPTION_SELECTED = "MOST_POPULAR_OPTION_SELECTED";
    public static final String TOP_RATED_OPTION_SELECTED = "TOP_RATED_OPTION_SELECTED";

    @BindView(R.id.rv_main_activity)
    public RecyclerView rvMain;

    @BindView(R.id.progressBar)
    public ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        movieResults = new ArrayList<>();

        movieAdapter = new MovieAdapter(this, movieResults, rvMain);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        if (!isNetworkAvailable()) {
            rvMain.setVisibility(View.GONE);
        }
        GridLayoutManager manager = new GridLayoutManager(this, 2);

        rvMain.setLayoutManager(manager);
        rvMain.setHasFixedSize(true);


        viewModel.getFavoriteMovies().observe(this, favorites -> {
            if (favorites != null && !mostPopularOptionSelected && !topRatedOptionSelected) {
                if (movieResults == null) {
                    movieResults = new ArrayList<>();
                } else {
                    movieAdapter.clear();
                }
                movieAdapter.addAll(favorites);

                if (movieResults.size() == 0) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), R.string.No_Movie_Added_Yet, Toast.LENGTH_SHORT).show();
                }
            }
        });
        movieService = Client.getRetrofitInstance().create(MovieDataService.class);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MOVIES_LIST)) {
                progressBar.setVisibility(View.GONE);
                setTitle(savedInstanceState.getString(ACTIVITY_TITLE));
                movieResults = savedInstanceState.getParcelableArrayList(MOVIES_LIST);
                movieAdapter.clear();
                movieAdapter.setData(movieResults);
            }

            if (savedInstanceState.containsKey(MOST_POPULAR_OPTION_SELECTED)) {
                mostPopularOptionSelected = savedInstanceState.getBoolean(MOST_POPULAR_OPTION_SELECTED);
            }
            if (savedInstanceState.containsKey(TOP_RATED_OPTION_SELECTED)) {
                topRatedOptionSelected = savedInstanceState.getBoolean(TOP_RATED_OPTION_SELECTED);
            }
        }

        if (savedInstanceState == null) {
            if (mostPopularOptionSelected) {
                progressBar.setVisibility(View.VISIBLE);
                getPopularMovies();
            } else if (topRatedOptionSelected) {
                progressBar.setVisibility(View.VISIBLE);
                getTopRatedMovies();
            }
        }
    }


    private void getPopularMovies() {
        if (MovieUtils.getInstance().isNetworkAvailable(this)) {

            Call<Movie_Results> call = movieService.getPopularMovies(API_KEY, FIRST_PAGE);
            call.enqueue(new Callback<Movie_Results>() {
                @Override
                public void onResponse(@NonNull Call<Movie_Results> call, @NonNull Response<Movie_Results> response) {
                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (response.body().getMovieResult() != null) {
                            movieResults = response.body().getMovieResult();
                            movieAdapter.addAll(response.body().getMovieResult());
                        }
                        rvMain.setAdapter(movieAdapter);
                        movieAdapter.notifyDataSetChanged();
                    } else {
                        assert response.body() != null;
                        List<Movie> movies = response.body().getMovieResult();
                        for (Movie movie : movies) {
                            movieResults.add(movie);
                            movieAdapter.notifyItemInserted(movieResults.size() - 1);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Movie_Results> call, Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.Error_Fetching_Data), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(MainActivity.this, getResources().getString(R.string.Network_Status_Not_Available), Toast.LENGTH_SHORT).show();
        }
    }

    private void getTopRatedMovies() {
        if (MovieUtils.getInstance().isNetworkAvailable(this)) {

            Call<Movie_Results> call = movieService.getTopRatedMovies(API_KEY, FIRST_PAGE);
            call.enqueue(new Callback<Movie_Results>() {
                @Override
                public void onResponse(Call<Movie_Results> call, Response<Movie_Results> response) {
                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (response.body().getMovieResult() != null) {
                            movieResults = response.body().getMovieResult();
                            movieAdapter.addAll(response.body().getMovieResult());
                        }
                        rvMain.setAdapter(movieAdapter);
                        movieAdapter.notifyDataSetChanged();
                    } else {
                        assert response.body() != null;
                        List<Movie> movies = response.body().getMovieResult();
                        for (Movie movie : movies) {
                            movieResults.add(movie);
                            movieAdapter.notifyItemInserted(movieResults.size() - 1);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Movie_Results> call, Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.Error_Fetching_Data), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.topRated:
                progressBar.setVisibility(View.VISIBLE);
                mostPopularOptionSelected = false;
                topRatedOptionSelected = true;
                movieAdapter.clear();
                getTopRatedMovies();
                setTitle(R.string.toprated_movies);
                actionBarTitle = getResources().getString(R.string.toprated_movies);
                break;
            case R.id.popular:
                mostPopularOptionSelected = true;
                topRatedOptionSelected = false;
                progressBar.setVisibility(View.VISIBLE);
                movieAdapter.clear();
                getPopularMovies();
                setTitle(R.string.popular_movies);
                actionBarTitle = getResources().getString(R.string.popular_movies);
                break;

            case R.id.favorite:
                mostPopularOptionSelected = false;
                topRatedOptionSelected = false;
                progressBar.setVisibility(View.VISIBLE);
                movieAdapter.clear();
                setTitle(R.string.favorite_movies);
                actionBarTitle = getResources().getString(R.string.favorite_movies);
                List<Movie> favoriteMovies = viewModel.getFavoriteMovies().getValue();
                if (favoriteMovies != null && favoriteMovies.size() > 0) {
                    progressBar.setVisibility(View.INVISIBLE);
                    movieAdapter.addAll(favoriteMovies);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), R.string.No_Movie_Added_Yet, Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvMain.setLayoutManager(new GridLayoutManager(this, 4));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            rvMain.setLayoutManager(new GridLayoutManager(this, 2));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(MOST_POPULAR_OPTION_SELECTED, mostPopularOptionSelected);
        outState.putBoolean(TOP_RATED_OPTION_SELECTED, topRatedOptionSelected);
        outState.putParcelableArrayList(MOVIES_LIST, movieResults);
        outState.putString(ACTIVITY_TITLE, actionBarTitle);
        outState.putParcelable(RECYCLER_VIEW_LAYOUT_MANAGER_STATE, rvMain.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}
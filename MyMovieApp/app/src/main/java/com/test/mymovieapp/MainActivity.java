package com.test.mymovieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String API_KEY = com.test.mymovieapp.BuildConfig.THE_MOVIE_DB_API_TOKEN;
    private static final int FIRST_PAGE = 1;

    private int totalPages;
    private int currentSortMode = 1;

    private Call<API_Results> call;
    private List<Movie> movieResults;
    private MovieAdapter movieAdapter;

    private SwipeRefreshLayout pullToRefresh;

    @BindView(R.id.rv_main_activity)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
                pullToRefresh.setRefreshing(false);
            }
        });
        if(!isNetworkAvailable()){
            recyclerView.setVisibility(View.GONE);
        }

        GridLayoutManager manager = new GridLayoutManager(this,2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if ((page + 1) <= totalPages && currentSortMode != 3) {
                    loadPage(page + 1);
                }
            }
        };

        recyclerView.addOnScrollListener(scrollListener);
        loadPage(FIRST_PAGE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //SortID 1 -> Popularity
        //SortID 2 -> Top rated
        switch (item.getItemId()) {
            case R.id.sort_by_popularity:
                currentSortMode = 1;
                break;
            case R.id.sort_by_topRated:
                currentSortMode = 2;
                break;
            case R.id.sort_by_my_movieList:
                currentSortMode = 3;
                break;
        }
        if (currentSortMode != 3) {
            loadPage(FIRST_PAGE);
        } else {
            ArrayList<Movie> listMovies = getMovies();
            movieAdapter = new MovieAdapter(listMovies, new MovieClickListener() {
                @Override
                public void onMovieClick(Movie movie) {
                    Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("movie", movie);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(movieAdapter);
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadPage(final int page) {
        MovieDataService movieDataService = Client.getRetrofitInstance().create(MovieDataService.class);

        switch (currentSortMode) {
            case 1:
                call = movieDataService.getPopularMovies(page, API_KEY);
                break;
            case 2:
                call = movieDataService.getTopRatedMovies(page, API_KEY);
                break;
        }
        call.enqueue(new Callback<API_Results>() {
            @Override
            public void onResponse(@NonNull Call<API_Results> call, @NonNull Response<API_Results> response) {

                if (page == 1) {
                    assert response.body() != null;
                    movieResults = response.body().getMovieResult();
                    assert response.body() != null;
                    totalPages = response.body().getTotalPages();

                    movieAdapter = new MovieAdapter(movieResults, new MovieClickListener() {
                        @Override
                        public void onMovieClick(Movie movie) {
                            Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("movie", movie);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(movieAdapter);
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
            public void onFailure(Call<API_Results> call, Throwable t) {
                Toast.makeText(MainActivity.this, R.string.error_fetching_data, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static String movieImagePathBuilder(String imagePath) {
        return "https://image.tmdb.org/t/p/" +
                "w500" +
                imagePath;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
    }

    private ArrayList<Movie> getMovies(){
        ArrayList<Movie> movieList = new ArrayList<>();
        Cursor cursor = getContentResolver()
                .query(MovieContent.MovieEntry.CONTENT_URI,null,null,null,null);

        if(cursor != null && cursor.moveToFirst()){
            do{
                Movie movie = new Movie();

                int id = cursor.getInt(cursor.getColumnIndex("movie_id"));
                String movieTitle = cursor.getString(cursor.getColumnIndex("movie_title"));
                String movieOverview = cursor.getString(cursor.getColumnIndex("movie_overview"));
                double movieVoteAverage = cursor.getDouble(cursor.getColumnIndex("movie_vote_average"));
                String movieReleaseDate = cursor.getString(cursor.getColumnIndex("movie_release_date"));
                String moviePosterPath = cursor.getString(cursor.getColumnIndex("movie_poster_path"));

                movie.setId(id);
                movie.setTitle(movieTitle);
                movie.setOverview(movieOverview);
                movie.setVoteAverage(movieVoteAverage);
                movie.setReleaseDate(movieReleaseDate);
                movie.setPosterPath(moviePosterPath);

                movieList.add(movie);

            }while(cursor.moveToNext());
        }
        cursor.close();

        return movieList;
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
    public void refreshData(){
        finish();
        startActivity(getIntent());
    }
}
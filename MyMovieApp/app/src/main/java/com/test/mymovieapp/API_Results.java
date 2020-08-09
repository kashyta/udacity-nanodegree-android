package com.test.mymovieapp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class API_Results implements Serializable {

    @SerializedName("page")
    private int page;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("results")
    private ArrayList<Movie> movieResult;

    public API_Results(int page, int totalResults, int totalPages, ArrayList<Movie> movieResult) {
        this.page = page;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
        this.movieResult = movieResult;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public ArrayList<Movie> getMovieResult() {
        return movieResult;
    }

    public void setMovieResult(ArrayList<Movie> movieResult) {
        this.movieResult = movieResult;
    }
}

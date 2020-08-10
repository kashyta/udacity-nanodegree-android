package com.test.mymovieapp.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class Movie_Results implements Parcelable {
    @SerializedName("page")
    private int page;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("results")
    private ArrayList<Movie> movieResult;

    public Movie_Results(int page, int totalResults, int totalPages, ArrayList<Movie> movieResult) {
        this.page = page;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
        this.movieResult = movieResult;
    }

    protected Movie_Results(Parcel in) {
        page = in.readInt();
        totalResults = in.readInt();
        totalPages = in.readInt();
        movieResult = in.createTypedArrayList(Movie.CREATOR);
    }

    public static final Creator<Movie_Results> CREATOR = new Creator<Movie_Results>() {
        @Override
        public Movie_Results createFromParcel(Parcel in) {
            return new Movie_Results(in);
        }

        @Override
        public Movie_Results[] newArray(int size) {
            return new Movie_Results[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(page);
        parcel.writeInt(totalResults);
        parcel.writeInt(totalPages);
        parcel.writeTypedList(movieResult);
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
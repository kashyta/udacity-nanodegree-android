package com.test.mymovieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Reviews_Results implements Parcelable {

    @SerializedName("results")
    private List<Reviews> reviewList;

    public Reviews_Results(List<Reviews> reviewList) {
        this.reviewList = reviewList;
    }

    protected Reviews_Results(Parcel in) {
        reviewList = in.createTypedArrayList(Reviews.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(reviewList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Reviews_Results> CREATOR = new Creator<Reviews_Results>() {
        @Override
        public Reviews_Results createFromParcel(Parcel in) {
            return new Reviews_Results(in);
        }

        @Override
        public Reviews_Results[] newArray(int size) {
            return new Reviews_Results[size];
        }
    };

    public List<Reviews> getReviewList() {
        return reviewList;
    }
}

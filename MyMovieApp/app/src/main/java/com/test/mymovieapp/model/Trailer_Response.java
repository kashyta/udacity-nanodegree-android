package com.test.mymovieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Trailer_Response implements Parcelable {

    @SerializedName("results")
    private List<Trailer> Trailers;


    public Trailer_Response(Parcel parcel) {
        parcel.readTypedList(Trailers, Trailer.CREATOR);
    }

    public List<Trailer> getTrailers() {
        return Trailers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(Trailers);
    }

    public static final Creator<Trailer_Response> CREATOR = new Creator<Trailer_Response>() {
        @Override
        public Trailer_Response createFromParcel(Parcel parcel) {
            return new Trailer_Response(parcel);
        }

        @Override
        public Trailer_Response[] newArray(int i) {
            return new Trailer_Response[i];
        }
    };
}

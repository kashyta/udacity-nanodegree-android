package com.test.mymovieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Reviews implements Parcelable {
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;

    private boolean expanded;

    public Reviews(String author, String content) {
        this.author = author;
        this.content = content;
    }

    protected Reviews(Parcel in) {
        author = in.readString();
        content = in.readString();
        expanded = in.readByte() != 0;
    }

    public static final Creator<Reviews> CREATOR = new Creator<Reviews>() {
        @Override
        public Reviews createFromParcel(Parcel in) {
            return new Reviews(in);
        }

        @Override
        public Reviews[] newArray(int size) {
            return new Reviews[size];
        }
    };

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(content);
        parcel.writeByte((byte) (expanded ? 1 : 0));
    }
}


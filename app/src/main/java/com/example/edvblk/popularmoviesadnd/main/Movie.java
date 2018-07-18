package com.example.edvblk.popularmoviesadnd.main;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {
    @SerializedName("poster_path")
    private final String posterPath;
    @SerializedName("release_date")
    private final String releaseDate;
    @SerializedName("vote_average")
    private final double averageVote;
    private final String overview;
    private final String title;

    Movie(String posterPath, String releaseDate, double averageVote, String plot, String title) {
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.averageVote = averageVote;
        this.overview = plot;
        this.title = title;
    }

    private Movie(Parcel in) {
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        title = in.readString();
        averageVote = in.readDouble();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getAverageVote() {
        return averageVote;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(posterPath);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeString(title);
        parcel.writeDouble(averageVote);
    }
}
package com.example.edvblk.popularmoviesadnd.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "movies")
public final class MovieEntity {
    @NonNull
    @PrimaryKey
    private final int id;
    @NonNull
    private final String title;
    @NonNull
    private final String posterPath;
    @NonNull
    private final String releaseDate;
    @NonNull
    private final double averageVote;
    @NonNull
    private final String overview;

    public MovieEntity(
            @NonNull int id,
            String title,
            String posterPath,
            String releaseDate,
            double averageVote,
            String overview
    ) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.averageVote = averageVote;
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getAverageVote() {
        return averageVote;
    }

    public String getOverview() {
        return overview;
    }

    @NonNull
    public int getId() {
        return id;
    }
}

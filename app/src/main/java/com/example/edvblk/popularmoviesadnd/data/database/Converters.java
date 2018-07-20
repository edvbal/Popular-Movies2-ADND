package com.example.edvblk.popularmoviesadnd.data.database;

import android.arch.persistence.room.TypeConverter;

import com.example.edvblk.popularmoviesadnd.main.Movie;

class Converters {
    @TypeConverter
    public Movie fromMovieEntityToMovie(MovieEntity movieEntity) {
        return new Movie(
                movieEntity.getPosterPath(),
                movieEntity.getReleaseDate(),
                movieEntity.getAverageVote(),
                movieEntity.getOverview(),
                movieEntity.getTitle()
        );
    }

//    @TypeConverter
//    public MovieEntity fromMovieToMovieEntity(Movie movie) {
//        return new MovieEntity(
//                movie.getTitle(),
//                movie.getPosterPath(),
//                movie.getReleaseDate(),
//                movie.getAverageVote(),
//                movie.getOverview()
//        );
//    }
}

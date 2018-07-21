package com.example.edvblk.popularmoviesadnd.data.database;

import com.example.edvblk.popularmoviesadnd.main.Movie;
import com.example.edvblk.popularmoviesadnd.utils.Mapper;

public class MovieEntityMapper implements Mapper<Movie, MovieEntity> {
    @Override
    public MovieEntity apply(Movie movie) {
        return new MovieEntity(
                movie.getId(),
                movie.getTitle(),
                movie.getPosterPath(),
                movie.getReleaseDate(),
                movie.getAverageVote(),
                movie.getOverview()
        );
    }
}
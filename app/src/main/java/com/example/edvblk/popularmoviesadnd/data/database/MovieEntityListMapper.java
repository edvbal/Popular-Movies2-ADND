package com.example.edvblk.popularmoviesadnd.data.database;

import com.example.edvblk.popularmoviesadnd.main.Movie;
import com.example.edvblk.popularmoviesadnd.utils.Lists;
import com.example.edvblk.popularmoviesadnd.utils.Mapper;

import java.util.List;

public class MovieEntityListMapper implements Mapper<List<MovieEntity>, List<Movie>> {
    @Override
    public List<Movie> apply(List<MovieEntity> movieEntities) {
        return Lists.map(movieEntities, movieEntity -> new Movie(
                movieEntity.getPosterPath(),
                movieEntity.getReleaseDate(),
                movieEntity.getAverageVote(),
                movieEntity.getOverview(),
                movieEntity.getTitle()
        ));
    }
}

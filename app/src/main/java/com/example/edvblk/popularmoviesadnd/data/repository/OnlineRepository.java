package com.example.edvblk.popularmoviesadnd.data.repository;

import com.example.edvblk.popularmoviesadnd.data.pojos.Movie;

import java.util.List;

import io.reactivex.Single;

public interface OnlineRepository {
    Single<List<Movie>> getPopularMovies();
    Single<List<Movie>> getHighestRatedMovies();
    Single<List<Movie>> getMovieVideos();
    Single<List<Movie>> getMovieReviews();
}

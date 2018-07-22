package com.example.edvblk.popularmoviesadnd.data.repository;

import com.example.edvblk.popularmoviesadnd.data.pojos.Movie;
import com.example.edvblk.popularmoviesadnd.data.pojos.MovieReview;
import com.example.edvblk.popularmoviesadnd.data.pojos.MovieTrailer;

import java.util.List;

import io.reactivex.Single;

public interface OnlineRepository {
    Single<List<Movie>> getPopularMovies();
    Single<List<Movie>> getHighestRatedMovies();
    Single<List<MovieTrailer>> getMovieTrailers(int id);
    Single<List<MovieReview>> getMovieReviews(int id);
}

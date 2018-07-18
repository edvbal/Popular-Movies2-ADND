package com.example.edvblk.popularmoviesadnd.utils.network;

import com.example.edvblk.popularmoviesadnd.BuildConfig;
import com.example.edvblk.popularmoviesadnd.main.Movie;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface MoviesService {
    @GET(BuildConfig.POPULAR_MOVIES)
    Single<MoviesResultResponse<List<Movie>>> getPopularMovies();

    @GET(BuildConfig.HIGHEST_RATED_MOVIES)
    Single<MoviesResultResponse<List<Movie>>> getHighestRatedMovies();
}
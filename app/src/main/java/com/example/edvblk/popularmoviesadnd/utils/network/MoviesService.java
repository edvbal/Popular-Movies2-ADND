package com.example.edvblk.popularmoviesadnd.utils.network;

import com.example.edvblk.popularmoviesadnd.BuildConfig;
import com.example.edvblk.popularmoviesadnd.data.pojos.Movie;
import com.example.edvblk.popularmoviesadnd.data.pojos.MovieReview;
import com.example.edvblk.popularmoviesadnd.data.pojos.MovieTrailer;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MoviesService {
    @GET(BuildConfig.POPULAR_MOVIES)
    Single<MoviesResultResponse<List<Movie>>> getPopularMovies();

    @GET(BuildConfig.HIGHEST_RATED_MOVIES)
    Single<MoviesResultResponse<List<Movie>>> getHighestRatedMovies();

    @GET("{id}/videos")
    Single<MoviesResultResponse<List<MovieTrailer>>> getMovieTrailers(@Path("id") int id);

    @GET("{id}/reviews")
    Single<MoviesResultResponse<List<MovieReview>>> getMovieReviews(@Path("id") int id);
}
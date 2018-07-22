package com.example.edvblk.popularmoviesadnd.data.repository;

import android.support.annotation.NonNull;

import com.example.edvblk.popularmoviesadnd.data.pojos.Movie;
import com.example.edvblk.popularmoviesadnd.data.pojos.MovieReview;
import com.example.edvblk.popularmoviesadnd.data.pojos.MovieTrailer;
import com.example.edvblk.popularmoviesadnd.utils.Mapper;
import com.example.edvblk.popularmoviesadnd.utils.network.MoviesResultResponse;
import com.example.edvblk.popularmoviesadnd.utils.network.MoviesService;

import java.util.List;

import io.reactivex.Single;

public class DefaultOnlineRepository implements OnlineRepository {
    private final MoviesService moviesService;

    public DefaultOnlineRepository(MoviesService moviesService) {
        this.moviesService = moviesService;
    }

    @Override
    public Single<List<Movie>> getPopularMovies() {
        return moviesService.getPopularMovies()
                .map(getMoviesMapper());
    }

    @Override
    public Single<List<Movie>> getHighestRatedMovies() {
        return moviesService.getHighestRatedMovies()
                .map(getMoviesMapper());
    }

    @Override
    public Single<List<MovieTrailer>> getMovieTrailers(int id) {
        return moviesService.getMovieTrailers(id)
                .map(getMovieTrailersMapper());
    }

    @Override
    public Single<List<MovieReview>> getMovieReviews(int id) {
        return moviesService.getMovieReviews(id)
                .map(getMovieReviewsMapper());
    }

    @NonNull
    private Mapper<MoviesResultResponse<List<MovieReview>>, List<MovieReview>> getMovieReviewsMapper() {
        return MoviesResultResponse::getResult;
    }

    @NonNull
    private Mapper<MoviesResultResponse<List<MovieTrailer>>, List<MovieTrailer>> getMovieTrailersMapper() {
        return MoviesResultResponse::getResult;
    }


    @NonNull
    private Mapper<MoviesResultResponse<List<Movie>>, List<Movie>> getMoviesMapper() {
        return MoviesResultResponse::getResult;
    }
}

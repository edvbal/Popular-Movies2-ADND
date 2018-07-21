package com.example.edvblk.popularmoviesadnd.data.repository;

import android.support.annotation.NonNull;

import com.example.edvblk.popularmoviesadnd.data.pojos.Movie;
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
                .map(getResponseMapper());
    }

    @Override
    public Single<List<Movie>> getHighestRatedMovies() {
        return moviesService.getHighestRatedMovies()
                .map(getResponseMapper());
    }

    @Override
    public Single<List<Movie>> getMovieVideos() {
        return null;
    }

    @Override
    public Single<List<Movie>> getMovieReviews() {
        return null;
    }


    @NonNull
    private Mapper<MoviesResultResponse<List<Movie>>, List<Movie>> getResponseMapper() {
        return MoviesResultResponse::getResult;
    }
}

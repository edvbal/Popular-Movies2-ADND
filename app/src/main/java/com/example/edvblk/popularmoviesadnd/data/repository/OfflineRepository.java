package com.example.edvblk.popularmoviesadnd.data.repository;

import android.arch.lifecycle.LiveData;

import com.example.edvblk.popularmoviesadnd.data.database.MovieEntity;
import com.example.edvblk.popularmoviesadnd.data.pojos.Movie;

import java.util.List;

import io.reactivex.Single;

public interface OfflineRepository {
    Single<Long> insertMovieToFavorites(Movie movie);

    Single<LiveData<List<MovieEntity>>> getMovies();

    Single<Boolean> isMovieInFavorites(String title);

    Single<Integer> deleteMovieFromFavorites(MovieEntity movie);
}

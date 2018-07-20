package com.example.edvblk.popularmoviesadnd.data.repository;

import android.arch.lifecycle.LiveData;

import com.example.edvblk.popularmoviesadnd.data.database.MovieEntity;
import com.example.edvblk.popularmoviesadnd.main.Movie;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.SingleSource;

public interface MovieRepository {
    Single<Long> insertMovieToFavorites(Movie movie);

    Single<LiveData<List<MovieEntity>>> getMovies();

    Single<Boolean> isMovieInFavorites(String title);

    Single<Integer> deleteMovieFromFavorites(String title);
}

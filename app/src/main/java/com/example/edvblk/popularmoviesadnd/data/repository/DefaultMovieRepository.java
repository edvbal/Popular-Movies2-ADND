package com.example.edvblk.popularmoviesadnd.data.repository;

import android.arch.lifecycle.LiveData;

import com.example.edvblk.popularmoviesadnd.data.database.MovieDao;
import com.example.edvblk.popularmoviesadnd.data.database.MovieEntity;
import com.example.edvblk.popularmoviesadnd.data.database.MovieEntityMapper;
import com.example.edvblk.popularmoviesadnd.main.Movie;
import com.example.edvblk.popularmoviesadnd.utils.Mapper;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.Single;

public final class DefaultMovieRepository implements MovieRepository {
    private final MovieDao movieDao;
    private final Scheduler scheduler;
    private final MovieEntityMapper movieEntityMapper;

    public DefaultMovieRepository(
            MovieDao movieDao,
            Scheduler scheduler,
            MovieEntityMapper movieEntityMapper
    ) {
        this.movieDao = movieDao;
        this.scheduler = scheduler;
        this.movieEntityMapper = movieEntityMapper;
    }

    @Override
    public Single<Long> insertMovieToFavorites(Movie movie) {
        return Single.fromCallable(() -> movieDao.insert(movieEntityMapper.apply(movie)))
                .subscribeOn(scheduler);
    }

    @Override
    public Single<LiveData<List<MovieEntity>>> getMovies() {
        return Single.fromCallable(movieDao::getMovies).subscribeOn(scheduler);
    }

    @Override
    public Single<Boolean> isMovieInFavorites(String title) {
        return movieDao.doesMovieExist(title)
                .subscribeOn(scheduler)
                .map((Mapper<Integer, Boolean>) integer -> integer > 0);
    }

    @Override
    public Single<Integer> deleteMovieFromFavorites(String title) {
        return null;
    }
}

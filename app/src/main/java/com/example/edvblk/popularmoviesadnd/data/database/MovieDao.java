package com.example.edvblk.popularmoviesadnd.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movies")
    LiveData<List<MovieEntity>> getMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(MovieEntity movie);

    @Query("SELECT COUNT(title) FROM movies WHERE title = :title")
    Single<Integer> doesMovieExist(String title);

    @Delete()
    int deleteMovie(MovieEntity movie);
}

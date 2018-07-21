package com.example.edvblk.popularmoviesadnd.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(version = 2, entities = {MovieEntity.class})
//@TypeConverters(Converters::class)
public abstract class MoviesDatabase extends RoomDatabase {
    public abstract MovieDao movieDao();
}

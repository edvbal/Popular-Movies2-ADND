package com.example.edvblk.popularmoviesadnd.base;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.example.edvblk.popularmoviesadnd.data.database.MoviesDatabase;
import com.example.edvblk.popularmoviesadnd.utils.network.RetrofitFactory;

import retrofit2.Retrofit;

public class BaseApplication extends Application {
    private Retrofit retrofit;
    private MoviesDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        retrofit = new RetrofitFactory().create();
        database = Room.databaseBuilder(this, MoviesDatabase.class, "database")
                .fallbackToDestructiveMigration()
                .build();
    }

    private Retrofit getRetrofit() {
        return retrofit;
    }

    public static Retrofit getRetrofit(Context context) {
        return getBaseApplication(context).getRetrofit();
    }

    private MoviesDatabase getDatabase() {
        return database;
    }

    public static MoviesDatabase getDatabase(Context context) {
        return getBaseApplication(context).getDatabase();
    }

    private static BaseApplication getBaseApplication(Context context) {
        return ((BaseApplication) context.getApplicationContext());
    }
}

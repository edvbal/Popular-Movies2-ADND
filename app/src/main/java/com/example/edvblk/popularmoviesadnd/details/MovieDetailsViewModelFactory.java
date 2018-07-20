package com.example.edvblk.popularmoviesadnd.details;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.edvblk.popularmoviesadnd.base.BaseApplication;
import com.example.edvblk.popularmoviesadnd.data.database.MovieDao;
import com.example.edvblk.popularmoviesadnd.data.database.MovieEntityMapper;
import com.example.edvblk.popularmoviesadnd.data.repository.DefaultMovieRepository;
import com.example.edvblk.popularmoviesadnd.utils.MessagesProviderImpl;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailsViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public MovieDetailsViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    public MovieDetailsViewModel create(@NonNull Class modelClass) {
        MovieDao movieDao = BaseApplication.getDatabase(context).movieDao();
        return new MovieDetailsViewModel(
                new MessagesProviderImpl(context.getResources()),
                new DefaultMovieRepository(
                        movieDao,
                        Schedulers.computation(),
                        new MovieEntityMapper()
                ),
                AndroidSchedulers.mainThread()
        );
    }
}

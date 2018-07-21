package com.example.edvblk.popularmoviesadnd.details;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.edvblk.popularmoviesadnd.base.BaseApplication;
import com.example.edvblk.popularmoviesadnd.data.database.MovieDao;
import com.example.edvblk.popularmoviesadnd.data.database.MovieEntityMapper;
import com.example.edvblk.popularmoviesadnd.data.repository.DefaultOfflineRepository;
import com.example.edvblk.popularmoviesadnd.data.repository.DefaultOnlineRepository;
import com.example.edvblk.popularmoviesadnd.utils.MessagesProviderImpl;
import com.example.edvblk.popularmoviesadnd.utils.network.MoviesService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MovieDetailsViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public MovieDetailsViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    public MovieDetailsViewModel create(@NonNull Class modelClass) {
        MovieDao movieDao = BaseApplication.getDatabase(context).movieDao();
        Retrofit retrofit = BaseApplication.getRetrofit(context);
        MoviesService service = retrofit.create(MoviesService.class);
        return new MovieDetailsViewModel(
                new MessagesProviderImpl(context.getResources()),
                new DefaultOfflineRepository(
                        movieDao,
                        Schedulers.computation(),
                        new MovieEntityMapper()
                ),
                new DefaultOnlineRepository(service),
                AndroidSchedulers.mainThread()
        );
    }
}

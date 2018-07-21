package com.example.edvblk.popularmoviesadnd.main;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.edvblk.popularmoviesadnd.data.database.MovieDao;
import com.example.edvblk.popularmoviesadnd.data.database.MovieEntityListMapper;
import com.example.edvblk.popularmoviesadnd.data.database.MovieEntityMapper;
import com.example.edvblk.popularmoviesadnd.data.repository.DefaultOfflineRepository;
import com.example.edvblk.popularmoviesadnd.data.repository.DefaultOnlineRepository;
import com.example.edvblk.popularmoviesadnd.utils.MessagesProviderImpl;
import com.example.edvblk.popularmoviesadnd.base.BaseApplication;
import com.example.edvblk.popularmoviesadnd.utils.network.DefaultInternetChecker;
import com.example.edvblk.popularmoviesadnd.utils.network.MoviesService;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MoviesViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public MoviesViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MoviesViewModel create(@NonNull Class modelClass) {
        Retrofit retrofit = BaseApplication.getRetrofit(context);
        MovieDao movieDao = BaseApplication.getDatabase(context).movieDao();
        MoviesService service = retrofit.create(MoviesService.class);
        DefaultInternetChecker internetChecker = new DefaultInternetChecker(context);
        MessagesProviderImpl messagesProvider = new MessagesProviderImpl(context.getResources());
        Scheduler scheduler = Schedulers.computation();
        MovieEntityMapper mapper = new MovieEntityMapper();
        DefaultOfflineRepository repository = new DefaultOfflineRepository(movieDao, scheduler, mapper);
        return new MoviesViewModel(
                internetChecker,
                messagesProvider,
                AndroidSchedulers.mainThread(),
                repository,
                new DefaultOnlineRepository(service),
                new MovieEntityListMapper()
        );
    }
}

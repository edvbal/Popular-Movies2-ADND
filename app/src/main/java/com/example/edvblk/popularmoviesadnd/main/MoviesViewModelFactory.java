package com.example.edvblk.popularmoviesadnd.main;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.edvblk.popularmoviesadnd.utils.MessagesProviderImpl;
import com.example.edvblk.popularmoviesadnd.base.BaseApplication;
import com.example.edvblk.popularmoviesadnd.utils.network.DefaultInternetChecker;
import com.example.edvblk.popularmoviesadnd.utils.network.MoviesService;

import io.reactivex.android.schedulers.AndroidSchedulers;
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
        MoviesService service = retrofit.create(MoviesService.class);
        DefaultInternetChecker internetChecker = new DefaultInternetChecker(context);
        MessagesProviderImpl messagesProvider = new MessagesProviderImpl(context.getResources());
        return new MoviesViewModel(
                service,
                internetChecker,
                messagesProvider,
                AndroidSchedulers.mainThread()
        );
    }
}

package com.example.edvblk.popularmoviesadnd.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.edvblk.popularmoviesadnd.data.database.MovieEntity;
import com.example.edvblk.popularmoviesadnd.data.database.MovieEntityListMapper;
import com.example.edvblk.popularmoviesadnd.data.pojos.Movie;
import com.example.edvblk.popularmoviesadnd.data.repository.OfflineRepository;
import com.example.edvblk.popularmoviesadnd.data.repository.OnlineRepository;
import com.example.edvblk.popularmoviesadnd.utils.MessagesProvider;
import com.example.edvblk.popularmoviesadnd.utils.mvvm.SingleLiveEvent;
import com.example.edvblk.popularmoviesadnd.utils.network.InternetChecker;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;

public class MoviesViewModel extends ViewModel {
    public static final String SORT_CONFIG_POPULARITY = "key.sortPopularity";
    public static final String SORT_CONFIG_RATINGS = "key.sortRatings";
    public static final String SORT_CONFIG_FAVORITES = "key.sortFavorites";
    public static final String DEFAULT_SORT_CONFIG = SORT_CONFIG_POPULARITY;
    private static final String CLASS_TAG = MoviesViewModel.class.getSimpleName();

    private final InternetChecker internetChecker;
    private final MessagesProvider messagesProvider;
    private final Scheduler scheduler;
    private final OfflineRepository offlineRepository;
    private final OnlineRepository onlineRepository;
    private final MovieEntityListMapper entityMapper;

    private CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<String> errorState = new SingleLiveEvent<>();
    private MutableLiveData<Movie> detailsState = new SingleLiveEvent<>();
    private MutableLiveData<Boolean> progressState = new MutableLiveData<>();
    private MutableLiveData<List<Movie>> popularMoviesState = new MutableLiveData<>();
    private MutableLiveData<List<Movie>> highestRatedMoviesState = new MutableLiveData<>();
    private MediatorLiveData<List<Movie>> favoriteMoviesState = new MediatorLiveData<>();

    public MoviesViewModel(
            InternetChecker internetChecker,
            MessagesProvider messagesProvider,
            Scheduler scheduler,
            OfflineRepository offlineRepository,
            OnlineRepository onlineRepository,
            MovieEntityListMapper entityMapper
    ) {
        this.internetChecker = internetChecker;
        this.messagesProvider = messagesProvider;
        this.scheduler = scheduler;
        this.offlineRepository = offlineRepository;
        this.onlineRepository = onlineRepository;
        this.entityMapper = entityMapper;
    }

    public LiveData<String> getErrorState() {
        return errorState;
    }

    public LiveData<Movie> getDetailsState() {
        return detailsState;
    }

    public LiveData<List<Movie>> getPopularMoviesState() {
        return popularMoviesState;
    }

    public LiveData<List<Movie>> getHighestRatedMoviesState() {
        return highestRatedMoviesState;
    }

    public LiveData<List<Movie>> getFavoriteMoviesState() {
        return favoriteMoviesState;
    }

    public LiveData<Boolean> getProgressState() {
        return progressState;
    }

    @Override
    protected void onCleared() {
        disposables.dispose();
    }

    public void loadMovies(String sortKey) {
        switch (sortKey) {
            case SORT_CONFIG_RATINGS:
                loadMoviesByRatings();
                break;
            case SORT_CONFIG_POPULARITY:
                loadMoviesByPopularity();
                break;
            case SORT_CONFIG_FAVORITES:
                loadFavoriteMovies();
                break;
        }
    }

    private void loadMoviesByPopularity() {
        if (!internetChecker.isInternetAvailable()) {
            errorState.setValue(messagesProvider.provideNetworkErrorMessage());
            return;
        }
        disposables.add(onlineRepository.getPopularMovies()
                .observeOn(scheduler)
                .doOnSubscribe(disposable -> progressState.postValue(true))
                .subscribe(this::onPopularMoviesRequestSuccess, this::onRequestError));
    }

    private void loadMoviesByRatings() {
        if (!internetChecker.isInternetAvailable()) {
            errorState.setValue(messagesProvider.provideNetworkErrorMessage());
            return;
        }
        disposables.add(onlineRepository.getHighestRatedMovies()
                .observeOn(scheduler)
                .doOnSubscribe(disposable -> progressState.postValue(true))
                .subscribe(this::onHighestRatedMoviesRequestSuccess, this::onRequestError));
    }

    private void loadFavoriteMovies() {
        disposables.add(offlineRepository.getMovies()
                .observeOn(scheduler)
                .doOnSubscribe(disposable -> progressState.postValue(true))
                .subscribe(
                        this::onRepositorySuccess,
                        this::onRepositoryFailure
                )
        );
    }

    private void onRepositoryFailure(Throwable throwable) {
        Log.e(CLASS_TAG, throwable.getMessage());
        errorState.postValue(messagesProvider.provideRepoReadFailureMessage());
        progressState.postValue(false);
    }

    private void onRepositorySuccess(LiveData<List<MovieEntity>> listLiveData) {
        favoriteMoviesState.addSource(listLiveData, movieEntities -> {
            if (movieEntities == null) return;
            favoriteMoviesState.postValue(entityMapper.apply(movieEntities));
        });
        progressState.postValue(false);
    }

    private void onPopularMoviesRequestSuccess(List<Movie> movies) {
        popularMoviesState.setValue(movies);
        progressState.setValue(false);
    }

    private void onHighestRatedMoviesRequestSuccess(List<Movie> movies) {
        highestRatedMoviesState.setValue(movies);
        progressState.setValue(false);
    }

    private void onRequestError(Throwable throwable) {
        Log.e(CLASS_TAG, throwable.getMessage());
        errorState.setValue(messagesProvider.provideEmptyMoviesListMessage());
        progressState.setValue(false);
    }

    public void onMovieClicked(Movie movie) {
        detailsState.setValue(movie);
    }
}
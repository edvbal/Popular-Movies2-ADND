package com.example.edvblk.popularmoviesadnd.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.edvblk.popularmoviesadnd.data.database.MovieEntity;
import com.example.edvblk.popularmoviesadnd.data.database.MovieEntityListMapper;
import com.example.edvblk.popularmoviesadnd.data.repository.MovieRepository;
import com.example.edvblk.popularmoviesadnd.utils.MessagesProvider;
import com.example.edvblk.popularmoviesadnd.utils.mvvm.SingleLiveEvent;
import com.example.edvblk.popularmoviesadnd.utils.network.InternetChecker;
import com.example.edvblk.popularmoviesadnd.utils.network.MoviesResultResponse;
import com.example.edvblk.popularmoviesadnd.utils.network.MoviesService;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class MoviesViewModel extends ViewModel {
    public static final String CLASS_TAG = MoviesViewModel.class.getSimpleName();
    private final MoviesService moviesService;
    private final InternetChecker internetChecker;
    private final MessagesProvider messagesProvider;
    private final Scheduler scheduler;
    private final MovieRepository repository;
    private final MovieEntityListMapper entityMapper;
    private CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<String> errorState = new SingleLiveEvent<>();
    private MutableLiveData<Movie> detailsState = new SingleLiveEvent<>();
    private MutableLiveData<List<Movie>> moviesState = new MutableLiveData<>();
    private MutableLiveData<Boolean> progressState = new MutableLiveData<>();
    private MediatorLiveData<List<Movie>> favoritesState = new MediatorLiveData<>();

    public MoviesViewModel(
            MoviesService moviesService, InternetChecker internetChecker,
            MessagesProvider messagesProvider,
            Scheduler scheduler,
            MovieRepository repository,
            MovieEntityListMapper entityMapper
    ) {
        this.moviesService = moviesService;
        this.internetChecker = internetChecker;
        this.messagesProvider = messagesProvider;
        this.scheduler = scheduler;
        this.repository = repository;
        this.entityMapper = entityMapper;
        loadMoviesByPopularity();
    }

    public LiveData<String> getErrorState() {
        return errorState;
    }

    public LiveData<Movie> getDetailsState() {
        return detailsState;
    }

    public LiveData<List<Movie>> getMoviesState() {
        return moviesState;
    }

    public LiveData<Boolean> getProgressState() {
        return progressState;
    }

    public LiveData<List<Movie>> getFavoritesState() {
        return favoritesState;
    }

    @Override
    protected void onCleared() {
        disposables.dispose();
    }

    public void loadMoviesByPopularity() {
        loadMovie(moviesService.getPopularMovies());
    }

    public void loadMoviesByRatings() {
        loadMovie(moviesService.getHighestRatedMovies());
    }

    private void loadMovie(Single<MoviesResultResponse<List<Movie>>> loadMoviesRequest) {
        if (!internetChecker.isInternetAvailable()) {
            errorState.setValue(messagesProvider.provideNetworkErrorMessage());
            return;
        }
        disposables.add(loadMoviesRequest
                .observeOn(scheduler)
                .doOnSubscribe(disposable -> progressState.setValue(true))
                .subscribe(
                        this::onRequestSuccess,
                        this::onRequestError
                ));
    }

    public void loadMoviesFromRepository() {
        disposables.add(repository.getMovies()
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
        favoritesState.addSource(listLiveData, movieEntities -> {
            if (movieEntities == null) return;
            favoritesState.postValue(entityMapper.apply(movieEntities));
        });
        progressState.postValue(false);
    }

    private void onRequestError(Throwable throwable) {
        Log.e(CLASS_TAG, throwable.getMessage());
        errorState.setValue(messagesProvider.provideEmptyMoviesListMessage());
        progressState.setValue(false);
    }

    private void onRequestSuccess(MoviesResultResponse<List<Movie>> movies) {
        moviesState.setValue(movies.getResult());
        progressState.setValue(false);
    }

    public void onMovieClicked(Movie movie) {
        detailsState.setValue(movie);
    }
}

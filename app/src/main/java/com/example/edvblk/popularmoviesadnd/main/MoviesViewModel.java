package com.example.edvblk.popularmoviesadnd.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.edvblk.popularmoviesadnd.utils.MessagesProvider;
import com.example.edvblk.popularmoviesadnd.utils.architecture.ViewModelState;
import com.example.edvblk.popularmoviesadnd.utils.network.InternetChecker;
import com.example.edvblk.popularmoviesadnd.utils.network.MoviesService;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MoviesViewModel extends ViewModel {
    private final MoviesService moviesService;
    private final InternetChecker internetChecker;
    private final MessagesProvider messagesProvider;
    private final Scheduler scheduler;
    private CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<ViewModelState> state = new MutableLiveData<>();

    public MoviesViewModel(
            MoviesService moviesService, InternetChecker internetChecker,
            MessagesProvider messagesProvider,
            Scheduler scheduler
    ) {
        this.moviesService = moviesService;
        this.internetChecker = internetChecker;
        this.messagesProvider = messagesProvider;
        this.scheduler = scheduler;
    }

    @Override
    protected void onCleared() {
        disposables.dispose();
    }

    public void loadMovies() {
        if (!internetChecker.isInternetAvailable()) {
            onError(messagesProvider.provideNetworkErrorMessage());
            return;
        }
        disposables.add(moviesService.getPopularMovies()
                .observeOn(scheduler)
                .doOnSubscribe(disposable -> state.setValue(new ViewModelState.LoadingState(true)))
                .doOnError(throwable -> state.setValue(new ViewModelState.LoadingState(false)))
                .doOnSuccess(disposable -> state.setValue(new ViewModelState.LoadingState(false)))
                .subscribe(
                        movies -> state.setValue(new MoviesListState(movies.getResult())),
                        throwable -> onError(messagesProvider.provideEmptyMoviesListMessage())
                )
        );
    }

    private void onError(String errorMessage) {
        state.setValue(new ViewModelState.ErrorState(errorMessage));
    }

    public LiveData<ViewModelState> getState() {
        return state;
    }

    public void onItemSelected(Movie movie) {
        state.setValue(new MovieDetailsState(movie));
    }

    public void loadHighestRated() {
        disposables.add(moviesService.getHighestRatedMovies()
                .observeOn(scheduler)
                .doOnSubscribe(disposable -> state.setValue(new ViewModelState.LoadingState(true)))
                .doOnError(throwable -> state.setValue(new ViewModelState.LoadingState(false)))
                .doOnSuccess(disposable -> state.setValue(new ViewModelState.LoadingState(false)))
                .subscribe(
                        movies -> state.setValue(new MoviesListState(movies.getResult())),
                        throwable -> onError(messagesProvider.provideEmptyMoviesListMessage())
                )
        );
    }

    public void loadMostPopular() {
        disposables.add(moviesService.getPopularMovies()
                .observeOn(scheduler)
                .doOnSubscribe(disposable -> state.setValue(new ViewModelState.LoadingState(true)))
                .doOnError(throwable -> state.setValue(new ViewModelState.LoadingState(false)))
                .doOnSuccess(disposable -> state.setValue(new ViewModelState.LoadingState(false)))
                .subscribe(
                        movies -> state.setValue(new MoviesListState(movies.getResult())),
                        throwable -> onError(messagesProvider.provideEmptyMoviesListMessage())
                )
        );
    }

    public final class MoviesListState extends ViewModelState {
        private final List<Movie> movies;

        MoviesListState(List<Movie> movies) {
            this.movies = movies;
        }

        public List<Movie> getMovies() {
            return movies;
        }
    }

    public final class MovieDetailsState extends ViewModelState {
        private final Movie movie;

        MovieDetailsState(Movie movie) {
            this.movie = movie;
        }

        public Movie getMovie() {
            return movie;
        }
    }
}

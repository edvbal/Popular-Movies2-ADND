package com.example.edvblk.popularmoviesadnd.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.edvblk.popularmoviesadnd.utils.MessagesProvider;
import com.example.edvblk.popularmoviesadnd.utils.mvvm.SingleLiveEvent;
import com.example.edvblk.popularmoviesadnd.utils.mvvm.ViewModelEvent;
import com.example.edvblk.popularmoviesadnd.utils.network.InternetChecker;
import com.example.edvblk.popularmoviesadnd.utils.network.MoviesResultResponse;
import com.example.edvblk.popularmoviesadnd.utils.network.MoviesService;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MoviesViewModel extends ViewModel {
    public static final String KEY_SORT_POPULARITY = "key.sortPopularity";
    public static final String KEY_SORT_RATING = "key.sortRating";
    public static final String KEY_SORT_DEFAULT = "key.sortPopularity";

    private final MoviesService moviesService;
    private final InternetChecker internetChecker;
    private final MessagesProvider messagesProvider;
    private final Scheduler scheduler;

    private CompositeDisposable disposables = new CompositeDisposable();
    private SingleLiveEvent<ViewModelEvent> event = new SingleLiveEvent<>();


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

    private void onError(String errorMessage) {
        event.setValue(new ViewModelEvent.ErrorEvent(errorMessage));
    }

    public LiveData<ViewModelEvent> getEvent() {
        return event;
    }

    public void onItemSelected(Movie movie) {
        event.setValue(new MovieDetailsEvent(movie));
    }

    public void loadMovies(String sortKey) {
        if (sortKey.equals(KEY_SORT_POPULARITY)) {
            loadMovie(moviesService.getPopularMovies());
        } else if (sortKey.equals(KEY_SORT_RATING)) {
            loadMovie(moviesService.getHighestRatedMovies());
        }
    }

    private void loadMovie(Single<MoviesResultResponse<List<Movie>>> loadMoviesRequest) {
        if (!internetChecker.isInternetAvailable()) {
            onError(messagesProvider.provideNetworkErrorMessage());
            return;
        }
        Disposable loadRequestDisposable = loadMoviesRequest
                .observeOn(scheduler)
                .doOnSubscribe(disposable -> event.setValue(new ViewModelEvent.LoadingEvent(true)))
                .doOnError(throwable -> event.setValue(new ViewModelEvent.LoadingEvent(false)))
                .subscribe(
                        movies -> {
                            event.setValue(new MoviesListEvent(movies.getResult()));
                            event.setValue(new ViewModelEvent.LoadingEvent(false));
                        },
                        throwable -> onError(messagesProvider.provideEmptyMoviesListMessage())
                );
        disposables.add(loadRequestDisposable);
    }

    public final class MoviesListEvent extends ViewModelEvent {
        private final List<Movie> movies;

        MoviesListEvent(List<Movie> movies) {
            this.movies = movies;
        }

        public List<Movie> getMovies() {
            return movies;
        }
    }

    public final class MovieDetailsEvent extends ViewModelEvent {
        private final Movie movie;

        MovieDetailsEvent(Movie movie) {
            this.movie = movie;
        }

        public Movie getMovie() {
            return movie;
        }
    }
}

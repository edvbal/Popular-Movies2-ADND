package com.example.edvblk.popularmoviesadnd.details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.edvblk.popularmoviesadnd.data.repository.MovieRepository;
import com.example.edvblk.popularmoviesadnd.main.Movie;
import com.example.edvblk.popularmoviesadnd.utils.Mapper;
import com.example.edvblk.popularmoviesadnd.utils.MessagesProvider;
import com.example.edvblk.popularmoviesadnd.utils.mvvm.SingleLiveEvent;

import io.reactivex.Scheduler;
import io.reactivex.SingleSource;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;

class MovieDetailsViewModel extends ViewModel {
    private final MessagesProvider messagesProvider;
    private final MovieRepository movieRepository;
    private final Scheduler scheduler;
    private MutableLiveData<String> errorState = new SingleLiveEvent<>();
    private MutableLiveData<Movie> movieDetailsState = new MutableLiveData<>();
    private MutableLiveData<String> favoriteState = new MutableLiveData<>();
    private MutableLiveData<Boolean> favoriteImageState = new SingleLiveEvent<>();
    private CompositeDisposable disposables = new CompositeDisposable();

    MovieDetailsViewModel(
            MessagesProvider messagesProvider,
            MovieRepository movieRepository,
            Scheduler scheduler) {
        this.messagesProvider = messagesProvider;
        this.movieRepository = movieRepository;
        this.scheduler = scheduler;
    }

    public void onMovieSelected(@Nullable Movie movie) {
        if (movie == null) {
            String errorMessage = messagesProvider.provideEmptyMovieDetailsMessage();
            errorState.setValue(errorMessage);
        } else {
            movieDetailsState.setValue(movie);
            disposables.add(movieRepository.isMovieInFavorites(movie.getTitle())
                    .observeOn(scheduler)
                    .subscribe(doesMovieExist -> favoriteImageState.postValue(doesMovieExist))
            );
        }
    }

    public LiveData<Movie> getMovieDetailsState() {
        return movieDetailsState;
    }

    public LiveData<String> getErrorState() {
        return errorState;
    }

    public LiveData<String> getFavoriteState() {
        return favoriteState;
    }

    public void onFavoriteSelected(Movie movie) {
        String title = movie.getTitle();
        disposables.add(movieRepository.isMovieInFavorites(title)
                .observeOn(scheduler)
                .flatMap((Mapper<Boolean, SingleSource<?>>) doesMovieExist -> {
                    if (doesMovieExist) {
                        return movieRepository.deleteMovieFromFavorites(title);
                    } else {
                        return movieRepository.insertMovieToFavorites(movie);
                    }
                }).subscribe(number -> {
                    if (doesMovieExist) {
                        movieRepository.removeFromFavorites(title)
                    }
                    favoriteImageState.postValue(doesMovieExist)
                })
        );

        disposables.add(movieRepository.insertMovieToFavorites(movie)
                .observeOn(scheduler)
                .subscribe(this::onInsertSuccess, this::onInsertError)
        );
    }

    private void onInsertError(Throwable throwable) {
        Log.e(MovieDetailsViewModel.class.getSimpleName(), throwable.getMessage());
        String message = messagesProvider.provideRepoWriteFailureMessage();
        errorState.postValue(message);
    }

    private void onInsertSuccess(Long insertedCount) {
        if (insertedCount > 0) {
            String message = messagesProvider.provideRepoWriteSuccessMessage();
            favoriteState.postValue(message);
        } else {
            String message = messagesProvider.provideMovieAlreadyFavoriteMessage();
            errorState.postValue(message);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public LiveData<Boolean> getFavoriteImageState() {
        return favoriteImageState;
    }
}

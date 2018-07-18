package com.example.edvblk.popularmoviesadnd.details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.edvblk.popularmoviesadnd.main.Movie;
import com.example.edvblk.popularmoviesadnd.utils.MessagesProvider;
import com.example.edvblk.popularmoviesadnd.utils.mvvm.ViewModelEvent;

import io.reactivex.annotations.Nullable;

class MovieDetailsViewModel extends ViewModel {
    private final MessagesProvider messagesProvider;

    private MutableLiveData<ViewModelEvent> mutableStates = new MutableLiveData<>();

    MovieDetailsViewModel(MessagesProvider messagesProvider) {
        this.messagesProvider = messagesProvider;
    }

    public void onMovieSelected(@Nullable Movie movie) {
        if (movie == null) {
            String errorMessage = messagesProvider.provideEmptyMovieDetailsMessage();
            mutableStates.setValue(new ViewModelEvent.ErrorEvent(errorMessage));
        } else {
            mutableStates.setValue(new MovieDetailEvent(movie));
        }
    }

    public final class MovieDetailEvent extends ViewModelEvent {
        private final Movie movieDetails;

        MovieDetailEvent(Movie movieDetails) {
            this.movieDetails = movieDetails;
        }

        public Movie getMovieDetails() {
            return movieDetails;
        }
    }

    public LiveData<ViewModelEvent> getMovieDetailsEvents() {
        return mutableStates;
    }

    public final class MovieDetailsEvent extends ViewModelEvent {
        private final Movie details;

        public MovieDetailsEvent(Movie details) {
            this.details = details;
        }

        public Movie getDetails() {
            return details;
        }
    }
}

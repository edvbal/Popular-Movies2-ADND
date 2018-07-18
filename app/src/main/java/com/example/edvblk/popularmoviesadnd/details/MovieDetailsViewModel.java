package com.example.edvblk.popularmoviesadnd.details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.edvblk.popularmoviesadnd.main.Movie;
import com.example.edvblk.popularmoviesadnd.utils.MessagesProvider;
import com.example.edvblk.popularmoviesadnd.utils.architecture.ViewModelState;

import io.reactivex.annotations.Nullable;

class MovieDetailsViewModel extends ViewModel {
    private final MessagesProvider messagesProvider;

    private MutableLiveData<ViewModelState> mutableStates = new MutableLiveData<>();

    MovieDetailsViewModel(MessagesProvider messagesProvider) {
        this.messagesProvider = messagesProvider;
    }

    public void onMovieSelected(@Nullable Movie movie) {
        if (movie == null) {
            String errorMessage = messagesProvider.provideEmptyMovieDetailsMessage();
            mutableStates.setValue(new ViewModelState.ErrorState(errorMessage));
        } else {
            mutableStates.setValue(new MovieDetailState(movie));
        }
    }

    public final class MovieDetailState extends ViewModelState {
        private final Movie movieDetails;

        MovieDetailState(Movie movieDetails) {
            this.movieDetails = movieDetails;
        }

        public Movie getMovieDetails() {
            return movieDetails;
        }
    }

    public LiveData<ViewModelState> getMovieDetailsStates() {
        return mutableStates;
    }

    public final class MovieDetailsState extends ViewModelState {
        private final Movie details;

        public MovieDetailsState(Movie details) {
            this.details = details;
        }

        public Movie getDetails() {
            return details;
        }
    }
}

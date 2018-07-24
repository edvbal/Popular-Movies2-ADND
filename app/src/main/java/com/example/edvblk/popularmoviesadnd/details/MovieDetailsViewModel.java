package com.example.edvblk.popularmoviesadnd.details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.edvblk.popularmoviesadnd.data.pojos.MovieReview;
import com.example.edvblk.popularmoviesadnd.data.pojos.MovieTrailer;
import com.example.edvblk.popularmoviesadnd.data.repository.OfflineRepository;
import com.example.edvblk.popularmoviesadnd.data.pojos.Movie;
import com.example.edvblk.popularmoviesadnd.data.repository.OnlineRepository;
import com.example.edvblk.popularmoviesadnd.utils.Mapper;
import com.example.edvblk.popularmoviesadnd.utils.MessagesProvider;
import com.example.edvblk.popularmoviesadnd.utils.mvvm.SingleLiveEvent;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.SingleSource;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;

class MovieDetailsViewModel extends ViewModel {
    private final MessagesProvider messagesProvider;
    private final OfflineRepository offlineRepository;
    private final OnlineRepository onlineRepository;
    private final Scheduler scheduler;
    private MutableLiveData<String> errorState = new SingleLiveEvent<>();
    private MutableLiveData<Boolean> reviewProgressState = new SingleLiveEvent<>();
    private MutableLiveData<Boolean> trailersProgressState = new SingleLiveEvent<>();
    private MutableLiveData<Movie> movieDetailsState = new MutableLiveData<>();
    private MutableLiveData<String> favoriteState = new SingleLiveEvent<>();
    private MutableLiveData<List<MovieTrailer>> movieTrailersState = new MutableLiveData<>();
    private MutableLiveData<List<MovieReview>> movieReviewsState = new MutableLiveData<>();
    private MutableLiveData<Boolean> favoriteImageState = new MutableLiveData<>();
    private CompositeDisposable disposables = new CompositeDisposable();

    MovieDetailsViewModel(
            MessagesProvider messagesProvider,
            OfflineRepository offlineRepository,
            OnlineRepository onlineRepository,
            Scheduler scheduler
    ) {
        this.messagesProvider = messagesProvider;
        this.offlineRepository = offlineRepository;
        this.onlineRepository = onlineRepository;
        this.scheduler = scheduler;
    }

    public void onMovieSelected(@Nullable Movie movie) {
        String errorMessage = messagesProvider.provideEmptyMovieDetailsMessage();
        if (movie == null) {
            errorState.setValue(errorMessage);
        } else {
            int id = movie.getId();
            disposables.add(offlineRepository.isMovieInFavorites(movie.getTitle())
                    .observeOn(scheduler)
                    .subscribe(doesMovieExist -> onDetailsReady(movie, doesMovieExist),
                            throwable -> errorState.postValue(errorMessage)));
            disposables.add(onlineRepository.getMovieTrailers(id)
                    .doOnSubscribe(disposable -> reviewProgressState.postValue(true))
                    .observeOn(scheduler)
                    .subscribe(this::onMovieTrailersReceived, this::onMovieTrailersRequestError));
            disposables.add(onlineRepository.getMovieReviews(id)
                    .doOnSubscribe(disposable -> trailersProgressState.postValue(true))
                    .observeOn(scheduler)
                    .subscribe(this::onMovieReviewsReceived, this::onMovieReviewsRequestError));
        }
    }

    private void onMovieReviewsRequestError(Throwable throwable) {
        Log.e(MovieDetailsViewModel.class.getSimpleName(), throwable.getMessage());
        reviewProgressState.postValue(false);
        errorState.postValue(messagesProvider.provideMovieReviewsLoadError());
    }

    private void onMovieReviewsReceived(List<MovieReview> movieReviews) {
        reviewProgressState.postValue(false);
        movieReviewsState.postValue(movieReviews);
    }

    private void onMovieTrailersRequestError(Throwable throwable) {
        Log.e(MovieDetailsViewModel.class.getSimpleName(), throwable.getMessage());
        trailersProgressState.postValue(false);
        errorState.postValue(messagesProvider.provideMovieTrailersLoadError());
    }

    private void onMovieTrailersReceived(List<MovieTrailer> movieTrailers) {
        trailersProgressState.postValue(false);
        movieTrailersState.postValue(movieTrailers);
    }

    private void onDetailsReady(@Nullable Movie movie, Boolean doesMovieExist) {
        favoriteImageState.postValue(doesMovieExist);
        movieDetailsState.setValue(movie);
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

    public LiveData<Boolean> getReviewProgressState() {
        return reviewProgressState;
    }

    public LiveData<Boolean> getTrailersProgressState() {
        return trailersProgressState;
    }

    public LiveData<List<MovieTrailer>> getMovieTrailersState() {
        return movieTrailersState;
    }

    public LiveData<List<MovieReview>> getMovieReviewsState() {
        return movieReviewsState;
    }

    public void onFavoriteSelected(Movie movie) {
        String title = movie.getTitle();
        disposables.add(offlineRepository.isMovieInFavorites(title)
                .observeOn(scheduler)
                .flatMap((Mapper<Boolean, SingleSource<?>>) doesMovieExist -> {
                    if (doesMovieExist) {
                        return offlineRepository.deleteMovieFromFavorites(movie.toEntity());
                    } else {
                        return offlineRepository.insertMovieToFavorites(movie);
                    }
                }).subscribe(number -> {
                    if (number instanceof Integer) {
                        String successMessage = messagesProvider.provideRepositoryDeleteSuccessMessage();
                        String errorMessage = messagesProvider.provideRepositoryDeleteError();
                        handleFavoriteMoviesChange(number.toString(), successMessage, errorMessage, false);
                    } else if (number instanceof Long) {
                        String successMessage = messagesProvider.provideRepoWriteSuccessMessage();
                        String errorMessage = messagesProvider.provideRepoWriteFailureMessage();
                        handleFavoriteMoviesChange(number.toString(), successMessage, errorMessage, true);
                    }
                })
        );
    }

    private void handleFavoriteMoviesChange(
            String itemCount,
            String successMessage,
            String errorMessage,
            boolean isInsertion
    ) {
        if (itemCount.equals("0")) {
            favoriteState.postValue(errorMessage);
        } else {
            favoriteState.postValue(successMessage);
            favoriteImageState.postValue(isInsertion);
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

package com.example.edvblk.popularmoviesadnd;

import com.example.edvblk.popularmoviesadnd.main.Movie;
import com.example.edvblk.popularmoviesadnd.main.MoviesViewModel;
import com.example.edvblk.popularmoviesadnd.utils.MessagesProvider;
import com.example.edvblk.popularmoviesadnd.utils.network.InternetChecker;
import com.example.edvblk.popularmoviesadnd.utils.network.MoviesResultResponse;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.TestScheduler;

import static com.example.edvblk.popularmoviesadnd.main.MainContract.Model;
import static com.example.edvblk.popularmoviesadnd.main.MainContract.View;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MoviesViewModelTest {
    private static final String DEFAULT_ERROR_MESSAGE = "errorMessage";
    private final List<Movie> moviesList = Arrays.asList(new Movie("link1"), new Movie("link2"));
    private final TestScheduler scheduler = new TestScheduler();
    private final MoviesResultResponse<List<Movie>> moviesResponse
            = new MoviesResultResponse<>(moviesList);
    private MoviesViewModel presenter;
    private InternetChecker internetChecker;
    private View view;
    private MessagesProvider messagesProvider;
    private Model model;

    @Before
    public void setUp() {
        view = mock(View.class);
        model = mock(Model.class);
        internetChecker = mock(InternetChecker.class);
        messagesProvider = mock(MessagesProvider.class);
        presenter = new MoviesViewModel(model, service, internetChecker, messagesProvider, scheduler, movieEntityMapper, asd, mapper);
        presenter.takeView(view);
        when(messagesProvider.provideNetworkErrorMessage()).thenReturn(DEFAULT_ERROR_MESSAGE);
        when(messagesProvider.provideEmptyMoviesListMessage()).thenReturn(DEFAULT_ERROR_MESSAGE);
        when(internetChecker.isInternetAvailable()).thenReturn(true);
        when(model.getPopularMovies()).thenReturn(Single.just(moviesResponse));
    }

    @Test
    public void onCreate_noInternet_callsViewShowErrorWithProvidedErrorMessage() {
        when(internetChecker.isInternetAvailable()).thenReturn(false);

        presenter.onCreate();
        scheduler.triggerActions();

        verify(view).showError(DEFAULT_ERROR_MESSAGE);
        verify(messagesProvider).provideNetworkErrorMessage();
    }

    @Test
    public void onCreate_noInternet_doesNotCallModel() {
        when(internetChecker.isInternetAvailable()).thenReturn(false);

        presenter.onCreate();
        scheduler.triggerActions();


        verifyZeroInteractions(model);
    }

    @Test
    public void onCreate_hasInternet_callsModelGetMovies() {
        when(internetChecker.isInternetAvailable()).thenReturn(true);

        presenter.onCreate();
        scheduler.triggerActions();


        verify(model).getPopularMovies();
    }

    @Test
    public void onCreate_hasInternetAfterDispose_doesNotInteractWithView() {
        when(internetChecker.isInternetAvailable()).thenReturn(true);

        presenter.onCreate();
        presenter.dropView();
        scheduler.triggerActions();

        verifyZeroInteractions(view);
    }

    @Test
    public void onCreate_successfulResponse_callsViewPopulateView() {
        presenter.onCreate();
        scheduler.triggerActions();


        verify(view).populateView(moviesList);
    }

    @Test
    public void onCreate_failureResponse_callsViewShowErrorWithProvidedMessage() {
        when(model.getPopularMovies()).thenReturn(Single.error(new RuntimeException()));

        presenter.onCreate();
        scheduler.triggerActions();


        verify(view).showError(messagesProvider.provideEmptyMoviesListMessage());
    }

    @Test
    public void onItemSelected_callView() {
        Movie movie = new Movie(id, "posterPath", releaseDate, averageVote, overView, title);

        presenter.onMovieClicked(movie);

        verify(view).openDetailsActivity(movie);
    }

    @Test
    public void dropView_viewsAreNotCalled() {
        presenter.onCreate();
        presenter.dropView();
        scheduler.triggerActions();

        verifyZeroInteractions(view);
    }
}
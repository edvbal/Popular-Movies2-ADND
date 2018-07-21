package com.example.edvblk.popularmoviesadnd.details;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MovieDetailsViewModelTest {
    @Test
    public void onCreate_callsView() {
        MovieDetailsViewModel presenter = new MovieDetailsViewModel(messagesProvider, database, onlineRepository, scheduler);
        MovieDetailsContract.View view = mock(MovieDetailsContract.View.class);
        presenter.takeView(view);

        presenter.onMovieSelected((Movie) extras.get(INTENT_EXTRA_KEY_MOVIE));

        verify(view).showMovieDetails();
    }
}
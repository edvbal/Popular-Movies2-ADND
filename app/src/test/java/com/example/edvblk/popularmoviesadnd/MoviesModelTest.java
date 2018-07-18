package com.example.edvblk.popularmoviesadnd;

import com.example.edvblk.popularmoviesadnd.utils.network.MoviesService;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MoviesModelTest {
    @Test
    public void getMovies_callsServiceGetMovies() {
        MoviesService service = mock(MoviesService.class);
        MoviesModel model = new MoviesModel(service);

        model.getPopularMovies();

        verify(service).getMoviesSortedByPopularity();
    }
}
package com.example.edvblk.popularmoviesadnd.main;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.edvblk.popularmoviesadnd.R;
import com.example.edvblk.popularmoviesadnd.base.BaseFragment;
import com.example.edvblk.popularmoviesadnd.data.pojos.Movie;
import com.example.edvblk.popularmoviesadnd.details.MovieDetailsActivity;
import com.example.edvblk.popularmoviesadnd.utils.Notifier;
import com.example.edvblk.popularmoviesadnd.utils.NotifierImpl;
import com.example.edvblk.popularmoviesadnd.utils.image.DefaultImageUrlProvider;
import com.example.edvblk.popularmoviesadnd.utils.image.GlideImageLoader;

import java.util.List;

import butterknife.BindView;

import static com.example.edvblk.popularmoviesadnd.main.MoviesViewModel.SORT_CONFIG_FAVORITES;
import static com.example.edvblk.popularmoviesadnd.main.MoviesViewModel.SORT_CONFIG_POPULARITY;
import static com.example.edvblk.popularmoviesadnd.main.MoviesViewModel.SORT_CONFIG_RATINGS;

public class MoviesFragment extends BaseFragment {
    private static final String KEY_SORT_CONFIG = "key.sortConfig";
    private static final String KEY_RECYCLER_STATE = "key.recyclerState";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private MoviesAdapter adapter;
    private MoviesViewModel moviesViewModel;
    private Notifier notifier;
    //    private Parcelable recyclerScrollState;
    private String sortConfig = MoviesViewModel.DEFAULT_SORT_CONFIG;

    public static MoviesFragment newInstance() {
        return new MoviesFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFields(view);
        setHasOptionsMenu(true);
        moviesViewModel.getErrorState().observe(this, notifier::showError);
        moviesViewModel.getProgressState().observe(this, this::showLoadingBar);
        moviesViewModel.getDetailsState().observe(this, this::showDetailsScreen);
    }

    private void initFields(View view) {
        initViewModel();
        initAdapter();
        initRecycler();
        notifier = new NotifierImpl(view);
    }

    private void initViewModel() {
        MoviesViewModelFactory factory = new MoviesViewModelFactory(getContext());
        moviesViewModel = ViewModelProviders.of(this, factory).get(MoviesViewModel.class);
    }

    private void initAdapter() {
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        DefaultImageUrlProvider urlProvider = new DefaultImageUrlProvider(widthPixels);
        adapter = new MoviesAdapter(
                new GlideImageLoader(getContext()),
                urlProvider,
                item -> moviesViewModel.onMovieClicked(item));
    }

    private void initRecycler() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_SORT_CONFIG, sortConfig);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            sortConfig = savedInstanceState.getString(KEY_SORT_CONFIG);
        }
        moviesViewModel.loadMovies(sortConfig);
        switch (sortConfig) {
            case SORT_CONFIG_RATINGS:
                moviesViewModel.getPopularMoviesState().removeObservers(this);
                moviesViewModel.getFavoriteMoviesState().removeObservers(this);
                moviesViewModel.getHighestRatedMoviesState().observe(this, adapter::setItems);
                break;
            case SORT_CONFIG_POPULARITY:
                moviesViewModel.getHighestRatedMoviesState().removeObservers(this);
                moviesViewModel.getFavoriteMoviesState().removeObservers(this);
                moviesViewModel.getPopularMoviesState().observe(this, adapter::setItems);
                break;
            case SORT_CONFIG_FAVORITES:
                moviesViewModel.getPopularMoviesState().removeObservers(this);
                moviesViewModel.getHighestRatedMoviesState().removeObservers(this);
                moviesViewModel.getFavoriteMoviesState().observe(this, adapter::setItems);
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    private void showLoadingBar(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }

    }

    private void showDetailsScreen(Movie movieDetails) {
        MovieDetailsActivity.start(getContext(), movieDetails);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_highest_rated) {
            sortConfig = SORT_CONFIG_RATINGS;
            moviesViewModel.getHighestRatedMoviesState().observe(this, adapter::setItems);
//            moviesViewModel.getHighestRatedMoviesState().removeObservers(this);
            moviesViewModel.loadMovies(SORT_CONFIG_RATINGS);
        } else if (item.getItemId() == R.id.menu_item_most_popular) {
            sortConfig = SORT_CONFIG_POPULARITY;
            moviesViewModel.getPopularMoviesState().observe(this, adapter::setItems);
//            moviesViewModel.getFavoriteMoviesState().removeObservers(this);
//            moviesViewModel.getHighestRatedMoviesState().removeObservers(this);
            moviesViewModel.loadMovies(MoviesViewModel.SORT_CONFIG_POPULARITY);
        } else if (item.getItemId() == R.id.menu_item_favorites) {
            sortConfig = SORT_CONFIG_FAVORITES;
            moviesViewModel.getFavoriteMoviesState().observe(this, adapter::setItems);
//            moviesViewModel.getPopularMoviesState().removeObservers(this);
//            moviesViewModel.getHighestRatedMoviesState().removeObservers(this);
            moviesViewModel.loadMovies(MoviesViewModel.SORT_CONFIG_FAVORITES);
        }
        return super.onOptionsItemSelected(item);
    }
}

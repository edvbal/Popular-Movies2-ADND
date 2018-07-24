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

import butterknife.BindView;

public class MoviesFragment extends BaseFragment {
    private static final String KEY_SORT_CONFIG = "key.sortConfig";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private MoviesAdapter adapter;
    private MoviesViewModel moviesViewModel;
    private Notifier notifier;
    private String sortConfig;

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
        if (sortConfig != null) {
            outState.putString(KEY_SORT_CONFIG, sortConfig);
        } else {
            outState.putString(KEY_SORT_CONFIG, MoviesViewModel.DEFAULT_SORT_CONFIG);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            sortConfig = savedInstanceState.getString(KEY_SORT_CONFIG);
            if (sortConfig != null) {
                switch (sortConfig) {
                    case MoviesViewModel.SORT_CONFIG_RATINGS:
                        observeRatingsState();
                        break;
                    case MoviesViewModel.SORT_CONFIG_POPULARITY:
                        observePopularityState();
                        break;
                    case MoviesViewModel.SORT_CONFIG_FAVORITES:
                        observeFavoritesState();
                        break;
                }
            }
        } else {
            moviesViewModel.getPopularMoviesState().observe(this, adapter::setItems);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_highest_rated) {
            sortConfig = MoviesViewModel.SORT_CONFIG_RATINGS;
            observeRatingsState();
            moviesViewModel.loadMovies(sortConfig);
        } else if (item.getItemId() == R.id.menu_item_most_popular) {
            sortConfig = MoviesViewModel.SORT_CONFIG_POPULARITY;
            observePopularityState();
            moviesViewModel.loadMovies(sortConfig);
        } else if (item.getItemId() == R.id.menu_item_favorites) {
            sortConfig = MoviesViewModel.SORT_CONFIG_FAVORITES;
            observeFavoritesState();
            moviesViewModel.loadMovies(sortConfig);
        }
        return super.onOptionsItemSelected(item);
    }

    private void observeFavoritesState() {
        moviesViewModel.getPopularMoviesState().removeObservers(this);
        moviesViewModel.getHighestRatedMoviesState().removeObservers(this);
        moviesViewModel.getFavoriteMoviesState().observe(this, adapter::setItems);
    }

    private void observeRatingsState() {
        moviesViewModel.getPopularMoviesState().removeObservers(this);
        moviesViewModel.getFavoriteMoviesState().removeObservers(this);
        moviesViewModel.getHighestRatedMoviesState().observe(this, adapter::setItems);
    }

    private void observePopularityState() {
        moviesViewModel.getHighestRatedMoviesState().removeObservers(this);
        moviesViewModel.getFavoriteMoviesState().removeObservers(this);
        moviesViewModel.getPopularMoviesState().observe(this, adapter::setItems);
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
}
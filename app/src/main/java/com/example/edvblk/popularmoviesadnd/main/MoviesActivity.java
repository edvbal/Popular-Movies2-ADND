package com.example.edvblk.popularmoviesadnd.main;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.edvblk.popularmoviesadnd.R;
import com.example.edvblk.popularmoviesadnd.base.BaseActivity;
import com.example.edvblk.popularmoviesadnd.details.MovieDetailsActivity;
import com.example.edvblk.popularmoviesadnd.utils.ErrorProvider;
import com.example.edvblk.popularmoviesadnd.utils.ErrorProviderImpl;
import com.example.edvblk.popularmoviesadnd.utils.architecture.ViewModelState;
import com.example.edvblk.popularmoviesadnd.utils.image.DefaultImageUrlProvider;
import com.example.edvblk.popularmoviesadnd.utils.image.GlideImageLoader;

import butterknife.BindView;

public class MoviesActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private MoviesAdapter adapter;
    private MoviesViewModel moviesViewModel;
    private ErrorProvider errorProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFields();
        setSupportActionBar(toolbar);
        moviesViewModel.getState().observe(this, viewModelState -> {
            if (viewModelState instanceof ViewModelState.ErrorState) {
                showError((ViewModelState.ErrorState) viewModelState);
            } else if (viewModelState instanceof ViewModelState.LoadingState) {
                showLoadingBar((ViewModelState.LoadingState) viewModelState);
            } else if (viewModelState instanceof MoviesViewModel.MoviesListState) {
                showItems((MoviesViewModel.MoviesListState) viewModelState);
            } else if (viewModelState instanceof MoviesViewModel.MovieDetailsState) {
                showDetailsScreen((MoviesViewModel.MovieDetailsState) viewModelState);
            }
        });
        moviesViewModel.loadMovies();
    }

    private void showLoadingBar(ViewModelState.LoadingState loadingState) {
        if (loadingState.isLoading()) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }

    }

    private void showDetailsScreen(MoviesViewModel.MovieDetailsState detailsState) {
        MovieDetailsActivity.start(this, detailsState.getMovie());
    }

    private void showItems(MoviesViewModel.MoviesListState viewModelState) {
        adapter.setItems(viewModelState.getMovies());
    }

    private void showError(ViewModelState.ErrorState errorState) {
        errorProvider.showError(errorState.getErrorMessage());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private void initFields() {
        errorProvider = new ErrorProviderImpl(recyclerView);
        initViewModel();
        initAdapter();
        initRecycler();
    }

    private void initViewModel() {
        MoviesViewModelFactory factory = new MoviesViewModelFactory(this);
        moviesViewModel = ViewModelProviders.of(this, factory).get(MoviesViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_highest_rated) {
            moviesViewModel.loadHighestRated();
        } else if (item.getItemId() == R.id.menu_item_most_popular) {
            moviesViewModel.loadMostPopular();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initAdapter() {
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        DefaultImageUrlProvider urlProvider = new DefaultImageUrlProvider(widthPixels);
        adapter = new MoviesAdapter(
                new GlideImageLoader(this),
                urlProvider,
                item -> moviesViewModel.onItemSelected(item));
    }

    private void initRecycler() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }
}
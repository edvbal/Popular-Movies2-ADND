package com.example.edvblk.popularmoviesadnd.main;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.edvblk.popularmoviesadnd.R;
import com.example.edvblk.popularmoviesadnd.details.MovieDetailsActivity;
import com.example.edvblk.popularmoviesadnd.utils.ErrorProvider;
import com.example.edvblk.popularmoviesadnd.utils.ErrorProviderImpl;
import com.example.edvblk.popularmoviesadnd.utils.image.DefaultImageUrlProvider;
import com.example.edvblk.popularmoviesadnd.utils.image.GlideImageLoader;
import com.example.edvblk.popularmoviesadnd.utils.mvvm.ViewModelEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.edvblk.popularmoviesadnd.main.MoviesViewModel.KEY_SORT_DEFAULT;

public class MoviesFragment extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private MoviesAdapter adapter;
    private MoviesViewModel moviesViewModel;
    private ErrorProvider errorProvider;
    private String selectedSortId = KEY_SORT_DEFAULT;
    private Unbinder unbinder;
    private static final String KEY_SORT = "key.sort";
    private static final String KEY_LIST_STATE = "key.listState";

    public static Fragment newInstance() {
        return new MoviesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        initViewModel();
        initAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        errorProvider = new ErrorProviderImpl(view);
        moviesViewModel.getEvent().observe(this, viewModelEvent -> {
            if (viewModelEvent instanceof ViewModelEvent.ErrorEvent) {
                showError((ViewModelEvent.ErrorEvent) viewModelEvent);
            } else if (viewModelEvent instanceof ViewModelEvent.LoadingEvent) {
                showLoadingBar((ViewModelEvent.LoadingEvent) viewModelEvent);
            } else if (viewModelEvent instanceof MoviesViewModel.MoviesListEvent) {
                showItems((MoviesViewModel.MoviesListEvent) viewModelEvent);
            } else if (viewModelEvent instanceof MoviesViewModel.MovieDetailsEvent) {
                showDetailsScreen((MoviesViewModel.MovieDetailsEvent) viewModelEvent);
            }
        });
        initRecycler();
        if (savedInstanceState != null) {
            selectedSortId = savedInstanceState.getString(KEY_SORT);
        } else {
            moviesViewModel.loadMovies(selectedSortId);
        }

    }

    private void initFields() {
        initViewModel();
        initAdapter();
        initRecycler();
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
                item -> moviesViewModel.onItemSelected(item));
    }

    private void initRecycler() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_SORT, selectedSortId);
        Parcelable recyclerState = recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(KEY_LIST_STATE, recyclerState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState == null) {
            return;
        }
        restoreSelectedSortState(savedInstanceState);
        restoreListState(savedInstanceState);
    }


    private void restoreSelectedSortState(Bundle outState) {
        String keyState = outState.getString(KEY_SORT);
        selectedSortId = keyState != null ? keyState : KEY_SORT_DEFAULT;
    }

    private void restoreListState(Bundle outState) {
        Parcelable listState = outState.getParcelable(KEY_LIST_STATE);
        if (listState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    private void showLoadingBar(ViewModelEvent.LoadingEvent loadingState) {
        if (loadingState.isLoading()) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }

    }

    private void showDetailsScreen(MoviesViewModel.MovieDetailsEvent detailsState) {
        MovieDetailsActivity.start(getContext(), detailsState.getMovie());
    }

    private void showItems(MoviesViewModel.MoviesListEvent viewModelState) {
        adapter.setItems(viewModelState.getMovies());
    }

    private void showError(ViewModelEvent.ErrorEvent errorState) {
        errorProvider.showError(errorState.getErrorMessage());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_highest_rated) {
            moviesViewModel.loadMovies(MoviesViewModel.KEY_SORT_RATING);
        } else if (item.getItemId() == R.id.menu_item_most_popular) {
            moviesViewModel.loadMovies(MoviesViewModel.KEY_SORT_POPULARITY);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}

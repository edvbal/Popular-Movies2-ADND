package com.example.edvblk.popularmoviesadnd.details;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.edvblk.popularmoviesadnd.data.pojos.Movie;
import com.example.edvblk.popularmoviesadnd.R;
import com.example.edvblk.popularmoviesadnd.base.BaseActivity;
import com.example.edvblk.popularmoviesadnd.data.pojos.MovieReview;
import com.example.edvblk.popularmoviesadnd.data.pojos.MovieTrailer;
import com.example.edvblk.popularmoviesadnd.details.recycler.OnTrailerClick;
import com.example.edvblk.popularmoviesadnd.details.recycler.ReviewAdapter;
import com.example.edvblk.popularmoviesadnd.details.recycler.TrailerAdapter;
import com.example.edvblk.popularmoviesadnd.utils.Notifier;
import com.example.edvblk.popularmoviesadnd.utils.NotifierImpl;
import com.example.edvblk.popularmoviesadnd.utils.image.DefaultImageUrlProvider;
import com.example.edvblk.popularmoviesadnd.utils.image.GlideImageLoader;
import com.example.edvblk.popularmoviesadnd.utils.image.ImageUrlProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MovieDetailsActivity extends BaseActivity implements OnTrailerClick {
    public static final String INTENT_EXTRA_KEY_MOVIE = "key.movies";
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.posterImageView)
    ImageView posterImageView;
    @BindView(R.id.favoriteImageView)
    ImageView favoriteImageView;
    @BindView(R.id.overviewTextView)
    TextView overviewTextView;
    @BindView(R.id.releaseDateTextView)
    TextView releaseDateTextView;
    @BindView(R.id.averageVoteTextView)
    TextView averageVoteTextView;
    @BindView(R.id.reviewsProgressBar)
    ProgressBar reviewsProgressBar;
    @BindView(R.id.trailersProgressBar)
    ProgressBar trailersProgressBar;
    @BindView(R.id.reviewsRecyclerView)
    RecyclerView reviewsRecyclerView;
    @BindView(R.id.trailersRecyclerView)
    RecyclerView trailersRecyclerView;
    private GlideImageLoader imageLoader;
    private MovieDetailsViewModel detailsViewModel;
    private Notifier notifier;
    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;

    public static void start(Context context, Movie movie) {
        Intent starter = new Intent(context, MovieDetailsActivity.class);
        starter.putExtra(INTENT_EXTRA_KEY_MOVIE, movie);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        setHomeAsUp();
        initFields();
        detailsViewModel.getMovieDetailsState().observe(this, this::showMovieDetails);
        detailsViewModel.getErrorState().observe(this, notifier::showError);
        detailsViewModel.getFavoriteState().observe(this, notifier::showSuccess);
        detailsViewModel.getFavoriteImageState().observe(this, this::setFavoriteImageView);
        detailsViewModel.getReviewProgressState().observe(this, this::setReviewProgress);
        detailsViewModel.getTrailersProgressState().observe(this, this::setTrailersProgress);
        detailsViewModel.getMovieReviewsState().observe(this, reviewAdapter::setItems);
        detailsViewModel.getMovieTrailersState().observe(this, trailerAdapter::setItems);
        Bundle extras = getIntent().getExtras();
        if (extras != null && savedInstanceState == null) {
            detailsViewModel.onMovieSelected((Movie) extras.get(INTENT_EXTRA_KEY_MOVIE));
        }
    }

    private void setTrailersProgress(Boolean isProgress) {
        setProgress(isProgress, trailersProgressBar);
    }

    private void setReviewProgress(Boolean isProgress) {
        setProgress(isProgress, reviewsProgressBar);
    }

    private void setProgress(Boolean isProgress, ProgressBar progressBar) {
        if (isProgress) {
            progressBar.setProgress(View.VISIBLE);
        } else {
            progressBar.setProgress(View.GONE);
        }
    }

    private void setFavoriteImageView(Boolean isFavorited) {
        if (isFavorited) {
            favoriteImageView.setBackground(getDrawable(R.drawable.ic_favorite));
            favoriteImageView.setVisibility(View.VISIBLE);
        } else {
            favoriteImageView.setBackground(getDrawable(R.drawable.ic_favorite_border));
            favoriteImageView.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.favoriteImageView)
    public void onFavoriteClick() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            detailsViewModel.onFavoriteSelected((Movie) extras.get(INTENT_EXTRA_KEY_MOVIE));
        }
    }

    private void initFields() {
        imageLoader = new GlideImageLoader(this);
        notifier = new NotifierImpl(posterImageView);
        MovieDetailsViewModelFactory viewModelFactory = new MovieDetailsViewModelFactory(this);
        detailsViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MovieDetailsViewModel.class);
        setReviewRecycler();
        setTrailerRecycler();
    }

    private void setTrailerRecycler() {
        trailerAdapter = new TrailerAdapter(this);
        trailersRecyclerView.setHasFixedSize(true);
        trailersRecyclerView.setAdapter(trailerAdapter);
    }

    private void setReviewRecycler() {
        reviewAdapter = new ReviewAdapter();
        reviewsRecyclerView.setHasFixedSize(true);
        reviewsRecyclerView.setAdapter(reviewAdapter);
    }

    private void showMovieDetails(Movie movie) {
        setImageViewFromUrl(movie.getPosterPath());
        overviewTextView.setText(movie.getOverview());
        releaseDateTextView.setText(movie.getReleaseDate());
        averageVoteTextView.setText(String.valueOf(movie.getAverageVote()));
        collapsingToolbar.setTitle(movie.getTitle());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_details;
    }

    private void setImageViewFromUrl(String url) {
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        ImageUrlProvider provider = new DefaultImageUrlProvider(widthPixels);
        imageLoader.loadImageFromUrl(posterImageView, provider.provideUrl(url));
    }

    @Override
    public void onTrailerClicked(int trailerPosition) {
        MovieTrailer trailer = trailerAdapter.getItemAt(trailerPosition);
        startActivity(new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey())));
    }
}
package com.example.edvblk.popularmoviesadnd.main;

import android.view.View;
import android.widget.ImageView;

import com.example.edvblk.popularmoviesadnd.R;
import com.example.edvblk.popularmoviesadnd.base.BaseViewHolder;
import com.example.edvblk.popularmoviesadnd.utils.ItemClickListener;
import com.example.edvblk.popularmoviesadnd.utils.image.ImageLoader;
import com.example.edvblk.popularmoviesadnd.utils.image.ImageUrlProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoviesViewHolder extends BaseViewHolder<Movie> {
    @BindView(R.id.imageView)
    ImageView imageView;
    private final ImageLoader imageLoader;
    private final ImageUrlProvider imageUrlProvider;
    private final ItemClickListener<Movie> listener;

    MoviesViewHolder(
            View itemView,
            ImageLoader imageLoader,
            ImageUrlProvider imageUrlProvider,
            ItemClickListener<Movie> listener
    ) {
        super(itemView);
        this.imageLoader = imageLoader;
        this.imageUrlProvider = imageUrlProvider;
        this.listener = listener;
        ButterKnife.bind(this, itemView);
    }

    public void onBind(Movie movie) {
        String posterPath = movie.getPosterPath();
        imageLoader.loadImageFromUrl(imageView, imageUrlProvider.provideUrl(posterPath));
    }

    @Override
    protected void onRecycle() {
        imageLoader.stopLoading(imageView);
    }

    @OnClick(R.id.imageView)
    public void onImageClick() {
        listener.onItemClick(getItem());
    }
}

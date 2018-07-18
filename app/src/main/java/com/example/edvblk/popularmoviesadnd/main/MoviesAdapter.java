package com.example.edvblk.popularmoviesadnd.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.edvblk.popularmoviesadnd.R;
import com.example.edvblk.popularmoviesadnd.base.BaseAdapter;
import com.example.edvblk.popularmoviesadnd.utils.ItemClickListener;
import com.example.edvblk.popularmoviesadnd.utils.image.ImageLoader;
import com.example.edvblk.popularmoviesadnd.utils.image.ImageUrlProvider;

public class MoviesAdapter extends BaseAdapter<Movie> {
    private final ImageLoader imageLoader;
    private final ImageUrlProvider imageUrlProvider;
    private ItemClickListener<Movie> listener;

    public MoviesAdapter(
            ImageLoader imageLoader,
            ImageUrlProvider imageUrlProvider,
            ItemClickListener<Movie> listener
    ) {
        this.imageLoader = imageLoader;
        this.imageUrlProvider = imageUrlProvider;
        this.listener = listener;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_movie, parent, false);
        return new MoviesViewHolder(itemView, imageLoader, imageUrlProvider, listener);
    }
}

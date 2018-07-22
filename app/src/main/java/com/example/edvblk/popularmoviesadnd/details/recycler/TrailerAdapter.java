package com.example.edvblk.popularmoviesadnd.details.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.edvblk.popularmoviesadnd.R;
import com.example.edvblk.popularmoviesadnd.base.BaseAdapter;
import com.example.edvblk.popularmoviesadnd.base.BaseViewHolder;
import com.example.edvblk.popularmoviesadnd.data.pojos.MovieTrailer;
import com.example.edvblk.popularmoviesadnd.details.MovieDetailsActivity;

public class TrailerAdapter extends BaseAdapter<MovieTrailer> {
    private final OnTrailerClick onTrailerClickListener;

    public TrailerAdapter(OnTrailerClick onTrailerClickListener) {
        this.onTrailerClickListener = onTrailerClickListener;
    }

    @Override
    public BaseViewHolder<MovieTrailer> onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_trailer, parent, false);
        return new TrailerViewHolder(itemView, onTrailerClickListener);
    }
}

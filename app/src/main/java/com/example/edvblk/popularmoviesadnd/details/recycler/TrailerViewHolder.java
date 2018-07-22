package com.example.edvblk.popularmoviesadnd.details.recycler;

import android.view.View;
import android.widget.TextView;

import com.example.edvblk.popularmoviesadnd.R;
import com.example.edvblk.popularmoviesadnd.base.BaseViewHolder;
import com.example.edvblk.popularmoviesadnd.data.pojos.MovieTrailer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailerViewHolder extends BaseViewHolder<MovieTrailer> {
    @BindView(R.id.titleTextView)
    TextView titleTextView;
    private final OnTrailerClick onTrailerClickListener;

    public TrailerViewHolder(View itemView, OnTrailerClick onTrailerClickListener) {
        super(itemView);
        this.onTrailerClickListener = onTrailerClickListener;
        ButterKnife.bind(this, itemView);
    }

    @Override
    protected void onBind(MovieTrailer item) {
        super.onBind(item);
        titleTextView.setText(item.getName());
        titleTextView.setOnClickListener(view ->
                onTrailerClickListener.onTrailerClicked(getAdapterPosition())
        );

    }
}

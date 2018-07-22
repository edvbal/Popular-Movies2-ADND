package com.example.edvblk.popularmoviesadnd.details.recycler;

import android.view.View;
import android.widget.TextView;

import com.example.edvblk.popularmoviesadnd.R;
import com.example.edvblk.popularmoviesadnd.base.BaseViewHolder;
import com.example.edvblk.popularmoviesadnd.data.pojos.MovieReview;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

class ReviewViewHolder extends BaseViewHolder<MovieReview> {
    @BindView(R.id.reviewTextView)
    TextView reviewTextView;
    @BindView(R.id.authorTextView)
    TextView authorTextView;

    ReviewViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    protected void onBind(MovieReview review) {
        super.onBind(review);
        reviewTextView.setText(review.getContent());
        authorTextView.setText(review.getAuthor());
    }
}

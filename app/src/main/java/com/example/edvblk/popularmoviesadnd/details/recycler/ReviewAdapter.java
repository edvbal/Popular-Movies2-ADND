package com.example.edvblk.popularmoviesadnd.details.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.edvblk.popularmoviesadnd.R;
import com.example.edvblk.popularmoviesadnd.base.BaseAdapter;
import com.example.edvblk.popularmoviesadnd.base.BaseViewHolder;
import com.example.edvblk.popularmoviesadnd.data.pojos.MovieReview;

public class ReviewAdapter extends BaseAdapter<MovieReview>{
    @Override
    public BaseViewHolder<MovieReview> onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(itemView);
    }

}

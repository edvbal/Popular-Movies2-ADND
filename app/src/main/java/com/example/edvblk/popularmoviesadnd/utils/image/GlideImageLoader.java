package com.example.edvblk.popularmoviesadnd.utils.image;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

public class GlideImageLoader implements ImageLoader {
    private final RequestManager requestManager;

    public GlideImageLoader(Context context) {
        requestManager = Glide.with(context);
    }

    @Override
    public void loadImageFromUrl(View view, String url) {
        requestManager
                .load(url)
                .into((ImageView) view);
    }

    @Override
    public void stopLoading(View view) {
        requestManager.clear(view);
    }
}

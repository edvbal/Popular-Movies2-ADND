package com.example.edvblk.popularmoviesadnd.utils.image;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

public class GlideImageLoader implements ImageLoader {
    private final RequestManager requestManager;

    public GlideImageLoader(Context context) {
        requestManager = Glide.with(context);
    }

    @Override
    public void loadImageFromUrl(View view, String url) {
        requestManager
                .load(url)
                .apply(new RequestOptions().override(Target.SIZE_ORIGINAL))
                .into((ImageView) view);
    }

    @Override
    public void stopLoading(View view) {
        requestManager.clear(view);
    }
}

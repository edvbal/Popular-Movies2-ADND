package com.example.edvblk.popularmoviesadnd.utils;

import android.content.res.Resources;

import com.example.edvblk.popularmoviesadnd.R;

public class MessagesProviderImpl implements MessagesProvider {
    private final Resources resources;

    public MessagesProviderImpl(Resources resources) {
        this.resources = resources;
    }

    @Override
    public String provideNetworkErrorMessage() {
        return resources.getString(R.string.error_no_network);
    }

    @Override
    public String provideEmptyMoviesListMessage() {
        return resources.getString(R.string.error_empty_movies_list);
    }

    @Override
    public String provideEmptyMovieDetailsMessage() {
        return resources.getString(R.string.error_empty_movies_list);
    }
}

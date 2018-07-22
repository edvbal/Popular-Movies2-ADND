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

    @Override
    public String provideRepoWriteFailureMessage() {
        return resources.getString(R.string.error_repository_write_failure);
    }

    @Override
    public String provideRepoReadFailureMessage() {
        return resources.getString(R.string.error_repository_read_failure);
    }

    @Override
    public String provideRepoWriteSuccessMessage() {
        return resources.getString(R.string.error_repository_write_success);
    }
    @Override
    public String provideMovieAlreadyFavoriteMessage() {
        return resources.getString(R.string.error_repository_write_success);
    }

    @Override
    public String provideMovieTrailersLoadError() {
        return resources.getString(R.string.error_could_not_load_trailers);
    }

    @Override
    public String provideMovieReviewsLoadError() {
        return resources.getString(R.string.error_could_not_load_reviews);
    }
}

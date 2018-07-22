package com.example.edvblk.popularmoviesadnd.utils;

public interface MessagesProvider {
    String provideNetworkErrorMessage();

    String provideEmptyMoviesListMessage();

    String provideEmptyMovieDetailsMessage();

    String provideRepoWriteFailureMessage();

    String provideRepoReadFailureMessage();

    String provideRepoWriteSuccessMessage();

    String provideMovieAlreadyFavoriteMessage();

    String provideMovieTrailersLoadError();

    String provideMovieReviewsLoadError();
}

package com.example.edvblk.popularmoviesadnd.details;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.edvblk.popularmoviesadnd.utils.MessagesProviderImpl;

public class MovieDetailsViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public MovieDetailsViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    public MovieDetailsViewModel create(@NonNull Class modelClass) {
        return new MovieDetailsViewModel(new MessagesProviderImpl(context.getResources()));
    }
}

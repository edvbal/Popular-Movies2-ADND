package com.example.edvblk.popularmoviesadnd.utils;

import android.support.annotation.NonNull;

public interface ViewConsumer<T> {
    void accept(@NonNull T view);
}

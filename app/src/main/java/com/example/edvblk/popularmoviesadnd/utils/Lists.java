package com.example.edvblk.popularmoviesadnd.utils;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class Lists {
    private Lists() {
    }

    public static <T, R> List<R> map(
            @NonNull Collection<T> source,
            @NonNull Mapper<T, R> mapper
    ) {
        return mapTo(source, new ArrayList<>(source.size()), mapper);
    }

    public static <T, R, V extends Collection<R>> V mapTo(
            @NonNull Collection<T> source,
            @NonNull V destination,
            @NonNull Mapper<T, R> mapper
    ) {
        for (T item : source) {
            destination.add(mapper.apply(item));
        }
        return destination;
    }
}

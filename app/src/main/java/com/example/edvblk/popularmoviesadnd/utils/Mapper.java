package com.example.edvblk.popularmoviesadnd.utils;

import java.util.Objects;

import io.reactivex.functions.Function;

public interface Mapper<T, R> extends Function<T, R> {
    @SuppressWarnings({"PMD.ShortVariable", "ParameterName"})
    R apply(T t);

    @SuppressWarnings({"PMD.ShortVariable", "ParameterName"})
    static <T> Mapper<T, T> identity() {
        return t -> t;
    }

    @SuppressWarnings({"PMD.ShortVariable", "ParameterName"})
    default <V> Mapper<T, V> andThen(Mapper<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }
}

package com.example.edvblk.popularmoviesadnd.base;

public interface BasePresenter<T> {
    void takeView(T view);

    void dropView();
}

package com.example.edvblk.popularmoviesadnd.utils.network;

public class MoviesResultResponse<T> {
    private T results;

    public MoviesResultResponse(T results) {
        this.results = results;
    }

    public T getResult() {
        return results;
    }
}

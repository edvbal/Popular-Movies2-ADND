package com.example.edvblk.popularmoviesadnd.base;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import com.example.edvblk.popularmoviesadnd.utils.ViewConsumer;

public class BasePresenterImpl<T> implements BasePresenter<T> {
    @Nullable
    private T view;

    @CallSuper
    @Override
    public void takeView(T view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        view = null;
    }

    protected final void onView(ViewConsumer<T> consumer) {
        if (view != null) {
            consumer.accept(view);
        }
    }
}

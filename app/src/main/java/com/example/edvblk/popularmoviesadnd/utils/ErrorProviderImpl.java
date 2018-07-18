package com.example.edvblk.popularmoviesadnd.utils;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.example.edvblk.popularmoviesadnd.R;

public class ErrorProviderImpl implements ErrorProvider {
    private final int color;
    private final View view;

    public ErrorProviderImpl(View view) {
        color = ContextCompat.getColor(view.getContext(), R.color.error_red);
        this.view = view;
    }

    @Override
    public void showError(String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(color);
        snackbar.show();
    }
}

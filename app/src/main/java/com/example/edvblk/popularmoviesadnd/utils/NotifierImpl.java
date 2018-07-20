package com.example.edvblk.popularmoviesadnd.utils;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.example.edvblk.popularmoviesadnd.R;

public class NotifierImpl implements Notifier {
    private final int errorColor;
    private final int successColor;
    private final View view;

    public NotifierImpl(View view) {
        errorColor = ContextCompat.getColor(view.getContext(), R.color.error_red);
        successColor = ContextCompat.getColor(view.getContext(), R.color.success_green);
        this.view = view;
    }

    @Override
    public void showError(String message) {
        showSnackbar(message, errorColor);
    }

    @Override
    public void showSuccess(String message) {
        showSnackbar(message, successColor);
    }

    private void showSnackbar(String message, int color) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(color);
        snackbar.show();
    }
}

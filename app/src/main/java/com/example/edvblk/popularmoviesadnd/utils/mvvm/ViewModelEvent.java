package com.example.edvblk.popularmoviesadnd.utils.mvvm;

public class ViewModelEvent {
    public static final class LoadingEvent extends ViewModelEvent {
        private final boolean isLoading;

        public LoadingEvent(boolean isLoading) {
            this.isLoading = isLoading;
        }

        public boolean isLoading() {
            return isLoading;
        }
    }

    public static final class ErrorEvent extends ViewModelEvent {
        private final String errorMessage;

        public ErrorEvent(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}

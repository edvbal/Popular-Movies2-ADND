package com.example.edvblk.popularmoviesadnd.utils.architecture;

public class ViewModelState {
    public static final class LoadingState extends ViewModelState {
        private final boolean isLoading;

        public LoadingState(boolean isLoading) {
            this.isLoading = isLoading;
        }

        public boolean isLoading() {
            return isLoading;
        }
    }

    public static final class ErrorState extends ViewModelState {
        private final String errorMessage;

        public ErrorState(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}

package com.example.edvblk.popularmoviesadnd.utils.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class DefaultInternetChecker implements InternetChecker {
    private final ConnectivityManager connectivityManager;

    public DefaultInternetChecker(Context context) {
        Object systemService = context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager = (ConnectivityManager) systemService;
    }

    @Override
    public boolean isInternetAvailable() {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}

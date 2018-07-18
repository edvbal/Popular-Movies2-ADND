package com.example.edvblk.popularmoviesadnd.utils.image;

import com.example.edvblk.popularmoviesadnd.BuildConfig;

public class DefaultImageUrlProvider implements ImageUrlProvider {
    private final int width;

    public DefaultImageUrlProvider(int width) {
        this.width = width;
    }

    @Override
    public String provideUrl(String posterPath) {
        String widthPath;
        if (width <= 92)
            widthPath = "/w92";
        else if (width <= 154)
            widthPath = "/w154";
        else if (width <= 185)
            widthPath = "/w185";
        else if (width <= 342)
            widthPath = "/w342";
        else if (width <= 500)
            widthPath = "/w500";
        else
            widthPath = "/w780";
        return BuildConfig.IMAGES_BASE_URL + widthPath + posterPath;
    }
}

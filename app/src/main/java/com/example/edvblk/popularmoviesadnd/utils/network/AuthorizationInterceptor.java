package com.example.edvblk.popularmoviesadnd.utils.network;

import android.support.annotation.NonNull;

import com.example.edvblk.popularmoviesadnd.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class AuthorizationInterceptor implements Interceptor {
    private static final String AUTHORIZATION_PARAMETER = "api_key";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        HttpUrl urlWithAuthorization = getUrlWithAuthorizationParameter(originalRequest);
        Request request = getRequestWithAuthorization(chain, urlWithAuthorization);
        return chain.proceed(request);
    }

    private Request getRequestWithAuthorization(Chain chain, HttpUrl urlWithAuthorization) {
        return chain.request().newBuilder()
                .url(urlWithAuthorization)
                .build();
    }

    @NonNull
    private HttpUrl getUrlWithAuthorizationParameter(Request originalRequest) {
        return originalRequest.url()
                .newBuilder()
                .setQueryParameter(AUTHORIZATION_PARAMETER, BuildConfig.API_KEY)
                .build();
    }
}

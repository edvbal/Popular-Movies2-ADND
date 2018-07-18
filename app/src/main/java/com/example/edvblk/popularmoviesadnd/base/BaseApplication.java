package com.example.edvblk.popularmoviesadnd.base;

import android.app.Application;
import android.content.Context;

import com.example.edvblk.popularmoviesadnd.utils.network.RetrofitFactory;

import retrofit2.Retrofit;

public class BaseApplication extends Application {
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();
        retrofit = new RetrofitFactory().create();
    }

    private Retrofit getRetrofit() {
        return retrofit;
    }

    public static Retrofit getRetrofit(Context context) {
        return getBaseApplication(context).getRetrofit();
    }

    private static BaseApplication getBaseApplication(Context context) {
        return ((BaseApplication) context.getApplicationContext());
    }
}

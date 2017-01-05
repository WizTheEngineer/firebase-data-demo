package com.waynejackson.firebasedatademo;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by wayne.jackson on 1/5/17.
 */

public class TopicsApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}

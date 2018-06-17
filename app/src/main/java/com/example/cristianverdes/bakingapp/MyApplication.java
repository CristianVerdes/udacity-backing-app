package com.example.cristianverdes.bakingapp;

import android.app.Application;

/**
 * Created by cristian.verdes on 15.03.2018.
 */

public class MyApplication extends Application {
    private static MyApplication application;

    public static MyApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }
}

package com.example.dpt.pulltorefreshdemo;

import android.app.Application;
import android.content.Context;

/**
 * Created by dupengtao on 15/6/2.
 */
public class DemoListApp extends Application{

    private static Context instances;

    @Override
    public void onCreate() {
        super.onCreate();
        instances =this;
    }

    public static Context getInstances() {
        return instances;
    }
}

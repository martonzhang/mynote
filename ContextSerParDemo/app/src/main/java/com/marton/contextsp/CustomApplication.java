package com.marton.contextsp;

import android.app.Application;
import android.content.Context;

/**
 * Created by marton on 16/4/2.
 */
public class CustomApplication extends Application{

    private static Context mGlobalContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mGlobalContext = getApplicationContext();
    }

    public static Context getmGlobalContext(){
        return mGlobalContext;
    }
}


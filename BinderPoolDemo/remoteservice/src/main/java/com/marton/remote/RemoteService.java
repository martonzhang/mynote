package com.marton.remote;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.test.ApplicationTestCase;

/**
 * Created by marton on 16/4/25.
 */
public class RemoteService extends Service {

    

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
     */
    public static class ApplicationTest extends ApplicationTestCase<Application> {
        public ApplicationTest() {
            super(Application.class);
        }
    }
}

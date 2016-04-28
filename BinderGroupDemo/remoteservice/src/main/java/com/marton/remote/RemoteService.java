package com.marton.remote;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.marton.aidl.IBinderPool;

/**
 * Created by marton on 16/4/25.
 */
public class RemoteService extends Service {

    private static final String TAG = "MainActivity";

    private BinderPoolImpl mBinderPoolImpl = new BinderPoolImpl();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"onBind RemoteSerivce");
        return mBinderPoolImpl;
    }


}

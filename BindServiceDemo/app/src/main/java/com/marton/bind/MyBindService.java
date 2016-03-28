package com.marton.bind;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by marton on 16/3/27.
 */
public class MyBindService extends Service {
    public MyBinder mMyBinder = new MyBinder();

    class MyBinder extends Binder{
        public int add(int a,int b){
            return a + b;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMyBinder;
    }

}

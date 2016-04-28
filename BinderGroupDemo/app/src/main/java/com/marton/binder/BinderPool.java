package com.marton.binder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.marton.aidl.IBinderPool;

import java.util.concurrent.CountDownLatch;

/**
 * Created by marton on 16/4/25.
 */
public class BinderPool {

    private static final String TAG = "MainActivity";

    private Context mContext;
    private IBinderPool mBinderPool;

    //加volatile，是避免高并发下，DCL的不安全性
    private static volatile BinderPool sInstance;

    private CountDownLatch mCDLatch;

    private ServiceConnection mSConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG,"onServiceConnected BinderPool");
            mBinderPool = IBinderPool.Stub.asInterface(service);
            try{
                mBinderPool.asBinder().linkToDeath(mBinderDeathRecipient,0);
            }catch (RemoteException e){
                e.printStackTrace();
            }
            mCDLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IBinder.DeathRecipient mBinderDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            mBinderPool.asBinder().unlinkToDeath(mBinderDeathRecipient,0);
            mBinderPool = null;
            bindService();
        }
    };

    private BinderPool(Context context){
        mContext = context;
        bindService();
    }

    public static BinderPool getsInstance(Context context){
        if (sInstance == null){
            synchronized (BinderPool.class){
                if (sInstance == null){
                    sInstance = new BinderPool(context);
                }
            }
        }
        return sInstance;
    }

    private void bindService(){
        Log.i(TAG,"bindService BinderPool");
        mCDLatch = new CountDownLatch(1);
        Intent intent = new Intent("android.intent.action.remoteservice");
        mContext.bindService(intent,mSConnection,Context.BIND_AUTO_CREATE);
        //异步转同步，由于Demo里美噢雨提前初始化的场景，调用proxy时，可能还没建立链接，这里改称同步，保证链接的建立
        try{
            mCDLatch.await();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public IBinder queryBinder(int binderCode){
        IBinder binder = null;
        try{
           if (mBinderPool != null){
               binder = mBinderPool.queryBinder(binderCode);
           }
        }catch(RemoteException e){
            e.printStackTrace();
        }
        return binder;
    }

}

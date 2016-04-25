package com.marton.remote;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.marton.aidl.Book;
import com.marton.aidl.IBookManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by marton on 16/4/16.
 */
public class RemoteService extends Service {

    private static final String TAG = "RemoteDemo";

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<Book>();

    IBookManager.Stub mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            Log.i(TAG,"getBookList in remote service");
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Log.i(TAG,"addBook in remote service : " + book.toString());
            mBookList.add(book);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"onCreate in remote service");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"onBind in remote service");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG,"onUnBind in remote service");
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"onStartCommand in remote service");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"onDestroy in remote service");
        super.onDestroy();
    }
}

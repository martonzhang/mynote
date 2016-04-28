package com.marton.remote;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.marton.aidl.Book;
import com.marton.aidl.IBookManager;
import com.marton.aidl.IOnNewBookArrivedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by marton on 16/4/16.
 */
public class RemoteService extends Service {

    private static final String TAG = "RemoteDemo";

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<Book>();

    private RemoteCallbackList<IOnNewBookArrivedListener> mListeners = new RemoteCallbackList<IOnNewBookArrivedListener>();

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

        @Override
        public void registerListner(IOnNewBookArrivedListener listener) throws RemoteException {
            Log.i(TAG,"registerListner in remote service : ");
            mListeners.register(listener);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            Log.i(TAG,"unregisterListener in remote service : ");
            mListeners.unregister(listener);
        }
    };

    private void onNewBookArrived(Book newBook) throws RemoteException{
        mBookList.add(newBook);
        final int N = mListeners.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IOnNewBookArrivedListener listener = mListeners.getBroadcastItem(i);
            if (listener != null){
                listener.onNewBookArrived(newBook);
            }
        }
        mListeners.finishBroadcast();
    }

    private int count = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"onCreate in remote service");
        new Thread(new ServiceWorker()).start();
    }

    private class ServiceWorker implements Runnable{
        @Override
        public void run() {
            Log.i(TAG,"run onCreate in remote service");
            while (count++ < 5) {
                try{
                    Thread.sleep(5000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                Log.i(TAG,"run : " + count);
                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId,"new book#" + bookId);
                try{
                    onNewBookArrived(newBook);
                }catch(RemoteException e){
                    e.printStackTrace();
                }
            }
        }
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

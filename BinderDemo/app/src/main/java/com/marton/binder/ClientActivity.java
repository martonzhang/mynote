package com.marton.binder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.marton.aidl.IBookManager;
import com.marton.aidl.Book;


import java.util.List;


public class ClientActivity extends Activity implements View.OnClickListener{

    private static final String TAG = "RemoteDemo";

    private Button mBindBtn;
    private Button mUnBindBtn;
    private Button mRemoteExeBtn;

    private IBookManager mBookManager;

    private int mBookId;

    private ServiceConnection mSConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG,"onServiceConnected in activity");
            mBookManager = IBookManager.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG,"onServiceDisconnected in activity");
            mBookManager = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        mBindBtn = (Button)findViewById(R.id.bind_service);
        mUnBindBtn = (Button)findViewById(R.id.unbind_service);
        mRemoteExeBtn = (Button)findViewById(R.id.remote_execute);
        mBindBtn.setOnClickListener(this);
        mUnBindBtn.setOnClickListener(this);
        mRemoteExeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bind_service:
                Intent intent = new Intent("android.intent.action.remoteservice");
                bindService(intent,mSConnection,BIND_AUTO_CREATE);
                break;
            case R.id.unbind_service:
                unbindService(mSConnection);
                break;
            case R.id.remote_execute:
                if(mBookManager != null){
                    Book book = new Book(mBookId,"myBook" + mBookId);
                    try{
                        mBookManager.addBook(book);
                        List<Book> books = mBookManager.getBookList();
                        if(books != null && books.size() > 0){
                            for (int i = 0; i < books.size(); i++) {
                                Log.i(TAG,books.get(i).toString() + "\n");
                            }
                        }
                        mBookId++;
                    }catch (RemoteException e){
                        Toast.makeText(ClientActivity.this, "remoteException", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}

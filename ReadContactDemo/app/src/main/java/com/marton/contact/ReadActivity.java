package com.marton.contact;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;


public class ReadActivity extends Activity implements Handler.Callback{

    private ProgressBar mProgressBar;
    ListView mContactListVeiw;
    ArrayAdapter<String> mAdapter;
    List<String> mContactList = new ArrayList<String>();
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_read);
        mHandler = new Handler(this);
        mContactListVeiw = (ListView)findViewById(R.id.data_list);
        mProgressBar = (ProgressBar)findViewById(R.id.processbar);
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mContactList);
        mContactListVeiw.setAdapter(mAdapter);
        mContactListVeiw.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        readContacts();
    }

    private void readContacts(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = null;
                try {
                    cursor = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
                    while(cursor.moveToNext()){
                        String name = cursor.getString(
                                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String number = cursor.getString(
                                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        mContactList.add(name + "\n" + number);
                    }
                    mHandler.sendMessage(Message.obtain());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(cursor != null){
                        cursor.close();
                    }
                }
            }
        }).start();
    }

    @Override
    public boolean handleMessage(Message msg) {
        mProgressBar.setVisibility(View.GONE);
        mContactListVeiw.setVisibility(View.VISIBLE);
        mAdapter.notifyDataSetInvalidated();
        return true;
    }
}

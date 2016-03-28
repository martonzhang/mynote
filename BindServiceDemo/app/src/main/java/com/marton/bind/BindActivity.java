package com.marton.bind;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class BindActivity extends Activity implements View.OnClickListener{

    private final static String TAG = "BindActivity";

    private Button mBindBtn;
    private Button mUnbindBtn;
    private MyBindService.MyBinder mMyBinder;

    private ServiceConnection mSConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMyBinder = (MyBindService.MyBinder)service;
            if(mMyBinder != null){
                Log.v(TAG, "2 + 5 = " + mMyBinder.add(2,5));
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind);
        mBindBtn = (Button)findViewById(R.id.bind_btn);
        mUnbindBtn = (Button)findViewById(R.id.ubind_btn);
        mBindBtn.setOnClickListener(this);
        mUnbindBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == mBindBtn){
            Intent bindIntent = new Intent(this,MyBindService.class);
            bindService(bindIntent,mSConnection,BIND_AUTO_CREATE);
        }else if(v == mUnbindBtn){
            unbindService(mSConnection);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bind, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

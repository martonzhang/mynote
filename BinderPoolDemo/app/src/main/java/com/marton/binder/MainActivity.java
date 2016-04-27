package com.marton.binder;

import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;

import com.marton.aidl.ICompute;
import com.marton.aidl.ISecurityCenter;
import com.marton.aidl.R;


public class MainActivity extends Activity {

    public static final int BINDER_CODE_NONE = -1;
    public static final int BINDER_CODE_COMPUTE = 0;
    public static final int BINDER_CODE_SECURITY_COUNTER = 1;

    private ISecurityCenter mSecurityCenter;
    private ICompute mCompute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            @Override
            public void run() {
                BinderPool binderPool = BinderPool.getsInstance(MainActivity.this);
                //BinderPool中的同步策略，保证这时连接已经建立了，binderpool一定不为空
                IBinder securityBinder = binderPool.queryBinder(BINDER_CODE_SECURITY_COUNTER);
                mSecurityCenter = ISecurityCenter.Stub.asInterface(securityBinder);
                IBinder computeBinder = binderPool.queryBinder(BINDER_CODE_COMPUTE);
                mCompute = ICompute.Stub.asInterface(computeBinder);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

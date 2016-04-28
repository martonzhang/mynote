package com.marton.binder;

import android.app.Activity;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.marton.aidl.ICompute;
import com.marton.aidl.ISecurityCenter;
import com.marton.aidl.R;


public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

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
                Log.i(TAG,"run onCreate");
                BinderPool binderPool = BinderPool.getsInstance(MainActivity.this);
                //BinderPool中的同步策略，保证这时连接已经建立了，binderpool一定不为空
                IBinder securityBinder = binderPool.queryBinder(BINDER_CODE_SECURITY_COUNTER);
                mSecurityCenter = ISecurityCenter.Stub.asInterface(securityBinder);
                IBinder computeBinder = binderPool.queryBinder(BINDER_CODE_COMPUTE);
                mCompute = ICompute.Stub.asInterface(computeBinder);
                Log.i(TAG,"visit IsecurityCenter");
                String msg = "helloworld-安卓";
                try{
                    String password = mSecurityCenter.encrypt(msg);
                    Log.i(TAG,password);
                    String content = mSecurityCenter.decrypt(password);
                    Log.i(TAG,content);
                    Log.i(TAG,"visit ICompute");
                    int result = mCompute.add(3,5);
                    Log.i(TAG,"3 + 5 = " + result);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        }).start();
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

package marton.messenger;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class ClinenActivity extends Activity {

    private static final String TAG = "ClinenActivity";

    private Messenger mSendMessenger;

    private Messenger mGetReplyMessneger = new Messenger(new MessengerHandler());

    private static class MessengerHandler extends android.os.Handler{

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MessengerService.MSG_TO_CLIENT:
                    String mess =  msg.getData().getString(MessengerService.KEY_TO_CLIENT);
                    Log.i(TAG,"Reply msg from service : " + mess);
                    break;
            }
        }
    }

    private ServiceConnection mServiceConnnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mSendMessenger = new Messenger(service);
            Message msg = Message.obtain();
            msg.what = MessengerService.MSG_FROM_CLIENT;
            Bundle bundle = new Bundle();
            bundle.putString(MessengerService.KEY_FROM_CLIENT,"this is client msg");
            msg.setData(bundle);
            msg.replyTo = mGetReplyMessneger;
            try{
                mSendMessenger.send(msg);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mSendMessenger = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinen);
        Intent intent = new Intent(this,MessengerService.class);
        bindService(intent,mServiceConnnection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnnection);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_clinen, menu);
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

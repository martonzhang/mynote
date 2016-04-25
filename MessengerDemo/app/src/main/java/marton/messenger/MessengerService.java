package marton.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by marton on 16/4/24.
 */
public class MessengerService extends Service{

    public static final int MSG_FROM_CLIENT = 0;

    public static final int MSG_TO_CLIENT = 1;


    public static final String KEY_FROM_CLIENT = "from_client";

    public static final String KEY_TO_CLIENT = "to_client";


    private static final String TAG = "MessengerService";

    private  Messenger mMessenger = new Messenger(new MessengerHandler());

    private static class MessengerHandler extends android.os.Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_FROM_CLIENT:
                    String mess = msg.getData().getString(KEY_FROM_CLIENT);
                    Log.i(TAG, "receive msg from client : " + mess);
                    Messenger messenger = msg.replyTo;
                    Message replyMsg = Message.obtain();
                    replyMsg.what = MSG_TO_CLIENT;
                    Bundle data = new Bundle();
                    data.putString(KEY_TO_CLIENT,"received , this is service");
                    replyMsg.setData(data);
                    try{
                        messenger.send(replyMsg);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                break;
            }
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mMessenger = null;
        return super.onUnbind(intent);
    }
}

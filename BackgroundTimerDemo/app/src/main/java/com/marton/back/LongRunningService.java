package com.marton.back;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by marton on 16/3/27.
 */
public class LongRunningService extends Service{

    private final static String TAG = "LongRunningService";

    private ExecutorService mThreadExeecutorService;

    private boolean isContinue;

    public static final String KEY_CONTINUE ="key_continue";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mThreadExeecutorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mThreadExeecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Log.v(TAG, "try to refresh data from remote server");
            }
        });
        isContinue = getSharedPreferences("continue_sp",MODE_PRIVATE).getBoolean(KEY_CONTINUE,true);
        if(isContinue){
            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            int durTime = 3 * 1000;
            long execuAtTime = SystemClock.elapsedRealtime() + durTime;
            Intent i = new Intent(this,AlarmReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(this,0,i,PendingIntent.FLAG_ONE_SHOT);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, execuAtTime, pi);
        }
        return super.onStartCommand(intent, flags, startId);
    }
}

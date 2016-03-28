package com.marton.foregrond;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.lang.reflect.Method;
/**
 * Created by marton on 16/3/27.
 */
public class ForegroundService extends Service {
    private NotificationManager mNotificationManager;
    private Method mSetForegroundMethod;

    private static final Class[] SET_FOREGROUND_PARAM = new Class[]{
            boolean.class
    };
//    private Method mStartForegroundMethod;
//    private Method mStopForegroundMethod;
//
//    private static final Class[] START_FOREGROUND_PARAM = new Class[]{
//            int.class,Notification.class
//    };
//    private static final Class[] STOP_FOREGROUND_PARAM = new Class[]{
//            boolean.class
//    };


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        try{
//            mStartForegroundMethod = ForegroundService.class.getMethod("startForeground",
//                    START_FOREGROUND_PARAM);
//             mStopForegroundMethod = ForegroundService.class.getMethod("stopForeground",
//                     STOP_FOREGROUND_PARAM);
//        }catch (NoSuchMethodException e){
//            e.printStackTrace();
//            mStopForegroundMethod = null;
//            mStartForegroundMethod = null;
//        }
        try{
            mSetForegroundMethod = ForegroundService.class.getMethod("setForeground",
                    SET_FOREGROUND_PARAM);
        }catch (NoSuchMethodException e){
            e.printStackTrace();
            mSetForegroundMethod = null;
        }
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Notification notification = new Notification.Builder(this).setTicker("my ticker on statsbar").
                setContentTitle("content title").setContentText("content text").
                setWhen(System.currentTimeMillis()).setShowWhen(true).
                setSmallIcon(R.drawable.ic_launcher).setContentIntent(pIntent).build();
        startForegroundCompt(1, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForegroundCompt(1);
    }

    private void startForegroundCompt(int id,Notification notification){
//        if(mStartForegroundMethod != null){
//            startForeground(id,notification);
//        }else{
//            setForeground(true);
//            mNotificationManager.notify(id,notification);
//        }
        if(mSetForegroundMethod != null){
            try{
                mSetForegroundMethod.invoke(this,new Object[]{true});
                mNotificationManager.notify(id,notification);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            startForeground(id,notification);
        }
    }

    private void stopForegroundCompt(int id){
//        if(mStopForegroundMethod != null){
//            stopForeground(true);
//        }else{
//            mNotificationManager.cancel(id);
//            setForeground(false);
//        }
        if(mSetForegroundMethod != null){
            try{
                mNotificationManager.cancel(id);
                mSetForegroundMethod.invoke(this,new Object[]{false});
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            stopForeground(true);
        }
    }
}

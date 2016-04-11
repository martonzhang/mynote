package com.marton.weather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.marton.weather.broadcastreceiver.AutoUpdateReceiver;
import com.marton.weather.util.HttpCallbackListener;
import com.marton.weather.util.HttpUtil;
import com.marton.weather.util.ThreadManager;
import com.marton.weather.util.Utility;

import org.json.JSONException;

/**
 * Created by marton on 16/4/9.
 */
public class AutoUpdateService extends Service{

    private static final String TAG = "AutoUpdateService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ThreadManager.executeOnThreadPool(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this);
                final String weatherCode = sp.getString("weather_code","");
                final String countryCode = sp.getString("country_code","");
                if (!TextUtils.isEmpty(weatherCode)){
                    String request_url = "http://www.weather.com.cn/data/cityinfo" + weatherCode + ".htnl";
                    HttpUtil.sendHttpRequestSync(request_url,new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                            try {
                                Utility.handleWeatherResp(AutoUpdateService.this,response,countryCode);
                            }catch (JSONException e){
                                e.printStackTrace();
                                Log.v(TAG,"请求数据失败");
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                }
            }
        });

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        int durHour = 8 * 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + durHour;
        Intent i = new Intent(this, AutoUpdateReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,0,i,0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }


}

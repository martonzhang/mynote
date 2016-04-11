package com.marton.wearher.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.marton.wearher.R;
import com.marton.weather.service.AutoUpdateService;
import com.marton.weather.util.HttpCallbackListener;
import com.marton.weather.util.HttpUtil;
import com.marton.weather.util.Utility;

import org.json.JSONException;

import java.util.logging.Handler;

/**
 * Created by marton on 16/4/9.
 */
public class WeatherActivity extends Activity implements View.OnClickListener, android.os.Handler.Callback{

    private static final int TYPE_WEATHER_CODE = 1;
    private static final int TYPE_COUNTRY_CODE = 2;

    private static final int MSG_REFRESH = 8;

    private TextView mTitleText;
    private TextView mPTimeText;
    private LinearLayout mWeatherInfoLayout;
    private TextView mDateText;
    private TextView mDespText;
    private TextView mTempText;
    private Button mRefreshBtn;
    private Button mChoiceBtn;

    private String mCuntryCode;

    private android.os.Handler mHandler;

    private boolean isFirstService = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        mTitleText = (TextView)findViewById(R.id.title);
        mPTimeText = (TextView)findViewById(R.id.push_time);
        mDateText = (TextView)findViewById(R.id.date);
        mDespText = (TextView)findViewById(R.id.weather);
        mTempText = (TextView)findViewById(R.id.temp);
        mWeatherInfoLayout = (LinearLayout)findViewById(R.id.weather_info_layout);
        mRefreshBtn = (Button)findViewById(R.id.refresh);
        mChoiceBtn = (Button)findViewById(R.id.choice_city);
        mRefreshBtn.setOnClickListener(this);
        mChoiceBtn.setOnClickListener(this);
        mHandler = new android.os.Handler(this);

        mCuntryCode = getIntent().getStringExtra("country_code");
        String countryName = getIntent().getStringExtra("country_name");
        if(!TextUtils.isEmpty(countryName)){
            mTitleText.setText(countryName);
        }
        if(!TextUtils.isEmpty(mCuntryCode)){
            mPTimeText.setText("同步中。。。");
            mWeatherInfoLayout.setVisibility(View.GONE);
            getWeatherCodeFromServer(mCuntryCode);
        }else{
            showWeatherLocal();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case MSG_REFRESH:
                mPTimeText.setText("同步失败");
                Toast.makeText(this,"加载失败",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.refresh:
                mPTimeText.setText("同步中。。。");
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                String weatherCode = sp.getString("weather_code","");
                if(!TextUtils.isEmpty(weatherCode)){
                    getWeatherCodeFromServer(weatherCode);
                }else{
                    mHandler.sendEmptyMessageDelayed(MSG_REFRESH,2000);
                }
                break;
            case R.id.choice_city:
                Intent intent = new Intent(this,AddressActivity.class);
                intent.putExtra("from_weather_activity",true);
                startActivity(intent);
                finish();
                break;
        }
    }

    /**
     * 查询天气代号
     * @param countryCode
     */
    private void getWeatherCodeFromServer(String countryCode){
        String request_url = "http://www.weather.com.cn/data/list3/city" + countryCode + ".xml";
        getInfoFromSever(request_url,TYPE_COUNTRY_CODE);
    }

    /**
     * 查询天气信息
     * @param weatherCode
     */
    private void getWeahterInfoFromServer(String weatherCode){
        String request_url = "http://www.weather.com.cn/data/list3/city" + weatherCode + ".xml";
        getInfoFromSever(request_url,TYPE_WEATHER_CODE);
    }

    private void getInfoFromSever(final String request_url, final int type){
        HttpUtil.sendHttpRequestASync(request_url,new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if(type == TYPE_COUNTRY_CODE){
                    if(!TextUtils.isEmpty(response)){
                        String[] array = response.split("\\|");
                        if (array != null && array.length > 1){
                            String weatherCode = array[1];
                            getWeahterInfoFromServer(request_url);
                        }
                    }
                }else if(type == TYPE_WEATHER_CODE){
                    try{
                        Utility.handleWeatherResp(WeatherActivity.this,response,mCuntryCode);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showWeatherLocal();
                            }
                        });
                    }catch (JSONException e){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mPTimeText.setText("同步失败");
                                Toast.makeText(WeatherActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onError(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPTimeText.setText("更新失败");
                    }
                });
            }
        });
    }

    private void showWeatherLocal(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String ptime = sp.getString("publish_time", "");
        String temp1 = sp.getString("temp1","");
        String temp2 = sp.getString("temp2","");
        String weatherDesp = sp.getString("weather_desp","");
        String date = sp.getString("current_data","");
        String countryName = sp.getString("country_name","");

        if(!TextUtils.isEmpty(countryName)){
            mTitleText.setText(countryName);
        }

        if (!TextUtils.isEmpty(ptime)){
            mPTimeText.setText("今天" + ptime + "发布");
        }

        mWeatherInfoLayout.setVisibility(View.VISIBLE);

        if(!TextUtils.isEmpty(date)){
            mDateText.setText(date);
            mDateText.setVisibility(View.VISIBLE);
        }else{
            mDateText.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(weatherDesp)){
            mDespText.setText(weatherDesp);
            mDateText.setVisibility(View.VISIBLE);
        }else{
            mDateText.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(temp1) && !TextUtils.isEmpty(temp2)){
            String temp = temp1 + "~" + temp2;
            mTempText.setText(temp);
            mTempText.setVisibility(View.VISIBLE);
        }else{
            mTempText.setVisibility(View.GONE);
        }

        if(isFirstService){
            isFirstService = false;
            Intent i = new Intent(this, AutoUpdateService.class);
            startService(i);
        }
    }

}

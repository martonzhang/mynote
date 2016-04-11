package com.marton.wearher.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.marton.wearher.R;
import com.marton.weather.db.CoolWeatherDB;
import com.marton.weather.model.City;
import com.marton.weather.model.Country;
import com.marton.weather.model.Province;
import com.marton.weather.util.HttpCallbackListener;
import com.marton.weather.util.HttpUtil;
import com.marton.weather.util.ThreadManager;
import com.marton.weather.util.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AddressActivity extends Activity implements ListView.OnItemClickListener{

    private final static int LEVEL_PROVINCE = 1;
    private final static int LEVEL_CITY = 2;
    private final static int LEVEL_COUNTRY = 3;

    private ListView mListView;
    private TextView mTitleText;
    private ProgressDialog mProgressDialog;

    private ArrayAdapter<String> mAdapter;
    private List<String> mDataList = new ArrayList<String>();
    private CoolWeatherDB mDB;

    private int mCurrentLevel;
    private boolean isFromWeatherActivity;

    private List<Province> mProvinceList;
    private List<City> mCityList;
    private List<Country> mCountryList;

    private Province mSelectedProvince;
    private City mSelectedCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isSelected = sp.getBoolean("country_selected",false);
        isFromWeatherActivity = sp.getBoolean("from_weather_activity",false);
        if(isSelected && !isFromWeatherActivity){
            String countryCode = sp.getString("country_code","");
            String countryName = sp.getString("country_name","");
            Intent intent = new Intent(this,WeatherActivity.class);
            intent.putExtra("country_code",countryCode);
            intent.putExtra("country_name",countryName);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_address);
        mListView = (ListView)findViewById(R.id.list);
        mTitleText = (TextView)findViewById(R.id.title);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,mDataList);
        mListView.setAdapter(mAdapter);
        mDB = CoolWeatherDB.getmCoolWeatherDB(this);
        mListView.setOnItemClickListener(this);
        getProvinceData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        isFromWeatherActivity = sp.getBoolean("from_weather_activity",false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mCurrentLevel == LEVEL_PROVINCE){
            mSelectedProvince = mProvinceList.get(position);
            getCitiesData();
        }else if(mCurrentLevel == LEVEL_CITY){
            mSelectedCity = mCityList.get(position);
            getCountriesData();
        }else if(mCurrentLevel == LEVEL_COUNTRY){
            Country mSelectedCountry = mCountryList.get(position);
            String countryCode = mSelectedCountry.getmCountryCode();
            String countryName = mSelectedCountry.getmCountryName();
            Intent intent = new Intent(AddressActivity.this,WeatherActivity.class);
            intent.putExtra("country_code",countryCode);
            intent.putExtra("country_name",countryName);
            startActivity(intent);
        }
    }

    private void getProvinceData(){
        mTitleText.setText("中国");
        mCurrentLevel = LEVEL_PROVINCE;
        if(mProvinceList != null && mProvinceList.size() > 0){
            mDataList.clear();
            for (Province pv : mProvinceList){
                mDataList.add(pv.getmProvinceName());
            }
            mAdapter.notifyDataSetChanged();
        }else{
            showProgressDialog();
            ThreadManager.executeOnThreadPool(new Runnable() {
                @Override
                public void run() {
                    mProvinceList = mDB.loadProvinces();
                    if(mProvinceList != null && mProvinceList.size() > 0){
                        mDataList.clear();
                        for (Province pv : mProvinceList){
                            mDataList.add(pv.getmProvinceName());
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismassProgressDialog();
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }else{
                        requestFromSever(null,LEVEL_PROVINCE);
                    }
                }
            });
        }
    }

    private void getCitiesData(){
        mTitleText.setText(mSelectedProvince.getmProvinceName());
        mCurrentLevel = LEVEL_CITY;
        if(mCityList != null && mCityList.size() > 0){
            mDataList.clear();
            for (City cy : mCityList){
                mDataList.add(cy.getmCityName());
            }
            mAdapter.notifyDataSetChanged();
        }else{
            showProgressDialog();
            ThreadManager.executeOnThreadPool(new Runnable() {
                @Override
                public void run() {
                    mCityList = mDB.loadCities(mSelectedProvince.getmId());
                    if(mCityList != null && mCityList.size() > 0){
                        mDataList.clear();
                        for (City cy : mCityList){
                            mDataList.add(cy.getmCityName());
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismassProgressDialog();
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }else{
                        requestFromSever(mSelectedProvince.getmProvinceCode(),LEVEL_CITY);
                    }
                }
            });
        }
    }

    private void getCountriesData(){
        mTitleText.setText(mSelectedCity.getmCityName());
        mCurrentLevel = LEVEL_COUNTRY;
        if(mCountryList != null && mCountryList.size() > 0){
            mDataList.clear();
            for (Country cy : mCountryList){
                mDataList.add(cy.getmCountryName());
            }
            mAdapter.notifyDataSetChanged();
        }else{
            showProgressDialog();
            ThreadManager.executeOnThreadPool(new Runnable() {
                @Override
                public void run() {
                    mCountryList = mDB.loadCountries(mSelectedCity.getmId());
                    if(mCountryList != null && mCountryList.size() > 0){
                        mDataList.clear();
                        for (Country cy : mCountryList){
                            mDataList.add(cy.getmCountryName());
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismassProgressDialog();
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }else{
                        requestFromSever(mSelectedCity.getmCityCode(),LEVEL_COUNTRY);
                    }
                }
            });
        }
    }



    private void requestFromSever(final String code,final int type){
        String request_rul;
        if(!TextUtils.isEmpty(code)){
           request_rul = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
        }else{
          request_rul = "http://www.weather.com.cn/data/list3/city.xml";
        }
        HttpUtil.sendHttpRequestSync(request_rul,new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if (type == LEVEL_PROVINCE){
                    result = Utility.handleProvinceResp(mDB, response, mProvinceList);
                }else if(type == LEVEL_CITY){
                    result = Utility.handleCityResp(mDB, response, mSelectedProvince.getmId(), mCityList);
                }else if(type == LEVEL_COUNTRY){
                    result =Utility.hndleCountryResp(mDB, response, mSelectedCity.getmId(), mCountryList);
                }
                if (result){
                    mDataList.clear();
                    if (type == LEVEL_PROVINCE){
                        if(mProvinceList != null && mProvinceList.size() > 0){
                           for (Province pv : mProvinceList){
                               mDataList.add(pv.getmProvinceName());
                           }
                        }
                    }else if(type == LEVEL_CITY){
                        if(mCityList != null && mCityList.size() > 0) {
                            for (City cy : mCityList) {
                                mDataList.add(cy.getmCityName());
                            }
                        }
                    }else if(type == LEVEL_COUNTRY){
                        if(mCountryList != null && mCountryList.size() > 0){
                            for (Country cou : mCountryList){
                                mDataList.add(cou.getmCountryName());
                            }
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismassProgressDialog();
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }

            @Override
            public void onError(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismassProgressDialog();
                        Toast.makeText(AddressActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showProgressDialog(){
        if (mProgressDialog == null){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("正在加载。。。");
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    private void dismassProgressDialog(){
        if (mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mCurrentLevel == LEVEL_COUNTRY){
            getCitiesData();
            return;
        }else if(mCurrentLevel == LEVEL_CITY){
            getProvinceData();
            return;
        }else{
            finish();
        }
    }
}

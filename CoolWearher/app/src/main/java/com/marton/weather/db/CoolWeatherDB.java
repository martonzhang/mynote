package com.marton.weather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.marton.weather.model.City;
import com.marton.weather.model.Country;
import com.marton.weather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marton on 16/4/7.
 */
public class CoolWeatherDB {

    /**
     * 数据库名
     */
    public static final String DB_NAME = "cool_weather";

    /**
     *  数据库版本
     */
    public static final int VERSION = 1;

    private static CoolWeatherDB mCoolWeatherDB;

    private SQLiteDatabase mDB;

    private CoolWeatherDB(Context context){
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
        mDB = dbHelper.getWritableDatabase();
    }

    /**
     * 获取CoolWeatherDB对象
     * @param context
     * @return
     */
    public static CoolWeatherDB getmCoolWeatherDB(Context context){
        if(mCoolWeatherDB == null){
            synchronized (CoolWeatherDB.class){
                if(mCoolWeatherDB != null){
                    mCoolWeatherDB = new CoolWeatherDB(context);
                }
            }
        }
        return mCoolWeatherDB;
    }

    /**
     * 保存Province实例到数据库
     * @param province
     */
    public void saveProvince(Province province){
        if(province != null){
            ContentValues values = new ContentValues();
            values.put("province_name",province.getmProvinceName());
            values.put("province_code",province.getmProvinceCode());
            mDB.insert("Province",null,values);
        }
    }

    /**
     * 保存City实例到数据库
     * @param city
     */
    public void saveCity(City city){
        if(city != null){
            ContentValues values = new ContentValues();
            values.put("city_name",city.getmCityName());
            values.put("city_code",city.getmCityCode());
            values.put("province_id",city.getmProvinceId());
            mDB.insert("City",null,values);
        }
    }

    /**
     * 保存Country实例到数据库
     * @param country
     */
    public void saveCountry(Country country){
        if(country != null){
            ContentValues values = new ContentValues();
            values.put("country_name",country.getmCountryName());
            values.put("country_code",country.getmCountryCode());
            values.put("city_id",country.getmCityId());
            mDB.insert("Country",null,values);
        }
    }

//    public List<Province> loadProvinces(){
//        List<Province> list = new ArrayList<Province>();
//        Cursor cursor = mDB.query("Province",null,null,null,null,null,null);
//        if(cursor.moveToFirst()){
//            do
//        }
//
//        return list;
//    }

}

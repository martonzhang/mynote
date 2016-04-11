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
    public static final String DB_NAME = "CoolWeather.DB";

    /**
     *  数据库版本
     */
    public static final int VERSION = 4;

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
                if(mCoolWeatherDB == null){
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

    /**
     * 从数据库中读取省的信息
     * @return
     */
    public List<Province> loadProvinces(){
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = mDB.query("Province",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Province pv = new Province();
                pv.setmId(cursor.getInt(cursor.getColumnIndex("id")));
                pv.setmProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                pv.setmProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(pv);
            }while(cursor.moveToNext());
        }
        if(cursor != null){
            cursor.close();
        }

        return list;
    }

    /**
     * 从数据库中读取某省下的所有市
     * @param provinceId
     * @return
     */
    public List<City> loadCities(int provinceId){
        List<City> list = new ArrayList<City>();
        Cursor cursor = mDB.query("City",null,"province_id = ?",new String[]{String.valueOf(provinceId)},null,null,null);
        if(cursor.moveToFirst()){
            do {
                City city = new City();
                city.setmId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setmCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setmCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setmProvinceId(provinceId);
            }while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * 从数据库中读取某市下的所有县
     * @param cityId
     * @return
     */
    public List<Country> loadCountries(int cityId){
        List<Country> list = new ArrayList<Country>();
        Cursor cursor = mDB.query("Country",null,"city_id = ?",new String[] {String.valueOf(cityId)},null,null,null);
        if(cursor.moveToFirst()){
            do{
              Country country = new Country();
                country.setmId(cursor.getInt(cursor.getColumnIndex("id")));
                country.setmCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
                country.setmCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
                country.setmCityId(cityId);
            }while (cursor.moveToNext());
        }
        if(cursor != null){
            cursor.close();
        }
        return list;
    }
}

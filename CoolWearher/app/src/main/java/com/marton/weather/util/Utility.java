package com.marton.weather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.marton.weather.db.CoolWeatherDB;
import com.marton.weather.model.City;
import com.marton.weather.model.Country;
import com.marton.weather.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by marton on 16/4/9.
 */
public class Utility {

    /**
     * 解析处理服务器返回的省级数据
     * @param db
     * @param response
     * @return
     */
    public static boolean handleProvinceResp(CoolWeatherDB db,String response,List<Province> provinces){
        if(!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(",");
            if(allProvinces != null && allProvinces.length > 0){
                for (String p : allProvinces) {
                    String array[] = p.split("\\|");
                    if(array != null && array.length > 1){
                        Province pv = new Province();
                        pv.setmProvinceName(array[1]);
                        pv.setmProvinceCode(array[0]);
                        db.saveProvince(pv);
                        provinces.add(pv);
                    }
                }
            }
            return true;
        }

        return false;
    }

    /**
     * 解析和处理从服务器返回的市级数据
     * @param db
     * @param response
     * @return
     */
    public static boolean handleCityResp(CoolWeatherDB db,String response,int provinceId,List<City> cities){
        if(!TextUtils.isEmpty(response)){
            String[] allCities = response.split(",");
            if (allCities != null && allCities.length > 0){
                for (String a : allCities){
                    String[] array = a.split("\\|");
                    if(array != null && array.length > 1){
                        City city = new City();
                        city.setmCityCode(array[0]);
                        city.setmCityName(array[1]);
                        city.setmProvinceId(provinceId);
                        db.saveCity(city);
                        cities.add(city);
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 解析和处理服务器反馈的市级数据
     * @param db
     * @param response
     * @param cityId
     * @return
     */
    public static boolean hndleCountryResp(CoolWeatherDB db,String response,int cityId,List<Country> countries){
        if(!TextUtils.isEmpty(response)){
            String[] allCountries = response.split(",");
            if(allCountries != null && allCountries.length > 0){
                for (String c : allCountries){
                    String[] array = c.split("\\|");
                    if(array != null && array.length > 1){
                        Country cou = new Country();
                        cou.setmCountryName(array[1]);
                        cou.setmCountryCode(array[0]);
                        cou.setmCityId(cityId);
                        db.saveCountry(cou);
                        countries.add(cou);
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     *解析和处理服务器返回的天气数据
     */
    public static void handleWeatherResp (Context context, String response,String countryCode) throws JSONException{
        JSONObject jsonObject = new JSONObject(response);
        JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
        String countryName = weatherInfo.getString("city");
        String weatherCode = weatherInfo.getString("cityid");
        String temp1 = weatherInfo.getString("temp1");
        String temp2 = weatherInfo.getString("temp2");
        String weatherDesp = weatherInfo.getString("weather");
        String publishTime = weatherInfo.getString("ptime");

        SimpleDateFormat sDF = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("country_selected",true);
        editor.putString("country_name",countryName);
        editor.putString("country_code",countryCode);
        editor.putString("weather_code",weatherCode);
        editor.putString("temp1",temp1);
        editor.putString("temp2",temp2);
        editor.putString("weather_desp",weatherDesp);
        editor.putString("publish_time",publishTime);
        editor.putString("current_data",sDF.format(new Date()));
        editor.commit();
    }
}

package com.marton.weather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by marton on 16/4/7.
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

    /**
     * 省对应的表
     */
    public static final String SQL_CREATE_PROVIENCE = "create table Province ("
            + "id integer primary key autoincrement,"
            + "province_name text,"
            + "province_code text)";

    /**
     * 市对应的表
     */
    public static final String SQL_CREATE_CITY = "create table City ("
            + "id integer primary key autoincrement,"
            + "city_name text,"
            + "city_code text,"
            + "province_id integer)";

    /**
     * 县对应的表
     */
    public static final String SQL_CREATE_COUNTRY = "create table Country ("
            + "id integer primary key autoincrement,"
            + "country_name text,"
            + "country_code text,"
            + "city_id integer)";


    public CoolWeatherOpenHelper(Context context, String name,
                                 SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PROVIENCE);
        db.execSQL(SQL_CREATE_CITY);
        db.execSQL(SQL_CREATE_COUNTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table Province");
        db.execSQL("drop table City");
        db.execSQL("drop table Country");
        db.execSQL(SQL_CREATE_PROVIENCE);
        db.execSQL(SQL_CREATE_CITY);
        db.execSQL(SQL_CREATE_COUNTRY);
    }
}

package com.marton.weather.model;

/**
 * Created by marton on 16/4/7.
 */
public class City {

    private String mCityName;
    private String mCityCode;
    private int mId;
    private int mProvinceId;

    public String getmCityName() {
        return mCityName;
    }

    public void setmCityName(String mCityName) {
        this.mCityName = mCityName;
    }

    public String getmCityCode() {
        return mCityCode;
    }

    public void setmCityCode(String mCityCode) {
        this.mCityCode = mCityCode;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getmProvinceId() {
        return mProvinceId;
    }

    public void setmProvinceId(int mProvinceId) {
        this.mProvinceId = mProvinceId;
    }
}

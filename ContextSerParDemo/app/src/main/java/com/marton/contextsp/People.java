package com.marton.contextsp;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by marton on 16/4/2.
 */
public class People implements Parcelable {
    private String mName;
    private int mAge;

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmAge() {
        return mAge;
    }

    public void setmAge(int mAge) {
        this.mAge = mAge;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeInt(mAge);
    }

    public final static Creator<People> CREATOR = new Creator<People>() {
        @Override
        public People createFromParcel(Parcel source) {
            People pl = new People();
            pl.mName = source.readString();
            pl.mAge = source.readInt();
            return pl;
        }

        @Override
        public People[] newArray(int size) {
            return new People[size];
        }
    };

}

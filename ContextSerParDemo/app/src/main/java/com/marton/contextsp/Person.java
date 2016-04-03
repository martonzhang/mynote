package com.marton.contextsp;

import java.io.Serializable;

/**
 * Created by marton on 16/4/2.
 */
public class Person implements Serializable {
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
}

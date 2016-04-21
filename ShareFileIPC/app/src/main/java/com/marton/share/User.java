package com.marton.share;

import java.io.Serializable;

/**
 * Created by marton on 16/4/20.
 */
public class User implements Serializable {

    private static final long serivalVersionUID = 10888008L;

    public int mUserId;
    public String mUserName;
    public boolean isMale;

    public User(int userId, String userName, boolean isMale){
        this.mUserId = userId;
        this.mUserName = userName;
        this.isMale = isMale;
    }
}

package com.marton.chat;

/**
 * Created by marton on 16/3/15.
 */
public class Msg {

    public static final int TYPE_MSG_SEND = 0;
    public static final int TYPE_MSG_RECEIVE = 1;

    private String mContent;
    private int mType;

    public Msg(int type, String content){
        this.mContent = content;
        this.mType = type;
    }

    public int getType(){
        return this.mType;
    }
    public String getContent(){
        return this.mContent;
    }
}

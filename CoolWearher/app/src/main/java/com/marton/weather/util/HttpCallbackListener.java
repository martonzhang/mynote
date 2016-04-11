package com.marton.weather.util;

/**
 * Created by marton on 16/4/8.
 */
public interface HttpCallbackListener {

    public void onFinish(String response);

    public void onError(String error);
}

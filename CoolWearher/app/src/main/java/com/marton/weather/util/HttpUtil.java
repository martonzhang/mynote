package com.marton.weather.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by marton on 16/4/8.
 */
public class HttpUtil {

    public static void sendHttpRequestSync(final String request_url,final HttpCallbackListener listener){
        HttpURLConnection connection = null;
        try{
            URL url = new URL(request_url);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            InputStream input = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                response.append(line);
            }
            if (listener != null){
                listener.onFinish(response.toString());
            }
        }catch(Exception e){
            if (listener != null){
                listener.onError("请求失败");
            }
        }finally {
            if(connection != null){
                connection.disconnect();
            }
        }
    }


    public static void sendHttpRequestASync(final String request_url, final HttpCallbackListener listener){
       ThreadManager.executeOnThreadPool(new Runnable() {
           @Override
           public void run() {
               sendHttpRequestSync(request_url,listener);
           }
       });
    }
}

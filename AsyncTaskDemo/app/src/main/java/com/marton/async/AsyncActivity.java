package com.marton.async;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class AsyncActivity extends Activity implements View.OnClickListener{

    private static final String TAG = "AsyncActivity";

    private Button mExecuteBtn;
    private Button mCancelBtn;
    private TextView mTextView;
    private BackgroundTask mBgTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async);
        mExecuteBtn = (Button)findViewById(R.id.excute_btn);
        mCancelBtn = (Button)findViewById(R.id.cancel_btn);
        mTextView = (TextView)findViewById(R.id.text);
        mExecuteBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
        mBgTask = new BackgroundTask();
        mCancelBtn.setEnabled(false);
        mExecuteBtn.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_btn:
                mBgTask.cancel(true);
                break;
            case R.id.excute_btn:
                mBgTask.execute("http://www.baidu.com");
                mCancelBtn.setEnabled(true);
                mExecuteBtn.setEnabled(false);
                break;
            default:
                break;
        }
    }

    private class BackgroundTask extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                InputStream input = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder text = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null){
                    text.append(line);
                    //为了效果更明显
                    Thread.sleep(500);
                }
                return text.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if(connection != null){
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            mCancelBtn.setEnabled(false);
            mTextView.setText("已取消");

        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onPostExecute(String s) {
            mTextView.setText(s);
            mCancelBtn.setEnabled(false);
        }

        @Override
        protected void onPreExecute() {
            mTextView.setText("正在加载...");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }
}

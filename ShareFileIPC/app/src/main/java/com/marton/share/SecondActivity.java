package com.marton.share;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by marton on 16/4/20.
 */
public class SecondActivity extends Activity {

    private ExecutorService mExecutor = Executors.newCachedThreadPool();
    private TextView mTextView;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mTextView = (TextView)findViewById(R.id.user);
        readFromFile();

    }

    private void readFromFile(){
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                File cashedFile = new File(Environment.getExternalStorageDirectory(),"cache.txt");
                if (cashedFile.exists()){
                    ObjectInputStream objInStreanm = null;
                    try{
                        objInStreanm = new ObjectInputStream(new FileInputStream(cashedFile));
                        mUser = (User)objInStreanm.readObject();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mUser != null){
                                    mTextView.setText(mUser.mUserId + mUser.mUserName + mUser.isMale);
                                }
                            }
                        });
                    }catch (IOException e){
                        e.printStackTrace();
                    }catch (ClassNotFoundException e){
                        e.printStackTrace();
                    }finally {
                        try{
                            if(objInStreanm != null){
                                objInStreanm.close();
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

}

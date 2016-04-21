package com.marton.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class MainActivity extends Activity {

    private ExecutorService mExecutor = Executors.newCachedThreadPool();
    private Button mBtn;
    private  User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn = (Button)findViewById(R.id.save);
        mUser = new User(1,"marton",false);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serToFile(mUser);
            }
        });
    }

    private void serToFile(final User user){
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ObjectOutputStream objOutStreanm = null;
                try{
                    File file = new File(Environment.getExternalStorageDirectory(),"cache.txt");
                    if (!file.exists()){
                        file.createNewFile();
                    }
                    objOutStreanm = new ObjectOutputStream(new FileOutputStream(file));
                    objOutStreanm.writeObject(user);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                            startActivity(intent);
                        }
                    });
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    try{
                        if(objOutStreanm != null){
                            objOutStreanm.close();
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.marton.contextsp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity implements View.OnClickListener{

    private Button mSerBtn;
    private Button mParBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSerBtn = (Button)findViewById(R.id.ser_btn);
        mParBtn = (Button)findViewById(R.id.par_btn);
        mSerBtn.setOnClickListener(this);
        mParBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == mSerBtn){
            Person per = new Person();
            per.setmName("martonzhang");
            per.setmAge(26);
            Intent serIntent = new Intent(this,SecondActivity.class);
            serIntent.putExtra("data",per);
            startActivity(serIntent);
        }else if(v == mParBtn){
            People pl = new People();
            pl.setmName("marton");
            pl.setmAge(25);
            Intent plIntent = new Intent(this,SecondActivity.class);
            plIntent.putExtra("data",pl);
            startActivity(plIntent);
        }
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

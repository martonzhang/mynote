package com.marton.move;

import android.app.Activity;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity implements View.OnClickListener {

    private Button mBtn;
    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn = (MoveView)findViewById(R.id.move);
        mBtn.setOnClickListener(this);
        mText = (TextView)findViewById(R.id.text);
    }

    @Override
    public void onClick(View v) {
        Log.d(MoveView.TAG,"on click");
        if (mText.getText().equals("changeText")){
            mText.setText("changed");
            mText.setTranslationX(mText.getX() + 100);
            mText.setTranslationY(mText.getY() + 100);
        }else{
            mText.setText("changeText");
            mText.setTranslationX(mText.getX() - 100);
            mText.setTranslationY(mText.getY() - 100);
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

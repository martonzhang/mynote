package com.marton.scroll;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;


public class MainActivity extends Activity implements View.OnClickListener{

    private Button mBtn1;
    private Button mBtn2;
    private TextView mText;
    private MyScrollView mScrollText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn1 = (Button)findViewById(R.id.btn1);
        mBtn2 = (Button)findViewById(R.id.btn2);
        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
        mText = (TextView)findViewById(R.id.text);
        mScrollText = (MyScrollView)findViewById(R.id.scrollview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1:
                mScrollText.smoothScrollTo(-500,-500);
                break;
            case R.id.btn2:
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,1).setDuration(3000);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float fraction = animation.getAnimatedFraction();
                        mText.scrollTo((int)(fraction*(-500)), (int)(fraction*(-500)));
                    }
                });
                valueAnimator.start();
                break;
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

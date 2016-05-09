package com.marton.demo;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{

    private ViewPager mViewPager;

    private ImageView mImageLine;

    private MyFragmentAdapter mAdapter;

    private TextView mText1;
    private TextView mText2;
    private TextView mText3;

    private int mTabWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initViews();
        initLineWidth();
        
    }

    private void initViews(){
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mImageLine = (ImageView)findViewById(R.id.line);
        mText1 = (TextView)findViewById(R.id.text1);
        mText2 = (TextView)findViewById(R.id.text2);
        mText3 = (TextView)findViewById(R.id.text3);
        mText1.setOnClickListener(this);
        mText2.setOnClickListener(this);
        mText3.setOnClickListener(this);
        Fragment1 fg1 = new Fragment1();
        Fragment2 fg2 = new Fragment2();
        Fragment3 fg3 = new Fragment3();
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(fg1);
        fragments.add(fg2);
        fragments.add(fg3);
        mAdapter = new MyFragmentAdapter(getSupportFragmentManager(),fragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setCurrentItem(0);
        mText1.setTextColor(Color.RED);
        mText2.setTextColor(Color.BLACK);
        mText3.setTextColor(Color.BLACK);
    }

    private void initLineWidth(){
        DisplayMetrics dsm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dsm);
        mImageLine.getLayoutParams().width = dsm.widthPixels / 3;
        mImageLine.requestLayout();
        mTabWidth = dsm.widthPixels / 3;
    }

    private void moveToPos(int pos){
        float curX = mImageLine.getX();
        float toX = mTabWidth * pos + mImageLine.getLeft();
        ObjectAnimator animator = ObjectAnimator.ofFloat(mImageLine,"translationX",curX,toX).setDuration(200);
        animator.start();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mImageLine.setX(position * mTabWidth + positionOffset * mTabWidth);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
//                moveToPos(0);
                mText1.setTextColor(Color.RED);
                mText2.setTextColor(Color.BLACK);
                mText3.setTextColor(Color.BLACK);
                break;
            case 1:
//                moveToPos(1);
                mText1.setTextColor(Color.BLACK);
                mText2.setTextColor(Color.RED);
                mText3.setTextColor(Color.BLACK);
                break;
            case 2:
//                moveToPos(2);
                mText1.setTextColor(Color.BLACK);
                mText2.setTextColor(Color.BLACK);
                mText3.setTextColor(Color.RED);
                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text1:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.text2:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.text3:
                mViewPager.setCurrentItem(2);
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

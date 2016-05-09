package com.marton.demo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by marton on 16/5/8.
 */
public class MyFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    public MyFragmentAdapter(FragmentManager manager, List<Fragment> fragments){
        super(manager);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}

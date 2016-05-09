package com.marton.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by marton on 16/5/7.
 */
public class Fragment2 extends android.support.v4.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment2 = LayoutInflater.from(getActivity()).inflate(R.layout.fragment2,null);
        return fragment2;
}    }


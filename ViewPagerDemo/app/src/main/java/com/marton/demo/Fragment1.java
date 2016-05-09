package com.marton.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marton on 16/5/7.
 */
public class Fragment1 extends android.support.v4.app.Fragment {

    private ListView mListView;

    private ArrayAdapter<String> mAdatper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment1 = LayoutInflater.from(getActivity()).inflate(R.layout.fragment1,null);
        mListView = (ListView)fragment1.findViewById(R.id.list);
        mListView.setAdapter(mAdatper);
        return fragment1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<String> datas = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            datas.add("num" + i);
        }
        mAdatper = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,datas);
    }
}


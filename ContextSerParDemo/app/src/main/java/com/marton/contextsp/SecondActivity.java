package com.marton.contextsp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by marton on 16/4/2.
 */
public class SecondActivity extends Activity {

    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);
        mText = (TextView)findViewById(R.id.text);
        Person per = (Person)getIntent().getSerializableExtra("data");
        People pl = (People)getIntent().getParcelableExtra("data");
        if(per != null){
            String name = per.getmName();
            int age = per.getmAge();
            StringBuilder sb = new StringBuilder();
            sb.append("name : ").append(name).append("\n").append("age : ").append(age);
            mText.setText(sb.toString());
            Toast.makeText(CustomApplication.getmGlobalContext(),sb.toString(),Toast.LENGTH_SHORT).show();
        }else if(pl != null){
            String name = pl.getmName();
            int age = pl.getmAge();
            StringBuilder sb = new StringBuilder();
            sb.append("name : ").append(name).append("\n").append("age : ").append(age);
            mText.setText(sb.toString());
            Toast.makeText(CustomApplication.getmGlobalContext(),sb.toString(),Toast.LENGTH_SHORT).show();
        }
    }
}

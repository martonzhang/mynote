package com.marton.offline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class AppActivity extends BaseActivity {
    private Button mSendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        mSendBtn = (Button)findViewById(R.id.send_btn);
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.marton.broadcast.FORCE_OFFLINE");
                sendBroadcast(intent);
            }
        });
    }

}

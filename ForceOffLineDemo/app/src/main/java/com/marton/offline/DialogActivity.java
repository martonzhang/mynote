package com.marton.offline;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by marton on 16/3/17.
 */
public class DialogActivity extends Activity implements View.OnClickListener{
    private Button mCancleBtn;
    private Button mOkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog);
        mCancleBtn = (Button)findViewById(R.id.cancel_btn);
        mOkBtn = (Button)findViewById(R.id.ok_btn);
        mCancleBtn.setOnClickListener(this);
        mOkBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.cancel_btn:
                finish();
                break;
            case R.id.ok_btn:
                ActivityCollector.finishAllAcitvity();
                Intent intent = new Intent(DialogActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}

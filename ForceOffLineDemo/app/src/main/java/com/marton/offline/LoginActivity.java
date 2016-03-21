package com.marton.offline;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by marton on 16/3/17.
 */
public class LoginActivity extends BaseActivity{
    private EditText mAccountText;
    private EditText mPasswrodText;
    private Button mLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        mAccountText = (EditText)findViewById(R.id.input_account);
        mPasswrodText = (EditText)findViewById(R.id.input_password);
        mLoginBtn = (Button)findViewById(R.id.login_btn);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = mAccountText.getText().toString();
                String password = mPasswrodText.getText().toString();
                if("marton".equals(account) && "123456".equals(password)){
                    Intent intent = new Intent(LoginActivity.this,AppActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    mAccountText.setText("");
                    mPasswrodText.setText("");
                    Toast.makeText(LoginActivity.this,"account or password error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

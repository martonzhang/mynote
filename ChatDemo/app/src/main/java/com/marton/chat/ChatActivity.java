package com.marton.chat;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends Activity {
    private ListView mChatListView;
    private EditText mInputText;
    private Button mSendBtn;
    private MsgAdapter mMsgAdapter;
    private List<Msg> mMsgList = new ArrayList<Msg>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);
        initHistorys();
        initViews();
        mMsgAdapter = new MsgAdapter(this,mMsgList);
        mChatListView.setAdapter(mMsgAdapter);
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mInputText.getText().toString();
                if(!TextUtils.isEmpty(input)){
                    Msg sendMsg= new Msg(Msg.TYPE_MSG_SEND,input);
                    mMsgAdapter.addMsg(sendMsg);
                    mChatListView.setSelection(mMsgList.size());
                    mInputText.setText("");
                }
            }
        });
    }

    private void initViews(){
        mChatListView = (ListView)findViewById(R.id.chat_listview);
        mInputText = (EditText)findViewById(R.id.input_text);
        mSendBtn = (Button)findViewById(R.id.send_btn);
    }

    private void initHistorys(){
        Msg msg1 = new Msg(Msg.TYPE_MSG_SEND,"Hi");
        Msg msg2 = new Msg(Msg.TYPE_MSG_RECEIVE,"Hello guy,what is that?");
        Msg msg3 = new Msg(Msg.TYPE_MSG_RECEIVE,"ok,get it");
        Msg msg4 = new Msg(Msg.TYPE_MSG_SEND,"just talk about it!");
        mMsgList.add(msg1);
        mMsgList.add(msg2);
        mMsgList.add(msg3);
        mMsgList.add(msg4);

    }

}

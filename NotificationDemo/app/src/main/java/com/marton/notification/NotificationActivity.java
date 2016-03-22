package com.marton.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;


public class NotificationActivity extends Activity implements View.OnClickListener{

    private Button mSendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mSendBtn = (Button)findViewById(R.id.send_btn);
        mSendBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == mSendBtn) {
            Intent intent = new Intent(this, NoteActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationManager noteManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Notification notification = new Notification.Builder(this).setTicker("my ticker on statsbar").
                    setContentTitle("content title").setContentText("content text").
                    setWhen(System.currentTimeMillis()).setShowWhen(true).
                    setSmallIcon(R.drawable.ic_launcher).setContentIntent(pIntent).build();
            noteManager.notify(1, notification);
        }
    }
}

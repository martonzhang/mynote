package com.marton.notification;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;

/**
 * Created by marton on 16/3/22.
 */
public class NoteActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(1);
    }
}

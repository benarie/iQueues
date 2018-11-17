package com.iQueues;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsManager;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String massage = intent.getStringExtra("massage");
        String number = intent.getStringExtra("number");

        SmsManager manager = SmsManager.getDefault();
        manager.sendTextMessage(number, null, massage, null, null);
    }
}

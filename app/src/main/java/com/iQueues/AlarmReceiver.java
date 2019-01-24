package com.iQueues;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import data.GlobalUtils;


public class AlarmReceiver extends BroadcastReceiver {
    String totalTime;
    @Override
    public void onReceive(Context context, Intent intent) {



        GlobalUtils.showNotification("התור שלך מתקרב "  , "נא להגיע בזמן!", context);

    }
}

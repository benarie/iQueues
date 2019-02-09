package com.iQueues;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import data.GlobalUtils;


/**
 * The type Alarm receiver.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        GlobalUtils.showNotification("התור שלך מתקרב "  , "נא להגיע בזמן!", context);

    }
}

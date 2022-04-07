package com.example.android.masjeed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartUpBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            //MindtrackLog.add("Boot Complete");
            context.startService(new Intent(context, PrayingDayCalculateHandler.class));
        }
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            //context.startService(new Intent(context, PrayingDayCalculateHandler.class));
        }
    }
}

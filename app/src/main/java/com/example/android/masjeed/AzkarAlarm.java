package com.example.android.masjeed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.legacy.content.WakefulBroadcastReceiver;

public class AzkarAlarm extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle b = intent.getExtras();
        Log.d("Azkar" , b.getString("Azkar")+"") ;
        Intent serviceNotification = new Intent(context, AzkarNotification.class).putExtra("Azkar" , b.getString("Azkar"));
        startWakefulService(context, serviceNotification);
    }
}

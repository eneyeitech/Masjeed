package com.example.android.masjeed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.legacy.content.WakefulBroadcastReceiver;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        startWakefulService(context, new Intent(context, PrayingDayCalculateHandler.class));

        Log.i("ACTIVITY_SRAT" , "Alarm Receiver is working well");

    }
}
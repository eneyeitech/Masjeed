package com.example.android.masjeed.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.android.masjeed.model.Prayer;
import com.example.android.masjeed.services.PrayerService;

import java.util.Calendar;
import java.util.Random;

public class SnoozeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, 5);

        Prayer prayer = new Prayer(
                new Random().nextInt(Integer.MAX_VALUE),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                "Snooze"
        );
        prayer.setRecurring(false);

        prayer.schedule(context);

        Intent intentService = new Intent(context, PrayerService.class);
        context.stopService(intentService);
    }
}

package com.example.android.masjeed.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.example.android.masjeed.services.PrayerAlarmService;
import com.example.android.masjeed.services.PrayerService;

public class PrayerBroadcastReceiver extends BroadcastReceiver {

    public static final String TITLE = "TITLE";
    public static final String FAJR = "fajr";
    public static final String SUNRISE = "sunrise";
    public static final String ZUHR = "zuhr";
    public static final String ASR = "asr";
    public static final String MAGRIB = "magrib";
    public static final String ISHA = "isha";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            String toastText = String.format("Alarm Reboot");
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            startPrayerAlarmService(context);
        } else {
            String toastText = String.format("Alarm Received");
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            startPrayerService(context, intent);
        }
    }

    private void startPrayerService(Context context, Intent intent) {
        Intent intentService = new Intent(context, PrayerService.class);
        intentService.putExtra(TITLE, intent.getStringExtra(TITLE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }

    private void startPrayerAlarmService(Context context) {
        Intent intentService = new Intent(context, PrayerAlarmService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }
}

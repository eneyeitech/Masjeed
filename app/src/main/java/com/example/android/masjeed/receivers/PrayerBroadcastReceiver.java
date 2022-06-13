package com.example.android.masjeed.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.android.masjeed.model.Prayer;
import com.example.android.masjeed.services.PrayerAlarmService;
import com.example.android.masjeed.services.PrayerService;
import com.example.android.masjeed.services.ReschedulePrayerAlarmService;

import java.util.Calendar;
import java.util.Random;

public class PrayerBroadcastReceiver extends BroadcastReceiver {

    public static final String TITLE = "TITLE";
    public static final String CODE = "CODE";
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
            startReschedulePrayerAlarmService(context);
        } else {
            boolean recurring = intent.getBooleanExtra("RECURRING", false);
            Log.d("#recurring", String.valueOf(recurring));
            Log.d("#recurring hr", String.valueOf(intent.getIntExtra("HOUR",0)));
            if(recurring){
                setNextPrayerTime(context,intent);
            }

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

    private void startReschedulePrayerAlarmService(Context context) {
        Intent intentService = new Intent(context, ReschedulePrayerAlarmService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }

    private void setNextPrayerTime(Context context, Intent intent){
        /** new */

        int hr = intent.getIntExtra("HOUR", 0);
        int min = intent.getIntExtra("MINUTE", 0);
        String title = intent.getStringExtra(CODE);
        Log.d("#title", title);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.MINUTE, 5);

            Prayer prayer = new Prayer(
                    new Random().nextInt(Integer.MAX_VALUE),
                    hr,
                    min,
                    title
            );
            Log.d("#Reschedule Prayer", prayer.toString());
            prayer.setReschedule(true);

            Log.d("#Reschedule Prayer", prayer.toString());

            prayer.schedule(context);





        /** new end*/

    }
}

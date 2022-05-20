package com.example.android.masjeed.model;

import static com.example.android.masjeed.receivers.PrayerBroadcastReceiver.ASR;
import static com.example.android.masjeed.receivers.PrayerBroadcastReceiver.FAJR;
import static com.example.android.masjeed.receivers.PrayerBroadcastReceiver.ISHA;
import static com.example.android.masjeed.receivers.PrayerBroadcastReceiver.MAGRIB;
import static com.example.android.masjeed.receivers.PrayerBroadcastReceiver.SUNRISE;
import static com.example.android.masjeed.receivers.PrayerBroadcastReceiver.TITLE;
import static com.example.android.masjeed.receivers.PrayerBroadcastReceiver.ZUHR;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.android.masjeed.receivers.PrayerBroadcastReceiver;

import java.util.Calendar;

public class Prayer {

    private int alarmId;

    private boolean started, recurring = true;

    private int hour, minute;

    private String title;


    public Prayer(int alarmId, int hour, int minute, String title) {
        this.alarmId = alarmId;
        this.hour = hour;
        this.minute = minute;
        this.title = title;

    }

    public Prayer(){

    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public boolean isStarted() {
        return started;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }




    public void schedule(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, PrayerBroadcastReceiver.class);



        switch (title){
            case FAJR:
                intent.putExtra(TITLE, "Fajr Prayer");
                break;
            case SUNRISE:
                intent.putExtra(TITLE, "Sunrise Prayer");
                break;
            case ZUHR:
                {
                    intent.putExtra(TITLE, "Zuhr Prayer");

                    /**Calendar cal = Calendar.getInstance();
                    boolean isFriday = cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;
                    // changes the
                    if (isFriday) {
                        intent.putExtra(TITLE, "Jumma'ah Prayer");
                    } else {
                        intent.putExtra(TITLE, "Zuhr Prayer");
                    }*/
                }
                break;
            case ASR:
                intent.putExtra(TITLE, "Asr Prayer");
                break;
            case MAGRIB:
                intent.putExtra(TITLE, "Magrib Prayer");
                break;
            case ISHA:
                intent.putExtra(TITLE, "Isha Prayer");
                break;
            default:
                intent.putExtra(TITLE, "Snooze");
        }

        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        int hr = hour;
        int min = minute;

        if(recurring){
            if ((min - 5) < 0){
                hr = hr - 1;
                min = 60 - Math.abs(min - 5);
            } else {
                min = min - 5;
            }
        }

        calendar.set(Calendar.HOUR_OF_DAY, hr);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if(recurring){
            //calendar.add(calendar.MINUTE, -6);
        }

        // if alarm time has already passed, increment day by 1
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }

        if (!recurring) {
            String toastText = null;
            try {
                toastText = String.format("One Time Alarm %s scheduled for %s at %02d:%02d", title, getTitleText(), hour, minute, alarmId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();

            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    alarmPendingIntent
            );
        } else {
            String toastText = String.format("Recurring Alarm %s scheduled for %s at %02d:%02d", title, getTitleText(), hour, minute, alarmId);
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();

            final long RUN_DAILY = 24 * 60 * 60 * 1000;
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    alarmPendingIntent
            );
        }


        this.started = true;
    }

    public String getTitleText(){
        switch (title){
            case FAJR:
                return "Fajr";
            case SUNRISE:
                return "Sunrise";
            case ZUHR:
            {
                Calendar cal = Calendar.getInstance();
                boolean isFriday = cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;
                // changes the
                if (isFriday) {
                    return "Jumma'ah";
                } else {
                    return "Zuhr";
                }
            }
            case ASR:
                return "Asr";
            case MAGRIB:
                return "Magrib";
            case ISHA:
                return "Isha";
            default:
                return "Snooze";
        }
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, PrayerBroadcastReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0);
        alarmManager.cancel(alarmPendingIntent);
        this.started = false;

        String toastText = String.format("Alarm cancelled for %02d:%02d with id %d", hour, minute, alarmId);
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
        Log.i("cancel", toastText);
    }

    @Override
    public String toString() {
        return "Prayer{" +
                "alarmId=" + alarmId +
                ", hour=" + hour +
                ", minute=" + minute +
                ", title='" + title + '\'' +
                '}';
    }
}



package com.example.android.masjeed.services;

import static com.example.android.masjeed.receivers.PrayerBroadcastReceiver.TITLE;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.android.masjeed.R;
import com.example.android.masjeed.activities.PrayerActivity;
import com.example.android.masjeed.application.App;
import com.example.android.masjeed.receivers.DismissReceiver;
import com.example.android.masjeed.receivers.SnoozeReceiver;

public class PrayerService extends Service {

    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = MediaPlayer.create(this, R.raw.yusuf_islam);
        mediaPlayer.setLooping(true);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Intent notificationIntent = new Intent(this, PrayerActivity.class);
        Intent notificationIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        String alarmTitle = String.format("%s", intent.getStringExtra(TITLE));

        Intent dismissIntent = new Intent(this, DismissReceiver.class);

        Intent snoozeIntent = new Intent(this, SnoozeReceiver.class);

        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setContentTitle(alarmTitle)
                .setContentText("Prayer reminder")
                .setSmallIcon(R.drawable.notification_white)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.notification_white, "Dismiss", PendingIntent.getBroadcast(this, 0, dismissIntent, 0))
                .addAction(R.drawable.notification_white, "Snooze", PendingIntent.getBroadcast(this, 0, snoozeIntent, 0))
                .build();

        mediaPlayer.start();

        long[] pattern = { 0, 100, 1000 };
        vibrator.vibrate(pattern, 0);

        startForeground(1, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
        vibrator.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

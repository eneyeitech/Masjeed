package com.example.android.masjeed.services;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.Observer;

import com.example.android.masjeed.MosqueTimings;
import com.example.android.masjeed.PrayerTime;
import com.example.android.masjeed.model.Prayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PrayerAlarmService extends LifecycleService {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Map<String, Object> sx = MosqueTimings.getMosqueTiming();
        String sc = (String) sx.get("fajr");
        Log.d("#String_date::maghrib1", sc.substring(11,19) + "");

        List<PrayerTime> prayerTimes = new ArrayList<>();
        for(Map.Entry<String, Object> mp : sx.entrySet()){
            String tString = (String) mp.getValue();
            prayerTimes.add(new PrayerTime(mp.getKey(), tString.substring(11,19)));
        }
        //prayerTimes.add(new PrayerTime("mid", "24:00"));

        Collections.sort(prayerTimes);

        int counter = 0;
        for(PrayerTime p: prayerTimes){
            final int id = (int) System.currentTimeMillis();
            Prayer prayer = new Prayer(id, p.getHour(), p.getMinute(), p.getName());
            prayer.schedule(getApplicationContext());
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }
}

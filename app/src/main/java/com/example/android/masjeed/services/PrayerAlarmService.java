package com.example.android.masjeed.services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
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

        int id1 = 1;
        /**for(PrayerTime p: prayerTimes){

            final int id = (int) System.currentTimeMillis();
            Prayer prayer = new Prayer(id1, p.getHour(), p.getMinute(), p.getName());
            prayer.cancelAlarm(getApplicationContext());
            //prayer.schedule(getApplicationContext());
            id1++;
        }*/
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();


        String idsConcat = preferences.getString("idsConcat", "");
        Log.d("#Shared Pref with IDS", String.valueOf(idsConcat.isEmpty()));

        if(!idsConcat.isEmpty()){
            idsConcat = idsConcat.trim();
            String[] idsArr = idsConcat.split(",");
            for(int i=0;i<idsArr.length;i++){
                String idStr = idsArr[i];
                idStr = idStr.trim();
                Log.d("#Id Str", idStr);
                String time = preferences.getString(idStr,"");
                Log.d("#Time available", String.valueOf(time.isEmpty()));
                Log.d("#Time", time);
                if(!time.isEmpty()){
                    String[] times = time.split(":");
                    int hour = Integer.parseInt(times[0]);
                    int minute = Integer.parseInt(times[1]);
                    String name = times[2];
                    int idInt = Integer.parseInt(idStr);
                    Prayer prayer = new Prayer(idInt, hour, minute, name);
                    prayer.cancelAlarm(getApplicationContext());
                    editor.remove(idStr);
                    editor.apply();
                    Log.d("#Prayer", prayer.toString());

                }
            }
            editor.remove("idsContact");
            editor.apply();
        }

        idsConcat = preferences.getString("idsConcat", "");
        Log.d("Shared Pref with IDS", String.valueOf(idsConcat.isEmpty()));

        if(!idsConcat.isEmpty()){
            idsConcat = idsConcat.trim();
            String[] idsArr = idsConcat.split(",");
            for(int i=0;i<idsArr.length;i++){
                String idStr = idsArr[i];
                idStr = idStr.trim();
                Log.d("#idsList", idStr);
                String time = preferences.getString(idStr,"");
                Log.d("#listTime", time);
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        for(PrayerTime p: prayerTimes){

            final int id = (int) System.currentTimeMillis();
            Prayer prayer = new Prayer(id, p.getHour(), p.getMinute(), p.getName());
            stringBuilder.append(","+id);
            editor.putString(id+"", p.getHour()+":"+p.getMinute()+":"+p.getName());
            Log.d("#Prayer Schedule", prayer.toString());
            prayer.schedule(getApplicationContext());
            id1++;
        }
        editor.putString("idsConcat",stringBuilder.toString());
        editor.apply();
        editor.commit();

        Log.d("#IDSCONCAT", preferences.getString("idsConcat",""));

        idsConcat = preferences.getString("idsConcat", "");
        Log.d("Shared Pref with IDS", String.valueOf(idsConcat.isEmpty()));

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

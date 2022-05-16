package com.example.android.masjeed.services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.Observer;

import com.example.android.masjeed.model.Prayer;

import java.util.List;

public class ReschedulePrayerAlarmService extends LifecycleService {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();


        String idsConcat = preferences.getString("idsConcat", "");
        Log.d("#IDSCONCATStatus", String.valueOf(idsConcat.isEmpty()));

        if(!idsConcat.isEmpty()){
            idsConcat = idsConcat.trim();
            String[] idsArr = idsConcat.split(",");
            for(int i=0;i<idsArr.length;i++){
                String idStr = idsArr[i];
                idStr = idStr.trim();
                Log.d("#idsList", idStr);
                String time = preferences.getString(idStr,"");
                Log.d("#idsListB", String.valueOf(time.isEmpty()));
                Log.d("#idTime", time);
                if(!time.isEmpty()){
                    String[] times = time.split(":");
                    int hour = Integer.parseInt(times[0]);
                    int minute = Integer.parseInt(times[1]);
                    String name = times[2];
                    int idInt = Integer.parseInt(idStr);
                    Prayer prayer = new Prayer(idInt, hour, minute, name);
                    prayer.schedule(getApplicationContext());
                    Log.d("#ScheduledPrayer", prayer.toString());

                }
            }
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


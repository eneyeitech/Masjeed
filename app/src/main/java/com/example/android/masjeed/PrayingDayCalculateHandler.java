package com.example.android.masjeed;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.annotation.RequiresApi;

public class PrayingDayCalculateHandler extends IntentService {
    private static final int PRAYER_SIG = 110, AZKAR_SIG = 895;

    public PrayingDayCalculateHandler() {
        super(PrayingDayCalculateHandler.class.getSimpleName());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onHandleIntent(Intent intent) {


        Calendar c = Calendar.getInstance();
        int hourNow = c.get(Calendar.HOUR_OF_DAY);
        int minsNow = c.get(Calendar.MINUTE);


        Map<String, Object> sx = MosqueTimings.getMosqueTiming();
        String sc = (String) sx.get("fajr");
        Log.d("String_date::maghrib1", sc.substring(11,19) + "");
        //Log.d("String_date::maghrib2", Integer.parseInt(sc.substring(14,16)) + " SIZE "+prayers.length+" hournow "+hourNow+" minutenow " + minsNow);

        //Sun Jan 02 19:19:00 GMT+01:00 2022
        List<PrayerTime> prayerTimes = new ArrayList<>();
        for(Map.Entry<String, Object> mp : sx.entrySet()){
            String tString = (String) mp.getValue();
            prayerTimes.add(new PrayerTime(mp.getKey(), tString.substring(11,19)));
        }
        prayerTimes.add(new PrayerTime("mid", "24:00"));

        Collections.sort(prayerTimes);

        int counter = 0;
        for(PrayerTime p: prayerTimes){
            Log.d("String_date::name", p.getName()+"");
            counter++;
            if (hourNow < p.getHour()) {
                break;
            } else {
                if (hourNow == p.getHour()) {
                    if (minsNow < p.getMinute()) {
                        break;
                    }
                }
            }
        }

        for (int i = (counter - 1); i < prayerTimes.size(); i++) {
            //alarm for every prayer
            int hr = prayerTimes.get(i).getHour();
            int min = prayerTimes.get(i).getMinute();
            if ((min - 5) < 0){
                hr = hr - 1;
                min = 60 - Math.abs(min - 5);
            } else {
                min = min - 5;
            }

            Alarms.setNotificationAlarm(getApplicationContext(), hr
                    , min, PRAYER_SIG + i, i + "");
            Log.d("%s :: %s", hr + " "+min+" "+i);
            Log.d("String_date" , prayerTimes.get(i).getHour()+" "+prayerTimes.get(i).getMinute());

        }

        //reset widget for new changes
        sendBroadcast(new Intent().setAction("prayer.information.change"));

        stopSelf();
        PrayingDayCalculateAlarm.completeWakefulIntent(intent);
    }


}

package com.example.android.masjeed;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MosqueTimings {


    private static List<Object> timings = null;
    private static List<Object> mosqueDefault = new ArrayList<>();
    private static Map<String, Object> mosqueTiming = new HashMap<>();

    static {
        MosqueTimings.addMosqueTiming("fajr", "Fri Dec 31 05:20:00 GMT+01:00 2021");
        MosqueTimings.addMosqueTiming("sunrise", "Fri Dec 31 06:20:00 GMT+01:00 2021");
        MosqueTimings.addMosqueTiming("zuhr", "Fri Dec 31 13:20:00 GMT+01:00 2021");
        MosqueTimings.addMosqueTiming("asr", "Fri Dec 31 15:20:00 GMT+01:00 2021");
        MosqueTimings.addMosqueTiming("magrib", "Fri Dec 31 18:10:00 GMT+01:00 2021");
        MosqueTimings.addMosqueTiming("isha", "Fri Dec 31 19:19:00 GMT+01:00 2021");
    }


    public static List<Object> getTimings() {
        if(timings == null){
            mosqueDefault.add("Default Mosque");
            mosqueDefault.add("A0");
            mosqueDefault.add("05:20 AM");
            mosqueDefault.add("06:20 AM");
            mosqueDefault.add("13:14 PM");
            mosqueDefault.add("15:35 PM");
            mosqueDefault.add("18:10 PM");
            mosqueDefault.add("19:19 PM");
            mosqueDefault.add("12:30 PM");

            return mosqueDefault;
        }
        return timings;
    }

    public static void addMosqueTiming(String key, String value){
        mosqueTiming.put(key, value);
    }

    public static void setMosqueTiming(Map<String, Object> mosqueTiming){
        MosqueTimings.mosqueTiming = new HashMap<>(mosqueTiming);
    }

    public static Map<String, Object> getMosqueTiming(){
        return mosqueTiming;
    }

    public static void setTimings(List<Object> t){
        timings = t;

        Log.d("SUCCESS--TIMINGS::1", "" + t.get(0) + "," + t.get(1) + "," + t.get(2) + "," + t.get(3) + "," + t.get(4) + "," + t.get(5) + "," + t.get(6)+ "," + t.get(7) + "," + t.get(8));


    }

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */


    /**
     * Prints the names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
     */
    public static void main(String... args)  {

    }
}


package com.example.android.masjeed;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import androidx.legacy.content.WakefulBroadcastReceiver;

public class SilentMoodAlarm extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //MindtrackLog.add("Silent Mood");

        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        mAudioManager.setRingerMode(0);
        if (false)
            //mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            mAudioManager.setRingerMode(1);
        Alarms.NormalAudio(context);
    }
}

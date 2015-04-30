package com.youngbin.programmers.proverbs;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by youngbin on 15. 4. 30.
 */
public class AutoUpdateAlarm {
    Context mContext;
    AlarmManager AM;
    Intent UpdateIntent;
    PendingIntent AlarmIntent;
    String TAG = "AutoUpdateAlarm";

    public AutoUpdateAlarm(Context c){
        Log.d(TAG, "INIT");
        mContext = c;
        AM = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        UpdateIntent = new Intent(c, ProverbUpdateService.class);
        UpdateIntent.putExtra("notify",true);
        AlarmIntent = PendingIntent.getService(c,0,UpdateIntent,0);
    }

    public void enableAlarm(){
        Log.d(TAG,"ENABLING");
        // Set the alarm to start at approximately 80:00 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);

        AM.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, AlarmIntent);
        Log.d(TAG, "ENABLED");
    }

    public void disableAlarm(){
        Log.d(TAG,"DISABLING");
        if (AM!= null) {
            AM.cancel(AlarmIntent);
            Log.d(TAG, "DISABLED");
        }
    }
}

package com.youngbin.programmers.proverbs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        AutoUpdateAlarm AUA = new AutoUpdateAlarm(context);

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isAlarmOn = SP.getBoolean("auto_update",true);
        if(isAlarmOn){
            AUA.enableAlarm();
        }else {
            AUA.disableAlarm();
        }
    }
}

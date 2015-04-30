package com.youngbin.programmers.proverbs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import com.youngbin.programmers.proverbs.compat.PreferenceFragment;


public class SettingsFragment extends PreferenceFragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_general);

        //Get app version name from Manifest
        String app_ver = null;
        try {
            app_ver = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



        findPreference("ver").setSummary(app_ver);
        findPreference("src").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://github.com/sukso96100/programmers-proverbs-android"));
                startActivity(intent);
                return false;
            }
        });
        findPreference("web").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://youngbin.tk"));
                startActivity(intent);
                return false;
            }
        });
        findPreference("auto_update").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity());
                boolean isAlarmOn = SP.getBoolean("auto_update",true);
                AutoUpdateAlarm AUA = new AutoUpdateAlarm(getActivity());
                if (isAlarmOn) {
                    AUA.enableAlarm();
                } else {
                    AUA.disableAlarm();
                }
                return false;
            }
        });
    }
}
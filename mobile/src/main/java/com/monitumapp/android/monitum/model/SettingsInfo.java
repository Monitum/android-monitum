package com.monitumapp.android.monitum.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Settings values.
 */
public class SettingsInfo {
    public static final String LUMINOUS_SWITCH = "luminous_switch";
    public static final String TIMECYCLE_SWITCH = "timecycle_switch";
    public static final String CALENDAR_OR_LIST_SWITCH = "calendar_or_list";

    public boolean luminous;
    public boolean timeCycle;
    public boolean calendarOrList;     // show details on Calendar or List

    public void loadFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences != null) {
            luminous       = sharedPreferences.getBoolean(SettingsInfo.LUMINOUS_SWITCH, true);
            timeCycle      = sharedPreferences.getBoolean(SettingsInfo.TIMECYCLE_SWITCH, true);
            calendarOrList = sharedPreferences.getBoolean(SettingsInfo.CALENDAR_OR_LIST_SWITCH, false);
        }
    }
}

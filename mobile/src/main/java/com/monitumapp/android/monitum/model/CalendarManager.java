package com.monitumapp.android.monitum.model;

import android.text.TextUtils;

/**
 * Parses Monitum Calendar and creates a list of HolyDayInfo objects.
 */
public class CalendarManager {

    private static CalendarManager sInstance;

    private MonitumCalendar mMonitumCalendar;

    public static CalendarManager newInstance() {
        if (sInstance == null) {
            sInstance = new CalendarManager();
        }
        return sInstance;
    }

    public void parseCalendarJson(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) return;
        mMonitumCalendar = new MonitumCalendar();
        mMonitumCalendar.parseFromJson(jsonStr);
    }

    public MonitumCalendar getMonitumCalendar() {
        return mMonitumCalendar;
    }
}

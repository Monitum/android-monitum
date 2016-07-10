package com.monitumapp.android.monitum.model;

import android.util.Log;

import com.monitumapp.android.monitum.MonitumStackRemoteViewsFactory;
import com.monitumapp.android.monitum.utils.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Holds a list of HolyDayInfo entries.
 */
public class MonitumCalendar {

    private static final String MONITUM_CALENDAR = "MonitumCalendar";
    private static final String TAG = MonitumCalendar.class.getSimpleName();

    // TODO make sure it is false for production
    private static final boolean TESTING_MODE = false;

    public void setHolyDayInfoList(List<HolyDayInfo> holyDayInfoList) {
        this.holyDayInfoList = holyDayInfoList;
    }

    private List<HolyDayInfo> holyDayInfoList;
    private SettingsInfo mSettingsInfo;

    public void parseFromJson(String jsonStr) {
        holyDayInfoList = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObj.getJSONArray(MONITUM_CALENDAR);

            HolyDayInfo holyDayInfo;
            for (int i = 0; i < jsonArray.length(); i++) {
                holyDayInfo = new HolyDayInfo();
                holyDayInfo.parseJson(jsonArray.getJSONObject(i));
                holyDayInfoList.add(holyDayInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * This is the full list.
     * @return
     */
    public List<HolyDayInfo> getHolyDayInfoList() {
        return holyDayInfoList;
    }

    public HolyDayInfo findTodayMonitumInfo() {
        if (holyDayInfoList != null) {
            if (TESTING_MODE) {
                // return a random number each time to test widget
                Random random = new Random();
                int index = (random.nextInt(holyDayInfoList.size() - 1));
                Log.d(TAG, "[findTodayMonitumInfo] index = " + index + " of " + holyDayInfoList.size() + ", date = " + DateUtils.formatDayOnly(holyDayInfoList.get(index).getDate()));
                return holyDayInfoList.get(index);
            } else {
                for (HolyDayInfo info : holyDayInfoList) {
                    if (info.isToday()) {
                        Log.d(TAG, "[findTodayMonitumInfo] date = " + DateUtils.formatDayOnly(info.getDate()));
                        return info;
                    }
                }
            }
        }
        return null;    // not found
    }

    /**
     * This is the sub-list of days to display on app widget.
     * @return
     */
    public List<HolyDayInfo> getHolyDayInfoDisplayList() {
        int startIndex = 0; // FIXME set today's index here
        int endIndex = MonitumStackRemoteViewsFactory.MAX_VIEW_COUNT;

        List<HolyDayInfo> displayList = null;
        if (holyDayInfoList != null) {
            displayList = holyDayInfoList.subList(startIndex, Math.min(endIndex, holyDayInfoList.size()));
        }
        return displayList;
    }

    public void setSettingsInfo(SettingsInfo info) {
        mSettingsInfo = info;
    }

    public SettingsInfo getSettingsInfo() {
        return mSettingsInfo;
    }
}

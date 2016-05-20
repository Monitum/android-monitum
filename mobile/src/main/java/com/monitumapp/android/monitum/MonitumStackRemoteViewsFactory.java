package com.monitumapp.android.monitum;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.monitumapp.android.monitum.model.CalendarManager;
import com.monitumapp.android.monitum.model.HolyDayInfo;
import com.monitumapp.android.monitum.model.MonitumCalendar;
import com.monitumapp.android.monitum.utils.DateUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Provide adapter info for drawing views.
 */
public class MonitumStackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = MonitumStackRemoteViewsFactory.class.getSimpleName();
    public static final int MAX_VIEW_COUNT = 10000;
    private final Context mContext;
    private int mAppWidgetId;
    private MonitumCalendar mMonitumCalendar;

    public MonitumStackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        CalendarManager calendarManager = CalendarManager.newInstance();
        mMonitumCalendar = calendarManager.getMonitumCalendar();
        Log.d(TAG, "[onCreate]");
    }

    @Override
    public void onDataSetChanged() {
        Log.d(TAG, "[onDataSetChanged]");
        run();  // fetch data
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return MAX_VIEW_COUNT;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d(TAG, "[getViewAt] position " + position);
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.item_monitum_appwidget_1x4);
        if (mMonitumCalendar != null && mMonitumCalendar.getHolyDayInfoDisplayList() != null) {
            List<HolyDayInfo> displayList = mMonitumCalendar.getHolyDayInfoDisplayList();
            if (displayList != null && position < displayList.size()) {
                HolyDayInfo holyDayInfo = displayList.get(position);

                // date
                CharSequence dayOnly = DateUtils.formatDayOnly(holyDayInfo.getDate());
                rv.setTextViewText(R.id.dateText, dayOnly);
                rv.setImageViewResource(R.id.dayColorImageView, holyDayInfo.getDaySquareImageId());
                if (holyDayInfo.getRosaryDetails() != null) {
                    rv.setTextViewText(R.id.rosaryTextView, holyDayInfo.getRosaryDetails().getTraditionalSeason());
                }
            }
        }
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public void run() {
        CalendarManager calendarManager = CalendarManager.newInstance();
        String jsonStr = loadJsonString();
        calendarManager.parseCalendarJson(jsonStr);

        mMonitumCalendar = calendarManager.getMonitumCalendar();
    }
    private String loadJsonString() {
        String jsonStr = "";

        try {
            jsonStr = readFromAssets(mContext, "monitum_info.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    // Anonymous code snippet
    public String readFromAssets(Context context, String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));

        // do reading, usually loop until end of file reading
        StringBuilder sb = new StringBuilder();
        String mLine = reader.readLine();
        while (mLine != null) {
            sb.append(mLine); // process line
            mLine = reader.readLine();
        }
        reader.close();
        return sb.toString();
    }

}

package com.monitumapp.android.monitum;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bottlerocketstudios.groundcontrol.convenience.GroundControl;
import com.bottlerocketstudios.groundcontrol.listener.AgentListener;
import com.monitumapp.android.monitum.agents.MonitumCalendarAgent;
import com.monitumapp.android.monitum.model.CalendarManager;
import com.monitumapp.android.monitum.model.HolyDayInfo;
import com.monitumapp.android.monitum.model.MonitumCalendar;

import java.util.List;

/**
 * Monitum Android App Widget Entry Point.
 */
public class MonitumStackWidgetProvider extends AppWidgetProvider {

    private static final String TAG = MonitumStackWidgetProvider.class.getSimpleName();

    public static final String LIST_ACTION = "com.monitumapp.android.monitum.LIST_ACTION";
    public static final String TOAST_ACTION = "com.monitumapp.android.monitum.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.monitumapp.android.monitum.EXTRA_ITEM";
    public static final String EXTRA_VIEW_TYPE = "com.monitumapp.android.monitum.EXTRA_VIEW_TYPE";

    public static final int TYPE_HDO = 0;
    public static final int TYPE_SDP = 1;
    public static final int TYPE_DIETARY_RESTRICTION = 2;
    public static final int TYPE_ROSARY = 3;

    private MonitumCalendar mMonitumCalendar;

    /**
     * For communication between widget UI and provider.
     * For communication between widget UI and provider.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        CalendarManager calendarManager = CalendarManager.newInstance();
        mMonitumCalendar = calendarManager.getMonitumCalendar();
        if (intent.getAction().equals(TOAST_ACTION)) {
            Log.d(TAG, "[onReceive] TOAST_ACTION");
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            int viewIndex = intent.getIntExtra(EXTRA_ITEM, appWidgetId);
            String reason = "-";
            int viewType = intent.getIntExtra(EXTRA_VIEW_TYPE, 0);
            switch (viewType) {
                case TYPE_HDO:
                    reason = mMonitumCalendar.findTodayMonitumInfo().getHdoName();
                    break;
                case TYPE_SDP:
                    reason = mMonitumCalendar.findTodayMonitumInfo().getSdpName();
                    break;
                case TYPE_DIETARY_RESTRICTION:
                    reason = mMonitumCalendar.findTodayMonitumInfo().getDietaryRestrictionsName();
                    break;
                case TYPE_ROSARY:
                    reason = mMonitumCalendar.findTodayMonitumInfo().getRosaryDetails().getReason(mMonitumCalendar.getSettingsInfo());
                    break;
                default:
                    break;
            }
            Toast.makeText(context, reason, Toast.LENGTH_SHORT).show();
        } else if (intent.getAction().equals(LIST_ACTION)) {
            // This activity will show either Calendar or List based on Settings
            Intent activityIntent = new Intent(context, MonitumListActivity.class);
            activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activityIntent);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        CalendarManager calendarManager = CalendarManager.newInstance();
        mMonitumCalendar = calendarManager.getMonitumCalendar();
        for (int i=0;i<appWidgetIds.length;i++) {
            Log.d(TAG, "[onUpdate] view #" + i);
            RemoteViews rv = buildView(context, appWidgetIds[i]);

            //appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.);
            appWidgetManager.updateAppWidget(appWidgetIds, rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @NonNull
    private RemoteViews buildView(Context context, int appWidgetId) {
        // get views from service
        Log.d(TAG, "[buildView] appWidgetId = " + appWidgetId);
//        Intent intent = new Intent(context, MonitumStackWidgetService.class);
//        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
//        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.item_monitum_appwidget_1x4);
//        rv.setRemoteAdapter(appWidgetId, intent);

        populateViews(context, rv, appWidgetId);

//        Intent toastIntent = new Intent(context, MonitumStackWidgetProvider.class);
//        toastIntent.setAction(MonitumStackWidgetProvider.TOAST_ACTION);
//        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
//        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
//        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        rv.setPendingIntentTemplate(R.id.list_view, toastPendingIntent);
        return rv;
    }

    private void populateViews(Context context, RemoteViews rv, int appWidgetId) {
        if (mMonitumCalendar != null && mMonitumCalendar.findTodayMonitumInfo() != null) {
            HolyViewHolder vh = new HolyViewHolder(rv, mMonitumCalendar.findTodayMonitumInfo(), mMonitumCalendar.getSettingsInfo());
            vh.update(context, appWidgetId);
        }
    }

    private void fetchCalendarData(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds, final RemoteViews rv) {
        MonitumCalendarAgent agent = new MonitumCalendarAgent(context);
        Log.d(TAG, "[agent] started fetching calendar data.");
        GroundControl.uiAgent(this, agent)
                .uiCallback(new AgentListener<MonitumCalendar, Integer>() {
                    @Override
                    public void onCompletion(String agentIdentifier, MonitumCalendar result) {
                        if (result != null) {
                            List<HolyDayInfo> list = result.getHolyDayInfoList();
                            if (list != null) {
                                Log.d(TAG, "[agent] found " + list.size() + " calendar entries.");
                                HolyDayInfo todayInfo = result.findTodayMonitumInfo();
                                if (todayInfo != null) {
                                    Log.d(TAG, "[agent] found today.");
                                    updateUI(context, todayInfo, appWidgetManager, appWidgetIds, rv);
                                }
                            }
                        }
                    }

                    @Override
                    public void onProgress(String agentIdentifier, Integer progress) {

                    }
                })
                .execute();
    }

    private void updateUI(Context context, HolyDayInfo todayInfo, AppWidgetManager appWidgetManager, int[] appWidgetIds, RemoteViews rv) {
        if (todayInfo == null) {
            Log.e(TAG, "[updateUI] no Monitum data for today.");
        } else {
            Log.e(TAG, "[updateUI] update views.");
        }
    }

}

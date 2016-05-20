package com.monitumapp.android.monitum;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.monitumapp.android.monitum.model.HolyDayInfo;
import com.monitumapp.android.monitum.model.SettingsInfo;
import com.monitumapp.android.monitum.utils.DateUtils;

/**
 * Displays Widget View and sets up action buttons.
 */
public class HolyViewHolder {

    private static final String TAG = HolyViewHolder.class.getSimpleName();
    private final RemoteViews mRemoteViews;
    private final HolyDayInfo mHolyDaysInfo;
    private final SettingsInfo mSettingsInfo;

    public HolyViewHolder(RemoteViews remoteViews, HolyDayInfo holyDayInfo, SettingsInfo settingsInfo) {
        mRemoteViews = remoteViews;
        mHolyDaysInfo = holyDayInfo;
        mSettingsInfo = settingsInfo;
    }

    public void update(Context context, int widgetId) {
        if (mHolyDaysInfo == null) return;
        String dayOnly = DateUtils.formatDayOnly(mHolyDaysInfo.getDate());
        Log.d(TAG, "[populateViews] holyDay " + dayOnly);

        // date
        mRemoteViews.setTextViewText(R.id.dateText, dayOnly);
        setOnClickListIntent(context, widgetId);
        mRemoteViews.setImageViewResource(R.id.dayColorImageView, mHolyDaysInfo.getDaySquareImageId());

        // HDO
        mRemoteViews.setImageViewResource(R.id.hdoButton, mHolyDaysInfo.getHdoImageId());
        if (mHolyDaysInfo.isHdo()) {
            setOnClickIntent(context, widgetId, R.id.hdoButton);
        }

        // SDP
        mRemoteViews.setImageViewResource(R.id.sdpButton, mHolyDaysInfo.getSdpImageId());
        if (mHolyDaysInfo.isSdp()) {
            setOnClickIntent(context, widgetId, R.id.sdpButton);
        }
        // dietary restrictions
        mRemoteViews.setImageViewResource(R.id.dietaryRestrictionsImageView, mHolyDaysInfo.getDietaryRestrictionImageId());
        if (mHolyDaysInfo.getDaySquareImageId() != R.drawable.ic_fish) {
            setOnClickIntent(context, widgetId, R.id.dietaryRestrictionsImageView);
        }

        // rosary code
        if (mHolyDaysInfo.getRosaryDetails() != null && mSettingsInfo != null) {
            String rosaryCode = mHolyDaysInfo.getRosaryDetails().getCode(mSettingsInfo);
            mRemoteViews.setTextViewText(R.id.rosaryTextView, rosaryCode);
        }
    }

    private void setOnClickListIntent(Context context, int appWidgetId) {
        final Intent onClickIntent = new Intent(context, MonitumStackWidgetProvider.class);
        onClickIntent.setAction(MonitumStackWidgetProvider.LIST_ACTION);
        onClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        onClickIntent.setData(Uri.parse(onClickIntent.toUri(Intent.URI_INTENT_SCHEME)));
        final PendingIntent onClickPendingIntent = PendingIntent.getBroadcast(context, 0,
                onClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.dateText, onClickPendingIntent);
    }

    private void setOnClickIntent(Context context, int appWidgetId, int resId) {
        final Intent onClickIntent = new Intent(context, MonitumStackWidgetProvider.class);
        onClickIntent.setAction(MonitumStackWidgetProvider.TOAST_ACTION);
        onClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        int viewType = MonitumStackWidgetProvider.TYPE_HDO;
        switch (resId) {
            case R.id.hdoButton:
                viewType = MonitumStackWidgetProvider.TYPE_HDO;
                break;
            case R.id.sdpButton:
                viewType = MonitumStackWidgetProvider.TYPE_SDP;
                break;
            case R.id.dietaryRestrictionsImageView:
                viewType = MonitumStackWidgetProvider.TYPE_DIETARY_RESTRICTION;
                break;
            case R.id.rosaryImageView:
                viewType = MonitumStackWidgetProvider.TYPE_ROSARY;
                break;
            default:
                break;
        }
        onClickIntent.putExtra(MonitumStackWidgetProvider.EXTRA_VIEW_TYPE, viewType);
        onClickIntent.setData(Uri.parse(onClickIntent.toUri(Intent.URI_INTENT_SCHEME)));
        final PendingIntent onClickPendingIntent = PendingIntent.getBroadcast(context, 0,
                onClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(resId, onClickPendingIntent);
    }
}

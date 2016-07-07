package com.monitumapp.android.monitum;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.monitumapp.android.monitum.model.HolyDayInfo;
import com.monitumapp.android.monitum.model.SettingsInfo;
import com.monitumapp.android.monitum.utils.DateUtils;

/**
 * Monitum - 4/29/16.
 */
public class HolyRecyclerViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = HolyRecyclerViewHolder.class.getSimpleName();

    ImageView sdpButton;
    ImageView hdoButton;
    TextView rosaryCodeTextView;

    private SettingsInfo mSettingsInfo;
    TextView dateTextView;
    ImageView dayColorImageView;
    ImageView dietaryRestrictionsImageView;


    public HolyRecyclerViewHolder(View view, SettingsInfo settingsInfo) {
        super(view);
        mSettingsInfo = settingsInfo;
        dateTextView = (TextView) view.findViewById(R.id.dateText);
        dayColorImageView = (ImageView) view.findViewById(R.id.dayColorImageView);
        hdoButton = (ImageView) view.findViewById(R.id.hdoButton);
        sdpButton = (ImageView) view.findViewById(R.id.sdpButton);
        dietaryRestrictionsImageView = (ImageView) view.findViewById(R.id.dietaryRestrictionsImageView);
        rosaryCodeTextView = (TextView) view.findViewById(R.id.rosaryTextView);

    }

    public void update(Context context, HolyDayInfo mHolyDaysInfo) {
        if (mHolyDaysInfo == null) return;
        String dayOnly = DateUtils.formatDayOnly(mHolyDaysInfo.getDate());
        Log.d(TAG, "[populateViews] holyDay " + dayOnly);

        // date
        dateTextView.setText(dayOnly);
        dayColorImageView.setImageResource(mHolyDaysInfo.getDaySquareImageId());

        // HDO
        hdoButton.setImageResource(mHolyDaysInfo.getHdoImageId());
        if (mHolyDaysInfo.isHdo()) {
            //setOnClickIntent(context, widgetId, R.id.hdoButton);
        }

        // SDP
        sdpButton.setImageResource(mHolyDaysInfo.getSdpImageId());
        if (mHolyDaysInfo.isSdp()) {
            //setOnClickIntent(context, widgetId, R.id.sdpButton);
        }
        // dietary restrictions
        dietaryRestrictionsImageView.setImageResource(mHolyDaysInfo.getDietaryRestrictionImageId());
        if (mHolyDaysInfo.getDaySquareImageId() != R.drawable.ic_fish) {
            //setOnClickIntent(context, widgetId, R.id.dietaryRestrictionsImageView);
        }

        // rosary code
        if (mHolyDaysInfo.getRosaryDetails() != null && mSettingsInfo != null) {
            String rosaryCode = mHolyDaysInfo.getRosaryDetails().getCode(mSettingsInfo);
            rosaryCodeTextView.setText(rosaryCode);
        }
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
        //mRemoteViews.setOnClickPendingIntent(resId, onClickPendingIntent);
    }

}

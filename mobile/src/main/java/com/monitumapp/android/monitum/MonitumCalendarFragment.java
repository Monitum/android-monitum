package com.monitumapp.android.monitum;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.bottlerocketstudios.groundcontrol.convenience.GroundControl;
import com.bottlerocketstudios.groundcontrol.listener.AgentListener;
import com.monitumapp.android.monitum.agents.MonitumCalendarAgent;
import com.monitumapp.android.monitum.model.CalendarManager;
import com.monitumapp.android.monitum.model.HolyDayInfo;
import com.monitumapp.android.monitum.model.MonitumCalendar;
import com.monitumapp.android.monitum.utils.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Shows Calendar and Holy Day Info for selected day.
 */
public class MonitumCalendarFragment extends Fragment {

    private static final String TAG = MonitumCalendarFragment.class.getSimpleName();
    private MonitumCalendar mMonitumCalendar;
    private Map<String,HolyDayInfo> mHolyDayInfoMap = new HashMap<>();

    private CalendarView mCalendarView;
    private View mHolyDayInfoView;  // parent container
    private HolyRecyclerViewHolder mViewHolder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMonitumCalendar = CalendarManager.newInstance().getMonitumCalendar();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monitum_calendar, container, false);

        // holy day info
        mHolyDayInfoView = view.findViewById(R.id.holyday_info_view);
        mHolyDayInfoView.setVisibility(View.GONE);
        mViewHolder = new HolyRecyclerViewHolder(mHolyDayInfoView, mMonitumCalendar.getSettingsInfo());

        // calendar
        mCalendarView = (CalendarView) view.findViewById(R.id.calendar_view);
        mCalendarView.setDate(System.currentTimeMillis());
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String dateStr = DateUtils.formatDateOnly(month, dayOfMonth, year);
                HolyDayInfo info = mHolyDayInfoMap.get(dateStr);
                showInfo(dateStr, info);
            }
        });
        return view;
    }

    private void showInfo(String dateStr, HolyDayInfo info) {
        if (info == null) {
            Toast.makeText(getContext(), getString(R.string.err_no_info, dateStr), Toast.LENGTH_SHORT).show();
            mHolyDayInfoView.setVisibility(View.GONE);
        } else {
            // update Holy Day Info
            mViewHolder.update(getContext(), info);
            mHolyDayInfoView.setVisibility(View.VISIBLE);
        }
    }

    public static Fragment newInstance() {
        return new MonitumCalendarFragment();
    }

    @Override
    public void onStart() {
        super.onStart();

        fetchCalendarData();
    }

    private void fetchCalendarData() {
        MonitumCalendarAgent agent = new MonitumCalendarAgent(getActivity());
        GroundControl.uiAgent(this, agent)
                .uiCallback(new AgentListener<MonitumCalendar, Integer>() {
                    @Override
                    public void onCompletion(String agentIdentifier, MonitumCalendar result) {
                        if (result != null) {
                            mMonitumCalendar.setHolyDayInfoList(result.getHolyDayInfoList());
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    buildMap();
                                    showTodaysInfo();
                                }
                            });
                        }
                    }

                    @Override
                    public void onProgress(String agentIdentifier, Integer progress) {

                    }
                })
                .execute();
    }

    private void showTodaysInfo() {
        Date date = new Date();
        String dateStr = DateUtils.formatDateOnly(date);
        HolyDayInfo info = mHolyDayInfoMap.get(dateStr);
        showInfo(dateStr, info);
    }

    /**
     * Build a Map for quick lookup.
     */
    private void buildMap() {
        if (mMonitumCalendar == null || mMonitumCalendar.getHolyDayInfoDisplayList() == null) return;
        int i=0;
        for (HolyDayInfo info : mMonitumCalendar.getHolyDayInfoDisplayList()) {
            String dateStr = DateUtils.formatDateOnly(info.getDate());
            mHolyDayInfoMap.put(dateStr, info);
            i++;
        }
        Log.d(TAG, "[buildMap] stored " + i + " entries in lookup map.");
    }

}

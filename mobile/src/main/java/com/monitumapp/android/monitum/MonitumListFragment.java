package com.monitumapp.android.monitum;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bottlerocketstudios.groundcontrol.convenience.GroundControl;
import com.bottlerocketstudios.groundcontrol.listener.AgentListener;
import com.monitumapp.android.monitum.agents.MonitumCalendarAgent;
import com.monitumapp.android.monitum.model.CalendarManager;
import com.monitumapp.android.monitum.model.HolyDayInfo;
import com.monitumapp.android.monitum.model.MonitumCalendar;

import java.util.ArrayList;
import java.util.List;

/**
 * Monitum - 4/29/16.
 */
public class MonitumListFragment extends Fragment {

    private static final String TAG = MonitumListFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MonitumCalendar mMonitumCalendar;
    private MonitumRecyclerViewAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMonitumCalendar = CalendarManager.newInstance().getMonitumCalendar();
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
                            mAdapter.swapData(mMonitumCalendar.getHolyDayInfoList());

                        }
                    }

                    @Override
                    public void onProgress(String agentIdentifier, Integer progress) {

                    }
                })
                .execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monitum_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new MonitumRecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    public static Fragment newInstance() {
        return new MonitumListFragment();
    }

    public class MonitumRecyclerViewAdapter extends RecyclerView.Adapter<HolyRecyclerViewHolder> {

        private List<HolyDayInfo> mList = new ArrayList<>();

        @Override
        public HolyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_monitum_appwidget_1x4, parent, false);
            return new HolyRecyclerViewHolder(view, mMonitumCalendar.getSettingsInfo());
        }

        @Override
        public void onBindViewHolder(HolyRecyclerViewHolder holder, int position) {
            HolyDayInfo info = mList.get(position);
            holder.update(getContext(), info);
        }

        @Override
        public int getItemCount() {
            Log.d(TAG, "[getItemCount] size " + mList.size());
            return mList.size();
        }

        public void swapData(List<HolyDayInfo> list) {
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

}

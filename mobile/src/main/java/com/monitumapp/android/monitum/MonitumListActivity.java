package com.monitumapp.android.monitum;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.monitumapp.android.monitum.model.SettingsInfo;

/**
 * Shows all dates - either in RecyclerView or CalendarView (depending on Setting).
 */
public class MonitumListActivity extends AppCompatActivity {

    private static final String RECYCLER_VIEW_FRAG = "recycler_view_fragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_monitum_list);

        if (savedInstanceState == null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            boolean calendarOrList = true;
            if (sharedPreferences != null) {
                // default should be List
                calendarOrList = sharedPreferences.getBoolean(SettingsInfo.CALENDAR_OR_LIST_SWITCH, false);
            }

            // show appropriate fragment based on settings
            Fragment fragment = calendarOrList ? MonitumCalendarFragment.newInstance() : MonitumListFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.top_container, fragment, RECYCLER_VIEW_FRAG)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}

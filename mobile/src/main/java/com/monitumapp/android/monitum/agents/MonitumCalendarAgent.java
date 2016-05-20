package com.monitumapp.android.monitum.agents;

import android.content.Context;

import com.bottlerocketstudios.groundcontrol.agent.AbstractAgent;
import com.monitumapp.android.monitum.model.CalendarManager;
import com.monitumapp.android.monitum.model.MonitumCalendar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Loads Calendor JSON file.
 */
public class MonitumCalendarAgent extends AbstractAgent<MonitumCalendar,Integer> {
    private static final String TAG = MonitumCalendarAgent.class.getSimpleName();
    private static final String FROM_LOCAL_JSON = "fromLocalJson";
    private final Context mContext;

    public MonitumCalendarAgent(Context context) {
        mContext = context;
    }

    @Override
    public String getUniqueIdentifier() {
        return FROM_LOCAL_JSON;
    }

    @Override
    public void cancel() {

    }

    @Override
    public void onProgressUpdateRequested() {

    }

    @Override
    public void run() {
        CalendarManager calendarManager = CalendarManager.newInstance();
        String jsonStr = loadJsonString();
        calendarManager.parseCalendarJson(jsonStr);

        MonitumCalendar calendar = calendarManager.getMonitumCalendar();

        // notify completion
        getAgentListener().onCompletion(getUniqueIdentifier(), calendar);
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

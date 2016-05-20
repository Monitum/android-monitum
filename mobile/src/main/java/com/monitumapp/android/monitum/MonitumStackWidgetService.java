package com.monitumapp.android.monitum;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

/**
 * Helps draw Views in the stack.
 */
public class MonitumStackWidgetService extends RemoteViewsService {

    private static final String TAG = MonitumStackWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(TAG, "[onGetViewFactory]");
        return new MonitumStackRemoteViewsFactory(this.getApplicationContext(), intent);
    }

}


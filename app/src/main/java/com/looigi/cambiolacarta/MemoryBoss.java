package com.looigi.cambiolacarta;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

public class MemoryBoss implements ComponentCallbacks2 {
    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
    }

    @Override
    public void onLowMemory() {
    }

    @Override
    public void onTrimMemory(final int level) {
        Log l = new Log();
        l.ScriveLog("MemoryBoss","Entrato in azione memory boss:" + level);
        if (level == ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE) {
            l.ScriveLog("MemoryBoss","Riavvio applicazione");
            Context context = SharedObjects.getInstance().getContext();
            Intent mStartActivity = new Intent(context, MainActivity.class);
            int mPendingIntentId = 123456;
            PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
            System.exit(0);
        }
        // you might as well implement some memory cleanup here and be a nice Android dev.
    }
}
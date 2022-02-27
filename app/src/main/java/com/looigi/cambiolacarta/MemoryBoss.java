package com.looigi.cambiolacarta;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

public class MemoryBoss implements ComponentCallbacks2 {
    Log l = new Log();

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        l.ScriveLog(new Object() {
                }.getClass().getEnclosingMethod().getName(),
                "Memory boss: configurazione cambiata");
        l.ScriveLog(new Object() {
                }.getClass().getEnclosingMethod().getName(),
                String.valueOf(newConfig));
    }

    @Override
    public void onLowMemory() {
        l.ScriveLog(new Object() {
                }.getClass().getEnclosingMethod().getName(),
                "Memory boss: Memoria Bassa");
    }

    @Override
    public void onTrimMemory(final int level) {
        String descrizione = "";

        switch (level) {
            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
                descrizione = "TRIM_MEMORY_BACKGROUND";
                break;
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                descrizione = "TRIM_MEMORY_COMPLETE";
                break;
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
                descrizione = "TRIM_MEMORY_MODERATE";
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:
                descrizione = "TRIM_MEMORY_RUNNING_CRITICAL";
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
                descrizione = "TRIM_MEMORY_RUNNING_LOW";
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
                descrizione = "TRIM_MEMORY_RUNNING_MODERATE";
                break;
            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:
                descrizione = "TRIM_MEMORY_UI_HIDDEN";
                break;
            default:
                descrizione="Altro";
                break;
        }

        l.ScriveLog(new Object() {
                }.getClass().getEnclosingMethod().getName(),
                "Memory boss: " + String.valueOf(level) + " -> " + descrizione);
        // you might as well implement some memory cleanup here and be a nice Android dev.
        if (level == ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE) {
            Log l = new Log();

            l.ScriveLog(new Object() {
                    }.getClass().getEnclosingMethod().getName(),
                    "Memory Boss. Riavvio l'applicazione");
            Context context = VariabiliGlobali.getInstance().getContext();
            Intent mStartActivity = new Intent(context, MainActivity.class);
            int mPendingIntentId = 123456;
            PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
            System.exit(0);
        }

    }
}

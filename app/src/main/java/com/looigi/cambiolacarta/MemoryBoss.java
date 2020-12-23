/* package com.looigi.cambiolacarta;

import android.content.ComponentCallbacks2;
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
    }
}
*/
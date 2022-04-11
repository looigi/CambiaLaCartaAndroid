package com.looigi.cambiolacarta;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PhoneUnlockedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log l = new Log();

        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
            VariabiliGlobali.getInstance().setScreenON(true);

            l.ScriveLog(new Object() {
                    }.getClass().getEnclosingMethod().getName(),
                    "Schermo attivo. Flag immagine: " + VariabiliGlobali.getInstance().isCambiataImmagine());

            if (VariabiliGlobali.getInstance().isCambiataImmagine()) {
                VariabiliGlobali.getInstance().setCambiataImmagine(false);

                l.ScriveLog(new Object() {
                        }.getClass().getEnclosingMethod().getName(),
                        "Schermo attivo. Cambio immagine");

                Utility u = new Utility();
                u.CambiaImmagine(true, 0);
            }
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            VariabiliGlobali.getInstance().setScreenON(false);

            l.ScriveLog(new Object() {
                    }.getClass().getEnclosingMethod().getName(),
                    "Schermo NON attivo");
        }
    }
}
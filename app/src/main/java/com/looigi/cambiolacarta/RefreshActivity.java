package com.looigi.cambiolacarta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

public class RefreshActivity {
    private static final RefreshActivity ourInstance = new RefreshActivity();

    private Runnable runRiga;
    private Handler hSelezionaRiga;
    private int conta;
    protected static Context context;
    protected static Activity act;

    public static RefreshActivity getInstance() {
        return ourInstance;
    }

    public void RilanciaServizio(final Context context, final Activity v) {
        this.context = context;
        this.act = v;
        conta=0;

        hSelezionaRiga = new Handler(Looper.getMainLooper());
        hSelezionaRiga.postDelayed(runRiga = new Runnable() {
            @Override
            public void run() {
                conta++;
                if (conta>30) {
                    RilanciaActivity();
                } else {
                    VariabiliGlobali.getInstance().setContext(context);
                    VariabiliGlobali.getInstance().setActivityPrincipale(v);

                    hSelezionaRiga.postDelayed(runRiga, 60000);
                }
            }
        },60000);
    }

    public void RilanciaActivity() {
        conta=0;
        hSelezionaRiga.removeCallbacks(runRiga);
        runRiga = null;

        if (VariabiliGlobali.getInstance().getContext()==null ||
            VariabiliGlobali.getInstance().getActivityPrincipale()==null ||
                SharedObjects.getInstance().getListaImmagini() == null) {
            if (this.context!=null) {
                VariabiliGlobali.getInstance().setContext(this.context);
            }
            if (this.act!=null) {
                VariabiliGlobali.getInstance().setActivityPrincipale(this.act);
            }
            if (VariabiliGlobali.getInstance().getContext()==null ||
                    VariabiliGlobali.getInstance().getActivityPrincipale()==null ||
                    SharedObjects.getInstance().getListaImmagini() == null) {
                Intent dialogIntent = new Intent(context, MainActivity.class);
                dialogIntent.putExtra("AUTOMATIC RELOAD", "YES");
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                act.startActivity(dialogIntent);
            } else {
                hSelezionaRiga.postDelayed(runRiga, 60000);
            }
        } else {
            hSelezionaRiga.postDelayed(runRiga, 60000);
        }
    }
}

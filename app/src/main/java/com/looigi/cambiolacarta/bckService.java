package com.looigi.cambiolacarta;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.IBinder;
import android.widget.LinearLayout;

public class bckService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DBLocale dbl=new DBLocale();
        Log l = new Log();

        SharedObjects.getInstance().setStaPartendo(true);
        if (SharedObjects.getInstance().getCaricaDati()==null) {
            SharedObjects.getInstance().setCaricaDati(false);
        }
        if (SharedObjects.getInstance().getCaricaDati()) {
            SharedObjects.getInstance().setCaricaDati(false);
            GestioneFilesCartelle gfc=new GestioneFilesCartelle();
            gfc.LeggeImmagini(VariabiliGlobali.getInstance().getContext(),
                    SharedObjects.getInstance().getOrigine());
        } else {
            LeggeImmagini(dbl);
            Utility u = new Utility();
            u.ScriveInfo(l);
        }
        SharedObjects.getInstance().setStaPartendo(false);

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    private void LeggeImmagini(DBLocale dbl) {
        SharedObjects.getInstance().setListaImmagini(dbl.RitornaImmagini(VariabiliGlobali.getInstance().getContext()));
        if (SharedObjects.getInstance().getListaImmagini()==null) {
            SharedObjects.getInstance().setQuanteImm(0);
        } else {
            SharedObjects.getInstance().setQuanteImm(SharedObjects.getInstance().getListaImmagini().size());
        }
    }
}
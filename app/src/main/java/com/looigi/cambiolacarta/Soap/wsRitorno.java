package com.looigi.cambiolacarta.Soap;

import android.os.Handler;
import android.os.Looper;

import com.looigi.cambiolacarta.DialogMessaggio;
import com.looigi.cambiolacarta.SharedObjects;
import com.looigi.cambiolacarta.Utility;
import com.looigi.cambiolacarta.VariabiliGlobali;

public class wsRitorno {
    private Runnable runRiga;
    private Handler hSelezionaRiga;

    public void TornaNumeroImmaginePerSfondo(final String Ritorno) {
        if (Ritorno.toUpperCase().contains("ERROR:")) {
            DialogMessaggio.getInstance().show(VariabiliGlobali.getInstance().getContext(),
                    Ritorno,
                    true,
                    "looVF",
                    false);
        } else {
            hSelezionaRiga = new Handler(Looper.getMainLooper());
            hSelezionaRiga.postDelayed(runRiga = new Runnable() {
                @Override
                public void run() {
                    hSelezionaRiga.removeCallbacks(runRiga);
                    runRiga = null;

                    String[] r = Ritorno.split(";");
                    int numero = Integer.parseInt(r[0]);

                    Utility u = new Utility();
                    u.CambiaImmagine(true, numero);
                    String NomeFile = SharedObjects.getInstance().getListaImmagini().get(numero);
                    u.ImpostaImmagineDiSfondo(NomeFile);
                }
            },50);
        }
    }
}

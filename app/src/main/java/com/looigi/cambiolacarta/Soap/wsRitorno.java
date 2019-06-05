package com.looigi.cambiolacarta.Soap;

import android.os.Handler;
import android.os.Looper;

import com.looigi.cambiolacarta.DialogMessaggio;
import com.looigi.cambiolacarta.SharedObjects;
import com.looigi.cambiolacarta.Utility;
import com.looigi.cambiolacarta.VariabiliGlobali;

import java.util.Random;

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
                    // int numero = Integer.parseInt(r[0]);

                    int numero = -1;

                    String i = r[1].replace("\\", "/").toUpperCase().trim();
                    int conta = 0;
                    for (String s : SharedObjects.getInstance().getListaImmagini()){
                        if (s.toUpperCase().trim().contains(i)) {
                            numero=conta;
                            break;
                        }
                        conta++;
                    }
                    if (numero == -1) {
                        numero = Integer.parseInt(r[0]);
                        if (numero>SharedObjects.getInstance().getListaImmagini().size()-1){
                            Random rr;
                            rr=new Random();
                            int i1=rr.nextInt(SharedObjects.getInstance().getListaImmagini().size());
                            i1--;
                            if (i1<0) {
                                i1=0;
                            }
                            numero=i1;
                        }
                    }

                    Utility u = new Utility();
                    u.CambiaImmagine(true, numero);
                    String NomeFile = SharedObjects.getInstance().getListaImmagini().get(numero);
                    u.ImpostaImmagineDiSfondo(NomeFile);
                }
            },50);
        }
    }
}

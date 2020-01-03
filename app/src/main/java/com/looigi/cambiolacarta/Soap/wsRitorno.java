package com.looigi.cambiolacarta.Soap;

import android.os.Handler;
import android.os.Looper;

import com.looigi.cambiolacarta.DBLocale;
import com.looigi.cambiolacarta.DialogMessaggio;
import com.looigi.cambiolacarta.DownloadFileFromURL;
import com.looigi.cambiolacarta.Log;
import com.looigi.cambiolacarta.Notifiche;
import com.looigi.cambiolacarta.SharedObjects;
import com.looigi.cambiolacarta.Utility;
import com.looigi.cambiolacarta.VariabiliGlobali;

import java.util.Random;

public class wsRitorno {
    private Runnable runRiga;
    private Handler hSelezionaRiga;

    public void TornaNumeroImmaginePerSfondo(final String Ritorno, final Log l) {
        if (Ritorno.toUpperCase().contains("ERROR:")) {
            DialogMessaggio.getInstance().show(VariabiliGlobali.getInstance().getContext(),
                    Ritorno,
                    true,
                    "looVF",
                    false);
        } else {
            l.ScriveLog(new Object() {
                    }.getClass().getEnclosingMethod().getName(),
                    "Torna numero immagine. Ritorno: " + Ritorno);

            hSelezionaRiga = new Handler(Looper.getMainLooper());
            hSelezionaRiga.postDelayed(runRiga = new Runnable() {
                @Override
                public void run() {
                    hSelezionaRiga.removeCallbacks(runRiga);
                    runRiga = null;

                    String[] r = Ritorno.split(";");
                    // int numero = Integer.parseInt(r[0]);

                    String PathImm = r[1].replace("\\", "/");

                    SharedObjects.getInstance().setPathUltimaImaggineDL(PathImm);

                    PathImm = "http://looigi.no-ip.biz:12345/looVF/SfondiDir/" + PathImm;

                    int numero = Integer.parseInt(r[0]);

                    l.ScriveLog(new Object() {
                            }.getClass().getEnclosingMethod().getName(),
                            "Nuovo numero immagine ritornato: " + Integer.toString(numero) +
                            ". Vecchia: " + Integer.toString(VariabiliGlobali.getInstance().getVecchiaImmagine()));

                    /* String i = r[1].replace("\\", "/").toUpperCase().trim();
                    int conta = 0;
                    for (String s : SharedObjects.getInstance().getListaImmagini()){
                        if (s.toUpperCase().trim().contains(i)) {
                            numero=conta;
                            break;
                        }
                        conta++;
                    }

                    l.ScriveLog(new Object() {
                            }.getClass().getEnclosingMethod().getName(),
                            "Torna numero immagine 1");

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

                    l.ScriveLog(new Object() {
                            }.getClass().getEnclosingMethod().getName(),
                            "Torna numero immagine 2"); */

                    if (numero != VariabiliGlobali.getInstance().getVecchiaImmagine()) {
                        l.ScriveLog(new Object() {
                                }.getClass().getEnclosingMethod().getName(),
                                "Torna numero immagine 3: " + Integer.toString(numero));

                        VariabiliGlobali.getInstance().setVecchiaImmagine(numero);

                        SharedObjects.getInstance().setQualeImmagineHaVisualizzato(numero);

                        DBLocale dbl = new DBLocale();
                        dbl.ScriveOpzioni(VariabiliGlobali.getInstance().getContext());

                        /* Utility u = new Utility();
                        u.CambiaImmagine(true, numero);
                        u.ScriveInfo(l);
                        String NomeFile = SharedObjects.getInstance().getListaImmagini().get(numero);
                        u.ImpostaImmagineDiSfondo(NomeFile, l);
                        Notifiche.getInstance().AggiornaNotifica(); */
                        new DownloadFileFromURL(l).execute(PathImm);
                    }
                }
            },50);
        }
    }
}

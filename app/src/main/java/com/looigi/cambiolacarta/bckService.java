package com.looigi.cambiolacarta;

import android.app.Activity;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.looigi.cambiolacarta.AutoStart.service;

import java.util.HashMap;

public class bckService extends Service {
    private Activity v;
    private PhoneUnlockedReceiver receiver;
    private Context context;
    private SoundPool soundPool;
    private HashMap<Integer, Integer> soundPoolMap;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        v = VariabiliGlobali.getInstance().getActivityPrincipale();
        context = this;

        // DBLocale dbl=new DBLocale();
        Log l = new Log();

        if (VariabiliGlobali.getInstance().getContext()==null) {
            l.ScriveLog(new Object() {
                    }.getClass().getEnclosingMethod().getName(),
                    "Contex vuoto, Ricarico dal servizio");

            VariabiliGlobali.getInstance().setContext(this);
        }

        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        receiver = new PhoneUnlockedReceiver();
        IntentFilter fRecv = new IntentFilter();
        fRecv.addAction(Intent.ACTION_USER_PRESENT);
        fRecv.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(receiver, fRecv);

        SharedObjects.getInstance().setAudioManager((AudioManager)getSystemService(context.AUDIO_SERVICE));

        if (SharedObjects.getInstance().getDimeWallWidthOriginale()==0 && SharedObjects.getInstance().getDimeWallHeightOriginale()==0) {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
            SharedObjects.getInstance().setDimeWallWidthOriginale (wallpaperManager.getDesiredMinimumWidth());
            SharedObjects.getInstance().setDimeWallHeightOriginale (wallpaperManager.getDesiredMinimumHeight());
        }

        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<Integer, Integer>();
        soundPoolMap.put(1, soundPool.load(this, R.raw.premuto, 1));

        SharedObjects.getInstance().setTxtPercorso((TextView) v.findViewById(R.id.txtPercorso));
        SharedObjects.getInstance().setTxtNumImm((TextView) v.findViewById(R.id.txtNumImm));
        SharedObjects.getInstance().setTxtSceltaCartella((TextView) v.findViewById(R.id.txtSceltaCartella));
        SharedObjects.getInstance().setTxtCambiaSubito((TextView) v.findViewById(R.id.txtCambiaSubito));
        SharedObjects.getInstance().setTxtListaImm((TextView) v.findViewById(R.id.txtListaImm));
        SharedObjects.getInstance().setTxtOpzioni((TextView) v.findViewById(R.id.txtOpzioni));
        SharedObjects.getInstance().setTxtTipoCambio((TextView) v.findViewById(R.id.txtTipoCambio));
        SharedObjects.getInstance().setTxtMinuti((TextView) v.findViewById(R.id.txtMinuti));
        SharedObjects.getInstance().setTxtImmVisua((TextView) v.findViewById(R.id.txtImmVisualizzata));
        SharedObjects.getInstance().setTxtNomeImm((TextView) v.findViewById(R.id.txtNomeImm));
        SharedObjects.getInstance().setTxtProssima((TextView) v.findViewById(R.id.txtProssima));
        SharedObjects.getInstance().setTxtPrecedente((TextView) v.findViewById(R.id.txtPrecedente));
        SharedObjects.getInstance().setTxtTempo((TextView) v.findViewById(R.id.txtTempo));
        SharedObjects.getInstance().setTxtCaffe((TextView) v.findViewById(R.id.txtCaffe));

        // txtTempo2=(TextView) findViewById(R.v.idv..txtTempo2);
        SharedObjects.getInstance().setChkAttivo((CheckBox) v.findViewById(R.id.chkAttivo));
        SharedObjects.getInstance().setImm((ImageView) v.findViewById(R.id.imgImmagine));

        //pIT=R.string.percorsoIT;
        //pEN=R.string.percorsoEN;
        //nIT=R.string.numimmIT;
        //nEN=R.string.numimmEN;
//
        // CreaBannerPubb();

        DisplayMetrics metrics = new DisplayMetrics();
        SharedObjects.getInstance().setA1(v.getWindowManager());
        SharedObjects.getInstance().getA1().getDefaultDisplay().getMetrics(metrics);

        SharedObjects.getInstance().setSchermoX(metrics.widthPixels);
        SharedObjects.getInstance().setSchermoY(metrics.heightPixels);

        DBLocale dbl=new DBLocale();
        dbl.CreaDB(context);
        dbl.LeggeOpzioni(context);
        dbl.LeggePercorsi(context);

        ImageView imgSceltaFolder=(ImageView) v.findViewById(R.id.imgSceltaCartella);
        imgSceltaFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuonAudio s=new SuonAudio();
                s.SuonaAudio(1, soundPool);

                // FermaTimerAggiornatore();
                Utility u = new Utility();
                u.FermaTimer();

                Intent Folder= new Intent(VariabiliGlobali.getInstance().getActivityPrincipale(), SceltaCartella.class);
                startActivity(Folder);

                VariabiliGlobali.getInstance().getActivityPrincipale().finish();
            }
        });

        ImageView imgRefresh=(ImageView) v.findViewById(R.id.imgRefresh);
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuonAudio s=new SuonAudio();
                s.SuonaAudio(1, soundPool);

                // FermaTimerAggiornatore();
                Utility u = new Utility();
                u.FermaTimer();

                GestioneFilesCartelle gfc=new GestioneFilesCartelle();
                gfc.LeggeImmagini(context, SharedObjects.getInstance().getOrigine());
            }
        });

        ImageView imgOpzioni=(ImageView) v.findViewById(R.id.imgAOpzioni);
        imgOpzioni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuonAudio s=new SuonAudio();
                s.SuonaAudio(1, soundPool);

                // FermaTimerAggiornatore();

                Intent Folder= new Intent(VariabiliGlobali.getInstance().getActivityPrincipale(), Opzioni.class);
                startActivity(Folder);

                VariabiliGlobali.getInstance().getActivityPrincipale().finish();
            }
        });

        ImageView imgCambia=(ImageView) v.findViewById(R.id.imgCambiaSubito);
        imgCambia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuonAudio s=new SuonAudio();
                s.SuonaAudio(1, soundPool);
                Utility u=new Utility();

                // CreaBannerPubb();
                // MinutiPassati=0;
	        	/* if (MinutiPassati<1) {
	        		MinutiPassati=1;
	        	} */

                if (SharedObjects.getInstance().getQuanteImm()>0) {
                    Boolean Ritorno=u.CambiaImmagine(false,0);

                    if (Ritorno) {
                        Toast.makeText(VariabiliGlobali.getInstance().getActivityPrincipale(), u.ControllaLingua(context,
                                R.string.immimpIT, R.string.immimpEN),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(VariabiliGlobali.getInstance().getActivityPrincipale(),
                                u.ControllaLingua(context, R.string.errimmimpIT, R.string.errimmimpEN),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VariabiliGlobali.getInstance().getActivityPrincipale(), u.ControllaLingua(context, R.string.noimmimpIT, R.string.noimmimpEN), Toast.LENGTH_SHORT).show();
                }
            }
        });

        LinearLayout layLista=(LinearLayout) v.findViewById(R.id.layLista);
        layLista.setVisibility(LinearLayout.GONE);

        ImageView imgLista=(ImageView) v.findViewById(R.id.imgListaImm);
        imgLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuonAudio s=new SuonAudio();
                s.SuonaAudio(1, soundPool);
            }
        });

        ImageView imgProssima=(ImageView) v.findViewById(R.id.imgProssima);
        imgProssima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log l = new Log();
                SuonAudio s=new SuonAudio();
                s.SuonaAudio(1, soundPool);

                Utility u = new Utility();
                int imm = u.PrendeNuovoNumero("Avanti", l);

                if (imm>=0) {
                    SharedObjects.getInstance().setQualeImmagineHaVisualizzato(imm);

                    DBLocale dbl = new DBLocale();
                    dbl.ScriveOpzioni(context);

                    u.ScriveInfo(l);

                    u.ImpostaImmagineDiSfondo(SharedObjects.getInstance().getListaImmagini().get(SharedObjects.getInstance().getQualeImmagineHaVisualizzato()), l);
                }
            }
        });

        ImageView imgPrec=(ImageView) v.findViewById(R.id.imgIndietro);
        imgPrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuonAudio s=new SuonAudio();
                s.SuonaAudio(1, soundPool);
                Log l = new Log();

                Utility u = new Utility();
                int imm = u.PrendeNuovoNumero("Indietro", l);

                if (imm>=0) {
                    SharedObjects.getInstance().setQualeImmagineHaVisualizzato(imm);

                    DBLocale dbl = new DBLocale();
                    dbl.ScriveOpzioni(context);

                    u.ScriveInfo(l);

                    u.ImpostaImmagineDiSfondo(SharedObjects.getInstance().getListaImmagini().get(SharedObjects.getInstance().getQualeImmagineHaVisualizzato()), l);
                }
            }
        });

        ImageView imgCaffe=(ImageView) v.findViewById(R.id.imgCaffe);
        imgCaffe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuonAudio s=new SuonAudio();
                s.SuonaAudio(1, soundPool);

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.looigi.cambiolacarta_donate"));
                startActivity(intent);
            }
        });

        SharedObjects.getInstance().getChkAttivo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log l = new Log();
                SuonAudio s=new SuonAudio();
                s.SuonaAudio(1, soundPool);

                Utility u = new Utility();
                if (SharedObjects.getInstance().getAttivo().equals("S")) {
                    SharedObjects.getInstance().setAttivo("N");
                    u.FermaTimer();
                } else {
                    SharedObjects.getInstance().setAttivo("S");
                    u.FaiPartireTimer();
                }

                DBLocale dbl=new DBLocale();
                dbl.ScriveOpzioni(context);

                u.ScriveInfo(l);
            }
        });

        ImageView imgItaliano=(ImageView) v.findViewById(R.id.imgItaliano);
        imgItaliano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log l = new Log();
                SuonAudio s=new SuonAudio();
                s.SuonaAudio(1, soundPool);

                SharedObjects.getInstance().setLingua("ITALIANO");

                DBLocale dbl=new DBLocale();
                dbl.ScriveOpzioni(context);

                Utility u = new Utility();
                u.ScriveInfo(l);
            }
        });

        ImageView imgInglese=(ImageView) v.findViewById(R.id.imgInglese);
        imgInglese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log l = new Log();
                SuonAudio s=new SuonAudio();
                s.SuonaAudio(1, soundPool);

                SharedObjects.getInstance().setLingua("INGLESE");

                DBLocale dbl=new DBLocale();
                dbl.ScriveOpzioni(context);

                Utility u = new Utility();
                u.ScriveInfo(l);
            }
        });

        ImageView imgApreInfo=(ImageView) v.findViewById(R.id.imgApreInfo);
        imgApreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuonAudio s=new SuonAudio();
                s.SuonaAudio(1, soundPool);

                LinearLayout layInfo=(LinearLayout) v.findViewById(R.id.layInfo);
                layInfo.setVisibility(LinearLayout.VISIBLE);
                LinearLayout layApreInfo=(LinearLayout) v.findViewById(R.id.layApriInfo);
                layApreInfo.setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgChiudeInfo=(ImageView) v.findViewById(R.id.imgChiudeInfo);
        imgChiudeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuonAudio s=new SuonAudio();
                s.SuonaAudio(1, soundPool);

                LinearLayout layInfo=(LinearLayout) v.findViewById(R.id.layInfo);
                layInfo.setVisibility(LinearLayout.GONE);
                LinearLayout layApreInfo=(LinearLayout) v.findViewById(R.id.layApriInfo);
                layApreInfo.setVisibility(LinearLayout.VISIBLE);
            }
        });

        ImpostaDimensioni();

        Notifiche.getInstance().CreaNotifica();
        Notifiche.getInstance().AggiornaNotifica();

        if (service.ChiudiMaschera==null) {
            service.ChiudiMaschera=false;
        }
        if (service.ChiudiMaschera) {
            v.moveTaskToBack(true);
            service.ChiudiMaschera=false;
        }

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

    private void ImpostaDimensioni() {
        LinearLayout layDirectory;
        LinearLayout layRefresh;
        LinearLayout layImposta;
        LinearLayout layAvanti;
        LinearLayout layIndietro;
        LinearLayout layOpzioni;

        layDirectory=(LinearLayout) v.findViewById(R.id.layDirectory);
        layRefresh=(LinearLayout) v.findViewById(R.id.layRefresh);
        layImposta=(LinearLayout) v.findViewById(R.id.layImposta);
        layAvanti=(LinearLayout) v.findViewById(R.id.layAvanti);
        layIndietro=(LinearLayout) v.findViewById(R.id.layIndietro);
        layOpzioni=(LinearLayout) v.findViewById(R.id.layOpzioni);
        LinearLayout layInfo=(LinearLayout) v.findViewById(R.id.layInfo);
        layInfo.setVisibility(LinearLayout.GONE);
        LinearLayout layApreInfo=(LinearLayout) v.findViewById(R.id.layApriInfo);
        layApreInfo.setVisibility(LinearLayout.VISIBLE);

        float SX2=((float) SharedObjects.getInstance().getSchermoX()/5);
        int SchermoX2=(int) SX2;
        SchermoX2-=7;
        if (SchermoX2<70) {
            SchermoX2=70;
        }

        float SY2=((float) SharedObjects.getInstance().getSchermoY()/12);
        int SchermoY2=(int) SY2;
        if (SchermoY2<80) {
            SchermoY2=80;
        }
        layDirectory.setLayoutParams(new LinearLayout.LayoutParams(SchermoX2, SchermoY2));
        layRefresh.setLayoutParams(new LinearLayout.LayoutParams(SchermoX2, SchermoY2));
        layImposta.setLayoutParams(new LinearLayout.LayoutParams(SchermoX2, SchermoY2));
        layAvanti.setLayoutParams(new LinearLayout.LayoutParams(SchermoX2, SchermoY2));
        layIndietro.setLayoutParams(new LinearLayout.LayoutParams(SchermoX2, SchermoY2));
        layOpzioni.setLayoutParams(new LinearLayout.LayoutParams(SchermoX2, SchermoY2));
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
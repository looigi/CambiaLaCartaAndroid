package com.looigi.cambiolacarta;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.looigi.cambiolacarta.R.string;
import com.looigi.cambiolacarta.Soap.DBRemoto;

import org.kobjects.util.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;

public class MainActivity extends Activity {
	static public Activity activity;
	static CountDownTimer ct = null;
	private boolean CiSonoPermessi;
	private Context context;
	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;
	private DBLocale dbl;
	// private MemoryBoss mMemoryBoss;
	// public static Context ctxPrincipale;

	/* @Override
	protected void onStop() {
		super.onStop();

		// unregisterComponentCallbacks(mMemoryBoss);

		Log l = new Log();

		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"On Stop");

		// MediaPlayer mp = MediaPlayer.create(VariabiliStatiche.getInstance().getContext(), R.raw.schui);
		// mp.start();
	} */

	// Banner di pubblicit�
	// private RelativeLayout layout;
	// private AdView mAdView;
	// private AdManager mManager;
	// private String BannerID="c1455de07e2c0c6071670d4cb8c4baec";
	// Banner di pubblicit�

	// private Context context;
	// private SoundPool soundPool;
	// private HashMap<Integer, Integer> soundPoolMap;

	//private int pIT;
	//private int pEN;
	//private int nIT;
	//private int nEN;
	
  	// private Handler handlerAgg;
  	// private Runnable rAgg;

    /* public void setContext(Context c) {
        this.context = c;
    }

    public Context getContext() {
        if (context!=null) {
            return context;
        } else {
            return this;
        }
    }

    @Override
	protected void onResume() {
		super.onResume();

		this.onCreate(null);
	} */

	// long pressedTime = 0;

	@Override
	public void onBackPressed() {
		Log l = new Log();

		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"Premuto indietro");

		/* if (pressedTime + 2000 > System.currentTimeMillis()) {
			super.onBackPressed();
			finish();
		} else { */
			moveTaskToBack(true);
		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"Nascosta activity");
			// Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
		// }
		// pressedTime = System.currentTimeMillis();
	}

	@Override
	protected void onDestroy() {
    	Log l = new Log();

		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"Attività distrutta");
		// this.recreate();

		MainActivity.ct = null;

        /* Commento per receiver blocco schermo
        l.ScriveLog(new Object() {
                }.getClass().getEnclosingMethod().getName(),
                "On Destroy servizio");

        if (receiver != null) {
            l.ScriveLog(new Object() {
                    }.getClass().getEnclosingMethod().getName(),
                    "Unregistro il receiver");

            unregisterReceiver(receiver);
            receiver = null;
        } */
		// EsegueEntrata();

		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();

		this.moveTaskToBack(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		activity = this;
		context = this;

		Permessi pp = new Permessi();
		CiSonoPermessi = pp.ControllaPermessi(this);

		if (CiSonoPermessi) {
			EsegueEntrata();

			/* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				mMemoryBoss = new MemoryBoss();
				registerComponentCallbacks(mMemoryBoss);
			} */
		}

	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		if (!CiSonoPermessi) {
			int index = 0;
			Map<String, Integer> PermissionsMap = new HashMap<String, Integer>();
			for (String permission : permissions) {
				PermissionsMap.put(permission, grantResults[index]);
				index++;
			}

			// EsegueEntrata();
		}
	}

	private PhoneUnlockedReceiver receiver; // Commento per receiver blocco schermo

	private void EsegueEntrata() {
		Log l = new Log();

		l.PulisceFileDiLog();

		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"Eseguo entrata");
		// String AutomaticReload = getIntent().getStringExtra("AUTOMATIC RELOAD");
		// if (AutomaticReload !=null && AutomaticReload.equals("YES")) {
		// }
		// context=this;

        /* Commento per receiver blocco schermo
        if (receiver == null) {
            receiver = new PhoneUnlockedReceiver();
            IntentFilter fRecv = new IntentFilter();
            fRecv.addAction(Intent.ACTION_USER_PRESENT);
            fRecv.addAction(Intent.ACTION_SCREEN_OFF);

            l.ScriveLog(new Object() {
                    }.getClass().getEnclosingMethod().getName(),
                    "Registro il receiver");
            // try {
            MainActivity.activity.registerReceiver(receiver, fRecv);
        } */

		SharedObjects.getInstance().setContext(this);
		VariabiliGlobali.getInstance().setContext(this);
		// VariabiliGlobali.getInstance().setActivityPrincipale(this);

		dbl=new DBLocale();
		dbl.CreaDB(context);

		// VariabiliGlobali.getInstance().setiServizio(new Intent(MainActivity.activity, bckService.class));
		// VariabiliGlobali.getInstance().getActivityPrincipale().startService(
		// 		VariabiliGlobali.getInstance().getiServizio());
		startService();

		// if (AutomaticReload !=null && AutomaticReload.equals("YES")) {
			// // // moveTaskToBack(true);
		// }
	}

	public void startService() {
		Log l = new Log();

		creaStrutturaInizio();

		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"Attivo servizio");

		Intent serviceIntent = new Intent(this, ServizioInterno.class);
		// serviceIntent.putExtra("inputExtra2", "Cambia la carta");
		// ContextCompat.startForegroundService(this, serviceIntent);
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
		/* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			startForegroundService(serviceIntent);
		} else {

		} */

		// new ServizioInterno(this);
		MainActivity.activity.startService(serviceIntent);

		// ServizioInterno s = new ServizioInterno();
		// s.creaStrutturaInizio();

		// if (SharedObjects.getInstance().getStaPartendo()) {
		// 	moveTaskToBack(true);
		// }
		// CreaNotifica();
		// AggiornaNotifica();

		// } else {
		// 	VariabiliGlobali.getInstance().getActivityPrincipale().startService(serviceIntent);
		// }
	}

	public void stopService() {
		Log l = new Log();

		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"Stoppo servizio");

		Intent serviceIntent = new Intent(this, ServizioInterno.class);
		stopService(serviceIntent);
	}

	// private void CreaBannerPubb() {
	// 	// BANNER
	// 	if (mAdView != null) {
	// 		removeBanner();
	// 	}
	// 	try {
	// 		layout = (RelativeLayout) findViewById(R.id.adMobFox);
	// 		mManager = new AdManager(this, "http://my.mobfox.com/vrequest.php",
	// 				BannerID, true);
	// 		mManager.setListener(this);
	// 		mAdView = new AdView(this, "http://my.mobfox.com/request.php",
	// 				BannerID, true, true);
	// 		mAdView.setAdListener(this);
	// 		layout.addView(mAdView);
	// 		// BANNER
	// 	} catch (Exception ignored) {
	//
	// 	}
	// }

	// public void FaiPartireAggiornatore() {
	// 	/* handlerAgg = new Handler();
    //     rAgg = new Runnable() {
    //         public void run() {
    //          	ScriveInfo();
    //         	AggiornaNotifica();
    //         	CreaBannerPubb();
    //
    //             handlerAgg.postDelayed(rAgg, 90000);
    //         }
    //     };
    //     handlerAgg.postDelayed(rAgg, 90000); */
	// }

	// private void FermaTimerAggiornatore() {
	// 	try {
	//     	handlerAgg.removeCallbacks(rAgg);
	//
	//     	handlerAgg=null;
	//     	rAgg=null;
	// 	} catch (Exception ignored) {
	//
	// 	}
	// }

	// private void LeggeImmagini(DBLocale dbl) {
	// 	SharedObjects.getInstance().setListaImmagini(dbl.RitornaImmagini(context));
	// 	if (SharedObjects.getInstance().getListaImmagini()==null) {
	// 		SharedObjects.getInstance().setQuanteImm(0);
	// 	} else {
	// 		SharedObjects.getInstance().setQuanteImm(SharedObjects.getInstance().getListaImmagini().size());
	// 	}
	// }

	/* @Override
	protected void onUserLeaveHint() {
	    super.onUserLeaveHint();
	    
	} */
	
	// @Override
	// protected void onPause() {
	    // super.onPause();
	    /* if (!mKeyPress && mUserLeaveHint) {
			CreaNotifica();
			AggiornaNotifica();
        	moveTaskToBack(true);          	
	    }
	    if (mUserLeaveHint==false) {
	    	mUserLeaveHint=true;
	    } */
	// }
	
	/* @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		super.onKeyDown(keyCode, event);

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(true); */
        	/* //
            Intent setIntent = new Intent(Intent.ACTION_MAIN);
            setIntent.addCategory(Intent.CATEGORY_HOME);
            setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(setIntent); */

        	/* return false;
		} else {
			return true;
		}
	} */

	/* @Override
	protected void onStop() {
		// Log l = new Log();

		// l.ScriveLog(new Object() {
		// 		}.getClass().getEnclosingMethod().getName(),
		// 		"Attività stoppata");

		// this.recreate();

		// unregisterComponentCallbacks(mMemoryBoss);

		// stopService();
		super.onStop();

		// DialogMessaggio.getInstance().show(VariabiliStaticheGlobali.getInstance().getContext(),
		//         "Richiamata funzione onStop",
		//         true,
		//         VariabiliStaticheGlobali.NomeApplicazione);
	} */

	// BANNER
	// private void removeBanner(){
	// 	if(mAdView!=null){
	// 		try {
	// 			layout.removeView(mAdView);
	// 		} catch (Exception ignored) {
	//
	// 		}
	// 		mAdView = null;
	// 	}
	// }
	//
	// public void onClickShowVideoInterstitial(View v) {
	// 	mManager.requestAd();
	// }
	//
	// @Override
	// public void adClicked() {
	// }
	//
	// @Override
	// public void adClosed(Ad arg0, boolean arg1) {
	// }
	//
	// @Override
	// public void adLoadSucceeded(Ad arg0) {
	// }
	//
	// @Override
	// public void adShown(Ad arg0, boolean arg1) {
	// }
	//
	// @Override
	// public void noAdFound() {
	// }
	// BANNER

	public void creaStrutturaInizio() {
		SharedObjects.getInstance().setStaPartendo(false);
		// context = this;

		// DBLocale dbl=new DBLocale();
		final Log l = new Log();

		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"creaStrutturaInizio");

        /* if (v == null || VariabiliGlobali.getInstance().getContext()==null || context==null) {
            if (MainActivity.ctxPrincipale == null) {
                l.ScriveLog(new Object() {
                        }.getClass().getEnclosingMethod().getName(),
                        "Context vuoto, Ricarico dal servizio");

                RefreshActivity.getInstance().RilanciaActivity();
                v = RefreshActivity.getAct();
                context = RefreshActivity.getContext();
                VariabiliGlobali.getInstance().setContext(context);
            } else {
                l.ScriveLog(new Object() {
                        }.getClass().getEnclosingMethod().getName(),
                        "Context vuoto ma non quello del main, Ricarico dal main");

                context = MainActivity.ctxPrincipale;
                VariabiliGlobali.getInstance().setContext(context);
            }
            if (context == null) {
                l.ScriveLog(new Object() {
                        }.getClass().getEnclosingMethod().getName(),
                        "Context continua a essere vuoto. Rilancio l'applicazione");
                Intent intentR = new Intent(this, MainActivity.class);
                this.startActivity(intentR);
                System.exit(0);
            }
        }

        receiver = new PhoneUnlockedReceiver();
        try {
            unregisterReceiver(receiver);
            receiver = null;
        } catch (Exception ignored) {

        }
        IntentFilter fRecv = new IntentFilter();
        fRecv.addAction(Intent.ACTION_USER_PRESENT);
        fRecv.addAction(Intent.ACTION_SCREEN_OFF);
        try {
            registerReceiver(receiver, fRecv);
        } catch (Exception ignored) {

        } */

		if (MainActivity.activity != null) {
			SharedObjects.getInstance().setAudioManager((AudioManager) MainActivity.activity.getSystemService(context.AUDIO_SERVICE));

			if (SharedObjects.getInstance().getDimeWallWidthOriginale()==0 && SharedObjects.getInstance().getDimeWallHeightOriginale()==0) {
				if (context == null) { context = MainActivity.activity; }
				WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
				SharedObjects.getInstance().setDimeWallWidthOriginale (wallpaperManager.getDesiredMinimumWidth());
				SharedObjects.getInstance().setDimeWallHeightOriginale (wallpaperManager.getDesiredMinimumHeight());
			}

			soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
			soundPoolMap = new HashMap<Integer, Integer>();
			soundPoolMap.put(1, soundPool.load(MainActivity.activity, R.raw.premuto, 1));

			ImageView imgSceltaFolder=null;
			ImageView imgRefresh=null;
			ImageView imgOpzioni=null;
			ImageView imgCambia=null;
			LinearLayout layLista=null;
			ImageView imgLista=null;
			ImageView imgProssima=null;
			ImageView imgPrec=null;
			ImageView imgCaffe=null;
			ImageView imgItaliano=null;
			ImageView imgInglese=null;
			ImageView imgApreInfo=null;
			ImageView imgChiudeInfo=null;

			if (MainActivity.activity == null && context != null) {
                /* new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Activity customActivity = MainActivity.activity;
                        customActivity.finish();

                        startActivity(customActivity.getIntent());
                    }
                }, 1); */
			}

			// if (v!=null) {
			l.ScriveLog(new Object() {
					}.getClass().getEnclosingMethod().getName(),
					"creaStrutturaInizio 1");

			SharedObjects.getInstance().setTxtPercorso((TextView) MainActivity.activity.findViewById(R.id.txtPercorso));
			SharedObjects.getInstance().setTxtNumImm((TextView) MainActivity.activity.findViewById(R.id.txtNumImm));
			SharedObjects.getInstance().setTxtSceltaCartella((TextView) MainActivity.activity.findViewById(R.id.txtSceltaCartella));
			SharedObjects.getInstance().setTxtCambiaSubito((TextView) MainActivity.activity.findViewById(R.id.txtCambiaSubito));
			SharedObjects.getInstance().setTxtListaImm((TextView) MainActivity.activity.findViewById(R.id.txtListaImm));
			SharedObjects.getInstance().setTxtOpzioni((TextView) MainActivity.activity.findViewById(R.id.txtOpzioni));
			SharedObjects.getInstance().setTxtTipoCambio((TextView) MainActivity.activity.findViewById(R.id.txtTipoCambio));
			SharedObjects.getInstance().setTxtMinuti((TextView) MainActivity.activity.findViewById(R.id.txtMinuti));
			SharedObjects.getInstance().setTxtImmVisua((TextView) MainActivity.activity.findViewById(R.id.txtImmVisualizzata));
			SharedObjects.getInstance().setTxtNomeImm((TextView) MainActivity.activity.findViewById(R.id.txtNomeImm));
			SharedObjects.getInstance().setTxtProssima((TextView) MainActivity.activity.findViewById(R.id.txtProssima));
			SharedObjects.getInstance().setTxtPrecedente((TextView) MainActivity.activity.findViewById(R.id.txtPrecedente));
			SharedObjects.getInstance().setTxtTempo((TextView) MainActivity.activity.findViewById(R.id.txtTempo));
			SharedObjects.getInstance().setTxtCaffe((TextView) MainActivity.activity.findViewById(R.id.txtCaffe));
			SharedObjects.getInstance().setTxtTempoPassato((TextView) MainActivity.activity.findViewById(R.id.txtTempoPassato));
			SharedObjects.getInstance().getTxtTempoPassato().setText("");

			// txtTempo2=(TextView) findViewById(R.v.idv..txtTempo2);
			SharedObjects.getInstance().setChkAttivo((CheckBox) MainActivity.activity.findViewById(R.id.chkAttivo));
			SharedObjects.getInstance().setImm((ImageView) MainActivity.activity.findViewById(R.id.imgImmagine));

			SharedObjects.getInstance().setA1(MainActivity.activity.getWindowManager());

			imgSceltaFolder=(ImageView) MainActivity.activity.findViewById(R.id.imgSceltaCartella);
			imgRefresh=(ImageView) MainActivity.activity.findViewById(R.id.imgRefresh);
			imgOpzioni=(ImageView) MainActivity.activity.findViewById(R.id.imgAOpzioni);
			imgCambia=(ImageView) MainActivity.activity.findViewById(R.id.imgCambiaSubito);
			layLista=(LinearLayout) MainActivity.activity.findViewById(R.id.layLista);
			imgLista=(ImageView) MainActivity.activity.findViewById(R.id.imgListaImm);
			imgProssima=(ImageView) MainActivity.activity.findViewById(R.id.imgProssima);
			imgPrec=(ImageView) MainActivity.activity.findViewById(R.id.imgIndietro);
			imgCaffe=(ImageView) MainActivity.activity.findViewById(R.id.imgCaffe);
			imgItaliano=(ImageView) MainActivity.activity.findViewById(R.id.imgItaliano);
			imgInglese=(ImageView) MainActivity.activity.findViewById(R.id.imgInglese);
			imgApreInfo=(ImageView) MainActivity.activity.findViewById(R.id.imgApreInfo);
			imgChiudeInfo=(ImageView) MainActivity.activity.findViewById(R.id.imgChiudeInfo);

			LinearLayout layImposta = (LinearLayout) MainActivity.activity.findViewById(R.id.layImposta);
			LinearLayout layPrec = (LinearLayout) MainActivity.activity.findViewById(R.id.layIndietro);
			LinearLayout layProssimo = (LinearLayout) MainActivity.activity.findViewById(R.id.layAvanti);
			LinearLayout layRefresh = (LinearLayout) MainActivity.activity.findViewById(R.id.layRefresh);
			LinearLayout layCambiaDir = (LinearLayout) MainActivity.activity.findViewById(R.id.layDirectory);

			SharedObjects.getInstance().setImgPrec(layPrec);
			SharedObjects.getInstance().setImgSucc(layProssimo);
			SharedObjects.getInstance().setImgCambia(layImposta);
			SharedObjects.getInstance().setImgRefresh(layRefresh);
			SharedObjects.getInstance().setImgCambiaDir(layCambiaDir);

			//pIT=R.string.percorsoIT;
			//pEN=R.string.percorsoEN;
			//nIT=R.string.numimmIT;
			//nEN=R.string.numimmEN;
			//
			// CreaBannerPubb();

			DisplayMetrics metrics = new DisplayMetrics();
			if (SharedObjects.getInstance().getA1() != null) {
				SharedObjects.getInstance().getA1().getDefaultDisplay().getMetrics(metrics);
			}

			SharedObjects.getInstance().setSchermoX(metrics.widthPixels);
			SharedObjects.getInstance().setSchermoY(metrics.heightPixels);

			imgSceltaFolder.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View vv) {
					l.ScriveLog(new Object() {
							}.getClass().getEnclosingMethod().getName(),
							"Click scelta folder");

					SuonAudio s=new SuonAudio();
					s.SuonaAudio(1, soundPool);

					// FermaTimerAggiornatore();
					Utility u = new Utility();
					u.FermaTimer();

					Intent Folder= new Intent(MainActivity.activity, SceltaCartella.class);
					MainActivity.activity.startActivity(Folder);

					MainActivity.activity.finish();
				}
			});

			imgRefresh.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View vv) {
					l.ScriveLog(new Object() {
							}.getClass().getEnclosingMethod().getName(),
							"Image refresh click");

					SuonAudio s=new SuonAudio();
					s.SuonaAudio(1, soundPool);

					// FermaTimerAggiornatore();
					Utility u = new Utility();
					u.FermaTimer();

					GestioneFilesCartelle gfc=new GestioneFilesCartelle();
					gfc.LeggeImmagini(context, SharedObjects.getInstance().getOrigine());
				}
			});

			imgOpzioni.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View vv) {
					l.ScriveLog(new Object() {
							}.getClass().getEnclosingMethod().getName(),
							"Image opzioni click");

					SuonAudio s=new SuonAudio();
					s.SuonaAudio(1, soundPool);

					// FermaTimerAggiornatore();

					Intent Folder= new Intent(MainActivity.activity, Opzioni.class);
					MainActivity.activity.startActivity(Folder);

					MainActivity.activity.finish();
				}
			});

			imgCambia.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View vv) {
					l.ScriveLog(new Object() {
							}.getClass().getEnclosingMethod().getName(),
							"Image cambia click");

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
							// Toast.makeText(VariabiliGlobali.getInstance().getActivityPrincipale(), u.ControllaLingua(context,
							//         R.string.immimpIT, R.string.immimpEN),
							//         Toast.LENGTH_SHORT).show();
						} else {
							// Toast.makeText(VariabiliGlobali.getInstance().getActivityPrincipale(),
							//         u.ControllaLingua(context, R.string.errimmimpIT, R.string.errimmimpEN),
							//         Toast.LENGTH_SHORT).show();
						}
					} else {
						// Toast.makeText(VariabiliGlobali.getInstance().getActivityPrincipale(), u.ControllaLingua(context, R.string.noimmimpIT, R.string.noimmimpEN), Toast.LENGTH_SHORT).show();
					}
				}
			});

			layLista.setVisibility(LinearLayout.GONE);

			imgLista.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View vv) {
					l.ScriveLog(new Object() {
							}.getClass().getEnclosingMethod().getName(),
							"Image lista click");

					SuonAudio s=new SuonAudio();
					s.SuonaAudio(1, soundPool);
				}
			});

			imgProssima.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View vv) {
					l.ScriveLog(new Object() {
							}.getClass().getEnclosingMethod().getName(),
							"Image prossima click");

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

						if (!SharedObjects.getInstance().getTipoCambio().equals("SINCRONIZZATA")) {
							u.ImpostaImmagineDiSfondo(SharedObjects.getInstance().getListaImmagini().get(SharedObjects.getInstance().getQualeImmagineHaVisualizzato()), l);
						} else {
							u.ImpostaImmagineDiSfondo(SharedObjects.getInstance().getPercorsoDIR() + "/Download.jpg", l);
						}
					}
				}
			});

			imgPrec.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View vv) {
					l.ScriveLog(new Object() {
							}.getClass().getEnclosingMethod().getName(),
							"Image prec click");

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

						if (!SharedObjects.getInstance().getTipoCambio().equals("SINCRONIZZATA")) {
							u.ImpostaImmagineDiSfondo(SharedObjects.getInstance().getListaImmagini().get(SharedObjects.getInstance().getQualeImmagineHaVisualizzato()), l);
						} else {
							u.ImpostaImmagineDiSfondo(SharedObjects.getInstance().getPercorsoDIR() + "/Download.jpg", l);
						}
					}
				}
			});

			imgCaffe.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View vv) {
					l.ScriveLog(new Object() {
							}.getClass().getEnclosingMethod().getName(),
							"Image caffe click");

					SuonAudio s=new SuonAudio();
					s.SuonaAudio(1, soundPool);

					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_BROWSABLE);
					intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.looigi.cambiolacarta_donate"));
					MainActivity.activity.startActivity(intent);
				}
			});

			SharedObjects.getInstance().getChkAttivo().setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View vv) {
					l.ScriveLog(new Object() {
							}.getClass().getEnclosingMethod().getName(),
							"Image check attivo click");

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

			imgItaliano.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View vv) {
					l.ScriveLog(new Object() {
							}.getClass().getEnclosingMethod().getName(),
							"Image italiano click");

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

			imgInglese.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View vv) {
					l.ScriveLog(new Object() {
							}.getClass().getEnclosingMethod().getName(),
							"Image inglese click");

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

			imgApreInfo.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View vv) {
					l.ScriveLog(new Object() {
							}.getClass().getEnclosingMethod().getName(),
							"Image apre info click");

					SuonAudio s=new SuonAudio();
					s.SuonaAudio(1, soundPool);

					LinearLayout layInfo=(LinearLayout) MainActivity.activity.findViewById(R.id.layInfo);
					layInfo.setVisibility(LinearLayout.VISIBLE);
					LinearLayout layApreInfo=(LinearLayout) MainActivity.activity.findViewById(R.id.layApriInfo);
					layApreInfo.setVisibility(LinearLayout.GONE);
				}
			});

			imgChiudeInfo.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View vv) {
					l.ScriveLog(new Object() {
							}.getClass().getEnclosingMethod().getName(),
							"Image chiude info click");

					SuonAudio s=new SuonAudio();
					s.SuonaAudio(1, soundPool);

					LinearLayout layInfo=(LinearLayout) MainActivity.activity.findViewById(R.id.layInfo);
					layInfo.setVisibility(LinearLayout.GONE);
					LinearLayout layApreInfo=(LinearLayout) MainActivity.activity.findViewById(R.id.layApriInfo);
					layApreInfo.setVisibility(LinearLayout.VISIBLE);
				}
			});

			ImpostaDimensioni();

			// Notifiche.getInstance().CreaNotifica();
			// Notifiche.getInstance().AggiornaNotifica();

            /* if (service.ChiudiMaschera==null) {
                service.ChiudiMaschera=false;
            }
            if (service.ChiudiMaschera) {
                if (v!=null) {
                    v.moveTaskToBack(true);
                }
                service.ChiudiMaschera=false;
            } */
			dbl.LeggeOpzioni(context);
			dbl.LeggePercorsi(context);

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

			// Utility u = new Utility();
			// Boolean Ritorno=u.CambiaImmagine(true, 0);

			// } catch (Exception ignored) {

			// }
			l.ScriveLog(new Object() {
					}.getClass().getEnclosingMethod().getName(),
					"Fine funzione inizio");

			if (!VariabiliGlobali.getInstance().isPartito()) {
				try {
					MainActivity.activity.moveTaskToBack(true);
				} catch (Exception ignored) {

				}
			}

			VariabiliGlobali.getInstance().setPartito(true);

			SharedObjects.getInstance().setStaPartendo(false);
		}
	}

	private void ImpostaDimensioni() {
		LinearLayout layDirectory;
		LinearLayout layRefresh;
		LinearLayout layImposta;
		LinearLayout layAvanti;
		LinearLayout layIndietro;
		LinearLayout layOpzioni;

		layDirectory=(LinearLayout) MainActivity.activity.findViewById(R.id.layDirectory);
		layRefresh=(LinearLayout) MainActivity.activity.findViewById(R.id.layRefresh);
		layImposta=(LinearLayout) MainActivity.activity.findViewById(R.id.layImposta);
		layAvanti=(LinearLayout) MainActivity.activity.findViewById(R.id.layAvanti);
		layIndietro=(LinearLayout) MainActivity.activity.findViewById(R.id.layIndietro);
		layOpzioni=(LinearLayout) MainActivity.activity.findViewById(R.id.layOpzioni);
		LinearLayout layInfo=(LinearLayout) MainActivity.activity.findViewById(R.id.layInfo);
		layInfo.setVisibility(LinearLayout.GONE);
		LinearLayout layApreInfo=(LinearLayout) MainActivity.activity.findViewById(R.id.layApriInfo);
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

		// RefreshActivity.getInstance().RilanciaServizio(context, v);
	}

	private void LeggeImmagini(DBLocale dbl) {
		Log l = new Log();
		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"Legge Immagini");

		SharedObjects.getInstance().setListaImmagini(dbl.RitornaImmagini(VariabiliGlobali.getInstance().getContext()));
		if (SharedObjects.getInstance().getListaImmagini()==null) {
			SharedObjects.getInstance().setQuanteImm(0);
		} else {
			SharedObjects.getInstance().setQuanteImm(SharedObjects.getInstance().getListaImmagini().size());
		}
		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"Legge Immagini Fine");
	}

}

package com.looigi.cambiolacarta;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.looigi.cambiolacarta.AutoStart.service;
import com.looigi.cambiolacarta.R.string;
import com.looigi.cambiolacarta.Soap.DBRemoto;

import org.kobjects.util.Util;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;

public class MainActivity extends Activity {
	// Banner di pubblicit�
	// private RelativeLayout layout;
	// private AdView mAdView;
	// private AdManager mManager;
	// private String BannerID="c1455de07e2c0c6071670d4cb8c4baec";
	// Banner di pubblicit�

	private Context context;
	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;
	private PhoneUnlockedReceiver receiver;

	//private int pIT;
	//private int pEN;
	//private int nIT;
	//private int nEN;
	
  	// private Handler handlerAgg;
  	// private Runnable rAgg;

    public Context getContext() {
        if (context!=null) {
            return context;
        } else {
            return this;
        }
    }

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		context=this;

		VariabiliGlobali.getInstance().setContext(this);
		VariabiliGlobali.getInstance().setActivityPrincipale(this);

		if (receiver != null) {
			unregisterReceiver(receiver);
			receiver = null;
		}
		receiver = new PhoneUnlockedReceiver();
		IntentFilter fRecv = new IntentFilter();
		fRecv.addAction(Intent.ACTION_USER_PRESENT);
		fRecv.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(receiver, fRecv);

		SharedObjects.getInstance().setContext(context);
		SharedObjects.getInstance().setAudioManager((AudioManager)getSystemService(context.AUDIO_SERVICE));
		
		if (SharedObjects.getInstance().getDimeWallWidthOriginale()==0 && SharedObjects.getInstance().getDimeWallHeightOriginale()==0) {
			WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
			SharedObjects.getInstance().setDimeWallWidthOriginale (wallpaperManager.getDesiredMinimumWidth());
			SharedObjects.getInstance().setDimeWallHeightOriginale (wallpaperManager.getDesiredMinimumHeight());
		}
		
	    soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();
		soundPoolMap.put(1, soundPool.load(this, R.raw.premuto, 1));

		SharedObjects.getInstance().setTxtPercorso((TextView) findViewById(R.id.txtPercorso));
		SharedObjects.getInstance().setTxtNumImm((TextView) findViewById(R.id.txtNumImm));
		SharedObjects.getInstance().setTxtSceltaCartella((TextView) findViewById(R.id.txtSceltaCartella));
		SharedObjects.getInstance().setTxtCambiaSubito((TextView) findViewById(R.id.txtCambiaSubito));
		SharedObjects.getInstance().setTxtListaImm((TextView) findViewById(R.id.txtListaImm));
		SharedObjects.getInstance().setTxtOpzioni((TextView) findViewById(R.id.txtOpzioni));
		SharedObjects.getInstance().setTxtTipoCambio((TextView) findViewById(R.id.txtTipoCambio));
		SharedObjects.getInstance().setTxtMinuti((TextView) findViewById(R.id.txtMinuti));
		SharedObjects.getInstance().setTxtImmVisua((TextView) findViewById(R.id.txtImmVisualizzata));
		SharedObjects.getInstance().setTxtNomeImm((TextView) findViewById(R.id.txtNomeImm));
		SharedObjects.getInstance().setTxtProssima((TextView) findViewById(R.id.txtProssima));
		SharedObjects.getInstance().setTxtPrecedente((TextView) findViewById(R.id.txtPrecedente));
		SharedObjects.getInstance().setTxtTempo((TextView) findViewById(R.id.txtTempo));
		SharedObjects.getInstance().setTxtCaffe((TextView) findViewById(R.id.txtCaffe));
		
		// txtTempo2=(TextView) findViewById(R.id.txtTempo2);
		SharedObjects.getInstance().setChkAttivo((CheckBox) findViewById(R.id.chkAttivo));
		SharedObjects.getInstance().setImm((ImageView) findViewById(R.id.imgImmagine));
		
		//pIT=R.string.percorsoIT;
		//pEN=R.string.percorsoEN;
		//nIT=R.string.numimmIT;
		//nEN=R.string.numimmEN;
//
		// CreaBannerPubb();
		
		DisplayMetrics metrics = new DisplayMetrics();
		SharedObjects.getInstance().setA1(getWindowManager());
		SharedObjects.getInstance().getA1().getDefaultDisplay().getMetrics(metrics);

		SharedObjects.getInstance().setSchermoX(metrics.widthPixels);
		SharedObjects.getInstance().setSchermoY(metrics.heightPixels);

		DBLocale dbl=new DBLocale();
		dbl.CreaDB(context);
		dbl.LeggeOpzioni(context);
		dbl.LeggePercorsi(context);
		
		ImageView imgSceltaFolder=(ImageView) findViewById(R.id.imgSceltaCartella);
		imgSceltaFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);
            	
				// FermaTimerAggiornatore();
				Utility u = new Utility();
				u.FermaTimer();
				
				Intent Folder= new Intent(MainActivity.this, SceltaCartella.class);
		        startActivity(Folder);
		        
		        finish();
            }
        });					        
		
		ImageView imgRefresh=(ImageView) findViewById(R.id.imgRefresh);
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
		
		ImageView imgOpzioni=(ImageView) findViewById(R.id.imgAOpzioni);
		imgOpzioni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);
            	
				// FermaTimerAggiornatore();
				
				Intent Folder= new Intent(MainActivity.this, Opzioni.class);
		        startActivity(Folder);
		        
		        finish();
           }
        });					        
		
		ImageView imgCambia=(ImageView) findViewById(R.id.imgCambiaSubito);
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
						Toast.makeText(MainActivity.this, u.ControllaLingua(context,
								R.string.immimpIT, R.string.immimpEN),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(MainActivity.this,
								u.ControllaLingua(context, R.string.errimmimpIT, R.string.errimmimpEN),
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(MainActivity.this, u.ControllaLingua(context, R.string.noimmimpIT, R.string.noimmimpEN), Toast.LENGTH_SHORT).show();
				}
            }
        });

		LinearLayout layLista=(LinearLayout) findViewById(R.id.layLista);
		layLista.setVisibility(LinearLayout.GONE);
		
		ImageView imgLista=(ImageView) findViewById(R.id.imgListaImm);
		imgLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);
            }
        });

		ImageView imgProssima=(ImageView) findViewById(R.id.imgProssima);
		imgProssima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);

				Utility u = new Utility();
				int imm = u.PrendeNuovoNumero("Avanti");

				if (imm>=0) {
					SharedObjects.getInstance().setQualeImmagineHaVisualizzato(imm);

					DBLocale dbl = new DBLocale();
					dbl.ScriveOpzioni(context);

					u.ScriveInfo();

					u.ImpostaImmagineDiSfondo(SharedObjects.getInstance().getListaImmagini().get(SharedObjects.getInstance().getQualeImmagineHaVisualizzato()));
				}
            }
        });					        

		ImageView imgPrec=(ImageView) findViewById(R.id.imgIndietro);
		imgPrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);

				Utility u = new Utility();
				int imm = u.PrendeNuovoNumero("Indietro");

				if (imm>=0) {
					SharedObjects.getInstance().setQualeImmagineHaVisualizzato(imm);

					DBLocale dbl = new DBLocale();
					dbl.ScriveOpzioni(context);

					u.ScriveInfo();

					u.ImpostaImmagineDiSfondo(SharedObjects.getInstance().getListaImmagini().get(SharedObjects.getInstance().getQualeImmagineHaVisualizzato()));
				}
            }
        });					        

		ImageView imgCaffe=(ImageView) findViewById(R.id.imgCaffe);
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

				u.ScriveInfo();
            }
        });					        
		
		ImageView imgItaliano=(ImageView) findViewById(R.id.imgItaliano);
		imgItaliano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);

				SharedObjects.getInstance().setLingua("ITALIANO");
				
				DBLocale dbl=new DBLocale();
				dbl.ScriveOpzioni(context);

				Utility u = new Utility();
				u.ScriveInfo();
            }
        });					        
		
		ImageView imgInglese=(ImageView) findViewById(R.id.imgInglese);
		imgInglese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);

				SharedObjects.getInstance().setLingua("INGLESE");
				
				DBLocale dbl=new DBLocale();
				dbl.ScriveOpzioni(context);

				Utility u = new Utility();
				u.ScriveInfo();
            }
        });					        
		
		ImageView imgApreInfo=(ImageView) findViewById(R.id.imgApreInfo);
		imgApreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);

				LinearLayout layInfo=(LinearLayout) findViewById(R.id.layInfo);
				layInfo.setVisibility(LinearLayout.VISIBLE);
				LinearLayout layApreInfo=(LinearLayout) findViewById(R.id.layApriInfo);
				layApreInfo.setVisibility(LinearLayout.GONE);
            }
        });					        
		
		ImageView imgChiudeInfo=(ImageView) findViewById(R.id.imgChiudeInfo);
		imgChiudeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);

				LinearLayout layInfo=(LinearLayout) findViewById(R.id.layInfo);
				layInfo.setVisibility(LinearLayout.GONE);
				LinearLayout layApreInfo=(LinearLayout) findViewById(R.id.layApriInfo);
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
			moveTaskToBack(true);
			service.ChiudiMaschera=false;
		}

		Intent i= new Intent(VariabiliGlobali.getInstance().getActivityPrincipale(), bckService.class);
		VariabiliGlobali.getInstance().setiServizio(i);
		VariabiliGlobali.getInstance().getActivityPrincipale().startService(
				VariabiliGlobali.getInstance().getiServizio());
	}

	private void ImpostaDimensioni() {
		LinearLayout layDirectory;
		LinearLayout layRefresh;
		LinearLayout layImposta;
		LinearLayout layAvanti;
		LinearLayout layIndietro;
		LinearLayout layOpzioni;

		layDirectory=(LinearLayout) findViewById(R.id.layDirectory);
		layRefresh=(LinearLayout) findViewById(R.id.layRefresh);
		layImposta=(LinearLayout) findViewById(R.id.layImposta);
		layAvanti=(LinearLayout) findViewById(R.id.layAvanti);
		layIndietro=(LinearLayout) findViewById(R.id.layIndietro);
		layOpzioni=(LinearLayout) findViewById(R.id.layOpzioni);
		LinearLayout layInfo=(LinearLayout) findViewById(R.id.layInfo);
		layInfo.setVisibility(LinearLayout.GONE);
		LinearLayout layApreInfo=(LinearLayout) findViewById(R.id.layApriInfo);
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
	    
	}
	
	@Override
	protected void onPause() {
	    super.onPause();
	    if (!mKeyPress && mUserLeaveHint) {
			CreaNotifica();
			AggiornaNotifica();
        	moveTaskToBack(true);          	
	    }
	    if (mUserLeaveHint==false) {
	    	mUserLeaveHint=true;
	    }
	} */
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {    
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			/* SuonAudio s=new SuonAudio();
			s.SuonaAudio(1, soundPool);
        	
			CreaNotifica();
			AggiornaNotifica(); */
        	moveTaskToBack(true);          	

        	return false;    
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
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

}

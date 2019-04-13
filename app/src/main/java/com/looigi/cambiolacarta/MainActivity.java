package com.looigi.cambiolacarta;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
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

	private NotificationManager notificationManager;
	private NotificationCompat.Builder notificationBuilder;
    private RemoteViews contentView;
	private Context context;
	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;

	//private int pIT;
	//private int pEN;
	//private int nIT;
	//private int nEN;
	
  	private Handler handler;
  	private Runnable r;
  	private Handler handlerAgg;
  	private Runnable rAgg;
	private int MinutiPassati;
	private Timer timer;
	
	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		context=this;
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
            	
				FermaTimerAggiornatore();
				FermaTimer();
				
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
            	
				FermaTimerAggiornatore();
				FermaTimer();
				
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
            	
				FermaTimerAggiornatore();
				
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
	        	MinutiPassati=0;
	        	/* if (MinutiPassati<1) {
	        		MinutiPassati=1;
	        	} */
				
				if (SharedObjects.getInstance().getQuanteImm()>0) {
					Boolean Ritorno=CambiaImmagine(false);
					
					if (Ritorno) {
						Toast.makeText(MainActivity.this, u.ControllaLingua(context, R.string.immimpIT, R.string.immimpEN), Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(MainActivity.this, u.ControllaLingua(context, R.string.errimmimpIT, R.string.errimmimpEN), Toast.LENGTH_SHORT).show();
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

				SharedObjects.getInstance().setQualeImmagineHaVisualizzato(PrendeNuovoNumero("Avanti"));
				
				DBLocale dbl=new DBLocale();
				dbl.ScriveOpzioni(context);
				
				ScriveInfo();

				ImpostaImmagineDiSfondo(SharedObjects.getInstance().getListaImmagini().get(SharedObjects.getInstance().getQualeImmagineHaVisualizzato()));
            }
        });					        

		ImageView imgPrec=(ImageView) findViewById(R.id.imgIndietro);
		imgPrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);

				SharedObjects.getInstance().setQualeImmagineHaVisualizzato(PrendeNuovoNumero("Indietro"));
				
				DBLocale dbl=new DBLocale();
				dbl.ScriveOpzioni(context);
				
				ScriveInfo();

				ImpostaImmagineDiSfondo(SharedObjects.getInstance().getListaImmagini().get(SharedObjects.getInstance().getQualeImmagineHaVisualizzato()));
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

				if (SharedObjects.getInstance().getAttivo().equals("S")) {
					SharedObjects.getInstance().setAttivo("N");
					FermaTimer();
				} else {
					SharedObjects.getInstance().setAttivo("S");
					FaiPartireTimer();
				}
				
				DBLocale dbl=new DBLocale();
				dbl.ScriveOpzioni(context);
				
				ScriveInfo();
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
				
				ScriveInfo();
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
				
				ScriveInfo();
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

		SharedObjects.getInstance().setStaPartendo(true);
		if (SharedObjects.getInstance().getCaricaDati()==null) {
			SharedObjects.getInstance().setCaricaDati(false);
		}
		if (SharedObjects.getInstance().getCaricaDati()) {
			SharedObjects.getInstance().setCaricaDati(false);
			GestioneFilesCartelle gfc=new GestioneFilesCartelle();
			gfc.LeggeImmagini(context, SharedObjects.getInstance().getOrigine());
		} else {
			LeggeImmagini(dbl);
			ScriveInfo();
		}
		SharedObjects.getInstance().setStaPartendo(false);

		ImpostaDimensioni();
		
		CreaNotifica();
		AggiornaNotifica();
		
		if (service.ChiudiMaschera==null) {
			service.ChiudiMaschera=false;
		}
		if (service.ChiudiMaschera) {
			moveTaskToBack(true);
			service.ChiudiMaschera=false;
		}
		
		FaiPartireAggiornatore();
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
	
	public void FaiPartireTimer() {
		if (SharedObjects.getInstance().getListaImmagini().size()>0) {
			SharedObjects.getInstance().setTimerPartito(true);
			
	       	final int TotMinuti=SharedObjects.getInstance().getMinutiPerCambio();
	        
	        timer = new Timer();
	        timer.scheduleAtFixedRate( new java.util.TimerTask() {
	           @Override
	           public void run() {
	             	MinutiPassati++;
	            	if (MinutiPassati>TotMinuti) {
	            		MinutiPassati=0;
	            		
	            		Looper.prepare();
	            		
			        	Boolean Ritorno=CambiaImmagine(true);
						if (!Ritorno) {
							Utility u=new Utility();
							Toast.makeText(MainActivity.this, u.ControllaLingua(context, R.string.errimmimpIT, R.string.errimmimpEN), Toast.LENGTH_SHORT).show();
						}
						
			        	timer.cancel();
			        	timer=null;
						
			        	FaiRipartireTimer();
			        	
						Looper.loop();
	            	}
	          }}, 0, 60000 );        
		}
	}
	
	private void FaiRipartireTimer() {
		handler = new Handler(); 
        r = new Runnable() {
            public void run() {
            	FaiPartireTimer();
            	
            	handler.removeCallbacks(r);
            	
            	handler=null;
            	r=null;
           }
        };
        handler.postDelayed(r, 1000);
	}
	
	public void FaiPartireAggiornatore() {
		/* handlerAgg = new Handler(); 
        rAgg = new Runnable() {
            public void run() {
             	ScriveInfo();
            	AggiornaNotifica();
            	CreaBannerPubb();
            	
                handlerAgg.postDelayed(rAgg, 90000);
            }
        };
        handlerAgg.postDelayed(rAgg, 90000); */
	}

	private void FermaTimerAggiornatore() {
		try {
	    	handlerAgg.removeCallbacks(rAgg);
	    	
	    	handlerAgg=null;
	    	rAgg=null;
		} catch (Exception ignored) {
			
		}
	}
	
	private void FermaTimer() {
		SharedObjects.getInstance().setTimerPartito(false);
		
		if (SharedObjects.getInstance().getListaImmagini()!=null) {
			if (SharedObjects.getInstance().getListaImmagini().size()>0) {
		    	if (handler!=null) {
					try {
						handler.removeCallbacks(r);
					} catch (Exception ignored) {
						
					}
					handler = null;
			        r = null;
				}
		    	
		    	timer.cancel();
		    	timer=null;
			}
		}
	}

	public Boolean CambiaImmagine(Boolean Cambia) {
		Boolean Ritorno=true;
		
		if (SharedObjects.getInstance().getListaImmagini().size()>0) {
			int NuovoNumero;
			if (Cambia) {
				NuovoNumero=PrendeNuovoNumero("Avanti");
				DBLocale dbl=new DBLocale();
				dbl.ScriveOpzioni(context);
			} else {
				NuovoNumero=SharedObjects.getInstance().getQualeImmagineHaVisualizzato();
			}
			String NomeFile=SharedObjects.getInstance().getListaImmagini().get(NuovoNumero);

			// Sistema immagine
			// Sistema immagine

			ChangeWallpaper cw=new ChangeWallpaper();
			Ritorno=cw.setWallpaper(NomeFile);
			
			if (SharedObjects.getInstance().getNotificaSiNo().equals("S")) {
				AggiornaNotifica();
			}
		} else {
			Ritorno=false;
		}
		
		return Ritorno;
	}

    public void AggiornaNotifica() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
	    	try {
				setListeners(contentView);
			    notificationBuilder.setContent(contentView);
			    notificationBuilder.setOngoing(true);
			    notificationManager.notify(1, notificationBuilder.build());
	    	} catch (Exception ignored) {
	    		
	    	}
        } else {
        	CreaNotifica();
        }
    }

    public void setListeners(RemoteViews view){
    	Utility u=new Utility();
		if (SharedObjects.getInstance().getNotificaSiNo().equals("S")) {
	    	String Immagine="";
	    	GestioneFilesCartelle gfc=new GestioneFilesCartelle();
	    	try {
	    		Immagine=gfc.PrendeNomeFile(SharedObjects.getInstance().getListaImmagini().get(SharedObjects.getInstance().getQualeImmagineHaVisualizzato()));
	    		int pos=Immagine.indexOf(".");
	    		if (pos>-1) {
	    			Immagine=Immagine.substring(0, pos);
	    		}
	    	} catch (Exception ignored) {
	    		
	    	}
	    	if (!Immagine.equals("")) {
	    		view.setTextViewText(R.id.txtImmagine, Immagine);
	    	} else {
	    		view.setTextViewText(R.id.txtImmagine, "");
	    	}
	    	view.setTextViewText(R.id.TextView01, u.ControllaLingua(context, string.app_name, R.string.app_nameEN));
	    	
			Immagine=SharedObjects.getInstance().getListaImmagini().get(SharedObjects.getInstance().getQualeImmagineHaVisualizzato());
	       	Bitmap Immagin=null;
	       	
			view.setViewVisibility(R.id.imgCopertinaM, LinearLayout.VISIBLE);
			view.setViewVisibility(R.id.txtImmagine, LinearLayout.VISIBLE);
			view.setViewVisibility(R.id.layPicture, LinearLayout.VISIBLE);

			if (Immagine!=null && !Immagine.equals("")) {
		       	try {
		       		Immagin=u.PrendeImmagineCompressa(Immagine);
			       	view.setImageViewBitmap(R.id.imgCopertinaM, Immagin);
		       	} catch (Exception ignored) {
		       		view.setImageViewResource(R.id.imgCopertinaM, R.drawable.ic_launcher);
		       	}
	    	} else {
	       		view.setImageViewResource(R.id.imgCopertinaM, R.drawable.ic_launcher);
	    	}
		} else {
			view.setTextViewText(R.id.txtImmagine, u.ControllaLingua(context, string.app_name, R.string.app_nameEN));

			view.setViewVisibility(R.id.imgCopertinaM, LinearLayout.GONE);
			view.setViewVisibility(R.id.TextView01, LinearLayout.GONE);
			view.setViewVisibility(R.id.layPicture, LinearLayout.GONE);
		}
    }
    
	public void CreaNotifica() {
		if (SharedObjects.getInstance().getNotificaSiNo().equals("S")) {
	        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
	    	    notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);        	
	        	notificationBuilder = new NotificationCompat.Builder(MainActivity.this);
			    notificationBuilder.setSmallIcon(R.drawable.ic_launcher);
			    notificationBuilder.setOngoing(true);
	 
			    contentView = new RemoteViews(getPackageName(), R.layout.barra_notifica);
	           	setListenersTasti(contentView);
	
	           	notificationBuilder.setContent(contentView);
	          	notificationBuilder.setAutoCancel(false);
	           	
			    notificationManager.notify(1, notificationBuilder.build());
	       } else {
				Utility u=new Utility();
				String Titolo=u.ControllaLingua(context, string.app_name, R.string.app_nameEN);
				String Titolo2=u.ControllaLingua(context, string.clickIT, R.string.clickEN);
	   		
				Intent notificationIntent = new Intent(this, MainActivity.class);
			    PendingIntent pi = PendingIntent.getActivity(this, 0, notificationIntent, 0);
			    
			    // PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
				Notification notification =  new NotificationCompat.Builder(this).setAutoCancel(false)
	                       .setContentTitle(Titolo)
	                       .setContentText(Titolo2)
	                       .setContentIntent(pi)
	                       .setSmallIcon(R.drawable.ic_launcher)
	                       .setWhen(System.currentTimeMillis())
	                       .setTicker(Titolo)
	                       .build();       
				NotificationManager notificationManager =
				            (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
				notificationManager.notify(0, notification);       
	       }
		}
	}

    private void setListenersTasti(RemoteViews view){
    	Utility u=new Utility();
		view.setTextViewText(R.id.TextView01, u.ControllaLingua(context, string.app_name, R.string.app_nameEN));

		Intent cambia=new Intent(context, PassaggioNotifica.class);
	    cambia.putExtra("DO", "cambia");
	    PendingIntent pCambia = PendingIntent.getActivity(context, 0, cambia, 0);
	    view.setOnClickPendingIntent(R.id.imgCambia, pCambia);
	    view.setImageViewResource(R.id.imgCambia, R.drawable.refresh);
	
	    Intent avanti=new Intent(context, PassaggioNotifica.class);
	    avanti.putExtra("DO", "avanti");
	    PendingIntent pAvanti = PendingIntent.getActivity(context, 1, avanti, 0);
	    view.setOnClickPendingIntent(R.id.ImgAvanti, pAvanti);
	    view.setImageViewResource(R.id.ImgAvanti, R.drawable.avanti);
	
	    Intent apre=new Intent(context, PassaggioNotifica.class);
	    apre.putExtra("DO", "apre");
	    PendingIntent pApre= PendingIntent.getActivity(context, 2, apre, 0);
	    view.setOnClickPendingIntent(R.id.ImgApreNB, pApre);
    }
	
	public int PrendeNuovoNumero(String Come) {
		int Ritorno=0;
		
		if (SharedObjects.getInstance().getTipoCambio().equals("RANDOM")) {
			Random r;
			r=new Random();
			int i1=r.nextInt(SharedObjects.getInstance().getListaImmagini().size());
			i1--;
			if (i1<0) {
				i1=0;
			}
			Ritorno=i1;
			SharedObjects.getInstance().setQualeImmagineHaVisualizzato(i1);
		} else {
			if (Come.equals("Indietro")) {
				SharedObjects.getInstance().setQualeImmagineHaVisualizzato(SharedObjects.getInstance().getQualeImmagineHaVisualizzato()-1);
				if (SharedObjects.getInstance().getQualeImmagineHaVisualizzato()<0){
					SharedObjects.getInstance().setQualeImmagineHaVisualizzato(SharedObjects.getInstance().getListaImmagini().size());
				}
				Ritorno=SharedObjects.getInstance().getQualeImmagineHaVisualizzato();
			} else {
				SharedObjects.getInstance().setQualeImmagineHaVisualizzato(SharedObjects.getInstance().getQualeImmagineHaVisualizzato()+1);
				if (SharedObjects.getInstance().getQualeImmagineHaVisualizzato()>=SharedObjects.getInstance().getListaImmagini().size()){
					SharedObjects.getInstance().setQualeImmagineHaVisualizzato(0);
				}
				Ritorno=SharedObjects.getInstance().getQualeImmagineHaVisualizzato();
			}
		}
		
		return Ritorno;
	}
	
	public void ScriveInfo() {
    	Context context=SharedObjects.getInstance().getContext();
		Utility u = new Utility();
		String s = u.ControllaLingua(context, R.string.percorsoIT, R.string.percorsoEN) + ": " + SharedObjects.getInstance().getOrigine();
		SharedObjects.getInstance().getTxtPercorso().setText(s);
		s = u.ControllaLingua(context, R.string.numimmIT, R.string.numimmEN) + ": " + SharedObjects.getInstance().getQuanteImm();
		SharedObjects.getInstance().getTxtNumImm().setText(s);
		SharedObjects.getInstance().getTxtSceltaCartella().setText(u.ControllaLingua(context, R.string.cambiofolderIT, R.string.cambiofolderEN));
		SharedObjects.getInstance().getTxtCambiaSubito().setText(u.ControllaLingua(context, R.string.cambiosubitoIT, R.string.cambiosubitoEN));
		SharedObjects.getInstance().getTxtListaImm().setText(u.ControllaLingua(context, R.string.listaIT, R.string.listaEN));
		SharedObjects.getInstance().getTxtOpzioni().setText(u.ControllaLingua(context, R.string.opzioniIT, R.string.opzioniEN));
		SharedObjects.getInstance().getTxtProssima().setText(u.ControllaLingua(context, R.string.avantiIT, R.string.avantiEN));
		SharedObjects.getInstance().getTxtPrecedente().setText(u.ControllaLingua(context, R.string.indietroIT, R.string.indietroEN));
		s = u.ControllaLingua(context, R.string.tempoIT, R.string.tempoEN) + ": " + SharedObjects.getInstance().getMinutiPerCambio();
		SharedObjects.getInstance().getTxtMinuti().setText(s);
		String sTipoCambio;
		if (SharedObjects.getInstance().getTipoCambio().equals("RANDOM")) {
			sTipoCambio = "Random";
		} else {
			sTipoCambio = u.ControllaLingua(context, R.string.optseqIT, R.string.optseqEN);
		}
		s = u.ControllaLingua(context, R.string.modalitaIT, R.string.modalitaEN) + ": " + sTipoCambio;
		SharedObjects.getInstance().getTxtTipoCambio().setText(s);
		s = u.ControllaLingua(context, R.string.immvisuaIT, R.string.immvisuaEN) + ": " + SharedObjects.getInstance().getQualeImmagineHaVisualizzato();
		SharedObjects.getInstance().getTxtImmVisua().setText(s);
		try {
			SharedObjects.getInstance().getTxtNomeImm().setText(SharedObjects.getInstance().getListaImmagini().get(SharedObjects.getInstance().getQualeImmagineHaVisualizzato()));
			ImpostaImmagineDiSfondo(SharedObjects.getInstance().getListaImmagini().get(SharedObjects.getInstance().getQualeImmagineHaVisualizzato()));
		} catch (Exception ignored) {
			SharedObjects.getInstance().getTxtNomeImm().setText("");
		}
		if (SharedObjects.getInstance().getAttivo().equals("S")) {
			SharedObjects.getInstance().getChkAttivo().setText(u.ControllaLingua(context, R.string.attivoIT, R.string.attivoEN));
			SharedObjects.getInstance().getChkAttivo().setChecked(true);
			if (SharedObjects.getInstance().getStaPartendo()) {
				if (SharedObjects.getInstance().getTimerPartito() == null) {
					SharedObjects.getInstance().setTimerPartito (false);
				}
				if (!SharedObjects.getInstance().getTimerPartito()) {
					FaiPartireTimer();
				}
			}
		} else {
			SharedObjects.getInstance().getChkAttivo().setText(u.ControllaLingua(context, R.string.inattivoIT, R.string.inattivoEN));
			SharedObjects.getInstance().getChkAttivo().setChecked(false);
			if (SharedObjects.getInstance().getStaPartendo()) {
				FermaTimer();
			}
		}
		// txtTempo2.setText(u.ControllaLingua(context, R.string.tempo2IT, R.string.tempo2EN));
		SharedObjects.getInstance().getTxtCaffe().setText(u.ControllaLingua(context, R.string.caffeIT, R.string.caffeEN));
		SharedObjects.getInstance().getTxtCaffe().setSelected(true);

		// ImpostaDimensioni();

		setTitle(u.ControllaLingua(context, R.string.app_name, R.string.app_nameEN));
	}
	
	public void ImpostaImmagineDiSfondo(String NomeImm) {
		if (NomeImm!=null) {
			if (!NomeImm.trim().equals("")) {
				GestioneFilesCartelle gfc=new GestioneFilesCartelle();
				if (gfc.EsisteFile(gfc.PrendeNomeFile(NomeImm), gfc.PrendeNomeCartella(NomeImm))) {
					Utility u=new Utility();
					
					Bitmap myBitmap = u.getPreview(NomeImm);
					myBitmap=u.ConverteDimensioniInterne(myBitmap);
					SharedObjects.getInstance().getImm().setImageBitmap(myBitmap);
				} else {
					SharedObjects.getInstance().getImm().setBackgroundResource(R.drawable.ic_launcher);
				}
			} else {
				SharedObjects.getInstance().getImm().setBackgroundResource(R.drawable.ic_launcher);
			}
		}
	}
	
	private void LeggeImmagini(DBLocale dbl) {
		SharedObjects.getInstance().setListaImmagini(dbl.RitornaImmagini(context));
		if (SharedObjects.getInstance().getListaImmagini()==null) {
			SharedObjects.getInstance().setQuanteImm(0);
		} else {
			SharedObjects.getInstance().setQuanteImm(SharedObjects.getInstance().getListaImmagini().size());
		}
	}

	public void RimuoviNotifica() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        	try {
        		notificationManager.cancel(1);
        		notificationManager=null;
        	} catch (Exception ignored) {
        		
        	}
        }
	}
	
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

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

import com.looigi.cambiolacarta.AutoStart.service;
import com.looigi.cambiolacarta.R.string;
import com.looigi.cambiolacarta.Soap.DBRemoto;

import org.kobjects.util.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;

public class MainActivity extends Activity {
	private boolean CiSonoPermessi;
	// private MemoryBoss mMemoryBoss;
	public static Context ctxPrincipale;
	private PhoneUnlockedReceiver receiver;

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

	@Override
	protected void onDestroy() {
    	Log l = new Log();

		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"Attività distrutta");

		this.recreate();

		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ctxPrincipale = this;

		Permessi pp = new Permessi();
		CiSonoPermessi = pp.ControllaPermessi(this);
		if (CiSonoPermessi) {
			EsegueEntrata();

			// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			// 	mMemoryBoss = new MemoryBoss();
			// 	registerComponentCallbacks(mMemoryBoss);
			// }
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

			}

			EsegueEntrata();
		}
	}

	private void EsegueEntrata() {
		String AutomaticReload = getIntent().getStringExtra("AUTOMATIC RELOAD");
		// if (AutomaticReload !=null && AutomaticReload.equals("YES")) {
		// }
		// context=this;

		SharedObjects.getInstance().setContext(this);
		VariabiliGlobali.getInstance().setContext(this);
		VariabiliGlobali.getInstance().setActivityPrincipale(this);

		// VariabiliGlobali.getInstance().setiServizio(new Intent(VariabiliGlobali.getInstance().getActivityPrincipale(), bckService.class));
		// VariabiliGlobali.getInstance().getActivityPrincipale().startService(
		// 		VariabiliGlobali.getInstance().getiServizio());
		startService();

		if (AutomaticReload !=null && AutomaticReload.equals("YES")) {
			moveTaskToBack(true);
		}
	}

	public void startService() {
		Log l = new Log();

		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"Attivo servizio");

		Intent serviceIntent = new Intent(this, ServizioInterno.class);
		serviceIntent.putExtra("inputExtra", "Cambia la carta");
		// ContextCompat.startForegroundService(this, serviceIntent);
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			startForegroundService(serviceIntent);
		} else {

		}

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
        	// moveTaskToBack(true);
            Intent setIntent = new Intent(Intent.ACTION_MAIN);
            setIntent.addCategory(Intent.CATEGORY_HOME);
            setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(setIntent);

        	return false;    
		}
		
		return super.onKeyDown(keyCode, event);
	}

	@Override
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

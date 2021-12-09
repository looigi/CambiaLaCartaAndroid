package com.looigi.cambiolacarta;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.HashMap;

public class Opzioni extends Activity{
	SoundPool soundPool;
	HashMap<Integer, Integer> soundPoolMap;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opzioni);
	
		context=this;
		
	    soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();
		soundPoolMap.put(1, soundPool.load(this, R.raw.premuto, 1));
		
		Utility u=new Utility();
		
		setTitle(u.ControllaLingua(context, R.string.app_name, R.string.app_nameEN));

		final CheckBox chkResize=(CheckBox) findViewById(R.id.chkResize);
		if (SharedObjects.getInstance().getStretch().equals("S")) {
			chkResize.setChecked(true);
		} else {
			chkResize.setChecked(false);
		}
		chkResize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);

				CheckBox chkStretch=(CheckBox) findViewById(R.id.chkStretch);
				if (chkResize.isChecked()) {
					SharedObjects.getInstance().setStretch("S");
					chkStretch.setVisibility(LinearLayout.VISIBLE);
				} else {
					SharedObjects.getInstance().setStretch("N");
					chkStretch.setVisibility(LinearLayout.GONE);
				}
				
				DBLocale dbl=new DBLocale();
				dbl.ScriveOpzioni(context);
				
				ImpostaCheck();
            }
        });

		final CheckBox chkLockScreen=(CheckBox) findViewById(R.id.chkLockScreen);
		chkLockScreen.setChecked(SharedObjects.getInstance().isSettaLockScreen());
		chkLockScreen.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);

				SharedObjects.getInstance().setSettaLockScreen(chkLockScreen.isChecked());

				DBLocale dbl=new DBLocale();
				dbl.ScriveOpzioni(context);

				ImpostaCheck();
			}
		});

		TextView t=(TextView) findViewById(R.id.txtNotifica);
		t.setText(u.ControllaLingua(context, R.string.tNotificaIT, R.string.tNotificaEN));
		
		final CheckBox chkNotifica=(CheckBox) findViewById(R.id.chkNotifica);
		chkNotifica.setText(u.ControllaLingua(context, R.string.NotificaIT, R.string.NotificaEN));
		if (SharedObjects.getInstance().getNotificaSiNo().equals("S")) {
			chkNotifica.setChecked(true);
		} else {
			chkNotifica.setChecked(false);
		}
		chkNotifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);

				if (chkNotifica.isChecked()) {
					SharedObjects.getInstance().setNotificaSiNo("S");
					// m.CreaNotifica();
				} else {
					SharedObjects.getInstance().setNotificaSiNo("N");
					// Notifiche.getInstance().RimuoviNotifica();
				}
				
				DBLocale dbl=new DBLocale();
				dbl.ScriveOpzioni(context);
            }
        });					        
		
		final CheckBox chkStretch=(CheckBox) findViewById(R.id.chkStretch);
		chkStretch.setText(u.ControllaLingua(context, R.string.stretchIT, R.string.stretchEN));
		if (chkResize.isChecked()) {
			chkStretch.setVisibility(LinearLayout.VISIBLE);
		} else {
			chkStretch.setVisibility(LinearLayout.GONE);
		}
		if (SharedObjects.getInstance().getModalitaVisua().equals("S")) {
			chkStretch.setChecked(true);
		} else {
			chkStretch.setChecked(false);
		}
		chkStretch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);

				if (chkStretch.isChecked()) {
					SharedObjects.getInstance().setModalitaVisua("S");
				} else {
					SharedObjects.getInstance().setModalitaVisua("N");
				}
				
				DBLocale dbl=new DBLocale();
				dbl.ScriveOpzioni(context);
				
				ImpostaCheck();
            }
        });					        

		RadioButton optRandom=(RadioButton) findViewById(R.id.optRandom);
		optRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);

				SharedObjects.getInstance().setTipoCambio("RANDOM");

				SharedObjects.getInstance().getImgCambia().setVisibility(LinearLayout.VISIBLE);
				SharedObjects.getInstance().getImgPrec().setVisibility(LinearLayout.VISIBLE);
				SharedObjects.getInstance().getImgSucc().setVisibility(LinearLayout.VISIBLE);
				SharedObjects.getInstance().getImgRefresh().setVisibility(LinearLayout.VISIBLE);
				SharedObjects.getInstance().getImgCambiaDir().setVisibility(LinearLayout.VISIBLE);

				DBLocale dbl=new DBLocale();
				dbl.ScriveOpzioni(context);
				
				ImpostaCheck();
            }
        });					        

		RadioButton optSequenziale=(RadioButton) findViewById(R.id.optSequenziale);
		optSequenziale.setText(u.ControllaLingua(context, R.string.optseqIT, R.string.optseqEN));
		optSequenziale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);

				SharedObjects.getInstance().setTipoCambio("SEQUENZIALE");

				SharedObjects.getInstance().getImgCambia().setVisibility(LinearLayout.VISIBLE);
				SharedObjects.getInstance().getImgPrec().setVisibility(LinearLayout.VISIBLE);
				SharedObjects.getInstance().getImgSucc().setVisibility(LinearLayout.VISIBLE);
				SharedObjects.getInstance().getImgRefresh().setVisibility(LinearLayout.VISIBLE);
				SharedObjects.getInstance().getImgCambiaDir().setVisibility(LinearLayout.VISIBLE);

 				DBLocale dbl=new DBLocale();
				dbl.ScriveOpzioni(context);
				
				ImpostaCheck();
            }
        });

		RadioButton optSincronizzata=(RadioButton) findViewById(R.id.optSincronizzata);
		optSincronizzata.setText(u.ControllaLingua(context, R.string.optsinIT, R.string.optsinEN));
		optSincronizzata.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);

				SharedObjects.getInstance().setTipoCambio("SINCRONIZZATA");

				SharedObjects.getInstance().getImgCambia().setVisibility(LinearLayout.GONE);
				SharedObjects.getInstance().getImgPrec().setVisibility(LinearLayout.GONE);
				SharedObjects.getInstance().getImgSucc().setVisibility(LinearLayout.GONE);
				SharedObjects.getInstance().getImgRefresh().setVisibility(LinearLayout.GONE);
				SharedObjects.getInstance().getImgCambiaDir().setVisibility(LinearLayout.GONE);

				DBLocale dbl=new DBLocale();
				dbl.ScriveOpzioni(context);

				ImpostaCheck();
			}
		});

		ImageView imgIndietro=(ImageView) findViewById(R.id.imgIndietro);
		imgIndietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);

                SharedObjects.getInstance().setGiaEntrato(false);

				Intent Folder= new Intent(Opzioni.this, MainActivity.class);
		        startActivity(Folder);
		        
		        finish();
            }
        });					        

		ImageView imgSalva=(ImageView) findViewById(R.id.imgSalvaSecondi);
		imgSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);

				EditText et=(EditText) findViewById(R.id.txtSecondi);
				int Minuti=Integer.parseInt(et.getText().toString());

				Utility u=new Utility();
				if (Minuti<1 || Minuti>60) {
					String Messaggio=u.ControllaLingua(context, R.string.minutinonvalidiIT, R.string.minutinonvalidiEN);
					
					VisualizzaPOPUP(context, Messaggio, false, 0);
				} else {
					SharedObjects.getInstance().setMinutiPerCambio(Minuti);
					
					DBLocale dbl=new DBLocale();
					dbl.ScriveOpzioni(context);

					String Messaggio=u.ControllaLingua(context, R.string.salvatoIT, R.string.salvatoEN);
					
					VisualizzaPOPUP(context, Messaggio, false, 0);
				}
            }
        });					        
		
		TextView tt=(TextView) findViewById(R.id.txtTempo);
		tt.setText(u.ControllaLingua(context, R.string.tempoIT, R.string.tempoEN));
		
		ImpostaCheck();
        
		getWindow().setSoftInputMode(
			      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);	
	}

	private void ImpostaCheck() {
		RadioButton optRandom=(RadioButton) findViewById(R.id.optRandom);
		RadioButton optSequenziale=(RadioButton) findViewById(R.id.optSequenziale);
		RadioButton optSinc=(RadioButton) findViewById(R.id.optSincronizzata);

		if (SharedObjects.getInstance().getTipoCambio().equals("RANDOM")) {
			optRandom.setChecked(true);
			optSequenziale.setChecked(false);
			optSinc.setChecked(false);
		} else {
			if (SharedObjects.getInstance().getTipoCambio().equals("SEQUENZIALE")) {
				optRandom.setChecked(false);
				optSequenziale.setChecked(true);
				optSinc.setChecked(false);
			} else {
				optRandom.setChecked(false);
				optSequenziale.setChecked(false);
				optSinc.setChecked(true);
			}
		}
		
		EditText et=(EditText) findViewById(R.id.txtSecondi);
		et.setText(""+SharedObjects.getInstance().getMinutiPerCambio());
	}
	
	private void VisualizzaPOPUP(final Context context, String Messaggio, final Boolean Tasti, final int QualeOperazione) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		Utility u=new Utility();
		builder.setTitle(u.ControllaLingua(context, R.string.titoloIT, R.string.titoloEN));

		builder.setMessage(Messaggio);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);
				
                dialog.cancel();
            }
        });		
		if (Tasti) {
			if (SharedObjects.getInstance().getLingua().equals("INGLESE")) {
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) {
						SuonAudio s=new SuonAudio();
						s.SuonaAudio(1, soundPool);
						
		                dialog.cancel();
		            }
		        });		
    		} else {
    			builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int id) {
    					SuonAudio s=new SuonAudio();
    					s.SuonaAudio(1, soundPool);
    					
    	                dialog.cancel();
    	            }
    	        });		
    		}
		}
		@SuppressWarnings("unused")
		AlertDialog dialog = builder.show();
	}
	
}

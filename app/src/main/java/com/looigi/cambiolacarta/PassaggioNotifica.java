package com.looigi.cambiolacarta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.looigi.cambiolacarta.AutoStart.service;

public class PassaggioNotifica extends Activity {
    Context context;
   
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		context=this;
		String action="";
		Boolean Apre=false;
		Log l = new Log();
		
		try {
			action= (String)getIntent().getExtras().get("DO");
		} catch (Exception ignored) {
			
		}

		Utility u = new Utility();
		if (action.equals("cambia")) {
			u.CambiaImmagine(false, 0);
		}
		
		if (action.equals("avanti")) {
			u.CambiaImmagine(true, 0);
		}
		
		if (action.equals("apre")) {
			u.ScriveInfo(l);
			String Nome="";
			
			try {
				Nome=SharedObjects.getInstance().getListaImmagini().get(SharedObjects.getInstance().getQualeImmagineHaVisualizzato());
			} catch (Exception ignored) {
				
			}
			if (Nome!=null) {
				u.ImpostaImmagineDiSfondo(Nome, l);
			}
			
			Apre=true;
		}

		if (!Apre) {
			try {
				moveTaskToBack(true);
			} catch (Exception ignored) {
				
			}
		} else {
			service.ChiudiMaschera = false;

			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
		
    	finish();
    }
}

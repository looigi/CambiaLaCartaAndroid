package com.looigi.cambiolacarta;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class PassaggioNotifica extends Activity {
    Context context;
   
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		context=this;
		String action="";
		Boolean Apre=false;
		
		try {
			action= (String)getIntent().getExtras().get("DO");
		} catch (Exception ignored) {
			
		}

		if (action.equals("cambia")) {
			MainActivity ma=new MainActivity();
			ma.CambiaImmagine(false);
		}
		
		if (action.equals("avanti")) {
			MainActivity ma=new MainActivity();
			ma.CambiaImmagine(true);
		}
		
		if (action.equals("apre")) {
			MainActivity ma=new MainActivity();
			ma.ScriveInfo();
			String Nome="";
			
			try {
				Nome=SharedObjects.getInstance().getListaImmagini().get(SharedObjects.getInstance().getQualeImmagineHaVisualizzato());
			} catch (Exception ignored) {
				
			}
			if (Nome!=null) {
				ma.ImpostaImmagineDiSfondo(Nome);
			}
			
			Apre=true;
		}

		if (!Apre) {
			try {
				moveTaskToBack(true);
			} catch (Exception ignored) {
				
			}
		}
		
    	finish();
    }
}

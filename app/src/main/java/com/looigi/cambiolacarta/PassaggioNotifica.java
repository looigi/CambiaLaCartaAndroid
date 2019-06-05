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

		Utility u = new Utility();
		if (action.equals("cambia")) {
			u.CambiaImmagine(false, 0);
		}
		
		if (action.equals("avanti")) {
			u.CambiaImmagine(true, 0);
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
				u.ImpostaImmagineDiSfondo(Nome);
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

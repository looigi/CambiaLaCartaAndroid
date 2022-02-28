package com.looigi.cambiolacarta;
/*
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class PassaggioNotifica extends Activity {
    Context context;
   
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		context=this;
		String action="";
		boolean Apre=false;
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

			if (SharedObjects.getInstance().getTipoCambio() == null) {
				DBLocale dbl = new DBLocale();
				dbl.LeggeOpzioni(context);
			}

			if (!SharedObjects.getInstance().getTipoCambio().equals("SINCRONIZZATA")) {
				try {
					Nome = SharedObjects.getInstance().getListaImmagini().get(SharedObjects.getInstance().getQualeImmagineHaVisualizzato());
				} catch (Exception ignored) {

				}
			} else {
				Nome = SharedObjects.getInstance().getPercorsoDIR() + "/Download.jpg";
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
			// service.ChiudiMaschera = false;

			SharedObjects.getInstance().setGiaEntrato(false);

			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
		
    	finish();
    }
}
*/
package com.looigi.cambiolacarta.Receivers;

// import android.content.BroadcastReceiver;
// import android.content.Context;
// import android.content.Intent;
//
// import com.looigi.detector.Utilities.Log;
// import com.looigi.detector.Fotocamera.UsaQuestoPerScattare;
//
// public class Photo extends BroadcastReceiver {
// 	@Override
// 	public void onReceive(Context context, Intent intent) {
// 		if(intent.getAction().equals("MAKE_CLICK")) {
// 			final Log l=new Log();
// 			l.PulisceFileDiLog();
//
// 			UsaQuestoPerScattare uq = new UsaQuestoPerScattare();
// 			uq.ScattaFoto(context, l);
// 		}
// 	}
// }


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.looigi.cambiolacarta.Log;
import com.looigi.cambiolacarta.MainActivity;
import com.looigi.cambiolacarta.R;
import com.looigi.cambiolacarta.SharedObjects;
import com.looigi.cambiolacarta.Utility;
import com.looigi.cambiolacarta.VariabiliGlobali;

public class Photo extends Activity {
	// private static Context context;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget);

		Toast.makeText(MainActivity.activity, "Cambio Immagine",
		        Toast.LENGTH_SHORT).show();

		// UsaQuestoPerScattare uq = new UsaQuestoPerScattare();
		// uq.ScattaFoto(l);
		String vecchio = SharedObjects.getInstance().getTipoCambio();
		SharedObjects.getInstance().setVecchioValoreCambio(vecchio);
		SharedObjects.getInstance().setTipoCambio("RANDOM");
		Boolean Ritorno = Utility.CambiaImmagine(true, 0);

		this.finish();
	}
}

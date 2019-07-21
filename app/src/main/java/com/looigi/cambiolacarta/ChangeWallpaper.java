package com.looigi.cambiolacarta;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ChangeWallpaper {

	public Boolean setWallpaper(String src, Log l) {
		Context context=SharedObjects.getInstance().getContext();
		boolean Ritorno=true;

		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"Cambio immagine: Caricamento bitmap.");

		Utility u=new Utility();
		Bitmap setWallToDevice = u.PrendeImmagineReale(src, l);
		
		if (setWallToDevice!=null) {
			l.ScriveLog(new Object() {
					}.getClass().getEnclosingMethod().getName(),
					"Cambio immagine: Applicazione wallpaper.");

			WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
			try {
				l.ScriveLog(new Object() {
						}.getClass().getEnclosingMethod().getName(),
						"Cambio immagine: Impostazione dimensioni.");

				if (SharedObjects.getInstance().getStretch().equals("S")) {
					wallpaperManager.suggestDesiredDimensions(SharedObjects.getInstance().getSchermoX(), SharedObjects.getInstance().getSchermoY());
				} else {
					wallpaperManager.suggestDesiredDimensions(SharedObjects.getInstance().getDimeWallWidthOriginale(), SharedObjects.getInstance().getDimeWallHeightOriginale());
				}

				l.ScriveLog(new Object() {
						}.getClass().getEnclosingMethod().getName(),
						"Cambio immagine: Settaggio bitmap.");

				wallpaperManager.setBitmap(setWallToDevice);

				if (SharedObjects.getInstance().isSettaLockScreen()) {
                    // set wallpaper lock screen
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    setWallToDevice.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);

                    WallpaperManager.getInstance(VariabiliGlobali.getInstance().getContext())
                            .setStream(bs, null, true, WallpaperManager.FLAG_LOCK);
                    // set wallpaper lock screen
                }
			} catch (IOException e) {
			    // l.ScriveLog("Errore: " + u.PrendeErroreDaException(e));
				// e.printStackTrace();

				l.ScriveLog(new Object() {
						}.getClass().getEnclosingMethod().getName(),
						"Cambio immagine: Errore\n" + u.PrendeErroreDaException(e));

				// Toast.makeText(VariabiliGlobali.getInstance().getContext(),
				// 		u.PrendeErroreDaException(e),
				// 		Toast.LENGTH_LONG).show();

				Ritorno=false;
			}			
		} else {
			Ritorno=false;
		}
		
		return Ritorno;
	}
}

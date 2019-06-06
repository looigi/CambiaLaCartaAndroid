package com.looigi.cambiolacarta;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.io.IOException;

public class ChangeWallpaper {

	public Boolean setWallpaper(String src) {
		Context context=SharedObjects.getInstance().getContext();
		boolean Ritorno=true;
		Log l=new Log();
		
		Utility u=new Utility();
		Bitmap setWallToDevice = u.PrendeImmagineReale(src);
		
		if (setWallToDevice!=null) {
			WallpaperManager wallpaperManager = WallpaperManager.getInstance(context); 
			try {
				if (SharedObjects.getInstance().getStretch().equals("S")) {
					wallpaperManager.suggestDesiredDimensions(SharedObjects.getInstance().getSchermoX(), SharedObjects.getInstance().getSchermoY());
				} else {
					wallpaperManager.suggestDesiredDimensions(SharedObjects.getInstance().getDimeWallWidthOriginale(), SharedObjects.getInstance().getDimeWallHeightOriginale());
				}
				wallpaperManager.setBitmap(setWallToDevice);
			} catch (IOException e) {
			    // l.ScriveLog("Errore: " + u.PrendeErroreDaException(e));
				// e.printStackTrace();

				Toast.makeText(VariabiliGlobali.getInstance().getContext(),
						u.PrendeErroreDaException(e),
						Toast.LENGTH_SHORT).show();

				Ritorno=false;
			}			
		} else {
			Ritorno=false;
		}
		
		return Ritorno;
	}
}

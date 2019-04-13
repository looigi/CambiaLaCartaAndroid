package com.looigi.cambiolacarta;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;

import java.io.IOException;

public class ChangeWallpaper {

	public Boolean setWallpaper(String src) {
		Context context=SharedObjects.getInstance().getContext();
		Boolean Ritorno=true;
		
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
				e.printStackTrace();
				Ritorno=false;
			}			
		} else {
			Ritorno=false;
		}
		
		return Ritorno;
	}
}

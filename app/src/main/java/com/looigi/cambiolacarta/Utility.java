package com.looigi.cambiolacarta;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

@SuppressWarnings("WeakerAccess")
public class Utility {
	private int BordoX=5;
	private int BordoY=5;
	
	public String ControllaLingua(Context context, int CosaIT, int CosaEN) {
		String Ritorno="";
		
		if (SharedObjects.getInstance().getLingua().equals("INGLESE")) {
			Ritorno=context.getString(CosaEN);
		} else {
			Ritorno=context.getString(CosaIT);
		}
		
		return Ritorno;
	}

	private String TransformError(String error) {
		String Return=error;

		if (Return.length()>250) {
			Return=Return.substring(0,247)+"...";
		}
		Return=Return.replace("\n"," ");

		return Return;
	}

	public String PrendeErroreDaException(Exception e) {
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));

		return TransformError(errors.toString());
	}

	public Bitmap PrendeImmagineReale(String FileName) {
		Bitmap myBitmap=null;
		
		GestioneFilesCartelle gfc=new GestioneFilesCartelle();
		if (gfc.EsisteFile(gfc.PrendeNomeFile(FileName), gfc.PrendeNomeCartella(FileName))) {
			DisplayMetrics metrics = new DisplayMetrics();
			SharedObjects.getInstance().getA1().getDefaultDisplay().getMetrics(metrics);

			SharedObjects.getInstance().setSchermoX(metrics.widthPixels);
			SharedObjects.getInstance().setSchermoY(metrics.heightPixels);
	
			myBitmap = getPreview(FileName);
			if (myBitmap!=null) {
				if (SharedObjects.getInstance().getStretch().equals("S")) {
					if (SharedObjects.getInstance().getModalitaVisua().equals("N")) {
						myBitmap=ConverteDimensioni(myBitmap);
						if (myBitmap!=null) {
							try {
								// Bitmap Immaginona = Bitmap.createBitmap(SharedObjects.getInstance().getSchermoX(), SharedObjects.getInstance().getSchermoY(), Bitmap.Config.ARGB_8888);
								// Canvas comboImage = new Canvas(Immaginona);
								// float Altezza=(((float) (SharedObjects.getInstance().getSchermoY()))/2)-(myBitmap.getHeight()/2);
								// float Larghezza=(((float) (SharedObjects.getInstance().getSchermoX()))/2)-(myBitmap.getWidth()/2);
								// comboImage.drawBitmap(myBitmap, Larghezza, Altezza, null);
								myBitmap=MetteBordoAImmagine(myBitmap);
							} catch (Exception ignored) {
								myBitmap=null;
							}
						} else {
							myBitmap=null;
						}
					} else {
						myBitmap = Bitmap.createScaledBitmap(myBitmap, (int) SharedObjects.getInstance().getSchermoX()-BordoX, 
								(int) SharedObjects.getInstance().getSchermoY()-BordoY, true);
					}
				}
				// SalvaImmagine(myBitmap);
			}
		} else {
			myBitmap=null;
		}
		
		return myBitmap;
	}

	private Bitmap MetteBordoAImmagine(Bitmap myBitmap) {
		int SchermoX = SharedObjects.getInstance().getSchermoX();
		int SchermoY = SharedObjects.getInstance().getSchermoY();

		int ImmagineX = myBitmap.getWidth();
		int ImmagineY = (SchermoY-myBitmap.getHeight())/2;

		// ImmagineX = SchermoX-ImmagineX;
		// ImmagineX = SchermoX;
        // ImmagineY = (SchermoY-ImmagineY)/2;

        // ImmagineX = 500;
		// ImmagineY = 100;

		Bitmap myOutputBitmap = myBitmap.copy(myBitmap.getConfig(), true);
		try {
			RenderScript renderScript = RenderScript.create(SharedObjects.getInstance().getContext());
			Allocation blurInput = Allocation.createFromBitmap(renderScript, myBitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
			Allocation blurOutput = Allocation.createFromBitmap(renderScript, myOutputBitmap);
			ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
					Element.U8_4(renderScript));
			blur.setInput(blurInput);
			blur.setRadius(25); // radius must be 0 < r <= 25
			blur.forEach(blurOutput);
			blurOutput.copyTo(myOutputBitmap);
			renderScript.destroy();
		} catch (Exception ignored) {
			int a=0;
		}

		int offset = 50;
		int divisore = 2;
		Bitmap Immaginona = Bitmap.createBitmap((SharedObjects.getInstance().getSchermoX()) + offset * 2,
				SharedObjects.getInstance().getSchermoY() + offset * 2, Bitmap.Config.ARGB_8888);

		Bitmap croppedSuperiore = Bitmap.createBitmap(myOutputBitmap, 0,0, SchermoX, (ImmagineY/divisore));
		Bitmap resizedBitmap = Bitmap.createScaledBitmap(croppedSuperiore, SchermoX, ImmagineY, false);
		Canvas canvas1 = new Canvas(Immaginona);
		canvas1.drawBitmap(resizedBitmap, 0, 0, null);

		canvas1.drawBitmap(myBitmap, 0, ImmagineY+1, null);

		Bitmap croppedInferiore = Bitmap.createBitmap(myOutputBitmap, 0,myBitmap.getHeight()-(ImmagineY/divisore), SchermoX, ImmagineY/divisore);
		resizedBitmap = Bitmap.createScaledBitmap(croppedInferiore, SchermoX, ImmagineY, false);
		canvas1.drawBitmap(resizedBitmap, 0, myBitmap.getHeight()+ImmagineY+1, null);

		return Immaginona;
	}

	private void SalvaBitmap(Bitmap b, String NomeFile) {
		String PercorsoDIR = Environment.getExternalStorageDirectory().getPath() + "/LooigiSoft/CambiaLaCarta";

		File dir = new File(PercorsoDIR + "/");
		if (!dir.exists())
			dir.mkdirs();
		File file = new File(dir, NomeFile);
		if (file.exists()) {
			file.delete();
		}
		try {
			FileOutputStream fOut = new FileOutputStream(file);

			b.compress(Bitmap.CompressFormat.PNG, 85, fOut);
			fOut.flush();
			fOut.close();
		} catch (IOException ignored) {

		}
	}

	public Bitmap getPreview(String uri) {
		try {
		    File image = new File(uri);
	
		    BitmapFactory.Options bounds = new BitmapFactory.Options();
		    bounds.inJustDecodeBounds = true;
		    BitmapFactory.decodeFile(image.getPath(), bounds);
		    if ((bounds.outWidth == -1) || (bounds.outHeight == -1))
		        return null;
	
		    int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight
		            : bounds.outWidth;
	
		    BitmapFactory.Options opts = new BitmapFactory.Options();
		    opts.inSampleSize = originalSize/(((SharedObjects.getInstance().getSchermoY()-BordoY)+
					(SharedObjects.getInstance().getSchermoX()-BordoX))/2);

		    return BitmapFactory.decodeFile(image.getPath(), opts);     
		} catch (Exception ignored) {
			return null;
		}
	}	
	
	public Bitmap ConverteDimensioni(Bitmap b) {
		if (b!=null) {
			try {
				Bitmap bb=b;
			    int width = bb.getWidth();
			    int height = bb.getHeight();

				float p1=(float)width/((float)(SharedObjects.getInstance().getSchermoX()));
				float p2=(float)height/((float)(SharedObjects.getInstance().getSchermoY()));
				float p;
				if (p1>p2) {
					p=p1;

					// p1=width/p;
					p2=height/p;
					bb = Bitmap.createScaledBitmap(bb, (int) SharedObjects.getInstance().getSchermoX(), (int) p2, true);
				} else {
					p=p2;

					p1=width/p;
					// p2=height/p;
					bb = Bitmap.createScaledBitmap(bb, (int) p1, (int) SharedObjects.getInstance().getSchermoY(), true);
				}

				return bb;
			} catch (Exception ignored) {
				return null;
			}
		} else {
			return null;
		}
	}
	
	public Bitmap ConverteDimensioniInterne(Bitmap b) {
		if (b!=null) {
			try {
				int DimeImmX=SharedObjects.getInstance().getSchermoX();
				int DimeImmY=SharedObjects.getInstance().getSchermoY()/2;
				
				Bitmap bb=b;
			    int width = bb.getWidth();
			    int height = bb.getHeight();

			    float p1=(float)width/((float)DimeImmX);
				float p2=(float)height/((float)DimeImmY);
				float p;
				if (p1>p2) {
					p=p1;
				} else {
					p=p2;
				}
				p1=width/p;
				p2=height/p;
				
				bb = Bitmap.createScaledBitmap(bb, (int) p1, (int) p2, true);
				
				return bb;
			} catch (Exception ignored) {
				return null;
			}
		} else {
			return null;
		}
	}
	
	public Bitmap PrendeImmagineCompressa(String uri) {
	    BitmapFactory.Options o = new BitmapFactory.Options(); 
        o.inJustDecodeBounds = true; 
        BitmapFactory.decodeFile(uri, o); 

        int REQUIRED_SIZE = SharedObjects.getInstance().getSchermoX();
        int width_tmp = o.outWidth, height_tmp = o.outHeight; 
        int scale = 1; 
        while(true) { 
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) break; 
            width_tmp /= 2; 
            height_tmp /= 2; 
            scale *= 2; 
        } 

        BitmapFactory.Options o2 = new BitmapFactory.Options(); 
        o2.inSampleSize = scale; 
        Bitmap bitmap = BitmapFactory.decodeFile(uri, o2); 

        ByteArrayOutputStream bs2 = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, bs2);
        
		return bitmap;
	}
	
    public Bitmap decodeFile(String NomeImm){
    	GestioneFilesCartelle gfc=new GestioneFilesCartelle();
    	if (gfc.EsisteFile(gfc.PrendeNomeFile(NomeImm), gfc.PrendeNomeCartella(NomeImm))) {
	    	File src = new java.io.File(NomeImm);
	        try {
	            BitmapFactory.Options o = new BitmapFactory.Options();
	            o.inJustDecodeBounds = true;
	            BitmapFactory.decodeStream(new FileInputStream(src),null,o);
	
	            final int REQUIRED_SIZE=100;
	            int width_tmp=o.outWidth, height_tmp=o.outHeight;
	            int scale=1;
	            while(true){
	                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
	                    break;
	                width_tmp/=2;
	                height_tmp/=2;
	                scale*=2;
	            }
	
	            BitmapFactory.Options o2 = new BitmapFactory.Options();
	            o2.inSampleSize=scale;
	            return BitmapFactory.decodeStream(new FileInputStream(src), null, o2);
	        } catch (FileNotFoundException ignored) {
	        	
	        }
    	}
        return null;
    } 

}

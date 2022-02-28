package com.looigi.cambiolacarta;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.looigi.cambiolacarta.Soap.DBRemoto;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.NOTIFICATION_SERVICE;

@SuppressWarnings("WeakerAccess")
public class Utility {
	private int BordoX=5;
	private int BordoY=5;
	// private Timer timer;
	private static int MinutiPassati;
	Timer t = null;
	TimerTask tt = null;
	private static long ultimoPassaggio = 0;
	private long ultimoPassaggioBordo = 0;
	private long ultimoPassaggioReale = 0;

	public String ControllaLingua(Context context, int CosaIT, int CosaEN) {
		String Ritorno="";

		if (context==null) {
			context = VariabiliGlobali.getInstance().getContext();
		}

		if (SharedObjects.getInstance().getLingua()==null) {
			SharedObjects.getInstance().setLingua("ITALIANO");
		}

		if (context!=null) {
			if (SharedObjects.getInstance().getLingua().equals("INGLESE")) {
				Ritorno = context.getString(CosaEN);
			} else {
				Ritorno = context.getString(CosaIT);
			}
		}
		
		return Ritorno;
	}
	
	public Bitmap PrendeImmagineReale(String FileName, Log l) {
		long adesso = System.currentTimeMillis();
		if (adesso - ultimoPassaggioReale > 10) {
			ultimoPassaggioReale = adesso;

			l.ScriveLog(new Object() {
					}.getClass().getEnclosingMethod().getName(),
					"Prende immagine reale");

			Bitmap myBitmap = null;

			GestioneFilesCartelle gfc = new GestioneFilesCartelle();
			if (gfc.EsisteFile(gfc.PrendeNomeFile(FileName), gfc.PrendeNomeCartella(FileName))) {
				l.ScriveLog(new Object() {
						}.getClass().getEnclosingMethod().getName(),
						"Cambio immagine. File esistente: " + gfc.PrendeNomeCartella(FileName) + "/" + gfc.PrendeNomeFile(FileName));

				DisplayMetrics metrics = new DisplayMetrics();
				if (SharedObjects.getInstance().getA1() == null) {
					SharedObjects.getInstance().setA1(MainActivity.activity.getWindowManager());
				}
				SharedObjects.getInstance().getA1().getDefaultDisplay().getMetrics(metrics);

				SharedObjects.getInstance().setSchermoX(metrics.widthPixels);
				SharedObjects.getInstance().setSchermoY(metrics.heightPixels);

				l.ScriveLog(new Object() {
						}.getClass().getEnclosingMethod().getName(),
						"Cambio immagine. Dimensioni schermo: " + SharedObjects.getInstance().getSchermoX() + "x" + SharedObjects.getInstance().getSchermoY());

				try {
					myBitmap = getPreview(FileName);
				} catch (Exception e) {
					l.ScriveLog(new Object() {
							}.getClass().getEnclosingMethod().getName(),
							"Cambio immagine. Errore preview");
				}

				l.ScriveLog(new Object() {
						}.getClass().getEnclosingMethod().getName(),
						"Cambio immagine. Impostata preview");

				if (myBitmap != null) {
					if (SharedObjects.getInstance().getStretch().equals("S")) {
						l.ScriveLog(new Object() {
								}.getClass().getEnclosingMethod().getName(),
								"Cambio immagine. Stretch = S");

						if (SharedObjects.getInstance().getModalitaVisua().equals("N")) {
							l.ScriveLog(new Object() {
									}.getClass().getEnclosingMethod().getName(),
									"Cambio immagine. Converte dimensioni");

							myBitmap = ConverteDimensioni(myBitmap, l);

							if (myBitmap != null) {
								VariabiliGlobali.getInstance().setBitmapOriginale(myBitmap);
								try {
									// Bitmap Immaginona = Bitmap.createBitmap(SharedObjects.getInstance().getSchermoX(), SharedObjects.getInstance().getSchermoY(), Bitmap.Config.ARGB_8888);
									// Canvas comboImage = new Canvas(Immaginona);
									// float Altezza=(((float) (SharedObjects.getInstance().getSchermoY()))/2)-(myBitmap.getHeight()/2);
									// float Larghezza=(((float) (SharedObjects.getInstance().getSchermoX()))/2)-(myBitmap.getWidth()/2);
									// comboImage.drawBitmap(myBitmap, Larghezza, Altezza, null);
									l.ScriveLog(new Object() {
											}.getClass().getEnclosingMethod().getName(),
											"Cambio immagine. Mette bordo a immagine");

									myBitmap = MetteBordoAImmagine(myBitmap, l);
								} catch (Exception e) {
									Utility u = new Utility();
									l.ScriveLog(new Object() {
											}.getClass().getEnclosingMethod().getName(),
											"Cambio immagine. Mette bordo a immagine. Errore:\n" + u.PrendeErroreDaException(e));
									myBitmap = null;
								}
							} else {
								l.ScriveLog(new Object() {
										}.getClass().getEnclosingMethod().getName(),
										"Cambio immagine. Converte dimensioni ha ritornato null");

								myBitmap = null;
							}
						} else {
							l.ScriveLog(new Object() {
									}.getClass().getEnclosingMethod().getName(),
									"Cambio immagine. Create scaled bitmap");

							myBitmap = Bitmap.createScaledBitmap(myBitmap, (int) SharedObjects.getInstance().getSchermoX() - BordoX,
									(int) SharedObjects.getInstance().getSchermoY() - BordoY, true);
						}
					}
					// SalvaImmagine(myBitmap);
				}
			} else {
				l.ScriveLog(new Object() {
						}.getClass().getEnclosingMethod().getName(),
						"Cambio immagine. Svuoto bitmap. File inesistente.");

				myBitmap = null;
			}

			return myBitmap;
		} else {
			return null;
		}
	}

	private Bitmap MetteBordoAImmagine(Bitmap myBitmap, Log l) {
		long adesso = System.currentTimeMillis();
		if (adesso - ultimoPassaggioBordo > 1000) {
			ultimoPassaggioBordo = adesso;

			l.ScriveLog(new Object() {
					}.getClass().getEnclosingMethod().getName(),
					"Mette bordo a immagine");

			int SchermoX = SharedObjects.getInstance().getSchermoX();
			int SchermoY = SharedObjects.getInstance().getSchermoY();

			int dimeImmX = myBitmap.getWidth();
			int dimeImmY = myBitmap.getHeight();

			int posX = (SchermoX / 2) - (dimeImmX / 2);
			int posY = (SchermoY / 2) - (dimeImmY / 2);

			l.ScriveLog(new Object() {
					}.getClass().getEnclosingMethod().getName(),
					"Mette bordo 1");

			// ImmagineX = SchermoX-ImmagineX;
			// ImmagineX = SchermoX;
			// ImmagineY = (SchermoY-ImmagineY)/2;

			// ImmagineX = 500;
			// ImmagineY = 100;

			Bitmap myOutputBitmap = myBitmap.copy(myBitmap.getConfig(), true);
			try {
				l.ScriveLog(new Object() {
						}.getClass().getEnclosingMethod().getName(),
						"Mette bordo 2");

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
			} catch (Exception e) {
				Utility u = new Utility();

				l.ScriveLog(new Object() {
						}.getClass().getEnclosingMethod().getName(),
						"Mette bordo: " + u.PrendeErroreDaException(e));
				int a = 0;
			}

			int offset = 50;
			int divisore = 2;

			l.ScriveLog(new Object() {
					}.getClass().getEnclosingMethod().getName(),
					"Mette bordo 2");

			// Bitmap Immaginona = Bitmap.createBitmap((SharedObjects.getInstance().getSchermoX()) + offset * 2,
// 					SharedObjects.getInstance().getSchermoY() + offset * 2, Bitmap.Config.ARGB_8888);
			Bitmap Immaginona = Bitmap.createBitmap(SchermoX, SchermoY, Bitmap.Config.ARGB_8888);

			l.ScriveLog(new Object() {
					}.getClass().getEnclosingMethod().getName(),
					"Mette bordo 3");

			Canvas canvas1 = new Canvas(Immaginona);
			if (posY > 0) {
				Bitmap croppedSuperiore = Bitmap.createBitmap(myOutputBitmap, 0, 0, SchermoX, (posY / divisore));
				Bitmap resizedBitmap = Bitmap.createScaledBitmap(croppedSuperiore, SchermoX, posY, false);
				canvas1.drawBitmap(resizedBitmap, 0, 0, null);
			}

			canvas1.drawBitmap(myBitmap, posX, posY, null);

			l.ScriveLog(new Object() {
					}.getClass().getEnclosingMethod().getName(),
					"Mette bordo 4");

			if (posY > 0) {
				Bitmap croppedInferiore = Bitmap.createBitmap(myOutputBitmap, 0, myBitmap.getHeight() - (posY / divisore), SchermoX, posY / divisore);
				Bitmap resizedBitmap = Bitmap.createScaledBitmap(croppedInferiore, SchermoX, posY, false);
				canvas1.drawBitmap(resizedBitmap, 0, myBitmap.getHeight() + posY + 1, null);
			}

			l.ScriveLog(new Object() {
					}.getClass().getEnclosingMethod().getName(),
					"Mette bordo uscita");

			return Immaginona;
		} else {
			return null;
		}
	}

	/* private void SalvaBitmap(Bitmap b, String NomeFile) {
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
	} */

	public Bitmap getPreview(String uri) {
		Log l = new Log();

		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"Get preview");

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

			l.ScriveLog(new Object() {
					}.getClass().getEnclosingMethod().getName(),
					"Get preview fatto");

		    return BitmapFactory.decodeFile(image.getPath(), opts);     
		} catch (Exception ignored) {
			l.ScriveLog(new Object() {
					}.getClass().getEnclosingMethod().getName(),
					"Prende Preview errore ");
			return null;
		}
	}

	public String PrendeErroreDaException(Exception e) {
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));

		return TransformError(errors.toString());
	}

	private String TransformError(String error) {
		String Return=error;

		if (Return.length()>250) {
			Return=Return.substring(0,247)+"...";
		}
		Return=Return.replace("\n"," ");

		return Return;
	}

	public Bitmap ConverteDimensioni(Bitmap b, Log l) {
		if (b!=null) {
			try {
				l.ScriveLog(new Object() {
						}.getClass().getEnclosingMethod().getName(),
						"Converte dimensioni 1");


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

				l.ScriveLog(new Object() {
						}.getClass().getEnclosingMethod().getName(),
						"Converte dimensioni 2");

				return bb;
			} catch (Exception ignored) {
				Utility u = new Utility();

				l.ScriveLog(new Object() {
						}.getClass().getEnclosingMethod().getName(),
						"Converte dimensioni errore:\n" + u.PrendeErroreDaException(ignored));

				return null;
			}
		} else {
			l.ScriveLog(new Object() {
					}.getClass().getEnclosingMethod().getName(),
					"Converte dimensioni. Ritorno nullo");

			return null;
		}
	}
	
	public Bitmap ConverteDimensioniInterne(Bitmap b, Log l) {
		if (b!=null) {
			try {
				int DimeImmX=SharedObjects.getInstance().getSchermoX();
				int DimeImmY=SharedObjects.getInstance().getSchermoY()/2;

				l.ScriveLog(new Object() {
						}.getClass().getEnclosingMethod().getName(),
						"Converte dimensioni Interne 1");

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


				l.ScriveLog(new Object() {
						}.getClass().getEnclosingMethod().getName(),
						"Converte dimensioni Interne 2");

				return bb;
			} catch (Exception ignored) {
				Utility u = new Utility();

				l.ScriveLog(new Object() {
						}.getClass().getEnclosingMethod().getName(),
						"Converte dimensioni Interne errore:\n" + u.PrendeErroreDaException(ignored));

				return null;
			}
		} else {
			l.ScriveLog(new Object() {
					}.getClass().getEnclosingMethod().getName(),
					"Converte dimensioni Interne. Ritorno nullo");

			return null;
		}
	}
	
	/* public Bitmap PrendeImmagineCompressa(String uri, Log l) {
		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"Prende immagine compressa 1");

	    BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true; 
        BitmapFactory.decodeFile(uri, o);

		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"Prende immagine compressa 2");

        int REQUIRED_SIZE = SharedObjects.getInstance().getSchermoX();
        int width_tmp = o.outWidth, height_tmp = o.outHeight; 
        int scale = 1;

		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"Prende immagine compressa 3");

        while(true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) break; 
            width_tmp /= 2; 
            height_tmp /= 2; 
            scale *= 2; 
        }

		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"Prende immagine compressa 4");

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale; 
        Bitmap bitmap = BitmapFactory.decodeFile(uri, o2);

		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"Prende immagine compressa 5");

        ByteArrayOutputStream bs2 = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, bs2);

		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"Prende immagine compressa uscita");

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
    } */

	public static int PrendeNuovoNumero(String Come, Log l) {
		int Ritorno=0;

		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"Prende nuovo numero");

		if (SharedObjects.getInstance().getTipoCambio().equals("RANDOM")) {
			l.ScriveLog(new Object() {
					}.getClass().getEnclosingMethod().getName(),
					"Prende nuovo numero random");

			Random r;
			r=new Random();
			if (SharedObjects.getInstance().getListaImmagini().size() > 0) {
				int i1 = r.nextInt(SharedObjects.getInstance().getListaImmagini().size());
				i1--;
				if (i1 < 0) {
					i1 = 0;
				}
				Ritorno = i1;
				SharedObjects.getInstance().setQualeImmagineHaVisualizzato(i1);
			} else {
				Ritorno = -1;
			}
		} else {
			if (SharedObjects.getInstance().getTipoCambio().equals("SEQUENZIALE")) {
				l.ScriveLog(new Object() {
						}.getClass().getEnclosingMethod().getName(),
						"Prende nuovo numero sequenziale");

				if (Come.equals("Indietro")) {
					l.ScriveLog(new Object() {
							}.getClass().getEnclosingMethod().getName(),
							"Prende nuovo numero sequenziale all'indietro");

					SharedObjects.getInstance().setQualeImmagineHaVisualizzato(SharedObjects.getInstance().getQualeImmagineHaVisualizzato() - 1);
					if (SharedObjects.getInstance().getQualeImmagineHaVisualizzato() < 0) {
						SharedObjects.getInstance().setQualeImmagineHaVisualizzato(SharedObjects.getInstance().getListaImmagini().size());
					}
					Ritorno = SharedObjects.getInstance().getQualeImmagineHaVisualizzato();
				} else {
					l.ScriveLog(new Object() {
							}.getClass().getEnclosingMethod().getName(),
							"Prende nuovo numero sequenziale in avanti");

					SharedObjects.getInstance().setQualeImmagineHaVisualizzato(SharedObjects.getInstance().getQualeImmagineHaVisualizzato() + 1);
					if (SharedObjects.getInstance().getQualeImmagineHaVisualizzato() >= SharedObjects.getInstance().getListaImmagini().size()) {
						SharedObjects.getInstance().setQualeImmagineHaVisualizzato(0);
					}
					Ritorno = SharedObjects.getInstance().getQualeImmagineHaVisualizzato();
				}
			} else {
				l.ScriveLog(new Object() {
						}.getClass().getEnclosingMethod().getName(),
						"Prende nuovo numero synchro");

				DBRemoto dbr = new DBRemoto();
				dbr.TornaNumeroImmaginePerSfondo();

				Ritorno = -1;
			}
		}

		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"Prende nuovo numero. Ritorno: " + Ritorno);

		return Ritorno;
	}

	public void ScriveInfo(Log l) {
		try {
			Context context = SharedObjects.getInstance().getContext();
			Utility u = new Utility();
			String s = u.ControllaLingua(context, R.string.percorsoIT, R.string.percorsoEN) + ": " + SharedObjects.getInstance().getOrigine();
			SharedObjects.getInstance().getTxtPercorso().setText(s);
			s = u.ControllaLingua(context, R.string.numimmIT, R.string.numimmEN) + ": " + SharedObjects.getInstance().getQuanteImm();
			SharedObjects.getInstance().getTxtNumImm().setText(s);
			SharedObjects.getInstance().getTxtSceltaCartella().setText(u.ControllaLingua(context, R.string.cambiofolderIT, R.string.cambiofolderEN));
			SharedObjects.getInstance().getTxtCambiaSubito().setText(u.ControllaLingua(context, R.string.cambiosubitoIT, R.string.cambiosubitoEN));
			SharedObjects.getInstance().getTxtListaImm().setText(u.ControllaLingua(context, R.string.listaIT, R.string.listaEN));
			SharedObjects.getInstance().getTxtOpzioni().setText(u.ControllaLingua(context, R.string.opzioniIT, R.string.opzioniEN));
			SharedObjects.getInstance().getTxtProssima().setText(u.ControllaLingua(context, R.string.avantiIT, R.string.avantiEN));
			SharedObjects.getInstance().getTxtPrecedente().setText(u.ControllaLingua(context, R.string.indietroIT, R.string.indietroEN));
			s = u.ControllaLingua(context, R.string.tempoIT, R.string.tempoEN) + ": " + SharedObjects.getInstance().getMinutiPerCambio();
			SharedObjects.getInstance().getTxtMinuti().setText(s);
			String sTipoCambio;
			if (SharedObjects.getInstance().getTipoCambio().equals("RANDOM")) {
				sTipoCambio = "Random";
			} else {
				sTipoCambio = u.ControllaLingua(context, R.string.optseqIT, R.string.optseqEN);
			}
			s = u.ControllaLingua(context, R.string.modalitaIT, R.string.modalitaEN) + ": " + sTipoCambio;
			SharedObjects.getInstance().getTxtTipoCambio().setText(s);
			s = u.ControllaLingua(context, R.string.immvisuaIT, R.string.immvisuaEN) + ": " + SharedObjects.getInstance().getQualeImmagineHaVisualizzato();
			SharedObjects.getInstance().getTxtImmVisua().setText(s);
			try {
				if (!SharedObjects.getInstance().getTipoCambio().equals("SINCRONIZZATA")) {
					SharedObjects.getInstance().getTxtNomeImm().setText(SharedObjects.getInstance().getListaImmagini()
							.get(SharedObjects.getInstance().getQualeImmagineHaVisualizzato()));
					u.ImpostaImmagineDiSfondo(SharedObjects.getInstance().getListaImmagini()
							.get(SharedObjects.getInstance().getQualeImmagineHaVisualizzato()), l);
				} else {
					SharedObjects.getInstance().getTxtNomeImm().setText(SharedObjects.getInstance().getPathUltimaImaggineDL());
					u.ImpostaImmagineDiSfondo(SharedObjects.getInstance().getPercorsoDIR() + "/Download.jpg", l);
				}
			} catch (Exception ignored) {
				SharedObjects.getInstance().getTxtNomeImm().setText("");
			}
			if (SharedObjects.getInstance().getAttivo().equals("S")) {
				SharedObjects.getInstance().getChkAttivo().setText(u.ControllaLingua(context, R.string.attivoIT, R.string.attivoEN));
				SharedObjects.getInstance().getChkAttivo().setChecked(true);
				if (SharedObjects.getInstance().getStaPartendo()) {
					if (SharedObjects.getInstance().getTimerPartito() == null) {
						SharedObjects.getInstance().setTimerPartito(false);
					}
					/* if (!SharedObjects.getInstance().getTimerPartito()) {
						FaiPartireTimer();
					} */
				}
			} else {
				SharedObjects.getInstance().getChkAttivo().setText(u.ControllaLingua(context, R.string.inattivoIT, R.string.inattivoEN));
				SharedObjects.getInstance().getChkAttivo().setChecked(false);
				/* if (SharedObjects.getInstance().getStaPartendo()) {
					FermaTimer();
				} */
			}
			// txtTempo2.setText(u.ControllaLingua(context, R.string.tempo2IT, R.string.tempo2EN));
			SharedObjects.getInstance().getTxtCaffe().setText(u.ControllaLingua(context, R.string.caffeIT, R.string.caffeEN));
			SharedObjects.getInstance().getTxtCaffe().setSelected(true);

			// ImpostaDimensioni();

			MainActivity.activity.setTitle(u.ControllaLingua(context, R.string.app_name, R.string.app_nameEN));
		} catch (Exception ignored) {

		}
	}

	/* public void FaiPartireTimer() {
		if (SharedObjects.getInstance().getListaImmagini().size()>0) {
			SharedObjects.getInstance().setTimerPartito(true);

			final int TotMinuti=SharedObjects.getInstance().getMinutiPerCambio();

			timer = new Timer();
			timer.scheduleAtFixedRate( new java.util.TimerTask() {
				@Override
				public void run() {
					MinutiPassati++;
					if (MinutiPassati>=TotMinuti) {
						MinutiPassati=0;

						Looper.prepare();

						Utility u = new Utility();
						Boolean Ritorno=u.CambiaImmagine(true, 0);
						// if (!Ritorno) {
							// Toast.makeText(VariabiliGlobali.getInstance().getContext(),
							// 		u.ControllaLingua(VariabiliGlobali.getInstance().getContext(),
							// 				R.string.errimmimpIT, R.string.errimmimpEN), Toast.LENGTH_SHORT).show();
						// }

						timer.cancel();
						timer=null;

						FaiRipartireTimer();

						Looper.loop();
					}
				}}, 0, 60000 );
		}
	} */

	// private Handler handlerAgg;
	// private Runnable rAgg;
	public void FaiPartireTimer() {
		final Log l = new Log();
		/* if (MainActivity.ct != null) {
			l.ScriveLog(new Object() {
					}.getClass().getEnclosingMethod().getName(),
					"Timer già partito. Esco");
			return;
		} */

		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"Fai partire Timer");
		// if (VariabiliGlobali.getInstance().getHandler() == null) {
			final int TotMinuti = SharedObjects.getInstance().getMinutiPerCambio();
			int Tempo = 60000;
			// final Utility u = new Utility();

		try {
			MainActivity.ct.cancel();
		} catch (Exception ignored) {

		}

		MainActivity.ct = new CountDownTimer( (Tempo * TotMinuti), Tempo) {
				public void onTick(long millisUntilFinished) {
					MinutiPassati++;
					SharedObjects.getInstance().getTxtTempoPassato().setText(" Min: " + Integer.toString(MinutiPassati) + "/" + Integer.toString(TotMinuti));

					l.ScriveLog(new Object() {
							}.getClass().getEnclosingMethod().getName(),
							"Minuti passati:" + Integer.toString(MinutiPassati) + "/" + Integer.toString(TotMinuti));
				}

				public void onFinish() {
					l.ScriveLog(new Object() {
							}.getClass().getEnclosingMethod().getName(),
							"Cambio immagine in timer");

					// MinutiPassati = 0;
					Boolean Ritorno = CambiaImmagine(true, 0);
				}
			}.start();

			/* handlerAgg = new Handler();
			    rAgg = new Runnable() {
			        public void run() {
			        	try {
							MinutiPassati++;

							l.ScriveLog(new Object() {
									}.getClass().getEnclosingMethod().getName(),
									"Minuti passati:" + Integer.toString(MinutiPassati) + "/" + Integer.toString(TotMinuti));

							if (MinutiPassati >= TotMinuti) {
								// if (!SharedObjects.getInstance().getStaPartendo()) {
								MinutiPassati = 0;

								Boolean Ritorno = CambiaImmagine(true, 0);
								// }
							}

							if (handlerAgg != null && rAgg != null) {
								handlerAgg.postDelayed(rAgg, Tempo);
							} else {
								l.ScriveLog(new Object() {
										}.getClass().getEnclosingMethod().getName(),
										"Handler sbragato. Faccio ripartire il timer");
								// VariabiliGlobali.getInstance().setHandler(null);
								FaiPartireTimer();
							}
						} catch (Exception e) {
							l.ScriveLog(new Object() {
									}.getClass().getEnclosingMethod().getName(),
									"Minuti passati. Errore:" +  u.PrendeErroreDaException(e));
						}
			        }
			    };
			handlerAgg.postDelayed(rAgg, Tempo); */

			// final Handler handler = new Handler();
			/* t = new Timer();
			tt = new TimerTask() {
				@Override
				public void run() {
					// Looper.prepare();

					MinutiPassati++;

					Log l = new Log();
					l.ScriveLog(new Object() {
							}.getClass().getEnclosingMethod().getName(),
							"Minuti passati:" + Integer.toString(MinutiPassati) + "/" + Integer.toString(TotMinuti));

					if (MinutiPassati >= TotMinuti) {
						// if (!SharedObjects.getInstance().getStaPartendo()) {
						MinutiPassati = 0;

						Boolean Ritorno = CambiaImmagine(true, 0);
						// }
					}

					// Looper.loop();
				}
			};

			t.scheduleAtFixedRate(tt ,0,60000); */


			/* VariabiliGlobali.getInstance().setHandler(new Handler());
			VariabiliGlobali.getInstance().setR(new Runnable() {
				public void run() {
					// FaiPartireTimer();
					VariabiliGlobali.getInstance().getHandler().postDelayed(VariabiliGlobali.getInstance().getR(), 60000);
				}
			});
			VariabiliGlobali.getInstance().getHandler().postDelayed(VariabiliGlobali.getInstance().getR(), 60000); */
		// }
	}

	public void FermaTimer() {
		/* SharedObjects.getInstance().setTimerPartito(false);

		if (SharedObjects.getInstance().getListaImmagini()!=null) {
			if (SharedObjects.getInstance().getListaImmagini().size()>0) {
				if (VariabiliGlobali.getInstance().getHandler()!=null) {
					/* try {
						VariabiliGlobali.getInstance().getHandler().removeCallbacks(VariabiliGlobali.getInstance().getR());
					} catch (Exception ignored) {

					}
					VariabiliGlobali.getInstance().setHandler(null);
					VariabiliGlobali.getInstance().setR(null); // /
					t.cancel();
					t.purge();
					t = null;
				}

			}
		} */
	}

	public void ImpostaImmagineDiSfondo(String NomeImm, Log l) {
		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"Imposta immagine di sfondo");

		if (SharedObjects.getInstance().getImm()==null) {
			// Notifiche.getInstance().CreaNotifica();
		}

		if (SharedObjects.getInstance().getImm()!=null) {
			if (NomeImm != null) {
				if (!NomeImm.trim().equals("")) {
					GestioneFilesCartelle gfc = new GestioneFilesCartelle();
					if (gfc.EsisteFile(gfc.PrendeNomeFile(NomeImm), gfc.PrendeNomeCartella(NomeImm))) {
						Utility u = new Utility();

						Bitmap myBitmap = u.getPreview(NomeImm);
						myBitmap = u.ConverteDimensioniInterne(myBitmap, l);
						SharedObjects.getInstance().getImm().setImageBitmap(myBitmap);
					} else {
						SharedObjects.getInstance().getImm().setBackgroundResource(R.drawable.ic_launcher);
					}
				} else {
					SharedObjects.getInstance().getImm().setBackgroundResource(R.drawable.ic_launcher);
				}
			}
		}

		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"Imposta immagine di sfondo. Fine");
	}

	public static Boolean CambiaImmagine(Boolean Cambia, int NumeroDarete) {
		final Log l = new Log();
		l.ScriveLog(new Object() {
				}.getClass().getEnclosingMethod().getName(),
				"Cambia immagine");

		long adesso = System.currentTimeMillis();
		long q = adesso - ultimoPassaggio;
		if (q > 1000) {
			ultimoPassaggio = adesso;
			/* if (VariabiliGlobali.getInstance().getContext()==null ||
					VariabiliGlobali.getInstance().getActivityPrincipale()==null ||
					SharedObjects.getInstance().getListaImmagini() == null) {
				RefreshActivity.getInstance().RilanciaActivity();
			} */

			Boolean Ritorno = false;

			l.ScriveLog(new Object() {
					}.getClass().getEnclosingMethod().getName(),
					"Cambia immagine. Schermo attivo: " + VariabiliGlobali.getInstance().getScreenON());

			if (VariabiliGlobali.getInstance().getScreenON()) {
				l.ScriveLog(new Object() {
						}.getClass().getEnclosingMethod().getName(),
						"Schermo attivo, cambio immagine");

				// Se per qualche motivo si è perso la lista delle immagini le ricarico
				/* if (SharedObjects.getInstance().getListaImmagini() == null || SharedObjects.getInstance().getListaImmagini().size() == 0) {
					l.ScriveLog(new Object() {
							}.getClass().getEnclosingMethod().getName(),
							"Lista immagini vuota. Ricarico");

					if (VariabiliGlobali.getInstance().getContext() == null) {
						l.ScriveLog(new Object() {
								}.getClass().getEnclosingMethod().getName(),
								"Contex vuoto, Ricarico dal main");

						// MainActivity m = new MainActivity();
						// VariabiliGlobali.getInstance().setContext(m.getContext());
					}

					DBLocale d = new DBLocale();
					d.LeggeOpzioni(VariabiliGlobali.getInstance().getContext());
					GestioneFilesCartelle gf = new GestioneFilesCartelle();
					gf.LeggeImmagini(VariabiliGlobali.getInstance().getContext(), SharedObjects.getInstance().getOrigine());
					// service.ChiudiMaschera = true;

					l.ScriveLog(new Object() {
							}.getClass().getEnclosingMethod().getName(),
							"Immagini lette: " + Integer.toString(SharedObjects.getInstance().getListaImmagini().size()));
				} */
				// Se per qualche motivo si è perso la lista delle immagini le ricarico

				// if (SharedObjects.getInstance().getListaImmagini().size() > 0) {
					l.ScriveLog(new Object() {
							}.getClass().getEnclosingMethod().getName(),
							"Immagini piene, effettuo cambio");

					int NuovoNumero;

					if (Cambia) {
						l.ScriveLog(new Object() {
								}.getClass().getEnclosingMethod().getName(),
								"Cambio immagine: " + Integer.toString(NumeroDarete));

						if (NumeroDarete == 0) {
							NuovoNumero = PrendeNuovoNumero("Avanti", l);

							if (NuovoNumero > -1) {
								l.ScriveLog(new Object() {
										}.getClass().getEnclosingMethod().getName(),
										"Cambio immagine: Modalità Synchro Lettura.");


								DBLocale dbl = new DBLocale();
								dbl.ScriveOpzioni(VariabiliGlobali.getInstance().getContext());
							}

							Ritorno = true;
						} else {
							l.ScriveLog(new Object() {
									}.getClass().getEnclosingMethod().getName(),
									"Cambio immagine: Modalità Synchro Ritorno: " + Integer.toString(NumeroDarete));

							NuovoNumero = NumeroDarete;

							DBLocale dbl = new DBLocale();
							dbl.ScriveOpzioni(VariabiliGlobali.getInstance().getContext());

							Ritorno = true;
						}
					} else {
						l.ScriveLog(new Object() {
								}.getClass().getEnclosingMethod().getName(),
								"Cambio immagine: Else 1.");

						NuovoNumero = SharedObjects.getInstance().getQualeImmagineHaVisualizzato();

						Ritorno = true;
					}

					if (NuovoNumero >= 0 && SharedObjects.getInstance().getListaImmagini().size() > 0) {
						String NomeFile = SharedObjects.getInstance().getListaImmagini().get(NuovoNumero);

						l.ScriveLog(new Object() {
								}.getClass().getEnclosingMethod().getName(),
								"Cambio immagine. Nome immagine: " + NomeFile);

						ChangeWallpaper cw = new ChangeWallpaper();
						Ritorno = cw.setWallpaper(NomeFile, l);

						l.ScriveLog(new Object() {
								}.getClass().getEnclosingMethod().getName(),
								"Cambio immagine. Ritorno: " + Ritorno);

						if (SharedObjects.getInstance().getNotificaSiNo().equals("S")) {
							// Notifiche.getInstance().AggiornaNotifica();
						}

						Utility u = new Utility();
						u.faiRipartireTimer();
					}
				// } else {
				// 	Ritorno = false;
				// }
			} else {
				l.ScriveLog(new Object() {
						}.getClass().getEnclosingMethod().getName(),
						"Cambio immagine: Setto flag cambiata immagine = true.");
				//
				VariabiliGlobali.getInstance().setCambiataImmagine(true);
				//
				Ritorno = true;
			}

			return Ritorno;
		} else {
			return false;
		}
	}

	public void faiRipartireTimer() {
		new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
			@Override
			public void run() {
				try {
					MainActivity.ct.cancel();
				} catch (Exception ignored) {

				}
				MainActivity.ct = null;

				Log l = new Log();
				l.ScriveLog(new Object() {
						}.getClass().getEnclosingMethod().getName(),
						"Faccio ripartire timer");
				Utility u = new Utility();
				u.FaiPartireTimer();
			}
		}, 1/* delay for no time, just to next loop*/);

		MinutiPassati = 0;
	}
}

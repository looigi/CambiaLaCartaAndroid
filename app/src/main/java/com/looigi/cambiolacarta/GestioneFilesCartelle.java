package com.looigi.cambiolacarta;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class GestioneFilesCartelle {
	private List<String> Cartelle;
	private List<String> Letta;
	private List<String> ListImages;
  	private Handler handler;
	private Runnable r;
	private Boolean Ancora=false;
	private int Quale=0;
	private int QuanteCart=0;
	private String Cartella="";
	private ProgressDialog progressDialog;
	// private Log l=new Log();
	private Boolean EsciDalCiclo=false;
	
	public long DimensioniFile(String NomeFile) {
		long Dime=-1;
		
		try {
        	File file = new File(NomeFile);
        	Dime=file.length();
    	} catch (Exception e) {
    		Dime=-1;
    	}
		
		return Dime;		
	}

    public String ContaImmaginiCartella(Context context, String Percorso) {
		int Quanti=0;
		String NomeFile="";
		
		try {
			File root ;
			File [] files = null ;
			
			root = new File(Percorso);
			files = root.listFiles();
			// l.ScriveLog("------------------------------------");
			if (files!=null) {
		        for (File file : files){
		        	if (!file.isDirectory()) {
		        		NomeFile=file.getPath().toString().toUpperCase();
		        		// l.ScriveLog("IMG Count: "+NomeFile);
	
		        		if (NomeFile.contains(".JPG") || NomeFile.contains(".JPEG")
		        				|| NomeFile.contains(".BMP") || NomeFile.contains(".PNG")) {
		        			Quanti++;
		        		}
		        	}
		        }
			}
			// l.ScriveLog("------------------------------------");
		} catch (Exception e) {
			// l.ScriveLog("Error on CONTAIMMAGINICARTELLA: "+e.getMessage());
			EsciDalCiclo=true;
			VisualizzaPOPUP(context, "Error: "+e.getMessage()+"\nRoutine: ContaImmaginiCartella\nDo you want send Log file to developer?", true);
		}
    	
    	return Integer.toString(Quanti);
	}
	
	private void LeggeCartelle(Context context, String Percorso) {
		try {
			File root = new File(Percorso);
			File [] files = root.listFiles();

			String NomeCartella="";
			// l.ScriveLog("------------------------------------");
			if (files!=null) {
		        for (File file : files){
		        	if (file.isDirectory()) {
		        		NomeCartella=file.getPath().toString();
		        		// l.ScriveLog("Read folder - Inner loop - : "+NomeCartella);
	
	        			Cartelle.add(NomeCartella);
	        			Letta.add("NO");
		        	}
		        }
			}
			// l.ScriveLog("------------------------------------");
		} catch (Exception e) {
			// l.ScriveLog("Error on LEGGECARTELLE: "+e.getMessage());
			EsciDalCiclo=true;
			VisualizzaPOPUP(context, "Error: "+e.getMessage()+"\nRoutine: LeggeCartelle\nDo you want send Log file to developer?", true);
		}
	}
	
	private void VisualizzaPOPUP(final Context context, String Messaggio, final Boolean Tasti) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if (SharedObjects.getInstance().getLingua().equals("INGLESE")) {
			builder.setTitle("Change Wallpaper");
		} else {
			builder.setTitle("Cambio la carta");
		}
		builder.setMessage(Messaggio);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                if (Tasti) {
                }
            }
        });		
		if (Tasti) {
			if (SharedObjects.getInstance().getLingua().equals("INGLESE")) {
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		            }
		        });		
    		} else {
    			builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	            }
    	        });		
    		}
		}
		@SuppressWarnings("unused")
		AlertDialog dialog = builder.show();
	}
	
    public boolean EsisteFile(String sFileName, String Percorso){
        String sFolder = Percorso ;
        String sFile=sFolder+"/"+sFileName;
        java.io.File file = new java.io.File(sFile);
        
        return file.exists();
    }

    private void LeggeImmaginiCartella(Context context, String Percorso) {
    	try {
			File root = new File(Percorso);
			File [] files = root.listFiles();
			
			String NomeFile="";
			// l.ScriveLog("------------------------------------");
			if (files!=null) {
		        for (File file : files){
		        	if (!file.isDirectory()) {
		        		NomeFile=file.getPath().toString().toUpperCase();
		        		// l.ScriveLog("Read IMG folder: "+NomeFile);
	
		        		if (NomeFile.contains(".JPG") || NomeFile.contains(".JPEG")
		        				|| NomeFile.contains(".PNG") || NomeFile.contains(".BMP")) {
		        			ListImages.add(file.getPath().toString());
		        		}
		        	}
		        }
			}
			// l.ScriveLog("------------------------------------");
		} catch (Exception e) {
			// l.ScriveLog("Error on LEGGEIMMAGINICARTELLA: "+e.getMessage());
			EsciDalCiclo=true;
			VisualizzaPOPUP(context, "Error: "+e.getMessage()+"\nRoutine: LeggeImmaginiCartella\nDo you want send Log file to developer?", true);
		}
	}

	private void ApreProgress(Context context, String Cosa) {
		try {
			progressDialog = new ProgressDialog(context);
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setMessage(Cosa);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.show();
		} catch (Exception ignored) {
			
		}
	}

    private void generateNoteOnSD(String Percorso, String sFileName, String sBody) {
        try {
            File gpxfile = new File(Percorso, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // e.printStackTrace();
            // VariabiliStaticheGlobali.getInstance().getLog().ScriveLog(new Object(){}.getClass().getEnclosingMethod().getName(), "Generazione file di testo. Errore: "+e.getMessage());
        }
    }

    public boolean fileExistsInSD(String sFileName, String Percorso){
        // VariabiliStaticheGlobali.getInstance().getLog().ScriveLog(new Object(){}.getClass().getEnclosingMethod().getName(), "Controllo esistenza file: "+Percorso+"/"+sFileName);
        String sFile=Percorso+"/"+sFileName;
        File file = new File(sFile);

        return file.exists();
    }

    public void CreaCartelle(final String Percorso) {
        // hCreaCartelle = new Handler();
        // hCreaCartelle.postDelayed(runCreaCartelle = new Runnable() {
        //    @Override
        //     public void run() {
        // VariabiliStaticheGlobali.getInstance().getLog().ScriveLog(new Object(){}.getClass().getEnclosingMethod().getName(), "Creazione cartelle "+Percorso);
        String Campi[]=(Percorso+"/").split("/",-1);
        String ss="";

        for (String s : Campi) {
            if (!s.isEmpty()) {
                ss += "/" + s;
                CreaCartella(ss);
                if (!fileExistsInSD(".noMedia",ss )) {
                    // Crea file per nascondere alla galleria i files immagine della cartella
                    generateNoteOnSD(ss, ".noMedia","");
                }
            }
        }
        //  }
        // }, 50);
    }

	private void displayDirectoryContents(File dir) {
		try {
			File[] files = dir.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isDirectory()) {
						// System.out.println("directory:" + file.getCanonicalPath());
						Cartelle.add(file.getCanonicalPath());
						displayDirectoryContents(file);
					} else {
						// System.out.println("     file:" + file.getCanonicalPath());
						String ff = file.getCanonicalPath().toUpperCase().trim();
						if (ff.contains(".JPG") || ff.contains(".JPEG") || ff.contains(".BMP") || ff.contains(".GIF") || ff.contains(".PNG")) {
							ListImages.add(file.getCanonicalPath());
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void LeggeImmagini(final Context context, String Percorso) {
		Log l = new Log();
		ListImages=new ArrayList<String>();
		Cartelle=new ArrayList<String>();

		File currentDir = new File(Percorso); // current directory
		displayDirectoryContents(currentDir);

		DBLocale dbl=new DBLocale();
		dbl.ScriveListaImmagini(context, ListImages);

		SharedObjects.getInstance().setListaImmagini(ListImages);
		SharedObjects.getInstance().setQuanteImm(ListImages.size());

		Utility u = new Utility();
		u.ScriveInfo(l);

		if (SharedObjects.getInstance().getAttivo().equals("S")) {
			u.FaiPartireTimer();
			// u.FaiPartireAggiornatore();
		}


		/* try {
			if (SharedObjects.getInstance().getLingua().equals("INGLESE")) {
				ApreProgress(context, "Please wait...\nReading folders in progress");
			} else {
				ApreProgress(context, "Attendere prego...\nLettura cartella in corso");
			}
			
			Cartelle=new ArrayList<String>();
			ListImages=new ArrayList<String>();
			Letta=new ArrayList<String>();
			
			l.ScriveLog("Start Read IMG Folder - Main loop External 0 - : "+Percorso);
			LeggeCartelle(context, Percorso);
			if (!EsciDalCiclo) {
				if (Cartelle.size()>0) {
					Letta.set(0, "OK");
					l.ScriveLog("Start Read IMG Folder - Main loop External 1 - : "+Cartelle.get(0));
					LeggeCartelle(context, Cartelle.get(0));
					Ancora=true;
				}
				
				if (!EsciDalCiclo) {
					Cartella=Percorso;
					l.ScriveLog("Start Read IMG Folder - Main loop External 2 - : "+Cartella);
					LeggeImmaginiCartella(context, Cartella);
			
					if (!EsciDalCiclo) {
						handler = new Handler(); 
				        r = new Runnable() {
				            public void run() {
				        		if (Ancora) {
				        			Ancora=false;
				        			for (int i=0;i<=Cartelle.size()-1;i++) {
				        				if (Cartelle.get(i)!=null) {
				        					if (Letta.get(i).equals("NO")) {
				        						Letta.set(i, "OK");
				
				        						l.ScriveLog("Read IMG Folder - Main loop - : "+Cartelle.get(i));
				        						
				        						LeggeCartelle(context, Cartelle.get(i));
				        						Ancora=true;
				        						
				        						if (!EsciDalCiclo) {
				        							break;
				        						}
	
				        						if (SharedObjects.getInstance().getLingua().equals("INGLESE")) {
				            						progressDialog.setMessage("Please wait...\nReading folder in progress.\nFolder: "+(i+1)+"/"+Cartelle.size());
				        						} else {
				            						progressDialog.setMessage("Attendere prego...\nLettura cartella in corso.\nCartelle: "+(i+1)+"/"+Cartelle.size());
				        						}
				        					}
				        				}
				        			}
				
				        			if (!EsciDalCiclo) {
				        				Ancora=false;
				        			}
				        			
				        			handler.postDelayed(r, 150);
				            	} else {
				                	handler = null;
				                    r = null;
				                    
				        			if (!EsciDalCiclo) {
					            		QuanteCart=Cartelle.size();
					            		Quale=0;
					            		//pb.setMax(QuanteCart);
					
					                	handler = new Handler(); 
					                    r = new Runnable() {
					                        public void run() {
					                        	//pb.setProgress(Quale);
					                    		if (Quale<QuanteCart) {
					                    			if (Cartelle.get(Quale)!=null) {
					                    				Cartella=Cartelle.get(Quale);
					                    				
					                    				l.ScriveLog("Read IMG Folder - Second loop - "+Cartella);
					                    				
					                    				LeggeImmaginiCartella(context, Cartella);
					            						
					            						if (SharedObjects.getInstance().getLingua().equals("INGLESE")) {
					                						progressDialog.setMessage("Please wait...\nReading folder in progress.\nFolder: "+(Quale+1)+"/"+Cartelle.size());
					            						} else {
					                						progressDialog.setMessage("Attendere prego...\nLettura cartella in corso.\nCartelle: "+(Quale+1)+"/"+Cartelle.size());
					            						}
					                    			}
					
					        						if (!EsciDalCiclo) {
						                    			Quale++;
					        						} else {
					        							Quale=QuanteCart;
					        						}
					                    			
					                    			handler.postDelayed(r, 30);
					                        	} else {
					                            	handler = null;
					                                r = null;
					                				
					        						if (!EsciDalCiclo) {
						                				DBLocale dbl=new DBLocale();
						                        		dbl.ScriveListaImmagini(context, ListImages);

														SharedObjects.getInstance().setListaImmagini(ListImages);
						                    			SharedObjects.getInstance().setQuanteImm(ListImages.size());

						                    			MainActivity ma=new MainActivity();
						                    			ma.ScriveInfo();
						                    			
						                    			if (SharedObjects.getInstance().getAttivo().equals("S")) {
						                    				ma.FaiPartireTimer();
						                    				ma.FaiPartireAggiornatore();
						                    			}
						                    			
						                				l.ScriveLog("Read IMG Folder - End loop - ");
						
						                        		progressDialog.dismiss();
						                        		
						                				l.ScriveLog("Start IMG Data write");
						                        		
						                				l.ScriveLog("End IMG Data write");
						                				l.ScriveLog("Start scan tag - Automatic");

						                				l.ScriveLog("End scan tag - Automatic");
					        						} else {
					        			        		progressDialog.dismiss();
					        						}
					                        	}
					                        }
					                    };
					                    handler.postDelayed(r, 1);
				    				} else {
				    	        		progressDialog.dismiss();
					            	}
				            	}
				            }
				        };
				        handler.postDelayed(r, 1);
					} else {
		        		progressDialog.dismiss();
			        }
				} else {
	        		progressDialog.dismiss();
				}
			} else {
        		progressDialog.dismiss();
			}
		} catch (Exception e) {
			l.ScriveLog("Error on LeggeImmagini: "+e.getMessage());
			EsciDalCiclo=true;
			VisualizzaPOPUP(context, "Error: "+e.getMessage()+"\nRoutine: LeggeImmagini\nDo you want send Log file to developer?", true);
		} */
	}

	public void CreaCartelleApplicazione(String Percorso) {
		int pos=0;
		String AppPerc=Percorso;
		String Perc="";
		
		// l.ScriveLog("------------------------------------");
		// l.ScriveLog("Start Folder Creation: "+Percorso);

		pos=AppPerc.indexOf("/");
		while (pos>-1) {
			Perc+="/"+AppPerc.substring(0,pos);
			Perc=Perc.replace("//", "/");
			// l.ScriveLog("      Folder Creation: "+Perc);
			CreaCartella(Perc);

			AppPerc=AppPerc.substring(pos+1,AppPerc.length());
			pos=AppPerc.indexOf("/");
		}
		Perc+="/"+AppPerc;
		// l.ScriveLog("      Folder Creation: "+Perc);
		CreaCartella(Perc);
		// l.ScriveLog("------------------------------------");
	}
    	
	public void CreaCartella(String Percorso) {
		try {
			File dDirectory = new File(Percorso);
			dDirectory.mkdirs();
		} catch (Exception ignored) {
		}  
	}

	public void EliminaFile(String NomeFile) {
		try {
        	File file = new File(NomeFile);
        	@SuppressWarnings("unused")
			boolean deleted = file.delete();
    	} catch (Exception ignored) {
    		
    	}
	}
	
	public void RinominaFile(String Percorso, String VecchioNome, String NuovoNome) {
		try {
			File from = new File(Percorso, VecchioNome);
			File to = new File(Percorso, NuovoNome);
			from.renameTo(to);
    	} catch (Exception ignored) {
    	}
	}
	
	public void EliminaCartella(String Percorso) {
		File root = new File(Percorso);
		File [] files = root.listFiles();
		int i;
		
		String NomeFile="";
		if (files!=null) {
	        for (File file : files){
	        	if (!file.isDirectory()) {
	        		NomeFile=file.getPath().toString();
	        		for (i=NomeFile.length()-1;i>0;i--) {
	        			if (NomeFile.substring(i,i+1).equals("/")) {
	        				NomeFile=NomeFile.substring(i+1,NomeFile.length());
	        				break;
	        			}
	        		}
	        		if (!NomeFile.trim().equals("")) {
	        			EliminaFile(Percorso+NomeFile);
	        		}
	        	}
	        }
		}
		try {
			File dDirectory = new File(Percorso);
			dDirectory.delete();
		} catch (Exception ignored) {
			
		}  
	}

	public List<String> OrdinaListaFiles(List<String> df) {
    	List<String> dfO=new ArrayList<String>();
		dfO=df;
		String Appoggio="";
		
		String Prima="";
		String Seconda="";
		for (int i=0;i<dfO.size();i++) {
			Prima=dfO.get(i).toUpperCase().trim();
			if (!Prima.equals("")) {
				if (Prima.substring(0,1).equals(".")) {
					Prima=Prima.substring(1,Prima.length());
				}
			}
			for (int k=0;k<dfO.size();k++) {
				Seconda=dfO.get(k).toUpperCase().trim();
				if (!Seconda.equals("")) {
					if (Seconda.substring(0,1).equals(".")) {
						Seconda=Seconda.substring(1,Seconda.length());
					}
				}
				if (i!=k) {
					if (Prima.compareTo(Seconda)<0) {
		    			Appoggio=dfO.get(i);
		    			dfO.set(i, dfO.get(k));
		    			dfO.set(k, Appoggio);
					}
				}
			}
		}
		
		return dfO;
	}
	
	public String PrendeNomeFile(String Percorso) {
		String Ritorno=Percorso;
		
		for (int i=Ritorno.length()-1;i>0;i--) {
			if (Ritorno.substring(i, i+1).equals("/")) {
				Ritorno=Ritorno.substring(i+1,Ritorno.length());
				break;
			}
		}
		
		return Ritorno;
	}
	
	public String PrendeNomeCartella(String Percorso) {
		String Ritorno=Percorso;
		
		for (int i=Ritorno.length()-1;i>0;i--) {
			if (Ritorno.substring(i, i+1).equals("/")) {
				Ritorno=Ritorno.substring(0,i);
				break;
			}
		}
		
		return Ritorno;
	}
	
	public long RitornaDimensioniFile(String NomeFile) {
		long Dime=0;
		
		File file = new File(NomeFile);
		Dime = file.length();
		
		return Dime;
	}
	
	public Boolean CopiaFile(String Origine, String Destinazione) {
		Boolean OK;
		File src=new File(Origine);
		File dst=new File(Destinazione);
		
	    InputStream in;
		try {
			in = new FileInputStream(src);
		    OutputStream out = new FileOutputStream(dst);

		    byte[] buf = new byte[1024];
		    int len;
		    try {
				while ((len = in.read(buf)) > 0) {
				    out.write(buf, 0, len);
				}
			    in.close();
			    out.close();
			    
			    OK=true;
			} catch (IOException e) {
				e.printStackTrace();
				OK=false;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			OK=false;
		}
		
		return OK;
	}
	
	public byte[] RitornaBytesDaFile(String NomeFile, int NumBytes) {
		File file = new File(NomeFile);
		int size;
		if (NumBytes==0) {
		    size = (int) file.length();
		} else {
			size=NumBytes;
		}
	    byte[] bytes = new byte[size];
	    try {
	        BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
	        buf.read(bytes, 0, size);
	        buf.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }		
	    
	    return bytes;
	}
}

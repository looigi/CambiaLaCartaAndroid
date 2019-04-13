package com.looigi.cambiolacarta;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class Log {
	private String PercorsoDIR=Environment.getExternalStorageDirectory().getPath()+"/LooigiSoft/CambiaLaCarta";
	private String NomeFile="log.txt";
	
	private String PrendeDataAttuale() {
		String Ritorno="";
		
		Calendar Oggi = Calendar.getInstance();
        int Giorno=Oggi.get(Calendar.DAY_OF_MONTH);
        int Mese=Oggi.get(Calendar.MONTH);
        int Anno=Oggi.get(Calendar.YEAR);
        String sGiorno=Integer.toString(Giorno).trim();
        String sMese=Integer.toString(Mese).trim();
        String sAnno=Integer.toString(Anno).trim();
        if (sGiorno.length()==1) {
        	sGiorno="0"+sGiorno;
        }
        if (sMese.length()==1) {
        	sMese="0"+sMese;
        }
        Ritorno=sGiorno+"/"+sMese+"/"+sAnno;
        
        return Ritorno;
	}
	
	private String PrendeOraAttuale() {
		String Ritorno="";
		
		Calendar Oggi = Calendar.getInstance();
        int Ore=Oggi.get(Calendar.HOUR);
        int Minuti=Oggi.get(Calendar.MINUTE);
        int Secondi=Oggi.get(Calendar.SECOND);
        String sOre=Integer.toString(Ore).trim();
        String sMinuti=Integer.toString(Minuti).trim();
        String sSecondi=Integer.toString(Secondi).trim();
        if (sOre.length()==1) {
        	sOre="0"+sOre;
        }
        if (sMinuti.length()==1) {
        	sMinuti="0"+sMinuti;
        }
        if (sSecondi.length()==1) {
        	sSecondi="0"+sSecondi;
        }
        Ritorno=sOre+":"+sMinuti+":"+sSecondi;
        
        return Ritorno;
	}
	
    public void PulisceFileDiLog() {
    	if (SharedObjects.getInstance().getLogAttivo()) {
	    	CreaCartelle(PercorsoDIR);
	    	String Datella="";
			Datella=PrendeDataAttuale()+" "+PrendeOraAttuale();
			
	        String sBody="Inizio log -> "+Datella;
			
	        File gpxfile = new File(PercorsoDIR, NomeFile);
	        FileWriter writer;
			try {
				writer = new FileWriter(gpxfile);
		        writer.append(sBody);
		        writer.flush();
		        writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}    	
    	}
    }
    
    public void ScriveLog(String MessaggioLog) {
    	if (SharedObjects.getInstance().getLogAttivo()) {
	    	CreaCartelle(PercorsoDIR);
			String sBody=MessaggioLog;
			
			String Datella="";
			Datella=PrendeDataAttuale()+" "+PrendeOraAttuale();
			
	        File gpxfile = new File(PercorsoDIR, NomeFile);
	        FileWriter writer;
			try {
				writer = new FileWriter(gpxfile,true);
		        writer.append(sBody+"->"+Datella+"\n");
		        writer.flush();
		        writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
    
	private void CreaCartelle(String Cartella) {
		for (int i=1;i<Cartella.length();i++) {
			if (Cartella.substring(i,i+1).equals("/")==true) {
				CreaCartella(Cartella.substring(0,i));
			}
		}
	}
	
    private void CreaCartella(String Percorso) {
		try {
			File dDirectory = new File(Percorso);
			dDirectory.mkdirs();
		} catch (Exception e) {
			
		}  
	}
    
}

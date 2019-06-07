package com.looigi.cambiolacarta;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.util.ArrayList;
import java.util.List;

public class DBLocale extends Activity {
	String NomeApp="SistemaMP3";
	
	public void CreaDB(Context context) {
		SQLiteDatabase myDB= ApreDB(context, "DatiLocali");

		try {
			myDB.execSQL("CREATE TABLE IF NOT EXISTS ListaImmagini (NomeImmagine Text);");
			myDB.execSQL("CREATE TABLE IF NOT EXISTS Opzioni (Lingua Text, " +
					"TipoCambio Text, MinutiPerCambio Text, QualeImm Text, " +
					"Attivo Text);");
			myDB.execSQL("CREATE TABLE IF NOT EXISTS Opzioni2 (ModalitaVisua Text, Stretch Text);");
			myDB.execSQL("CREATE TABLE IF NOT EXISTS Percorsi (Origine Text);");
			myDB.execSQL("CREATE TABLE IF NOT EXISTS Notifica (SiNo Text);");
			myDB.close();
    	} catch(Exception ignored) {

		} finally {
		   if (myDB != null){
			   myDB.close();
		   }
		}
	}

	public void ScriveListaImmagini(Context context, List<String> ListaImm) {
		EliminaListaImmagini(context);
		
		SQLiteDatabase myDB= ApreDB(context, "DatiLocali");
		String Sql;
		for (int i=0;i<ListaImm.size();i++) {
		   	Sql="Insert Into ListaImmagini Values ('"+ListaImm.get(i).replace("'","''")+"');";
		   	myDB.execSQL(Sql);
		}
	   	
	   	myDB.close();
	}
	
    public void PulisceDB(Context context) {
		SQLiteDatabase myDB= ApreDB(context, "DatiLocali");

    	try {
			myDB.execSQL("DROP TABLE ListaImmagini;");
			myDB.execSQL("DROP TABLE Opzioni;");
			myDB.execSQL("DROP TABLE Opzioni2;");
			myDB.execSQL("DROP TABLE Percorsi;");
    	} catch (Exception ignored) {
    		
    	}
    }

	public void EliminaListaImmagini(Context context) {
		SQLiteDatabase myDB= ApreDB(context, "DatiLocali");
	   	String Sql="Delete From ListaImmagini;";
	   	myDB.execSQL(Sql);
	   	
	   	myDB.close();
	}

	public void EliminaImmagine(Context context, String Nome) {
		SQLiteDatabase myDB= ApreDB(context, "DatiLocali");
	   	String Sql="Delete From ListaImmagini Where NomeImmagine='"+Nome.replace("'","''")+"';";
	   	myDB.execSQL(Sql);
	   	
	   	myDB.close();
	}
	
	public List<String> RitornaImmagini(Context context) {
		List<String> Imm=new ArrayList<String>();
		SQLiteDatabase myDB= ApreDB(context, "DatiLocali");
	   	String Sql="SELECT NomeImmagine FROM ListaImmagini Order By NomeImmagine;";
		Cursor c = myDB.rawQuery(Sql , null);
		c.moveToFirst();
		try {
			Imm.add(c.getString(0).replace("''","'"));
			while (c.moveToNext()) {
				Imm.add(c.getString(0).replace("''","'"));
	        }; 
		} catch (Exception ignored) {
			
		}
		c.close();
		
		return Imm;
	}

    public int QuanteImmagini(Context context) {
    	int Quanti=0;
    	
		SQLiteDatabase myDB= ApreDB(context, "DatiLocali");
	   	String Sql="SELECT Count(*) FROM ListaImmagini;";
		Cursor c = myDB.rawQuery(Sql , null);
		c.moveToFirst();
		try {
			Quanti=c.getInt(0);
		} catch (Exception ignored) {
			
		}
		c.close();
		
    	return Quanti;
    }

	private SQLiteDatabase ApreDB (Context context, String DB) {
		SQLiteDatabase mDB= null;
		
		if (context==null) {
			context=SharedObjects.getInstance().getContext();
			if (context == null) {
				MainActivity m = new MainActivity();
				context = m.getContext();
			}
		}
		mDB = context.openOrCreateDatabase(DB, MODE_PRIVATE, null);
		
		return mDB;
	}
		
	public void CambiaPercorsoOrigine(Context context, String Percorso) {
		SQLiteDatabase myDB= ApreDB(context, "DatiLocali");
		String Sql="Update Percorsi Set Origine='"+Percorso.replace("'","''")+"';";
	   	myDB.execSQL(Sql);
	   	
	   	myDB.close();
	}

	public void LeggePercorsi(Context context) {
    	String Origine="";
    	
		SQLiteDatabase myDB= ApreDB(context, "DatiLocali");
	   	String Sql="SELECT * FROM Percorsi;";
		Cursor c = myDB.rawQuery(Sql , null);
		c.moveToFirst();
		try {
			Origine=c.getString(0);
		} catch (Exception ignored) {
		   	Sql="Delete From Percorsi";
		   	myDB.execSQL(Sql);
		   	
		   	Sql="Insert Into Percorsi Values ('"+Environment.getExternalStorageDirectory().getPath()+"');";
		   	myDB.execSQL(Sql);
		   	
		   	Origine=Environment.getExternalStorageDirectory().getPath();;
		}
		c.close();
		myDB.close();

		SharedObjects.getInstance().setOrigine(Origine);
	}
	
	public void LeggeOpzioni(Context context) {
		SQLiteDatabase myDB= ApreDB(context, "DatiLocali");

	   	String Sql="SELECT * FROM Opzioni;";
		Cursor c = myDB.rawQuery(Sql , null);
		c.moveToFirst();
		try {
			String Ritorno;
			
			Ritorno=c.getString(0);
			SharedObjects.getInstance().setLingua(Ritorno);

			Ritorno=c.getString(1);
			SharedObjects.getInstance().setTipoCambio(Ritorno);

			Ritorno=c.getString(2);
			SharedObjects.getInstance().setMinutiPerCambio(Integer.parseInt(Ritorno));

			Ritorno=c.getString(3);
			SharedObjects.getInstance().setQualeImmagineHaVisualizzato(Integer.parseInt(Ritorno));

			Ritorno=c.getString(4);
			SharedObjects.getInstance().setAttivo(Ritorno);
		} catch (Exception ignored) {
		   	Sql="Delete From Opzioni";
		   	myDB.execSQL(Sql);
		   	
		   	Sql="Insert Into Opzioni Values ('INGLESE', 'RANDOM', '5', '0', 'S')";
		   	myDB.execSQL(Sql);

			SharedObjects.getInstance().setLingua("INGLESE");
			SharedObjects.getInstance().setTipoCambio("RANDOM");
			SharedObjects.getInstance().setMinutiPerCambio(5);
			SharedObjects.getInstance().setQualeImmagineHaVisualizzato(0);
			SharedObjects.getInstance().setAttivo("S");
		}
		c.close();

	   	Sql="SELECT * FROM Opzioni2;";
		c = myDB.rawQuery(Sql , null);
		c.moveToFirst();
		try {
			String Ritorno;
			
			Ritorno=c.getString(0);
			SharedObjects.getInstance().setModalitaVisua(Ritorno);
			
			Ritorno=c.getString(1);
			SharedObjects.getInstance().setStretch(Ritorno);
		} catch (Exception ignored) {
		   	Sql="Delete From Opzioni2";
		   	myDB.execSQL(Sql);
		   	
		   	Sql="Insert Into Opzioni2 Values ('S', 'N')";
		   	myDB.execSQL(Sql);

			SharedObjects.getInstance().setModalitaVisua("S");
			SharedObjects.getInstance().setStretch("N");
		}
		c.close();

	   	Sql="SELECT * FROM Notifica;";
		c = myDB.rawQuery(Sql , null);
		c.moveToFirst();
		try {
			String Ritorno;
			
			Ritorno=c.getString(0);
			SharedObjects.getInstance().setNotificaSiNo(Ritorno);
		} catch (Exception ignored) {
		   	Sql="Delete From Notifica";
		   	myDB.execSQL(Sql);
		   	
		   	Sql="Insert Into Notifica Values ('S')";
		   	myDB.execSQL(Sql);

			SharedObjects.getInstance().setNotificaSiNo("S");
		}
		c.close();

		myDB.close();
	}
	
	public void ScriveOpzioni(Context context) {
		String Lingua=SharedObjects.getInstance().getLingua();
		String Tipo=SharedObjects.getInstance().getTipoCambio();
		String Minuti=Integer.toString(SharedObjects.getInstance().getMinutiPerCambio());
		String QualeImm=Integer.toString(SharedObjects.getInstance().getQualeImmagineHaVisualizzato());
		String Attivo=SharedObjects.getInstance().getAttivo();
		String MVisua=SharedObjects.getInstance().getModalitaVisua();
		String Stretch=SharedObjects.getInstance().getStretch();
		String MNotSiNo=SharedObjects.getInstance().getNotificaSiNo();
		
		SQLiteDatabase myDB= ApreDB(context, "DatiLocali");
	   	
	   	String Sql="Update Opzioni Set "+
	   			"Lingua='"+Lingua+"', "+
	   			"TipoCambio='"+Tipo+"', "+
	   			"MinutiPerCambio='"+Minuti+"', "+
	   			"QualeImm='"+QualeImm+"', "+
	   			"Attivo='"+Attivo+"' "+
	   			";";
	   	myDB.execSQL(Sql);
	   	
	   	Sql="Update Opzioni2 Set "+
	   			"ModalitaVisua='"+MVisua+"', "+
	   			"Stretch='"+Stretch+"' "+
	   			";";
	   	myDB.execSQL(Sql);
	   	
	   	Sql="Update Notifica Set "+
	   			"SiNo='"+MNotSiNo+"' "+
	   			";";
	   	myDB.execSQL(Sql);
	   	
	   	myDB.close();
	}
	
}

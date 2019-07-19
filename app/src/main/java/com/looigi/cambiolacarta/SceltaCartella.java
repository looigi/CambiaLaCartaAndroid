package com.looigi.cambiolacarta;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SceltaCartella extends Activity {
	private String Percorso;
	private String Cartell[];
	private Boolean StoInRoot;
	private Context context;

	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scelta_cartella);

		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();
		soundPoolMap.put(1, soundPool.load(this, R.raw.premuto, 1));

		context=this;

		ImpostaTestoPerLingua();

		Percorso=SharedObjects.getInstance().getOrigine();
		if (Percorso.trim().equals("")) {
			Percorso=Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		TextView tp=(TextView) findViewById(R.id.txtPercorsoSC);
		tp.setText(Percorso);

		ListView lvf=(ListView) findViewById(R.id.lstFolder);
		lvf.setClickable(true);
		lvf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);

				String Link = (String) Cartell[position];

				Percorso=Percorso.replace("//", "/");
				if (Percorso.substring(Percorso.length()-1,Percorso.length()).equals("/")) {
					Percorso=Percorso.substring(0,Percorso.length()-1);
				}
				if (Link.equals("..")) {
					for (int i=Percorso.length()-1;i>=0;i--) {
						if (Percorso.substring(i, i+1).equals("/")) {
							Percorso=Percorso.substring(0,i);
							break;
						}
					}
				} else {
					Percorso+="/"+Link;
				}

				if (Percorso.contains(" (")) {
					Percorso=Percorso.substring(0, Percorso.indexOf(" ("));
				}

				Percorso+="/";

				CaricaFolder(Percorso);
			}
		});

		ImageView btnIndietro = (ImageView) findViewById(R.id.imgIndietroCa);
		btnIndietro.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);

				Intent Indietro= new Intent(SceltaCartella.this, MainActivity.class);
				startActivity(Indietro);

				finish();
			}
		});

		ImageView btnSeleziona = (ImageView) findViewById(R.id.imgSeleziona);
		btnSeleziona.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);

				DBLocale dbl=new DBLocale();
				dbl.CambiaPercorsoOrigine(context, Percorso);

				SharedObjects.getInstance().setOrigine(Percorso);
				SharedObjects.getInstance().setCaricaDati(true);

				Intent Indietro= new Intent(SceltaCartella.this, MainActivity.class);
				startActivity(Indietro);

				finish();
			}
		});

		ImageView btnNuova = (ImageView) findViewById(R.id.imgNuova);
		btnNuova.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);

				ImmissioneTesto();
			}
		});

		CaricaFolder(Percorso);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			return false;
		}

		return super.onKeyDown(keyCode, event);
	}

	private void ImmissioneTesto() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		if (SharedObjects.getInstance().getLingua().equals("INGLESE")) {
			alert.setTitle("MP3 Organizer");
			alert.setMessage("Please insert the name of the new folder");
		} else {
			alert.setTitle("Sistema MP3");
			alert.setMessage("Immettere il nome della nuova cartella");
		}

		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);

				String value = input.getText().toString();

				Percorso=Percorso.replace("//", "/");
				if (Percorso.substring(Percorso.length()-1,Percorso.length()).equals("/")) {
					Percorso=Percorso.substring(0,Percorso.length()-1);
				}

				if (Percorso.contains(" (")) {
					Percorso=Percorso.substring(0, Percorso.indexOf(" ("));
				}

				Percorso+="/"+value+"/";

				GestioneFilesCartelle gfc=new GestioneFilesCartelle();
				gfc.CreaCartella(Percorso);

				CaricaFolder(Percorso);
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				SuonAudio s=new SuonAudio();
				s.SuonaAudio(1, soundPool);
			}
		});

		alert.show();
	}

	private void CaricaFolder(String Percorso) {
		GestioneFilesCartelle gfc=new GestioneFilesCartelle();
		TextView tv=(TextView) findViewById(R.id.txtPercorsoSC);
		tv.setText(Percorso);

		int i=0;
		ListView Lista=(ListView) findViewById(R.id.lstFolder);
		List<String> Cart=new ArrayList<String>();
		List<String> Quanti=new ArrayList<String>();
		File root = new File(Percorso);
		File [] files = root.listFiles();

		String NomeCartella="";
		Cart.add("");
		Quanti.add("");
		if (files!=null) {
			for (File file : files){
				if (file.isDirectory()) {
					NomeCartella=file.getPath();
					for (i=NomeCartella.length()-1;i>0;i--) {
						if (NomeCartella.substring(i,i+1).equals("/")) {
							NomeCartella=NomeCartella.substring(i+1,NomeCartella.length());
							break;
						}
					}
					if (!NomeCartella.trim().equals("")) {
						//NomeCartella=NomeCartella.replace(".", "");
						if (NomeCartella.subSequence(0, 1).equals("/")) {
							NomeCartella=NomeCartella.substring(1,NomeCartella.length());
						}
						Cart.add(NomeCartella);
						Quanti.add(NomeCartella+"*"+gfc.ContaImmaginiCartella(context, file.toString()));
					}
				}
			}
		}

		List<String> CartOrdinata=gfc.OrdinaListaFiles(Cart);

		Cartell=new String[CartOrdinata.size()+2];

		int Inizio=0;
		String Appoggio;
		String Appoggio2;

		if (!Percorso.equals("/")) {
			Cartell[0]="..";
			Inizio=1;
			StoInRoot=false;
		} else {
			Inizio=0;
			StoInRoot=true;
		}
		for (i=0;i<CartOrdinata.size();i++) {
			if (!CartOrdinata.get(i).equals("")) {
				Cartell[Inizio]=CartOrdinata.get(i);
				for (int k=0;k<CartOrdinata.size();k++) {
					Appoggio2=Quanti.get(k);
					if (!Appoggio2.equals("")) {
						Appoggio=Appoggio2.substring(0,Appoggio2.indexOf("*"));
						Appoggio2=Appoggio2.substring(Appoggio2.indexOf("*")+1, Appoggio2.length());
						if (Appoggio.contains(CartOrdinata.get(i))) {
							Cartell[Inizio]+=" ("+Appoggio2+")";
							break;
						}
					}
				}
				Inizio++;
			}
		}
		int QuantiLinks=Cartell.length;

		HashMap<String,Object>
				ListaLink=new HashMap<String, Object>();
		ArrayList<HashMap<String, Object>>
				data=new ArrayList<HashMap<String,Object>>();

		int pref;

		for(i=0;i<=QuantiLinks;i++){
			try {
				Appoggio=Cartell[i];

				if (!Appoggio.trim().equals("")) {
					pref=R.drawable.su;

					ListaLink=new HashMap<String, Object>();
					ListaLink.put("cartella", Appoggio);
					ListaLink.put("imgCartella", pref);

					data.add(ListaLink);
				}
			} catch (Exception ignored) {

			}
		}
		String[] from={"cartella", "imgCartella"};
		int[] to={R.id.txtCartella, R.id.imgCartella};
		SimpleAdapter adapter = new SimpleAdapter(
				context,
				data,
				R.layout.listview_cartelle,
				from,
				to);

		Lista.setAdapter(adapter);
	}

	private void ImpostaTestoPerLingua() {
		String Path="";
		String NuovaCartella="";
		String Seleziona="";

		if (SharedObjects.getInstance().getLingua().equals("INGLESE")) {
			Path="Path";
			NuovaCartella="New folder";
			Seleziona="Select";
		} else {
			Path="Percorso";
			NuovaCartella="Nuova cartella";
			Seleziona="Seleziona";
		}

		TextView tp=(TextView) findViewById(R.id.txtPath);
		tp.setText(Path);
		TextView tnc=(TextView) findViewById(R.id.txtNuovaC);
		tnc.setText(NuovaCartella);
		TextView ts=(TextView) findViewById(R.id.txtSeleziona);
		ts.setText(Seleziona);
	}

}

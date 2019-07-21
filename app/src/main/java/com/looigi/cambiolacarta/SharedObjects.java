package com.looigi.cambiolacarta;

import android.content.Context;
import android.media.AudioManager;
import android.os.Environment;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class SharedObjects {
    private static SharedObjects instance = null;

    private SharedObjects() {
    }

    public static SharedObjects getInstance()
    {
    	if (instance == null) instance = new SharedObjects();
    	return instance;
    }

    private String PercorsoDIR= Environment.getExternalStorageDirectory().getPath()+"/LooigiSoft/CambiaLaCarta";
    protected Context context;
    private String Stretch;
    private int SchermoX;
    private int SchermoY;
    private int DimeWallWidthOriginale;
    private int DimeWallHeightOriginale;
    private String Origine;
    private String Lingua;
    private String TipoCambio;
    private int MinutiPerCambio;
    private String Attivo;
    private int QualeImmagineHaVisualizzato;
    private String ModalitaVisua;
    private String NotificaSiNo;
    protected List<String> ListaImmagini;
    private int QuanteImm;
    private Boolean LogAttivo=true;
    private Boolean CaricaDati;
    private Boolean SuonaAudio=true;
    private boolean SettaLockScreen=true;
    private AudioManager audioManager;
    private android.view.WindowManager a1;
    private ImageView imm;
    private TextView txtPercorso;
    private TextView txtNumImm;
    private TextView txtSceltaCartella;
    private TextView txtCambiaSubito;
    private TextView txtListaImm;
    private TextView txtOpzioni;
    private TextView txtMinuti;
    private TextView txtTipoCambio;
    private TextView txtImmVisua;
    private TextView txtNomeImm;
    private TextView txtProssima;
    private TextView txtPrecedente;
    private TextView txtTempo;
    private TextView txtCaffe;
    private CheckBox chkAttivo;
    private Boolean TimerPartito;
    private Boolean StaPartendo;
    private String PathUltimaImaggineDL;
    private LinearLayout imgPrec;
    private LinearLayout imgSucc;
    private LinearLayout imgCambia;
    private LinearLayout imgRefresh;
    private LinearLayout imgCambiaDir;

    public LinearLayout getImgRefresh() {
        return imgRefresh;
    }

    public void setImgRefresh(LinearLayout imgRefresh) {
        this.imgRefresh = imgRefresh;
    }

    public LinearLayout getImgCambiaDir() {
        return imgCambiaDir;
    }

    public void setImgCambiaDir(LinearLayout imgCambiaDir) {
        this.imgCambiaDir = imgCambiaDir;
    }

    public LinearLayout getImgPrec() {
        return imgPrec;
    }

    public void setImgPrec(LinearLayout imgPrec) {
        this.imgPrec = imgPrec;
    }

    public LinearLayout getImgSucc() {
        return imgSucc;
    }

    public void setImgSucc(LinearLayout imgSucc) {
        this.imgSucc = imgSucc;
    }

    public LinearLayout getImgCambia() {
        return imgCambia;
    }

    public void setImgCambia(LinearLayout imgCambia) {
        this.imgCambia = imgCambia;
    }

    public String getPathUltimaImaggineDL() {
        return PathUltimaImaggineDL;
    }

    public void setPathUltimaImaggineDL(String pathUltimaImaggineDL) {
        PathUltimaImaggineDL = pathUltimaImaggineDL;
    }

    public String getPercorsoDIR() {
        return PercorsoDIR;
    }

    public void setPercorsoDIR(String percorsoDIR) {
        PercorsoDIR = percorsoDIR;
    }

    public boolean isSettaLockScreen() {
        return SettaLockScreen;
    }

    public void setSettaLockScreen(boolean settaLockScreen) {
        SettaLockScreen = settaLockScreen;
    }

    public Boolean getStaPartendo() {
        return StaPartendo;
    }

    public void setStaPartendo(Boolean staPartendo) {
        StaPartendo = staPartendo;
    }

    public Boolean getTimerPartito() {
        return TimerPartito;
    }

    public void setTimerPartito(Boolean timerPartito) {
        TimerPartito = timerPartito;
    }

    public TextView getTxtPercorso() {
        return txtPercorso;
    }

    public void setTxtPercorso(TextView txtPercorso) {
        this.txtPercorso = txtPercorso;
    }

    public TextView getTxtNumImm() {
        return txtNumImm;
    }

    public void setTxtNumImm(TextView txtNumImm) {
        this.txtNumImm = txtNumImm;
    }

    public TextView getTxtSceltaCartella() {
        return txtSceltaCartella;
    }

    public void setTxtSceltaCartella(TextView txtSceltaCartella) {
        this.txtSceltaCartella = txtSceltaCartella;
    }

    public TextView getTxtCambiaSubito() {
        return txtCambiaSubito;
    }

    public void setTxtCambiaSubito(TextView txtCambiaSubito) {
        this.txtCambiaSubito = txtCambiaSubito;
    }

    public TextView getTxtListaImm() {
        return txtListaImm;
    }

    public void setTxtListaImm(TextView txtListaImm) {
        this.txtListaImm = txtListaImm;
    }

    public TextView getTxtOpzioni() {
        return txtOpzioni;
    }

    public void setTxtOpzioni(TextView txtOpzioni) {
        this.txtOpzioni = txtOpzioni;
    }

    public TextView getTxtMinuti() {
        return txtMinuti;
    }

    public void setTxtMinuti(TextView txtMinuti) {
        this.txtMinuti = txtMinuti;
    }

    public TextView getTxtTipoCambio() {
        return txtTipoCambio;
    }

    public void setTxtTipoCambio(TextView txtTipoCambio) {
        this.txtTipoCambio = txtTipoCambio;
    }

    public TextView getTxtImmVisua() {
        return txtImmVisua;
    }

    public void setTxtImmVisua(TextView txtImmVisua) {
        this.txtImmVisua = txtImmVisua;
    }

    public TextView getTxtNomeImm() {
        return txtNomeImm;
    }

    public void setTxtNomeImm(TextView txtNomeImm) {
        this.txtNomeImm = txtNomeImm;
    }

    public TextView getTxtProssima() {
        return txtProssima;
    }

    public void setTxtProssima(TextView txtProssima) {
        this.txtProssima = txtProssima;
    }

    public TextView getTxtPrecedente() {
        return txtPrecedente;
    }

    public void setTxtPrecedente(TextView txtPrecedente) {
        this.txtPrecedente = txtPrecedente;
    }

    public TextView getTxtTempo() {
        return txtTempo;
    }

    public void setTxtTempo(TextView txtTempo) {
        this.txtTempo = txtTempo;
    }

    public TextView getTxtCaffe() {
        return txtCaffe;
    }

    public void setTxtCaffe(TextView txtCaffe) {
        this.txtCaffe = txtCaffe;
    }

    public CheckBox getChkAttivo() {
        return chkAttivo;
    }

    public void setChkAttivo(CheckBox chkAttivo) {
        this.chkAttivo = chkAttivo;
    }

    public ImageView getImm() {
        return imm;
    }

    public void setImm(ImageView imm) {
        this.imm = imm;
    }

    public WindowManager getA1() {
        return a1;
    }

    public void setA1(WindowManager a1) {
        this.a1 = a1;
    }

    public Boolean getSuonaAudio() {
        return SuonaAudio;
    }

    public void setSuonaAudio(Boolean suonaAudio) {
        SuonaAudio = suonaAudio;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public void setAudioManager(AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    public Boolean getCaricaDati() {
        return CaricaDati;
    }

    public void setCaricaDati(Boolean caricaDati) {
        CaricaDati = caricaDati;
    }

    public Boolean getLogAttivo() {
        return LogAttivo;
    }

    public void setLogAttivo(Boolean logAttivo) {
        LogAttivo = logAttivo;
    }

    public int getQuanteImm() {
        return QuanteImm;
    }

    public void setQuanteImm(int quanteImm) {
        QuanteImm = quanteImm;
    }

    public List<String> getListaImmagini() {
        return ListaImmagini;
    }

    public void setListaImmagini(List<String> listaImmagini) {
        ListaImmagini = listaImmagini;
    }

    public String getNotificaSiNo() {
        return NotificaSiNo;
    }

    public void setNotificaSiNo(String notificaSiNo) {
        NotificaSiNo = notificaSiNo;
    }

    public String getModalitaVisua() {
        return ModalitaVisua;
    }

    public void setModalitaVisua(String modalitaVisua) {
        ModalitaVisua = modalitaVisua;
    }

    public int getQualeImmagineHaVisualizzato() {
        return QualeImmagineHaVisualizzato;
    }

    public void setQualeImmagineHaVisualizzato(int qualeImmagineHaVisualizzato) {
        QualeImmagineHaVisualizzato = qualeImmagineHaVisualizzato;
    }

    public String getOrigine() {
        return Origine;
    }

    public void setOrigine(String origine) {
        Origine = origine;
    }

    public String getLingua() {
        return Lingua;
    }

    public void setLingua(String lingua) {
        Lingua = lingua;
    }

    public String getTipoCambio() {
        return TipoCambio;
    }

    public void setTipoCambio(String tipoCambio) {
        TipoCambio = tipoCambio;
    }

    public int getMinutiPerCambio() {
        return MinutiPerCambio;
    }

    public void setMinutiPerCambio(int minutiPerCambio) {
        MinutiPerCambio = minutiPerCambio;
    }

    public String getAttivo() {
        return Attivo;
    }

    public void setAttivo(String attivo) {
        Attivo = attivo;
    }

    public int getDimeWallWidthOriginale() {
        return DimeWallWidthOriginale;
    }

    public void setDimeWallWidthOriginale(int dimeWallWidthOriginale) {
        DimeWallWidthOriginale = dimeWallWidthOriginale;
    }

    public int getDimeWallHeightOriginale() {
        return DimeWallHeightOriginale;
    }

    public void setDimeWallHeightOriginale(int dimeWallHeightOriginale) {
        DimeWallHeightOriginale = dimeWallHeightOriginale;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getStretch() {
        return Stretch;
    }

    public void setStretch(String stretch) {
        Stretch = stretch;
    }

    public int getSchermoX() {
        return SchermoX;
    }

    public void setSchermoX(int schermoX) {
        SchermoX = schermoX;
    }

    public int getSchermoY() {
        return SchermoY;
    }

    public void setSchermoY(int schermoY) {
        SchermoY = schermoY;
    }

}

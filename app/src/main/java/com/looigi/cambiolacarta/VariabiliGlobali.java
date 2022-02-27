package com.looigi.cambiolacarta;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

public class VariabiliGlobali {
    private static final VariabiliGlobali ourInstance = new VariabiliGlobali();

    public static VariabiliGlobali getInstance() {
        return ourInstance;
    }

    private VariabiliGlobali() {
    }

    public String PercorsoDIR= Environment.getExternalStorageDirectory().getPath()+"/LooigiSoft/CambiaLaCarta";
    protected Context context;
    // protected Activity activityPrincipale;
    // private NotificationManager notificationManager;
    // private NotificationCompat.Builder notificationBuilder;
    // private RemoteViews contentView;
    private int VecchiaImmagine = -1;
    private Boolean ScreenON = true;
    private boolean CambiataImmagine=false;
    // private Intent iServizio;
    private Handler handler = null;
    private Runnable r;
    private String CHANNEL_ID = "ForegroundServiceChannel";
    private boolean Partito = false;

    public Bitmap getBitmapOriginale() {
        return bitmapOriginale;
    }

    public void setBitmapOriginale(Bitmap bitmapOriginale) {
        this.bitmapOriginale = bitmapOriginale;
    }

    private Bitmap bitmapOriginale;

    public boolean isPartito() {
        return Partito;
    }

    public void setPartito(boolean partito) {
        Partito = partito;
    }

    public String getCHANNEL_ID() {
        return CHANNEL_ID;
    }

    public void setCHANNEL_ID(String CHANNEL_ID) {
        this.CHANNEL_ID = CHANNEL_ID;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Runnable getR() {
        return r;
    }

    public void setR(Runnable r) {
        this.r = r;
    }

    /* public Activity getActivityPrincipale() {
        return activityPrincipale;
    }

    public void setActivityPrincipale(Activity activityPrincipale) {
        this.activityPrincipale = activityPrincipale;
    } */

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /* public Intent getiServizio() {
        return iServizio;
    }

    public void setiServizio(Intent iServizio) {
        this.iServizio = iServizio;
    }
    */

    public boolean isCambiataImmagine() {
        return CambiataImmagine;
    }

    public void setCambiataImmagine(boolean cambiataImmagine) {
        CambiataImmagine = cambiataImmagine;
    }

    public Boolean getScreenON() {
        return ScreenON;
    }

    public void setScreenON(Boolean screenON) {
        ScreenON = screenON;
    }

    public int getVecchiaImmagine() {
        return VecchiaImmagine;
    }

    public void setVecchiaImmagine(int vecchiaImmagine) {
        VecchiaImmagine = vecchiaImmagine;
    }

    /* public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public void setNotificationManager(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    public NotificationCompat.Builder getNotificationBuilder() {
        return notificationBuilder;
    }

    public void setNotificationBuilder(NotificationCompat.Builder notificationBuilder) {
        this.notificationBuilder = notificationBuilder;
    }

    public RemoteViews getContentView() {
        return contentView;
    }

    public void setContentView(RemoteViews contentView) {
        this.contentView = contentView;
    }
    */
}

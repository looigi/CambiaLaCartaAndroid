package com.looigi.cambiolacarta;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

public class VariabiliGlobali {
    private static final VariabiliGlobali ourInstance = new VariabiliGlobali();

    public static VariabiliGlobali getInstance() {
        return ourInstance;
    }

    private VariabiliGlobali() {
    }

    private Context context;
    private Activity activityPrincipale;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    private RemoteViews contentView;
    private int VecchiaImmagine = -1;

    public Activity getActivityPrincipale() {
        return activityPrincipale;
    }

    public void setActivityPrincipale(Activity activityPrincipale) {
        this.activityPrincipale = activityPrincipale;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getVecchiaImmagine() {
        return VecchiaImmagine;
    }

    public void setVecchiaImmagine(int vecchiaImmagine) {
        VecchiaImmagine = vecchiaImmagine;
    }

    public NotificationManager getNotificationManager() {
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
}

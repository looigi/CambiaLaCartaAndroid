package com.looigi.cambiolacarta;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.os.Build;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.support.v4.app.NotificationCompat;

public class Notifiche {
    private int id = 11;
    // private NotificationManager notificationManager;
    // private NotificationCompat.Builder notificationBuilder;
    // private RemoteViews contentView;

    private static final Notifiche ourInstance = new Notifiche();

    public static Notifiche getInstance() {
        return ourInstance;
    }

    private Notifiche() {
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    VariabiliGlobali.getInstance().getCHANNEL_ID(),
                    "Cambia la carta",
                    NotificationManager.IMPORTANCE_LOW
            );
            if (VariabiliGlobali.getInstance().getActivityPrincipale() != null) {
                NotificationManager manager = VariabiliGlobali.getInstance().getActivityPrincipale().getSystemService(NotificationManager.class);
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    private void impostaOggettiDescrittivi(RemoteViews view){
        Log l = new Log();
        Utility u=new Utility();
        if (SharedObjects.getInstance().getNotificaSiNo().equals("S")) {
            String Immagine="";

            if (!SharedObjects.getInstance().getTipoCambio().equals("SINCRONIZZATA")) {
                GestioneFilesCartelle gfc = new GestioneFilesCartelle();
                try {
                    Immagine = gfc.PrendeNomeFile(SharedObjects.getInstance().getListaImmagini().get(SharedObjects.getInstance().getQualeImmagineHaVisualizzato()));
                    int pos = Immagine.indexOf(".");
                    if (pos > -1) {
                        Immagine = Immagine.substring(0, pos);
                    }
                } catch (Exception ignored) {

                }
            } else {
                Immagine = SharedObjects.getInstance().getPathUltimaImaggineDL();
            }
            if (Immagine != null) {
                if (!Immagine.equals("")) {
                    view.setTextViewText(R.id.txtImmagine, Immagine);
                } else {
                    view.setTextViewText(R.id.txtImmagine, "");
                }
            }
            view.setTextViewText(R.id.TextView01, u.ControllaLingua(VariabiliGlobali.getInstance().getContext(),
                    R.string.app_name, R.string.app_nameEN));

            if (Immagine != null) {
                if (!SharedObjects.getInstance().getTipoCambio().equals("SINCRONIZZATA")) {
                    Immagine = SharedObjects.getInstance().getListaImmagini().get(SharedObjects.getInstance().getQualeImmagineHaVisualizzato());
                } else {
                    Immagine = SharedObjects.getInstance().getPercorsoDIR() + "/Download.jpg";
                }
            }
            Bitmap Immagin=null;

            view.setViewVisibility(R.id.imgCopertinaM, LinearLayout.VISIBLE);
            view.setViewVisibility(R.id.txtImmagine, LinearLayout.VISIBLE);
            // view.setViewVisibility(R.id.layPicture, LinearLayout.VISIBLE);

            if (Immagine!=null && !Immagine.equals("")) {
                try {
                    Immagin=u.PrendeImmagineCompressa(Immagine, l);
                    view.setImageViewBitmap(R.id.imgCopertinaM, Immagin);
                } catch (Exception ignored) {
                    view.setImageViewResource(R.id.imgCopertinaM, R.drawable.ic_launcher);
                }
            } else {
                view.setImageViewResource(R.id.imgCopertinaM, R.drawable.ic_launcher);
            }
        } else {
            view.setTextViewText(R.id.txtImmagine, u.ControllaLingua(VariabiliGlobali.getInstance().getContext(),
                    R.string.app_name, R.string.app_nameEN));

            view.setViewVisibility(R.id.imgCopertinaM, LinearLayout.GONE);
            view.setViewVisibility(R.id.TextView01, LinearLayout.GONE);
            // view.setViewVisibility(R.id.layPicture, LinearLayout.GONE);
        }
    }

    public Notification CreaNotifichella() {
        if (VariabiliGlobali.getInstance().getActivityPrincipale() != null) {
            Intent notificationIntent = new Intent(VariabiliGlobali.getInstance().getContext(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(VariabiliGlobali.getInstance().getContext(),
                    0, notificationIntent, 0);
            RemoteViews contentView = (new RemoteViews(VariabiliGlobali.getInstance().getActivityPrincipale().getPackageName(), //
                    R.layout.barra_notifica)); //
            setListenersTasti(contentView); //
            impostaOggettiDescrittivi(contentView);
            return new NotificationCompat.Builder(VariabiliGlobali.getInstance().getContext(), VariabiliGlobali.getInstance().getCHANNEL_ID())
                    .setContentTitle("Cambia la carta")
                    .setContentText("")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setContent(contentView) //
                    .setAutoCancel(false) //
                    .build();
        } else {
            return null;
        }
    }

    private void setListenersTasti(RemoteViews view){
        Utility u=new Utility();
        view.setTextViewText(R.id.TextView01, u.ControllaLingua(VariabiliGlobali.getInstance().getContext(), R.string.app_name, R.string.app_nameEN));

        Intent cambia=new Intent(VariabiliGlobali.getInstance().getContext(), PassaggioNotifica.class);
        cambia.putExtra("DO", "cambia");
        PendingIntent pCambia = PendingIntent.getActivity(VariabiliGlobali.getInstance().getContext(), 0, cambia, 0);
        view.setOnClickPendingIntent(R.id.imgCambia, pCambia);
        view.setImageViewResource(R.id.imgCambia, R.drawable.refresh);

        Intent avanti=new Intent(VariabiliGlobali.getInstance().getContext(), PassaggioNotifica.class);
        avanti.putExtra("DO", "avanti");
        PendingIntent pAvanti = PendingIntent.getActivity(VariabiliGlobali.getInstance().getContext(), 1, avanti, 0);
        view.setOnClickPendingIntent(R.id.ImgAvanti, pAvanti);
        view.setImageViewResource(R.id.ImgAvanti, R.drawable.avanti);

        Intent apre=new Intent(VariabiliGlobali.getInstance().getContext(), PassaggioNotifica.class);
        apre.putExtra("DO", "apre");
        PendingIntent pApre= PendingIntent.getActivity(VariabiliGlobali.getInstance().getContext(), 2, apre, 0);
        view.setOnClickPendingIntent(R.id.ImgApreNB, pApre);
    }

    /* private void setListenersTasti(RemoteViews view){
        Utility u=new Utility();
        view.setTextViewText(R.id.TextView01, u.ControllaLingua(VariabiliGlobali.getInstance().getContext(), R.string.app_name, R.string.app_nameEN));

        Intent cambia=new Intent(VariabiliGlobali.getInstance().getContext(), PassaggioNotifica.class);
        cambia.putExtra("DO", "cambia");
        PendingIntent pCambia = PendingIntent.getActivity(VariabiliGlobali.getInstance().getContext(), 0, cambia, 0);
        view.setOnClickPendingIntent(R.id.imgCambia, pCambia);
        view.setImageViewResource(R.id.imgCambia, R.drawable.refresh);

        Intent avanti=new Intent(VariabiliGlobali.getInstance().getContext(), PassaggioNotifica.class);
        avanti.putExtra("DO", "avanti");
        PendingIntent pAvanti = PendingIntent.getActivity(VariabiliGlobali.getInstance().getContext(), 1, avanti, 0);
        view.setOnClickPendingIntent(R.id.ImgAvanti, pAvanti);
        view.setImageViewResource(R.id.ImgAvanti, R.drawable.avanti);

        Intent apre=new Intent(VariabiliGlobali.getInstance().getContext(), PassaggioNotifica.class);
        apre.putExtra("DO", "apre");
        PendingIntent pApre= PendingIntent.getActivity(VariabiliGlobali.getInstance().getContext(), 2, apre, 0);
        view.setOnClickPendingIntent(R.id.ImgApreNB, pApre);
    } */

    /* public void CreaNotificaNuova() {
        notificationManager = ((NotificationManager)
                VariabiliGlobali.getInstance().getActivityPrincipale().getSystemService(NOTIFICATION_SERVICE));
        notificationBuilder = (new NotificationCompat.Builder(VariabiliGlobali.getInstance().getContext(), VariabiliGlobali.getInstance().getCHANNEL_ID()));
        notificationBuilder.setSmallIcon(R.drawable.ic_launcher);
        notificationBuilder.setOngoing(true);
        contentView = (new RemoteViews(VariabiliGlobali.getInstance().getActivityPrincipale().getPackageName(),
                R.layout.barra_notifica));
        // setListenersTasti(contentView);
        notificationBuilder.setContent(contentView);
        notificationBuilder.setAutoCancel(false);
        // impostaOggettiDescrittivi(contentView);
        notificationManager.notify(1, notificationBuilder.build());
    } */

    public void CreaNotifica() {
        // notificationManager.notify(1, notificationBuilder.build());
    }

    public void AggiornaNotifica() {
        if (SharedObjects.getInstance().getVecchioValoreCambio() != "") {
            SharedObjects.getInstance().setTipoCambio(SharedObjects.getInstance().getVecchioValoreCambio());
            SharedObjects.getInstance().setVecchioValoreCambio("");
        }

        // try {
            // setListeners(contentView);
            // RemoteViews contentView = (new RemoteViews(VariabiliGlobali.getInstance().getActivityPrincipale().getPackageName(), //
            //         R.layout.barra_notifica)); //
            Notification notification = CreaNotifichella();
            // impostaOggettiDescrittivi(contentView);
            NotificationManager mNotificationManager=(NotificationManager) VariabiliGlobali.getInstance().getActivityPrincipale()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, notification);
            // notificationBuilder.setContent(contentView);
            // notificationBuilder.setOngoing(true);
            // notificationManager.notify(id, notificationBuilder.build());
        // } catch (Exception ignored) {

        // }
    }

    public void RimuoviNotifica() {
        /* try {
            notificationManager.cancel(1);
            notificationManager = null;
        } catch (Exception ignored) {

        } */
    }

    /* public void AggiornaNotifica() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            try {
                setListeners(VariabiliGlobali.getInstance().getContentView());
                VariabiliGlobali.getInstance().getNotificationBuilder().setContent(VariabiliGlobali.getInstance().getContentView());
                VariabiliGlobali.getInstance().getNotificationBuilder().setOngoing(true);
                VariabiliGlobali.getInstance().getNotificationManager().notify(1, VariabiliGlobali.getInstance().getNotificationBuilder().build());
            } catch (Exception ignored) {

            }
        } else {
            CreaNotifica();
        }
    }

    private void setListenersTasti(RemoteViews view){
        Utility u=new Utility();
        view.setTextViewText(R.id.TextView01, u.ControllaLingua(VariabiliGlobali.getInstance().getContext(), R.string.app_name, R.string.app_nameEN));

        Intent cambia=new Intent(VariabiliGlobali.getInstance().getContext(), PassaggioNotifica.class);
        cambia.putExtra("DO", "cambia");
        PendingIntent pCambia = PendingIntent.getActivity(VariabiliGlobali.getInstance().getContext(), 0, cambia, 0);
        view.setOnClickPendingIntent(R.id.imgCambia, pCambia);
        view.setImageViewResource(R.id.imgCambia, R.drawable.refresh);

        Intent avanti=new Intent(VariabiliGlobali.getInstance().getContext(), PassaggioNotifica.class);
        avanti.putExtra("DO", "avanti");
        PendingIntent pAvanti = PendingIntent.getActivity(VariabiliGlobali.getInstance().getContext(), 1, avanti, 0);
        view.setOnClickPendingIntent(R.id.ImgAvanti, pAvanti);
        view.setImageViewResource(R.id.ImgAvanti, R.drawable.avanti);

        Intent apre=new Intent(VariabiliGlobali.getInstance().getContext(), PassaggioNotifica.class);
        apre.putExtra("DO", "apre");
        PendingIntent pApre= PendingIntent.getActivity(VariabiliGlobali.getInstance().getContext(), 2, apre, 0);
        view.setOnClickPendingIntent(R.id.ImgApreNB, pApre);
    }

    public void CreaNotifica() {
        // if (SharedObjects.getInstance().getNotificaSiNo().equals("S")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                VariabiliGlobali.getInstance().setNotificationManager((NotificationManager)
                        VariabiliGlobali.getInstance().getActivityPrincipale().getSystemService(NOTIFICATION_SERVICE));
                VariabiliGlobali.getInstance().setNotificationBuilder(new NotificationCompat.Builder(VariabiliGlobali.getInstance().getContext()));
                VariabiliGlobali.getInstance().getNotificationBuilder().setSmallIcon(R.drawable.ic_launcher);
                VariabiliGlobali.getInstance().getNotificationBuilder().setOngoing(true);

                VariabiliGlobali.getInstance().setContentView(new RemoteViews(VariabiliGlobali.getInstance().getActivityPrincipale().getPackageName(),
                        R.layout.barra_notifica));
                setListenersTasti(VariabiliGlobali.getInstance().getContentView());

                VariabiliGlobali.getInstance().getNotificationBuilder().setContent(VariabiliGlobali.getInstance().getContentView());
                VariabiliGlobali.getInstance().getNotificationBuilder().setAutoCancel(false);

                VariabiliGlobali.getInstance().getNotificationManager().notify(1, VariabiliGlobali.getInstance().getNotificationBuilder().build());
            } else {
                Utility u=new Utility();
                String Titolo=u.ControllaLingua(VariabiliGlobali.getInstance().getContext(), R.string.app_name, R.string.app_nameEN);
                String Titolo2=u.ControllaLingua(VariabiliGlobali.getInstance().getContext(), R.string.clickIT, R.string.clickEN);

                Intent notificationIntent = new Intent(VariabiliGlobali.getInstance().getContext(), MainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(VariabiliGlobali.getInstance().getContext(), 0, notificationIntent, 0);

                // PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
                Notification notification =  new NotificationCompat.Builder(VariabiliGlobali.getInstance().getContext()).setAutoCancel(false)
                        .setContentTitle(Titolo)
                        .setContentText(Titolo2)
                        .setContentIntent(pi)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setWhen(System.currentTimeMillis())
                        .setTicker(Titolo)
                        .build();
                NotificationManager notificationManager =
                        (NotificationManager) VariabiliGlobali.getInstance().getActivityPrincipale().getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(0, notification);
            }
        // }
    }

    public void impostaOggettiDescrittivi(RemoteViews view){
        Log l = new Log();
        Utility u=new Utility();
        if (SharedObjects.getInstance().getNotificaSiNo().equals("S")) {
            String Immagine="";

            if (!SharedObjects.getInstance().getTipoCambio().equals("SINCRONIZZATA")) {
                GestioneFilesCartelle gfc = new GestioneFilesCartelle();
                try {
                    Immagine = gfc.PrendeNomeFile(SharedObjects.getInstance().getListaImmagini().get(SharedObjects.getInstance().getQualeImmagineHaVisualizzato()));
                    int pos = Immagine.indexOf(".");
                    if (pos > -1) {
                        Immagine = Immagine.substring(0, pos);
                    }
                } catch (Exception ignored) {

                }
            } else {
                Immagine = SharedObjects.getInstance().getPathUltimaImaggineDL();
            }
            if (!Immagine.equals("")) {
                view.setTextViewText(R.id.txtImmagine, Immagine);
            } else {
                view.setTextViewText(R.id.txtImmagine, "");
            }
            view.setTextViewText(R.id.TextView01, u.ControllaLingua(VariabiliGlobali.getInstance().getContext(),
                    R.string.app_name, R.string.app_nameEN));

            if (!SharedObjects.getInstance().getTipoCambio().equals("SINCRONIZZATA")) {
                Immagine = SharedObjects.getInstance().getListaImmagini().get(SharedObjects.getInstance().getQualeImmagineHaVisualizzato());
            } else {
                Immagine = SharedObjects.getInstance().getPercorsoDIR() + "/Download.jpg";
            }
            Bitmap Immagin=null;

            view.setViewVisibility(R.id.imgCopertinaM, LinearLayout.VISIBLE);
            view.setViewVisibility(R.id.txtImmagine, LinearLayout.VISIBLE);
            // view.setViewVisibility(R.id.layPicture, LinearLayout.VISIBLE);

            if (Immagine!=null && !Immagine.equals("")) {
                try {
                    Immagin=u.PrendeImmagineCompressa(Immagine, l);
                    view.setImageViewBitmap(R.id.imgCopertinaM, Immagin);
                } catch (Exception ignored) {
                    view.setImageViewResource(R.id.imgCopertinaM, R.drawable.ic_launcher);
                }
            } else {
                view.setImageViewResource(R.id.imgCopertinaM, R.drawable.ic_launcher);
            }
        } else {
            view.setTextViewText(R.id.txtImmagine, u.ControllaLingua(VariabiliGlobali.getInstance().getContext(),
                    R.string.app_name, R.string.app_nameEN));

            view.setViewVisibility(R.id.imgCopertinaM, LinearLayout.GONE);
            view.setViewVisibility(R.id.TextView01, LinearLayout.GONE);
            // view.setViewVisibility(R.id.layPicture, LinearLayout.GONE);
        }
    }

    public void RimuoviNotifica() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            try {
                VariabiliGlobali.getInstance().getNotificationManager().cancel(1);
                VariabiliGlobali.getInstance().setNotificationManager(null);
            } catch (Exception ignored) {

            }
        }
    }
    */
}

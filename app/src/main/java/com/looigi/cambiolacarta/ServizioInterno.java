package com.looigi.cambiolacarta;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.app.Notification;
import android.os.IBinder;
import android.os.Build;
// import android.app.NotificationChannel;
import android.support.v7.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.HashMap;

public class ServizioInterno extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannelS2";
    protected Activity v;
    private Handler mHandler;
    private Intent inte;
    private Utility u = new Utility();

    @Override
    public void onCreate() {
        super.onCreate();

        partenza();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null) {
            inte = intent;
        }
        // partenza();
        // return START_NOT_STICKY;

        return START_STICKY; // START_REDELIVER_INTENT;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    private void partenza() {
        // String input = inte.getStringExtra("inputExtra2");

        /* creaStrutturaInizio(); */

        /* Notifiche.getInstance().createNotificationChannel();
        Notification notification = Notifiche.getInstance().CreaNotifichella();
        startForeground(1, notification); */

        /* if (!VariabiliGlobali.getInstance().isPartito()) {
            // // // VariabiliGlobali.getInstance().getActivityPrincipale().moveTaskToBack(true);
        }

        VariabiliGlobali.getInstance().setPartito(true);

        // String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("CambiaLaCarta")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification); */

        // creaStrutturaInizio();

        u.FaiPartireTimer();
    }

    private void createNotificationChannel() {
        /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "CambiaLaCarta",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        } */
    }

    private Handler handlerAgg;
    private Runnable rAgg;

    @Override
    public void onDestroy() {
        // partenza();
        // startService(inte);

        super.onDestroy();

        // l.ScriveLog(new Object() {
        // }.getClass().getEnclosingMethod().getName(),"Destroy service");

        /* handlerAgg = new Handler();
        rAgg = new Runnable() {
            public void run() {
                stopService(inte);
                startService(inte);
            }
        };
        handlerAgg.postDelayed(rAgg, 1000); */
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log l = new Log();
        l.ScriveLog(new Object() {
                }.getClass().getEnclosingMethod().getName(),
                "onBind servizio");
        return null;
    }
}

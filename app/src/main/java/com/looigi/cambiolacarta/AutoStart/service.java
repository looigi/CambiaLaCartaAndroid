package com.looigi.cambiolacarta.AutoStart;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.looigi.cambiolacarta.MainActivity;

public class service extends Service {
	
	public static Boolean ChiudiMaschera;
	
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressWarnings("deprecation")
	@Override
    public void onStart(Intent intent, int startid) {
        super.onStart(intent, startid);

        Intent intents = new Intent(getBaseContext(), MainActivity.class);
        intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intents);
        
        ChiudiMaschera=true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }
  
}
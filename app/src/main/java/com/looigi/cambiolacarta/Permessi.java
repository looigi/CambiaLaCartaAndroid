package com.looigi.cambiolacarta;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

public class Permessi {
    public boolean ControllaPermessi(Activity context) {
        int permissionRequestCode1 = 1193;

        String[] PERMISSIONS = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                // Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.SET_WALLPAPER,
                Manifest.permission.SET_WALLPAPER_HINTS,
                Manifest.permission.INTERNET,
                Manifest.permission.RECEIVE_BOOT_COMPLETED,
                // Manifest.permission.INSTANT_APP_FOREGROUND_SERVICE
                // android.Manifest.permission.BLUETOOTH,
                // android.Manifest.permission.BLUETOOTH_ADMIN
        };

        if(!hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions(context, PERMISSIONS, permissionRequestCode1);
            return false;
        } else {
            return true;
        }
    }

    private boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

}

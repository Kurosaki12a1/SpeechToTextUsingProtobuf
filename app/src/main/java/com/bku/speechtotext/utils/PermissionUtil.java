package com.bku.speechtotext.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionUtil {
    public static int ALL_PERMISSION_CODE = 1;
    public static String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    public static boolean checkSelfPermission(Activity activity) {
        return checkSelfPermission(activity, PERMISSIONS);
    }
    private static boolean checkSelfPermission(Activity activity, String... permissions) {
        if (activity == null || permissions == null) {
            return false;
        }
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions){
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    public static void requestPermission(Activity activity) {
        requestPermission(activity, PERMISSIONS);
    }
    private static void requestPermission(Activity activity, String... permissions) {
        ActivityCompat.requestPermissions(activity, permissions, 0);
    }
}

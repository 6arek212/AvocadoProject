package com.example.testavocado.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import static com.example.testavocado.EditeInfo.ProfilePhotoUploadFragment.VERIFY_PERMISSIONS_REQUEST;

public class Permissions {
    private static final String TAG = "Permissions";

    public static final String[] PERMISSIONS={
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION

    };


    public static final String CAMERA_PERMISSION=Manifest.permission.CAMERA;
    public static final String WRITE_STORAGE_PERMISSION= Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String READ_STORAGE_PERMISSION= Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String GPS= Manifest.permission.ACCESS_FINE_LOCATION;









    /**
     * verifiy all the permissions passed to the array
     */

    public static void verifyPermission(String[] permissions,Activity activity) {
        Log.d(TAG, "verifyPermission: verfiying permissions");
        ActivityCompat.requestPermissions(activity, permissions, VERIFY_PERMISSIONS_REQUEST);
    }



    public static void verifyOnePermission(String permission,Activity activity) {
        Log.d(TAG, "verifyPermission: verfiying permissions");
        String[] per=new String[1];
        per[0]=permission;
        ActivityCompat.requestPermissions(activity, per, VERIFY_PERMISSIONS_REQUEST);
    }




    /**
     * check all premissions if are granted if not return false
     */

    public static boolean checkPermissionsArray(String[] permissions,Context context) {
        Log.d(TAG, "checkPermissionsArray: checking permission Array");

        for (int i = 0; i < permissions.length; i++) {
            String check = permissions[i];

            if (!checkPermission(check,context)) {
                return false;
            }
        }
        return true;
    }


    //check a single permission if it has been granted or not

    public static boolean checkPermission(String permissions,Context context) {
        Log.d(TAG, "checkPermissionsArray: verifying permission Array  " + permissions);

        int permissionRequest = ActivityCompat.checkSelfPermission(context, permissions);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "verifyPermission: Permission was not granted  for :" + permissions);
            return false;
        }
        Log.d(TAG, "verifyPermission: Permission was granted  for :" + permissions);
        return true;
    }



}

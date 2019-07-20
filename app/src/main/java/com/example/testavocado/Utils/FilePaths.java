package com.example.testavocado.Utils;

import android.content.Context;
import android.os.Environment;

import com.example.testavocado.BaseActivity;

public class FilePaths {


Context context;

    //"storage/emulated/0"
    public String ROOT_DIR= Environment.getExternalStorageDirectory().getPath();


    public String PICTURES=ROOT_DIR+"/Pictures";
    public String CAMERA=ROOT_DIR+"/DCIM/Camera";





    public static String FIREBASE_IMAGE_STORAGE="photos/users/";

}

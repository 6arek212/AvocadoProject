package com.example.testavocado.GalleryAndPicSnap;

import android.graphics.Bitmap;
import android.net.Uri;

public interface onSelectedImageListener {
    public void onSelectedUri(Uri uri);
    public void onSelectedPathImage(String imagePath);
    public void onCancel();
}

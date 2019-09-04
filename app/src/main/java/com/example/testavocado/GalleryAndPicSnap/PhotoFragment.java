package com.example.testavocado.GalleryAndPicSnap;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testavocado.BuildConfig;
import com.example.testavocado.R;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.os.Environment.DIRECTORY_PICTURES;


public class PhotoFragment extends Fragment {
    private static final String TAG = "PhotoFragment";

    //activity result codes
    public static final int PHOTO_FRAGMENT_NUM=1;
    public static final int CAMERA_REQUEST_CODE=5;
    private static final int REQUEST_CAPTURE_IMAGE = 100;



    //widgets
    private ImageView close;
    private  Button btnLaunchCamera;


    //vars
    private onSelectedImageListener onSelectedImageListener;
    private Context mContext;
    private String imageFilePath;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_photo,container,false);
        initWidgets(view);
        return view;
    }


    /**
     *
     *              initializing all the widgets + attaching onClicks
     *
     * @param view
     */
    Uri imageUri;

    private void initWidgets(View view) {
        Log.d(TAG, "initWidgets: inistializing the widgets");
        close=view.findViewById(R.id.close);
        btnLaunchCamera =view.findViewById(R.id.btnOpenCamera);
        mContext=getContext();

        btnLaunchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraIntent();
            }
        });
    }




    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }







    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(mContext.getPackageManager()) != null){
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            if (photoFile != null) {

               Uri photoURI = FileProvider.getUriForFile(mContext,mContext.getPackageName(), photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                startActivityForResult(pictureIntent,
                        REQUEST_CAPTURE_IMAGE);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onActivityResult: "+imageFilePath);
            onSelectedImageListener.onSelectedPathImage(imageFilePath);
        }
        else if(resultCode == Activity.RESULT_CANCELED) {
            // User Cancelled the action
        }
    }

    /**
     *
     *              getting a reference to getaPic activity
     *
     * @param context
     */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onSelectedImageListener=(GetaPicActivity)context;
    }



}

package com.example.testavocado.GalleryAndPicSnap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

import com.example.testavocado.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.DIRECTORY_PICTURES;


public class PhotoFragment extends Fragment {
    private static final String TAG = "PhotoFragment";

    //activity result codes
    public static final int PHOTO_FRAGMENT_NUM=1;
    public static final int CAMERA_REQUEST_CODE=5;



    //widgets
    private ImageView close;
    private  Button btnLaunchCamera;


    //vars
    private onSelectedImageListener onSelectedImageListener;
    private Context mContext;


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
                if(((GetaPicActivity)getActivity()).getCurrentTab()==PHOTO_FRAGMENT_NUM){
                    Log.d(TAG, "onCreateView: starting camera");

                    //"android.media.action.IMAGE_CAPTURE"
                  //  Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                   // startActivityForResult(cameraIntent,CAMERA_REQUEST_CODE);

                    dispatchTakePictureIntent();

                 /*  ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New Picture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                    imageUri = getActivity().getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 55);*/
                }
            }
        });
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





    /**
     *
     *
     *                      checking the result from the camera
     *                      to update getaPic Activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 55)
            if (resultCode == Activity.RESULT_OK) {
                try {
                   /* Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                            getActivity().getContentResolver(), imageUri);
                    String  imageurl = getRealPathFromURI(imageUri);
                    */


                    onSelectedImageListener.onSelectedPathImage(currentPhotoPath);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "onActivityResult: error getting bitmap"+e.getMessage() );
                }

            }


        if(requestCode==CAMERA_REQUEST_CODE && data!=null){
            Log.d(TAG, "onActivityResult: navigating to home activity");


            /*Bitmap bitmap;
            bitmap=(Bitmap) data.getExtras().get("data");

            onSelectedImageListener.onSelectedBitmapImage(bitmap);*/



        }

    }


    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }






    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, "dispatchTakePictureIntent: IOException"+ex.getMessage() );

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(mContext,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }






    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir =Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

}

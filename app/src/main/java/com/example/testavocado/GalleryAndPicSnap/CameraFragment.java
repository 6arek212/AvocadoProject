package com.example.testavocado.GalleryAndPicSnap;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.testavocado.R;
import com.google.android.cameraview.CameraView;
import com.google.android.cameraview.CameraViewImpl;

import java.io.File;
import java.io.FileOutputStream;

public class CameraFragment extends Fragment {
    private static final String TAG = "CameraFragment";


    CameraView cameraView;
    Button pic,a1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_camra,container,false);

        cameraView=view.findViewById(R.id.camera_view);
        pic=view.findViewById(R.id.m1);
        a1=view.findViewById(R.id.a1);




        return view;
    }




    private  void aa (){


        cameraView.setOnFocusLockedListener(new CameraViewImpl.OnFocusLockedListener() {
            @Override
            public void onFocusLocked() {
                //playShutterAnimation();
            }
        });

        cameraView.setOnPictureTakenListener(new CameraViewImpl.OnPictureTakenListener() {
            @Override
            public void onPictureTaken(Bitmap bitmap, int rotationDegrees) {

                Log.d(TAG, "onPictureTaken: ");

                Matrix matrix = new Matrix();
                matrix.postRotate(-rotationDegrees);
                Bitmap bitmap1= Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);



                //

                // saveImage(bitmap1,"Aaaa");


                CapturePhotoUtils.insertImage(getActivity().getContentResolver(),bitmap1,"aaa","Asd");

                Toast.makeText(getContext(), "pic", Toast.LENGTH_SHORT).show();

                // startSavingPhoto(bitmap, int rotationDegrees);
            }
        });


        cameraView.setOnTurnCameraFailListener(new CameraViewImpl.OnTurnCameraFailListener() {
            @Override
            public void onTurnCameraFail(Exception e) {
                Log.d(TAG, "onTurnCameraFail: ");
                Toast.makeText(getContext(), "Switch Camera Failed. Does you device has a front camera?",
                        Toast.LENGTH_SHORT).show();
            }
        });

        cameraView.setOnCameraErrorListener(new CameraViewImpl.OnCameraErrorListener() {
            @Override
            public void onCameraError(Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.takePicture();

            }
        });

        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.switchCamera();

            }
        });
    }



    private void saveImage(Bitmap finalBitmap, String image_name) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = "Image-" + image_name+ ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraView.start();
        aa();
    }

    @Override
    public void onPause() {
        cameraView.stop();
        super.onPause();
    }
}

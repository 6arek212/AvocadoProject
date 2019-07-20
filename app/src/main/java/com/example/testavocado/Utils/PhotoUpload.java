package com.example.testavocado.Utils;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import android.util.Log;

import com.example.testavocado.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PhotoUpload {
    private static final String TAG = "PhotoUpload";

    public interface OnUploadingPostListener {
        public void onSuccessListener(String ImageUrl);

        public void onFailureListener(String ex);
    }


    public static void uploadNewPhoto(String photoType, String datetime, byte[] bytes, int user_id, final Context mContext, final OnUploadingPostListener listener) {
        Log.d(TAG, "uploadNewPhoto: uploading new photo ");


        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();


        //case 1: new photo
        final StorageReference storageReference;
        if (photoType.equals(mContext.getString(R.string.new_photo))) {
            Log.d(TAG, "uploadNewPhoto: uploading new photo");

            storageReference = mStorageReference
                    .child(FilePaths.FIREBASE_IMAGE_STORAGE + "/" + user_id + "/photo" + datetime);
        } else {
            storageReference = mStorageReference
                    .child(FilePaths.FIREBASE_IMAGE_STORAGE + "/" + user_id + "/ProfilePhoto");
        }


        UploadTask uploadTask = storageReference.putBytes(bytes);

        //add file on Firebase and got Download Link


        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageReference.getDownloadUrl();
            }
        })
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();

                            Log.d(TAG, "onSuccess: firebaseUrl : " + downloadUri.toString());

                            listener.onSuccessListener(downloadUri.toString());

                        } else {
                            // Handle failures
                            listener.onFailureListener(task.getException().toString());
                            // ...
                        }
                    }
                });

    }








    public interface OnUploadingPostListener2 {
        public void onSuccessListener(String ImageUrl);

        public void onFailureListener(String ex);
    }



    public static void uploadNewPhotoFirebase(String photoType, String datetime, Uri uri, int user_id,
                                              final Context mContext, final OnUploadingPostListener2 listener) {


        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();


        //case 1: new photo
        final StorageReference storageReference;
        if (photoType.equals(mContext.getString(R.string.new_photo))) {
            Log.d(TAG, "uploadNewPhoto: uploading new photo");

            storageReference = mStorageReference
                    .child(FilePaths.FIREBASE_IMAGE_STORAGE + "/" + user_id + "/photo" + datetime);
        } else {
            storageReference = mStorageReference
                    .child(FilePaths.FIREBASE_IMAGE_STORAGE + "/" + user_id + "/ProfilePhoto");
        }



/*
// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...

            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful())
                    String s = task.getResult().toString();
            }
        });*/

            UploadTask uploadTask = storageReference.putFile(uri);


            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Log.d(TAG, "onComplete: " + downloadUri);
                        listener.onSuccessListener(downloadUri.toString());
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });


    }


}

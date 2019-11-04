package com.example.testavocado.Utils;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import android.util.Log;

import com.example.testavocado.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PhotoUpload {
    private static final String TAG = "PhotoUpload";

    public interface OnUploadingPostListener {
        public void onSuccessListener(String ImageUrl);

        public void onFailureListener(String ex);
    }


    public static void uploadNewPhoto(final String photoType, String datetime, byte[] bytes, final int user_id, final Context mContext, final OnUploadingPostListener listener) {
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

                            Log.d(TAG, "onSuccess: firebaseUrl : " + downloadUri.toString()+" type "+photoType );
                            if (photoType.equals(mContext.getString(R.string.user_profile_photo))){
                                DatabaseReference ds= FirebaseDatabase.getInstance().getReference();
                                ds.child("users").child(String.valueOf(user_id)).child("profilePic").setValue(downloadUri.toString());
                            }
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



    public static void uploadNewPhotoFirebase(final String photoType, String datetime, Uri uri,final int user_id,
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
                        if (photoType.equals(mContext.getString(R.string.user_profile_photo))){
                            DatabaseReference ds= FirebaseDatabase.getInstance().getReference();
                            ds.child("users").child(String.valueOf(user_id)).child("profilePic").setValue(downloadUri.toString());
                        }
                    } else {
                        // Handle failures
                        listener.onFailureListener(task.getException().toString());
                        // ...
                    }
                }
            });


    }


}

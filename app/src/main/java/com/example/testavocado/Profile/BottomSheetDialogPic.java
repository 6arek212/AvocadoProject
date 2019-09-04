package com.example.testavocado.Profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.testavocado.BaseActivity;
import com.example.testavocado.EditeInfo.InfoMethodsHandler;
import com.example.testavocado.GalleryAndPicSnap.GetaPicActivity;
import com.example.testavocado.Home.AddNewPostActivity;
import com.example.testavocado.Home.PostMethods;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.ImageFragment;
import com.example.testavocado.Utils.Permissions;
import com.example.testavocado.Utils.PhotoUpload;
import com.example.testavocado.Utils.TimeMethods;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.example.testavocado.EditeInfo.ProfilePhotoUploadFragment.PHOTO_CODE;

public class BottomSheetDialogPic extends BottomSheetDialogFragment {
    private static final String TAG = "BottomSheetDialog";


    public interface OnChangeProfilePicListener {
        void onChange(String imageUrl);
    }



    public void setOnChangeProfilePicListner(OnChangeProfilePicListener onChangeProfilePicListner) {
        this.onChangeProfilePicListner = onChangeProfilePicListner;
    }



    //interface
    public OnChangeProfilePicListener onChangeProfilePicListner;




    //widgets
    TextView mChangePic, mShowPic;
    //vars
    private Context mContext;
    private String[] permission;
    private int current_user_id;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_profilepic_option_bottomsheet, container, false);

        current_user_id = HelpMethods.checkSharedPreferencesForUserId(getContext());
        initWidgets(view);




        return view;
    }


    private void initWidgets(View view) {
        mContext=getContext();
        mChangePic = view.findViewById(R.id.changePic);
        mShowPic = view.findViewById(R.id.showPic);
        permission = new String[]{Permissions.CAMERA_PERMISSION, Permissions.READ_STORAGE_PERMISSION, Permissions.WRITE_STORAGE_PERMISSION};


        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();

                switch (id) {
                    case R.id.changePic:
                        if (Permissions.checkPermissionsArray(permission, mContext)) {
                            startActivityForResult(new Intent(mContext, GetaPicActivity.class), PHOTO_CODE);

                        } else {

                            Permissions.verifyPermission(permission, (BaseActivity)mContext);
                        }
                        break;

                    case R.id.showPic:
                        showPic();
                        break;

                }
            }
        };


        mChangePic.setOnClickListener(clickListener);
        mShowPic.setOnClickListener(clickListener);
    }



    private void showPic(){
        ImageFragment imageFragment=new ImageFragment();
        imageFragment.imageUrl=HelpMethods.get_user_profile_pic_sharedprefernces(mContext);
        FragmentManager fr=getFragmentManager();
        FragmentTransaction tr=fr.beginTransaction();
        tr.addToBackStack(getString(R.string.image_fragment)).replace(R.id.baseLayout,imageFragment).commit();
        dismiss();
    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                dismiss();

                if (data.getExtras().get(getString(R.string.imagePath)) != null) {
                    String imagePath = data.getExtras().getString(getString(R.string.imagePath));
                    Log.d(TAG, "onActivityResult: got image path " + imagePath);

                    Uri uri = Uri.fromFile(new File(imagePath));

                    PhotoUpload.uploadNewPhotoFirebase(getString(R.string.user_profile_photo), TimeMethods.getUTCdatetimeAsStringFormat2(), uri, current_user_id, mContext, new PhotoUpload.OnUploadingPostListener2() {
                        @Override
                        public void onSuccessListener(final String ImageUrl) {
                            Log.d(TAG, "onSuccessListener: uploading succcess ");

                            InfoMethodsHandler.updateProfileImage(current_user_id, ImageUrl, new InfoMethodsHandler.OnUpdateListener() {
                                @Override
                                public void onSuccessListener() {
                                    Log.d(TAG, "onSuccessListener: profile photo updated ");
                                    onChangeProfilePicListner.onChange(ImageUrl);
                                }

                                @Override
                                public void onServerException(String ex) {
                                    Log.d(TAG, "onServerException: "+ex);

                                }

                                @Override
                                public void onFailureListener(String ex) {
                                    Log.d(TAG, "onFailureListener: "+ex);

                                }
                            });


                        }

                        @Override
                        public void onFailureListener(String ex) {
                            Log.d(TAG, "onFailureListener: failed uploading photo  " + ex);

                        }
                    });

                }

            }
        }
    }






}

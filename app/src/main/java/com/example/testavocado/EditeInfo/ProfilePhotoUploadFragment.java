package com.example.testavocado.EditeInfo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testavocado.GalleryAndPicSnap.GetaPicActivity;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.ImageManager;
import com.example.testavocado.Utils.Permissions;
import com.example.testavocado.Utils.PhotoUpload;
import com.example.testavocado.Utils.TimeMethods;

import static android.app.Activity.RESULT_OK;

public class ProfilePhotoUploadFragment extends Fragment implements FragmentLifecycle {
    private static final String TAG = "ProfilePhotoUploadFragm";


    @Override
    public void showArrow() {
        backArrow.setVisibility(View.VISIBLE);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickNextListener.onClickArrow();
            }
        });
    }

    @Override
    public void onFirstPage() {
        backArrow.setVisibility(View.GONE);
    }

    @Override
    public void onLastPageSelected() {
        mNext.setText("finish");
    }


    //Const
    public static int PHOTO_CODE = 22;
    public static final int VERIFY_PERMISSIONS_REQUEST = 1;


    //widgets
    private Button mNext, mPermission;
    private ImageView backArrow, mProfilePic;
    private TextView editProfilePic;


    //vars
    private onClickNextListener onClickNextListener;
    private String mAPPEND = "file:/";
    private int selectedImageType;
    private Bitmap selectedBitMap;
    private String selectedImagePath;
    private byte[] mSelectedImage;
    private boolean image_is_selected;
    private int user_id;
    private Context mContext;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_photo_upload, container, false);

        initWidgets(view);
        return view;
    }


    private void initWidgets(View view) {
        mNext = view.findViewById(R.id.btnAction);
        backArrow = view.findViewById(R.id.backArror);
        mPermission = view.findViewById(R.id.btnPermisson);
        mProfilePic = view.findViewById(R.id.profileImage);
        editProfilePic = view.findViewById(R.id.editProfileImage);
        mContext=getContext();
        user_id=HelpMethods.checkSharedPreferencesForUserId(mContext);


        if (BaseProfileEditActivity.position == 0)
            backArrow.setVisibility(View.GONE);





        attachingOnClicks();
    }


    /**
     * type 0- bitmap
     * type 1- uri / file
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");

        if (requestCode == PHOTO_CODE) {
            if (resultCode == RESULT_OK && data != null) {

                if (data.getExtras().get(getString(R.string.imagePath)) != null) {

                    String imageUrl = data.getStringExtra(getString(R.string.imagePath));
                    Log.d(TAG, "onActivityResult: got image path " + imageUrl);

                    Glide.with(mContext)
                            .load(imageUrl)
                            .centerCrop()
                            .error(R.drawable.error)
                            .into(mProfilePic);

                    selectedImageType = 1;
                    selectedImagePath = imageUrl;

                    mSelectedImage = ImageManager.getBytesFromBitmap(ImageManager.getBitmap(imageUrl), 50);
                    image_is_selected = true;

                } else if (data.getExtras().get(getString(R.string.bitmap)) != null) {
                    Log.d(TAG, "onActivityResult: got bitmap  ");

                    Bitmap bitmap = (Bitmap) data.getExtras().get(getString(R.string.bitmap));
                    mProfilePic.setImageBitmap(bitmap);
                    selectedImageType = 0;
                    selectedBitMap = bitmap;
                }

                //TODO UPLOADING THE PHOTO
            }
        }
    }




    /**
     * attaching on click listeners for all the widgets that need it
     */

    private void attachingOnClicks() {
        mProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        editProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissionsArray(Permissions.PERMISSIONS)) {
                    Intent intent = new Intent(getContext(), GetaPicActivity.class);
                    startActivityForResult(intent, PHOTO_CODE);

                } else {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.permission), Snackbar.LENGTH_SHORT).show();
                    verifyPermission(Permissions.PERMISSIONS);
                }
            }
        });


        mPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissionsArray(Permissions.PERMISSIONS)) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.permission), Snackbar.LENGTH_SHORT).show();

                } else {
                    verifyPermission(Permissions.PERMISSIONS);
                }
            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (image_is_selected)
                    PhotoUpload.uploadNewPhoto("profile", TimeMethods.getUTCdatetimeAsStringFormat2(), mSelectedImage, user_id, mContext, new PhotoUpload.OnUploadingPostListener() {
                        @Override
                        public void onSuccessListener(String ImageUrl) {
                            InfoMethodsHandler.updateProfileImage(user_id, ImageUrl, new InfoMethodsHandler.OnUpdateListener() {
                                @Override
                                public void onSuccessListener() {
                                    Toast.makeText(mContext, "Profile photo updated", Toast.LENGTH_SHORT).show();
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
                            Log.d(TAG, "onFailureListener: "+ex);

                        }
                    });

                onClickNextListener.onClickNext();
            }
        });
    }


    /**
     * verifiy all the permissions passed to the array
     */

    public void verifyPermission(String[] permissions) {
        Log.d(TAG, "verifyPermission: verfiying permissions");
        ActivityCompat.requestPermissions(getActivity(), permissions, VERIFY_PERMISSIONS_REQUEST);
    }


    /**
     * check all premissions if are granted if not return false
     */

    private boolean checkPermissionsArray(String[] permissions) {
        Log.d(TAG, "checkPermissionsArray: checking permission Array");

        for (int i = 0; i < permissions.length; i++) {
            String check = permissions[i];

            if (!checkPermission(check)) {
                return false;
            }
        }
        return true;
    }


    //check a single permission if it has been granted or not

    public boolean checkPermission(String permissions) {
        Log.d(TAG, "checkPermissionsArray: verifying permission Array  " + permissions);

        int permissionRequest = ActivityCompat.checkSelfPermission(getContext(), permissions);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "verifyPermission: Permission was not granted  for :" + permissions);
            return false;
        }
        Log.d(TAG, "verifyPermission: Permission was granted  for :" + permissions);
        return true;
    }


    /**
     * getting the activity referenecce for updating the viewpager
     *
     * @param context
     */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onClickNextListener = (com.example.testavocado.EditeInfo.onClickNextListener) context;

        } catch (ClassCastException e) {
            Log.d(TAG, "onAttach: error ClassCastException " + e.getMessage());
        }
    }

}

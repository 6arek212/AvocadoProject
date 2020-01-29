package com.example.testavocado.GalleryAndPicSnap;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.testavocado.R;
import com.example.testavocado.Utils.FilePaths;
import com.example.testavocado.Utils.FileSearch;
import com.example.testavocado.Utils.GridImageAdapter;
import com.example.testavocado.Utils.Permissions;


import java.util.ArrayList;

public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";

    //Const
    private static final int NUM_GRID_CLUM = 3;

    //widgets
    private GridView gridView;
    private ImageView galleryImage,close,done;
    private Spinner directorySpinner;

    //vars
    private ArrayList<String> directories;
    private String mAPPEND = "file:/";
    private String mSelectedImage;
    private onSelectedImageListener onSelectedImageListener;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        mContext=getContext();
        initWidgets(view);
        init();



        return view;
    }


    /**
     *
     *          initializing all the widgets + attaching onClick
     *
     * @param view
     */

    private void initWidgets(View view) {
        gridView = view.findViewById(R.id.gridView);
        galleryImage = view.findViewById(R.id.galleryImageView);
        close=view.findViewById(R.id.close);
        done=view.findViewById(R.id.done);
        directorySpinner = view.findViewById(R.id.spinnerDirectory);
        directories = new ArrayList<>();


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectedImageListener.onSelectedPathImage(mSelectedImage);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the gallery fragment");
                onSelectedImageListener.onCancel();
            }
        });
    }


    /**
     *              getting the getaPic activity reference
     *
     * @param context
     */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            onSelectedImageListener=(GetaPicActivity)context;
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException "+e.getMessage() );
        }
    }


    /**
     *
     *          getting the filePaths and initializing the spinner
     *
     *
     */

    private void init() {
        if (!isPermissonGranted()){
            return;
        }

        FilePaths filePaths = new FilePaths();


        //check for other folders inside "/storage/emulated/0/pictures"

        if (FileSearch.getDirectoryPaths(filePaths.PICTURES) != null)
            directories = FileSearch.getDirectoryPaths(filePaths.PICTURES);

        directories.add(filePaths.CAMERA);

        ArrayList<String> direcotryNames = new ArrayList<>();

        for (int i = 0; i < directories.size(); i++) {

            int index = directories.get(i).lastIndexOf("/")+1;
            String string = directories.get(i).substring(index);
            direcotryNames.add(string);
        }


      //  direcotryNames.addAll(getAllShownImagesPath(getActivity()));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, direcotryNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        directorySpinner.setAdapter(arrayAdapter);

        directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick:  selected   :" + directories.get(position));
                setupGridView(directories.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }




    private boolean isPermissonGranted(){
        String[] permission = new String[]{Permissions.CAMERA_PERMISSION, Permissions.READ_STORAGE_PERMISSION, Permissions.WRITE_STORAGE_PERMISSION};
        if (!Permissions.checkPermissionsArray(permission, mContext)) {
            Permissions.verifyPermission(permission, getActivity());
            return false;
        }
        return true;
    }


    /**
     * Getting All Images Path.
     *
     * @param activity
     *            the activity
     * @return ArrayList with images Path
     */
    private ArrayList<String> getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }





    /**
     *
     *
     *          setting up the gridView
     *
     * @param selectedDirectory
     */
    private void setupGridView(String selectedDirectory) {
        Log.d(TAG, "setupGridView: directory choosen  " + selectedDirectory);

        final ArrayList<String> imgURLs = FileSearch.getFilePaths(selectedDirectory);

        //set grid column width
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth / NUM_GRID_CLUM;
        gridView.setColumnWidth(imageWidth);

        //use the grid adapter to adapte the images to the gridview
        GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_imageview, mAPPEND, imgURLs);
        gridView.setAdapter(adapter);

        imgURLs.addAll(getAllShownImagesPath(getActivity()));

        //set the first image to be displayed
        try{
            setImage(imgURLs.get(0), galleryImage, mAPPEND);
            mSelectedImage=imgURLs.get(0);
        }
        catch (IndexOutOfBoundsException e){
            Log.e(TAG, "setupGridView: ArrayIndexOutOfBoundsException" + e.getMessage() );
            galleryImage.setImageResource(0);
        }



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: selected an image  :" + imgURLs.get(position));

                setImage(imgURLs.get(position), galleryImage, mAPPEND);
                mSelectedImage=imgURLs.get(position);
            }
        });

    }





    /**
     *               setting the images with the ImageLoader with progressBar
     * @param imageUrl
     * @param imageView
     * @param append
     */
    private void setImage(String imageUrl, ImageView imageView, String append) {
        Log.d(TAG, "setImage: setting image");


        Glide.with(mContext)
                .load(imageUrl)
                .centerCrop()
                .error(R.drawable.error)
                .placeholder(R.drawable.loading_img)
                .into(imageView);
    }

}

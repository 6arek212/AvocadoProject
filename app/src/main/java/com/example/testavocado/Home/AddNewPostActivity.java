package com.example.testavocado.Home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.example.testavocado.Models.Setting;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testavocado.GalleryAndPicSnap.GetaPicActivity;
import com.example.testavocado.Models.Post;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.NetworkClient;
import com.example.testavocado.Utils.Permissions;
import com.example.testavocado.Utils.PhotoUpload;
import com.example.testavocado.Utils.PostTypes;
import com.example.testavocado.Utils.TimeMethods;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.testavocado.EditeInfo.ProfilePhotoUploadFragment.PHOTO_CODE;

public class AddNewPostActivity extends AppCompatActivity {
    private static final String TAG = "AddNewPostActivity";


    //widgets
    private CircleImageView mProfilePhoto;
    private ImageView mClose;
    private TextView mWhatOnYourMind, mAddPic;
    private EditText mPostText;
    private Spinner mPostType;
    private ImageButton mPost;
    private ViewPager mViewPager;
    private TabLayout tablayout;
    private TextInputLayout input_counter;


    //vars
    private Context mContext;
    private String[] permission;
    private boolean is_image_seclected = false;
    int user_id;


    private HorizotalRecyclerImageSlide adap;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    int index;
    private ProgressBar mProgressBar;
    private List<String> imageUrls=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: stated");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_post);

        initWidgets();
    }


    /**
     * initializing all the widgets + attaching the onClicks
     */

    private void initWidgets() {
        mProfilePhoto = findViewById(R.id.profileImage);
        mClose = findViewById(R.id.close);
        mWhatOnYourMind = findViewById(R.id.whatsOnYourMind);
        mAddPic = findViewById(R.id.addPic);
        mProgressBar=findViewById(R.id.progressBar);
        mPostText = findViewById(R.id.postText);
        mPostType = findViewById(R.id.spinner);
        mViewPager = findViewById(R.id.viewPager);
        mContext = AddNewPostActivity.this;
        mPost = findViewById(R.id.sharePost);
        tablayout = findViewById(R.id.tablayout);
        mProgressBar.setVisibility(View.GONE);
        input_counter=findViewById(R.id.input_counter);

        initSpinner();

        Setting setting=HelpMethods.getSharedPreferences(mContext);
        mWhatOnYourMind.setText(getString(R.string.hello) +" "+setting.getUser_first_name()+" "+setting.getUser_last_name()
        +" "+getString(R.string.whatsOnYourMind));

        Glide.with(mContext)
                .load(setting.getProfilePic())
                .centerCrop()
                .error(R.drawable.error)
                .into(mProfilePhoto);


        permission = new String[]{Permissions.CAMERA_PERMISSION, Permissions.READ_STORAGE_PERMISSION, Permissions.WRITE_STORAGE_PERMISSION};
        user_id = HelpMethods.checkSharedPreferencesForUserId(mContext);


        Log.d(TAG, "initWidgets: "+user_id);


        initRecyclerView();


        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String postText;
                postText = mPostText.getText().toString();


                if (postText.trim().isEmpty()) {
                    Toast.makeText(mContext, mContext.getString(R.string.enterText), Toast.LENGTH_SHORT).show();
                } else {
                    final int postType = mPostType.getSelectedItemPosition();
                    mProgressBar.setVisibility(View.VISIBLE);

                    if (!adap.images.isEmpty()) {
                        imageUrls.clear();

                         for(index=0;index<adap.images.size();index++) {
                            Log.d(TAG, "onClick: index "+index);

                           PhotoUpload.uploadNewPhotoFirebase(getString(R.string.new_photo), TimeMethods.getUTCdatetimeAsStringFormat2()+index, adap.images.get(index), user_id, mContext, new PhotoUpload.OnUploadingPostListener2() {
                                @Override
                                public void onSuccessListener(String ImageUrl) {
                                    Log.d(TAG, "onSuccessListener: uploading succcess ");
                                    imageUrls.add(ImageUrl);

                                    Log.d(TAG, "onSuccessListener: publish post "+(adap.images.size()-1)+"   "+(imageUrls.size()-1));

                                    if(adap.images.size()-1==imageUrls.size()-1) {
                                        uploadingPost(postType, postText, imageUrls);
                                    }
                                }

                                @Override
                                public void onFailureListener(String ex) {
                                    Log.d(TAG, "onFailureListener: failed uploading photo  " + ex);

                                }
                            });
                        }
                    } else {
                        uploadingPost(postType, postText, null);
                    }

                }
            }
        });


        mAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Permissions.checkPermissionsArray(permission, mContext)) {
                    startActivityForResult(new Intent(mContext, GetaPicActivity.class), PHOTO_CODE);

                } else {

                    Permissions.verifyPermission(permission, AddNewPostActivity.this);
                }

            }
        });

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initRecyclerView() {
        layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        adap = new HorizotalRecyclerImageSlide(mContext);
        recyclerView.setAdapter(adap);

    }


    public void uploadingPost(int postType, String postText, List<String> imagePaths) {
        Log.d(TAG, "uploadingPost: uploading");
        Post post = new Post();
        post.setUser_id(user_id);
        post.setPost_text(postText);
        post.setPost_images_url(imagePaths);
        post.setPost_date_time(TimeMethods.getUTCdatetimeAsString());
        post.setPost_type(postType);

        PostMethods.addPost(post, new PostMethods.OnAddingNewPostListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "post published", Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
                finish();
            }

            @Override
            public void onServerException(String ex) {
                Log.d(TAG, "onServerException: " + ex);
                Toast.makeText(mContext, getString(R.string.ERROR_TOAST), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(String ex) {
                Log.d(TAG, "onFailure: " + ex);
                Toast.makeText(mContext, getString(R.string.ERROR_TOAST), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");

        if (requestCode == PHOTO_CODE) {
            if (resultCode == RESULT_OK && data != null) {

                if (data.getExtras().get(getString(R.string.imagePath)) != null) {
                    String imagePath = data.getExtras().getString(getString(R.string.imagePath));
                    Log.d(TAG, "onActivityResult: got image path " + imagePath);

                    Uri uri = Uri.fromFile(new File(imagePath));
                    adap.addImage(uri, adap.getItemCount());
                    is_image_seclected = true;

                } else if (data.getExtras().get(getString(R.string.bitmap)) != null) {
                    Log.d(TAG, "onActivityResult: got bitmap  ");
                    Uri uri = data.getData();


                    is_image_seclected = true;

                } else if (data.getExtras().get(getString(R.string.uri)) != null) {
                    Log.d(TAG, "onActivityResult: got bitmap  ");
                    Uri uri = (Uri) data.getExtras().get(getString(R.string.uri));


                    is_image_seclected = true;

                }


            }
        }
    }


    private void uploadToServer(String filePath) {
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        NetworkClient.UploadAPIs uploadAPIs = retrofit.create(NetworkClient.UploadAPIs.class);
        //Create a file object using file path
        File file = new File(filePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("upload", file.getName(), fileReqBody);
        //Create request body with text description and text media type
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
        //

        Call call = uploadAPIs.uploadImage(part, description, 6);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d(TAG, "onResponse: " + response.isSuccessful() + " " + response+"  "+response.body()+"  "+response.message());

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }


    /**
     * initializing the post type spinner
     * <p>
     * filling the spinner with values
     */
    private void initSpinner() {

        ArrayList<String> postTypes = new ArrayList<String>();

        String[] post_types = PostTypes.getPostTypes(mContext);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, post_types);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPostType.setAdapter(arrayAdapter);
    }


}














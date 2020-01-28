package com.example.testavocado.user_login_register;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testavocado.BaseActivity;
import com.example.testavocado.EditeInfo.InfoMethodsHandler;
import com.example.testavocado.GalleryAndPicSnap.GetaPicActivity;
import com.example.testavocado.Home.AddNewPostActivity;
import com.example.testavocado.Models.Setting;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.Permissions;
import com.example.testavocado.Utils.PhotoUpload;
import com.example.testavocado.Utils.TimeMethods;
import com.example.testavocado.methods.add_newuser.add_new_user_method;
import com.example.testavocado.methods.add_newuser.on_add_new_user;
import com.example.testavocado.objects.user;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.example.testavocado.EditeInfo.ProfilePhotoUploadFragment.PHOTO_CODE;


public class register_page1_Fragment extends Fragment {
    public static final String TAG = "register_page1_Fragment";
    private TextView txtv_selectdate;
    private de.hdodenhof.circleimageview.CircleImageView circleImageViewprofilepicture;
    private DatePickerDialog.OnDateSetListener mdateSetListener;
    private Context mContext;
    private RadioGroup mradioGroup;
    private RadioButton radio_btn_male, radio_btn_female;
    private Button btn_register;
    private View view;
    private String firstname, lastname, emil, password;
    private user myuser;
    private int year1, month1, day1;
    private boolean first_date_select = true;
    public Setting setting;
    private ProgressBar progressBar;
    private boolean is_image_seclected = false;
    private String[] permission;
    private Uri selectedImage;
    private TextView selectCountry;
    private String mDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        view = inflater.inflate(R.layout.fragment_register_pag1, container, false);
        load_widgets(view);
        return view;
    }

    // laod widgets
    public void load_widgets(final View view) {
        Log.d(TAG, "load_widgets: ");
        myuser = new user();
        permission = new String[]{Permissions.CAMERA_PERMISSION, Permissions.READ_STORAGE_PERMISSION, Permissions.WRITE_STORAGE_PERMISSION};

        txtv_selectdate = (TextView) view.findViewById(R.id.txtv_selectdate_merge_ceneter_profile_register);
        circleImageViewprofilepicture = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.profile_image);
        mradioGroup = (RadioGroup) view.findViewById(R.id.radiogrup_merge_center_profile_register);
        radio_btn_female = (RadioButton) view.findViewById(R.id.radio_btn_female_merge_center_profile_register);
        radio_btn_male = (RadioButton) view.findViewById(R.id.radio_btn_male_merge_center_profile_register);
        btn_register = (Button) view.findViewById(R.id.btn_register_merge_center_profile_register);
        selectCountry = view.findViewById(R.id.selectCountry);
        progressBar=view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        // set on click---------------------------------------------------------------------------------------------------->
        txtv_selectdate.setOnClickListener(new onclick());
        btn_register.setOnClickListener(new onclick());
        circleImageViewprofilepicture.setOnClickListener(new onclick());
        selectCountry.setOnClickListener(new onclick());
        //set date listner
        mdateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // insert into varibales
                year1 = year;
                month1 = month;
                day1 = dayOfMonth;

                first_date_select = false;
                Log.d(TAG, "onDateSet: ");
                mDate=year + "/" + month+1 + "/" + dayOfMonth;
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                txtv_selectdate.setText(date);

            }
        };
        mradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton btn = (RadioButton) view.findViewById(checkedId);
            }
        });
        //get info from last activity-------------------->
        Log.d(TAG, "load_widgets: myobject= " + myuser);
    }

    class onclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: ");
            switch (v.getId()) {

                case R.id.txtv_selectdate_merge_ceneter_profile_register:
                    // datepicker dialog show when clikc in textview select date
                    //if this first select
                    if (first_date_select) {
                        Calendar mcalendar = Calendar.getInstance();
                        year1 = mcalendar.get(Calendar.YEAR);
                        month1 = mcalendar.get(Calendar.MONTH);
                        day1 = mcalendar.get(Calendar.DAY_OF_MONTH);
                    }

                    DatePickerDialog mpicker = new DatePickerDialog(mContext, android.R.style.
                            Theme_Holo_Light_Dialog_MinWidth, mdateSetListener, year1, month1, day1);
                    mpicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Calendar calendar = Calendar.getInstance();
                    //made the register age minimum 13 year
                    int maxyear = calendar.get(Calendar.YEAR) - 13;
                    int minyear = calendar.get(Calendar.YEAR) - 120;
                    calendar.set(Calendar.YEAR, maxyear);
                    Date date = new Date();
                    // set max select year currenttime-13 and min year currenttime-120
                    mpicker.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.set(Calendar.YEAR, minyear);
                    mpicker.getDatePicker().setMinDate(calendar1.getTimeInMillis());
                    mpicker.show();

                    break;


                case R.id.profile_image:

                    if (Permissions.checkPermissionsArray(permission, mContext)) {
                        startActivityForResult(new Intent(mContext, GetaPicActivity.class), PHOTO_CODE);

                    } else {

                        Permissions.verifyPermission(permission, getActivity());
                    }

                    break;


                case R.id.btn_register_merge_center_profile_register: {
                    progressBar.setVisibility(View.VISIBLE);

                    RadioButton btn = (RadioButton) view.findViewById(mradioGroup.getCheckedRadioButtonId());


                    //asking if the client selected a date if not will get toast message
                    if (txtv_selectdate.getText().equals("Select Date")) {
                        Toast.makeText(mContext, getString(R.string.select_your_birthdate), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        return;
                    }

                    if(btn==null){
                        Toast.makeText(mContext, getString(R.string.select_your_gender), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        return;
                    }

                    if(selectCountry.getText().toString().equals(getString(R.string.select_country))){
                        Toast.makeText(mContext, getString(R.string.select_your_country), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        return;
                    }

                    //TODO
                    Log.d(TAG, "onClick: date "+mDate+"   date2 "+ TimeMethods.convertDateTimeFormatDateOnly2(txtv_selectdate.getText().toString()));
                    String birthday = TimeMethods.convertDateTimeFormatDateOnly2(txtv_selectdate.getText().toString());
                    myuser.setUser_birthday(mDate);
                    // if selected was female the value will be true else will be false

                    if (btn.getId() == R.id.radio_btn_female_merge_center_profile_register)
                        myuser.setUser_gender(0);
                    else
                        myuser.setUser_gender(1);

                    Log.d(TAG, "onClick: myuser=" + setting+"  selectedImaage "+selectedImage);


                    //add new user method

                    setting.setAccount_is_private(false);
                    setting.setUser_location_switch(false);

                    if (selectedImage != null) {
                        PhotoUpload.uploadNewPhotoFirebase(getString(R.string.user_profile_photo), TimeMethods.getUTCdatetimeAsStringFormat2(), selectedImage, setting.getUser_id(), mContext, new PhotoUpload.OnUploadingPostListener2() {
                            @Override
                            public void onSuccessListener(String ImageUrl) {
                                Log.d(TAG, "onSuccessListener: uploading succcess ");
                                setting.setProfilePic(ImageUrl);
                                HelpMethods.updateProfilePic(ImageUrl,mContext);
                                updateGenderPhone(ImageUrl);
                            }

                            @Override
                            public void onFailureListener(String ex) {
                                Log.d(TAG, "onFailureListener: failed uploading photo  " + ex);
                                Toast.makeText(mContext, getString(R.string.ERROR_TOAST), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        updateGenderPhone("");
                    }

                }


                break;

                case R.id.selectCountry:
                    final CountryPicker picker = CountryPicker.newInstance("Select Country");  // dialog title
                    picker.setListener(new CountryPickerListener() {
                        @Override
                        public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                            // Implement your code here
                            selectCountry.setText(name);
                            myuser.setCountry(name);
                            Log.d(TAG, "onSelectCountry: " + name);
                            picker.dismiss();
                        }
                    });
                    picker.show(getFragmentManager(), "COUNTRY_PICKER");

                    break;
            }
        }
    }


    private void updateUI() {
        HelpMethods.addSharedPreferences(setting, mContext);
        Intent intent = new Intent(mContext, BaseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();

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
                    circleImageViewprofilepicture.setImageURI(uri);
                    selectedImage = uri;
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


    public void updateGenderPhone(String imgUrl) {
        InfoMethodsHandler.updateProfilePhotoGenderBirthDate(setting.getUser_id(), imgUrl, myuser.getUser_gender(), myuser.getUser_birthday(), myuser.getCountry(), new InfoMethodsHandler.OnUpdateListener() {
            @Override
            public void onSuccessListener() {
                Log.d(TAG, "onSuccessListener: ");
                updateUI();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onServerException(String ex) {
                Log.d(TAG, "onServerException: " + ex);
                Toast.makeText(mContext, getString(R.string.CHECK_INTERNET), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailureListener(String ex) {
                Log.d(TAG, "onFailureListener: " + ex);
                Toast.makeText(mContext, getString(R.string.ERROR_TOAST), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mContext = (Activity) context;
            Log.d(TAG, "onAttach: " + mContext);

        }

    }
}

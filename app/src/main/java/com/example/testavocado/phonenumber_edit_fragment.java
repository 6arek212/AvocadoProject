package com.example.testavocado;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Update;

import com.example.testavocado.Models.Setting;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.validation.validations;
import com.hbb20.CountryCodePicker;

public class phonenumber_edit_fragment extends Fragment {
    public static final String TAG = "phonenumber_edit_fragme";
    private Context mcontext;
    private View myview;
    private TextView txtv_title;
    private ImageView imgv_arrow_back;
    private EditText edtxt_phonenumber;
    private Button btn_continue,btn_cancel;
    private CountryCodePicker countryCodePicker1;
    private Setting setting1;
    private LinearLayout linearLayout1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = getLayoutInflater().inflate(R.layout.fragment_phonenumber_edit, container, false);
        load_widgets();
        return myview;
    }

    public void load_widgets() {
        linearLayout1=(LinearLayout)myview.findViewById(R.id.linear_layout_fullphonenumber);
        txtv_title = (TextView) myview.findViewById(R.id.txtv_post_created_firstname_merge_topbar_back_arrow);
        imgv_arrow_back = (ImageView) myview.findViewById(R.id.imgv_arrow_back_merge_topbar_back_arrow);
        countryCodePicker1 = (CountryCodePicker) myview.findViewById(R.id.coutrycodepicker1);
        btn_continue = (Button) myview.findViewById(R.id.btn_continue);
        edtxt_phonenumber = (EditText) myview.findViewById(R.id.edtxt_phonenumber);
        btn_cancel=(Button)myview.findViewById(R.id.btn_cancel11);
        //get values
        setting1 = HelpMethods.getSharedPreferences(mcontext);

        //set on click
        linearLayout1.setOnClickListener(new onclick());
        imgv_arrow_back.setOnClickListener(new onclick());
        btn_continue.setOnClickListener(new onclick());
        btn_cancel.setOnClickListener(new onclick());
        countryCodePicker1.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                Toast.makeText(mcontext, "Updated " + countryCodePicker1.getSelectedCountryName(), Toast.LENGTH_SHORT).show();
            }
        });

        //set values
        txtv_title.setText(getString(R.string.phonenumber_title) + "");
    }


    class onclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imgv_arrow_back_merge_topbar_back_arrow:
                    close_all_fragments();
                    break;
                case R.id.btn_cancel11:
                    close_all_fragments();
                    break;


                case R.id.btn_continue:
                    validations validations1 = new validations();
                    String phone_number = edtxt_phonenumber.getText().toString().trim();
                    //space removing
                   phone_number= validations.space_remove(phone_number);
                    edtxt_phonenumber.setText(phone_number);
                    if (validations1.isValidMobile(phone_number)) {

                        //alert dialog
                        final AlertDialog dialog;   dialog=new AlertDialog.Builder(mcontext)
                                .setTitle("Change PhoneNumber")
                                .setMessage("Are you sure about this?")
                                .setPositiveButton("Yes",null)
                                .setNegativeButton("Cancel",null)
                                .show();
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);

                        Button positivebutton=dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        final String finalPhone_number = phone_number;
                        positivebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Update_information_Methods.Update_phonenumber(mcontext, setting1.getUser_id(), finalPhone_number, new Update_information_Methods.on_first_last_name_updated() {
                                    @Override
                                    public void onSuccessListener(int result) {
                                        Toast.makeText(mcontext, "Success phone number changed", Toast.LENGTH_SHORT).show();
                                        edtxt_phonenumber.setText("");
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onServerException(String ex) {
                                        Toast.makeText(mcontext, ex, Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "onServerException: " + ex);
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onFailureListener(String ex) {
                                        Toast.makeText(mcontext, getString(R.string.CHECK_INTERNET), Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "onFailureListener: " + ex);
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
                        //------------------------------------------------------------------------->

                    }
                    else if(phone_number.isEmpty())
                    {
                        Toast.makeText(mcontext, "There is no written number !", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(mcontext, "Wrong phone number", Toast.LENGTH_SHORT).show();

                    break;
            }
        }
    }

    //closeing all of the fragment in backstack
    public void close_all_fragments() {
        getFragmentManager().popBackStack();

    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mcontext = (Activity) context;
            Log.d(TAG, "onAttach: " + mcontext);
        }
    }
}

package com.example.testavocado;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testavocado.Utils.HelpMethods;

import java.util.Calendar;
import java.util.Date;

public class change_age_fragment extends Fragment {
    public static final String TAG = "change_age_fragment";
    private View myview;
    private Context mcontext;
    private ImageView img_arrow;
    private Button btn_save,btn_cancel;
    private LinearLayout linearLayout_full1;
    private TextView txt_title,txtv_selectdate;
    private boolean first_date_select = true;
    private DatePickerDialog.OnDateSetListener mdateSetListener;
    private int year1, month1, day1;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=getLayoutInflater().inflate(R.layout.fragment_change_age,container,false);
        load_widgets();
        return myview;
    }

    public void load_widgets()
    {
        //set widgets
        linearLayout_full1=(LinearLayout)myview.findViewById(R.id.linearlayout_full_changeage);
        img_arrow=(ImageView)myview.findViewById(R.id.imgv_arrow_back_merge_topbar_back_arrow);
        txt_title=(TextView)myview.findViewById(R.id.txtv_post_created_firstname_merge_topbar_back_arrow);
        txtv_selectdate=(TextView)myview.findViewById(R.id.txtv_selectdate_merge_ceneter_profile_register);
        btn_save=(Button)myview.findViewById(R.id.btn_save_birthdate_update);
        btn_cancel=(Button)myview.findViewById(R.id.btn_cancel_birthdate_update);

        //set values
        txt_title.setText("Birth date modify");

        //set on click
        btn_cancel.setOnClickListener(new onclick());
        btn_save.setOnClickListener(new onclick());
        txtv_selectdate.setOnClickListener(new onclick());
        linearLayout_full1.setOnClickListener(new onclick());
        img_arrow.setOnClickListener(new onclick());

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
                month = month + 1;
                String date = year + "/" + month + "/" + dayOfMonth;
                txtv_selectdate.setText(date);

            }
        };
    }

    class onclick implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.btn_save_birthdate_update:

                    //asking if the client selected a date if not will get toast message
                    if (txtv_selectdate.getText().equals("Select Date")) {
                        Toast.makeText(mcontext, getString(R.string.select_your_birthdate), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else
                    {
                        final String birthdate1=txtv_selectdate.getText().toString().trim();


                        //aler dialog
                        final AlertDialog dialog=new AlertDialog.Builder(mcontext)
                                .setTitle("Change Email")
                                .setMessage("Are you sure about that?")
                                .setPositiveButton("Yes",null)
                                .setNegativeButton("Cancel",null)
                                .show();
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);

                        Button positivebutton=dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positivebutton.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view) {

                                Update_information_Methods.Update_Birth_date(mcontext, HelpMethods.get_userid_sharedp(mcontext), birthdate1, new Update_information_Methods.on_first_last_name_updated() {
                                    @Override
                                    public void onSuccessListener(int result) {
                                        Log.d(TAG, "onSuccessListener: ");
                                        Toast.makeText(mcontext, "Success,Data has been changed", Toast.LENGTH_SHORT).show();
                                        txtv_selectdate.setText("Select Date");
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onServerException(String ex) {
                                        Log.d(TAG, "onServerException: ");
                                        Toast.makeText(mcontext, ex, Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onFailureListener(String ex) {
                                        Log.d(TAG, "onFailureListener: ");
                                        Toast.makeText(mcontext, getString(R.string.CHECK_INTERNET), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });

                            }
                        });





                    }
                    break;

                case R.id.btn_cancel_birthdate_update:
                    close_all_fragments();
                    break;

                case R.id.imgv_arrow_back_merge_topbar_back_arrow:
                    close_all_fragments();
                    break;

                case R.id.txtv_selectdate_merge_ceneter_profile_register:
                    // datepicker dialog show when clikc in textview select date
                    //if this first select
                    if (first_date_select) {
                        Calendar mcalendar = Calendar.getInstance();
                        year1 = mcalendar.get(Calendar.YEAR);
                        month1 = mcalendar.get(Calendar.MONTH);
                        day1 = mcalendar.get(Calendar.DAY_OF_MONTH);
                    }

                    DatePickerDialog mpicker = new DatePickerDialog(mcontext, android.R.style.
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

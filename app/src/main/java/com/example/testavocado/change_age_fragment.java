package com.example.testavocado;

import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
                    Toast.makeText(mcontext, "Save", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.btn_cancel_birthdate_update:
                    Toast.makeText(mcontext, "Cancel", Toast.LENGTH_SHORT).show();
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

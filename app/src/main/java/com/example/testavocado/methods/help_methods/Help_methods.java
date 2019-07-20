package com.example.testavocado.methods.help_methods;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Help_methods {

    public static final String TAG = "Help_methods";


    // keyboard hide method
    public static void hideKeyboard(Activity activity) {
        Log.d(TAG, "hideKeyboard: sucess keyboard hidden");
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // keyboard open method
    public static void showKeyboard(Activity activity) {
        Log.d(TAG, "showKeyboard: Sucess ketboard showed");
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


    //SharedPreferences save user_id
    public static void save_user_id(int userid, Context context) {
        SharedPreferences mysharedPreferences = context.getSharedPreferences("user_id", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mysharedPreferences.edit();

        editor.putInt("user_id", userid);
        editor.apply();
    }

    public static int get_user_id_sharedprefernces(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_id", 0);
        int user_id_shared = sharedPreferences.getInt("user_id", 0);
        return user_id_shared;
    }

    public static String get_user_emil_sharedprefernces(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_data", 0);
        String emil = sharedPreferences.getString("emil", "Not found");
        return emil;
    }

    public static String get_user_password_sharedprefernces(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_data", 0);
        String password = sharedPreferences.getString("password", "Not found");
        return password;
    }

    // save emil and password shared prefernce
    public static void save_emil_password(String emil, String password, Context context) {
        SharedPreferences mysharedPreferences = context.getSharedPreferences("user_data", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mysharedPreferences.edit();
        editor.putString("emil", emil);
        editor.putString("password", password);
        editor.apply();
    }
    public static void remove_sharedprefrences_password(Context context) {
        SharedPreferences mysharedPreferences = context.getSharedPreferences("user_data", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mysharedPreferences.edit();
         editor.remove("password").commit();
         editor.apply();
    }

//date format
    static final String DATE_FORMAT ="MM/dd/yyyy HH:mm:ss";
    static final String DATE_FORMAT2 = "MM/dd/yyyy HH:mm:ss a";


    // get the utc time
    public static String get_utctime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT2);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Log.d(TAG, "get_utctime: The time is :" + dateFormat.format(date));
        return dateFormat.format(date);
    }

//Convert UTC to current locale time
    public static String convert_utc_time_to_current_locale_time(String utcdate) {
        String formattedDate = "";
        try {
            SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df.parse(utcdate);
            df.setTimeZone(TimeZone.getDefault());
            formattedDate = df.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "getNewDate: " + e.getMessage());
        }

        return formattedDate.toString();
    }

    static public String gettime_Difference(String date)
    {
        Log.d(TAG, "gettime_Difference: ");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(DATE_FORMAT,Locale.getDefault());
        try {
            Date lastadte= simpleDateFormat.parse(date);

            Date currentdate= Calendar.getInstance().getTime();
            long datemalies=((currentdate.getTime()-lastadte.getTime())/1000);// getting time in miele sec and /1000 to make it in sec
            if(datemalies >=60)//if lower than 60 its sec
            {
                datemalies/=60;// convert to mins

                if(datemalies>=60)// if lower than 60 its min
                {
                    datemalies/=60;// convert to hour

                    if(datemalies>=24)
                    {
                        datemalies/=24;// convet to day

                        if(datemalies>=7)
                        {
                            datemalies/=7;//convert to week
                            return datemalies+" weeks ago";
                        }
                        else
                            return datemalies+" days ago";
                    }
                    else
                        return datemalies+" hours ago";
                }
                else
                    return  datemalies+" min ago";
            }
            else
                return datemalies+" sec ago";

        }
        catch (ParseException ex)
        {
            Log.d(TAG, "gettime_Difference: exception="+ex.getMessage().toString());
            return  ex.getMessage().toString();
        }
    }

}

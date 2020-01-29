package com.example.testavocado.Utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeMethods {
    private static final String TAG = "TimeMethods";

    static final String DATE_FORMAT2 = "yyyy-MM-dd HH:mm:ss";

    static final String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";

    static final String DATE_FORMAT3 = "dd/MM/yyyy HH:mm:ss";


    public static String convertDateTimeFormat(String datetime){

        Date date = null;
        String dueDateAsNormal ="";

        try {
            date = new SimpleDateFormat(DATE_FORMAT).parse(datetime);
            SimpleDateFormat newFormatter = new SimpleDateFormat("HH:mm");
            newFormatter.setTimeZone(TimeZone.getDefault());
            dueDateAsNormal = newFormatter.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return  dueDateAsNormal;
    }

    public static String convertDateTimeFormatDateOnly2(String datetime){

        Date date = null;
        String dueDateAsNormal ="";

        try {
            date = new SimpleDateFormat(DATE_FORMAT3).parse(datetime);
            SimpleDateFormat newFormatter = new SimpleDateFormat("yyyy-MM-dd");
            newFormatter.setTimeZone(TimeZone.getDefault());
            dueDateAsNormal = newFormatter.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return  dueDateAsNormal;
    }



    public static String convertDateTimeFormatDateOnly(String datetime){

        Date date = null;
        String dueDateAsNormal ="";

        try {
            date = new SimpleDateFormat(DATE_FORMAT).parse(datetime);
            SimpleDateFormat newFormatter = new SimpleDateFormat("dd-MM-yyyy");
            newFormatter.setTimeZone(TimeZone.getDefault());
            dueDateAsNormal = newFormatter.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return  dueDateAsNormal;
    }


    public static String convertDateTimeFormat2(String date){
        SimpleDateFormat oldFormatter = new SimpleDateFormat(DATE_FORMAT);
        oldFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        String dueDateAsNormal ="";

        try {
            value = oldFormatter.parse(date);
            SimpleDateFormat newFormatter = new SimpleDateFormat("HH:mm");

            newFormatter.setTimeZone(TimeZone.getDefault());
            dueDateAsNormal = newFormatter.format(value);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "getNewDate: "+e.getMessage());
        }

        return dueDateAsNormal;
    }


    public static String convertDateTimeFormat3(String date){
        Log.d(TAG, "convertDateTimeFormat3: "+date);
        SimpleDateFormat oldFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        oldFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        String dueDateAsNormal ="";

        try {
            value = oldFormatter.parse(date);
            SimpleDateFormat newFormatter = new SimpleDateFormat("HH:mm a");

            newFormatter.setTimeZone(TimeZone.getDefault());
            dueDateAsNormal = newFormatter.format(value);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "getNewDate: "+e.getMessage());
        }

        return dueDateAsNormal;
    }


    public static String getNewLocalDate(String date){
        SimpleDateFormat oldFormatter = new SimpleDateFormat(DATE_FORMAT);
        oldFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        String dueDateAsNormal ="";

        try {
            value = oldFormatter.parse(date);
            SimpleDateFormat newFormatter = new SimpleDateFormat(DATE_FORMAT);

            newFormatter.setTimeZone(TimeZone.getDefault());
            dueDateAsNormal = newFormatter.format(value);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "getNewDate: "+e.getMessage());
        }

        return dueDateAsNormal;
    }




    public static Date getUTCdatetimeAsDate() {
        // note: doesn't check for null
        return stringDateToDate(getUTCdatetimeAsString());
    }



    public static String getUTCdatetimeAsString() {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT,Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(new Date());

        Log.d(TAG, "getUTCdatetimeAsString: utc time "+utcTime);

        return utcTime;
    }

    public static String getUTCdatetimeAsStringFormat2() {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT2,Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(new Date());

        Log.d(TAG, "getUTCdatetimeAsString: utc time "+utcTime);

        return utcTime;
    }






    public static Date stringDateToDate(String StrDate) {
        Date dateToReturn = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        try {
            dateToReturn = (Date)dateFormat.parse(StrDate);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return dateToReturn;
    }
















    /**
     * Returns string represnting the number of days ago the post was made
     * /
     *          *      1- sec
     *          *      2-min
     *          *      3-hr
     *          *      4-day
     *
     *
     *
     *
     * @return
     */
    public static String getTimestampDifference(String date) {
        Log.d(TAG, "getTimestampDifference: getting timestamp diffrence");

        SimpleDateFormat oldDateFormate = new SimpleDateFormat(DATE_FORMAT,Locale.getDefault());

        try{
            Date inCommingDate=oldDateFormate.parse(date);
            Log.d(TAG, "getTimestampDifference: inCommingDate pass "+inCommingDate.getTime());
            Date currentDate=getUTCdatetimeAsDate();

            int t = Math.round((currentDate.getTime() - inCommingDate.getTime()) / 1000);




            if (t > 60) {
                t = Math.round(t / 60);

                if (t > 60) {
                    t = Math.round(t / 60);

                    if (t > 24) {
                        t = Math.round(t / 24);

                        if(t>6)
                        {
                            t = Math.round(t / 7);

                            if(t>3){
                                t = Math.round(t / 4);

                                if(t>11){
                                    //in months
                                    return t+" year's ago";

                                }
                                else{
                                    //in months
                                    return t+" month's ago";
                                }

                            }
                            else{
                                //in week
                                return t+" week's ago";
                            }

                        }
                        else {
                            //in day
                            return t+" day's ago";
                        }

                    } else {
                        //in hour
                        return t+" hour ago";
                    }

                } else {
                    //in min
                    return t+" min ago";

                }
            } else {
                //in sec
                return t+" sec ago";
            }


        }catch (ParseException e){
            Log.d(TAG, "getTimestampDifference: ParseException "+e.getMessage());
            return "0";
        }
    }




}

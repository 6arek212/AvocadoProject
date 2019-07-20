package com.example.testavocado.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.testavocado.R;

public class Validation {
    private static final String TAG = "Validation";

    //for password validation
    private static char[] chars = {'"', '(', ')', '*', '&', '^', '%', '$', '#', '@', '!'};

    //widgets
    private Context mContext;


    public Validation(Context mContext) {
        this.mContext = mContext;
    }


    /**
     * 0- EMAIL IS VALID :D
     * <p>
     * 1-email format !!
     *
     * @param email
     * @return
     */
    public boolean Email(String email) {
        if (!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        }
        Log.d(TAG, "Email: " + mContext.getString(R.string.EMAIL_PATTERN));
        Toast.makeText(mContext, mContext.getString(R.string.EMAIL_PATTERN), Toast.LENGTH_SHORT).show();
        return false;
    }


    /*
            0- nmae is valid :D

            1- name contain spaces
            2-name is longer than 25>

     */
    public boolean Name(String name) {
        if (name.indexOf(" ") == 1) {
            Log.d(TAG, "Name: " + mContext.getString(R.string.NAME_SPACE));
            Toast.makeText(mContext, mContext.getString(R.string.NAME_SPACE), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (name.length() > 30) {
            Log.d(TAG, "Name: " + mContext.getString(R.string.NAME_TOO_LONG));
            Toast.makeText(mContext, mContext.getString(R.string.NAME_TOO_LONG), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    /**
     * 0- password is valid
     * <p>
     * 1- password is less than 8
     * 2- password is longer than 16
     * 3- password contain special character
     *
     * @param password
     * @return
     */


    public boolean Password(String password) {
        if (password.length() < 8) {
            Log.d(TAG, "Password: " + mContext.getString(R.string.PASSWORD_SHORT));
            Toast.makeText(mContext, mContext.getString(R.string.PASSWORD_SHORT), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() > 16) {
            Log.d(TAG, "Password: " + mContext.getString(R.string.PASSWORD_LONG));
            Toast.makeText(mContext, mContext.getString(R.string.PASSWORD_LONG), Toast.LENGTH_SHORT).show();
            return false;
        }

        for (int i = 0; i < chars.length; i++) {
            if (password.indexOf(chars[i]) == 1) {
                Log.d(TAG, "Password: "+mContext.getString(R.string.PASSWORD_INVALED));
                Toast.makeText(mContext, mContext.getString(R.string.PASSWORD_INVALED), Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }


}

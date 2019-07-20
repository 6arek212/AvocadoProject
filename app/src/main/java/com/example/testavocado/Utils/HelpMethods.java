package com.example.testavocado.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.testavocado.Models.Setting;
import com.example.testavocado.R;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class HelpMethods {
    private static final String TAG = "HelpMethods";




    /**
     *          adding shared Preferences
     *
     */
    public static void deleteUserIdSharedPreferences(Context context,Activity activity){
        SharedPreferences preferences=context.getSharedPreferences(context.getString(R.string.settings),Context.MODE_PRIVATE);
        SharedPreferences.Editor ed=preferences.edit();

        ed.remove(context.getString(R.string.sharedPref_userId));
        ed.remove(context.getString(R.string.sharedPref_userfristname));
        ed.remove(context.getString(R.string.sharedPref_userlastname));
        ed.remove(context.getString(R.string.sharedPref_profilepic));
        ed.remove(context.getString(R.string.private_profile_switch));
        ed.remove(context.getString(R.string.location_switch));

        ed.apply();
    }




    /**
     *          adding shared Preferences
     *
     */
    public static void addSharedPreferences(Setting setting, Context context){
        Log.d(TAG, "addSharedPreferences: adding shared preferences "+setting);

        SharedPreferences preferences=context.getSharedPreferences(context.getString(R.string.settings),Context.MODE_PRIVATE);
        SharedPreferences.Editor ed=preferences.edit();

        ed.putInt(context.getString(R.string.sharedPref_userId),setting.getUser_id());
        ed.putString(context.getString(R.string.sharedPref_userfristname),setting.getUser_first_name());
        ed.putString(context.getString(R.string.sharedPref_userlastname),setting.getUser_last_name());
        ed.putString(context.getString(R.string.sharedPref_profilepic),setting.getProfilePic());
        ed.putBoolean(context.getString(R.string.private_profile_switch),setting.isAccount_is_private());
        ed.putBoolean(context.getString(R.string.location_switch),setting.isUser_location_switch());
        ed.apply();
    }

//geeting first and last name and profile pic shared prefe
    public static String get_user_name_sharedprefernces(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.settings), 0);
        String firstname= sharedPreferences.getString(context.getString(R.string.sharedPref_userfristname), "null");
        String lastname=sharedPreferences.getString(context.getString(R.string.sharedPref_userlastname),"null");
        String name=firstname+" "+lastname;
        return name;
    }
//getting user profile pic shared prefe link
    public static String get_user_profile_pic_sharedprefernces(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.settings), 0);
        String path_profile_pic=sharedPreferences.getString(context.getString(R.string.sharedPref_profilepic),"null");
        return path_profile_pic;
    }


    //getting user profile pic shared prefe link
    public static int get_userid_sharedp(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.settings), 0);
        int userid= sharedPreferences.getInt(context.getString(R.string.sharedPref_userId),-1);
        return userid;
    }
    /**
     *
     *                  checking the shared preferences for a user_id
     *
     *                  if the user logged in previously
     *
     * @return
     */

    public static int checkSharedPreferencesForUserId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.settings), Context.MODE_PRIVATE);
        int user_id = preferences.getInt(context.getString(R.string.sharedPref_userId), context.getResources().getInteger(R.integer.defaultValue));

        Log.d(TAG, "checkSharedPreferencesForUserId: getting user id "+user_id);
        return user_id;
    }




    /**
     *
     *          closing keyboard
     */

    public static  void closeKeyboard(Activity activity){
        View view=activity.getCurrentFocus();
        if(view!=null){
            InputMethodManager imm=(InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }


    public static  void closeKeyboard2(View view,Context context){
        View view1=view.findFocus();

        if(view!=null){
            InputMethodManager imm=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }




    //SharedPreferences save user_id
    public static void save_user_id(int userid, Context context) {
        SharedPreferences mysharedPreferences = context.getSharedPreferences(context.getString(R.string.settings), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mysharedPreferences.edit();

        editor.putInt("user_id", userid);
        editor.apply();
    }

}

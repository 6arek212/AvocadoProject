package com.example.testavocado.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.testavocado.Home.PostMethods;
import com.example.testavocado.Models.Post;
import com.example.testavocado.Models.Setting;
import com.example.testavocado.R;
import com.example.testavocado.Settings.MenuFragment;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class HelpMethods {
    private static final String TAG = "HelpMethods";


   public interface OnClickDialog {
        void onClick();
    }


    public static void showAlert(String title,Context context,final OnClickDialog click){
        AlertDialog.Builder builder=new AlertDialog.Builder(context,R.style.AlertDialogStyle2);
        builder.setTitle(title);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                click.onClick();
            }
        });

        builder.setNegativeButton("Cancel",null);
        builder.show();
    }



    public static void alertDialog(final Post post,final int user_id,final Context mContext) {
        // Set up the alert builder
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.AlertDialogStyle2);
        builder.setTitle("Are you sure you want to share " + post.getUser_name() + " " + post.getUser_last_name() + " post ?");
        //builder.setMessage("Choose sharing option");

        final int[] type = new int[1];
// Add a checkbox list

        final String[][] choices = {{"friends only", "public"}};
        builder.setSingleChoiceItems(choices[0], 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: ooo "+which);
                type[0] =which;
            }
        });



// Add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                // The user clicked OK
                Log.d(TAG, "onClick: onshare wich "+type[0]);

                post.setUser_id(user_id);
                post.setPost_text(post.getPost_text());
                post.setPost_images_url(post.getPost_images_url());
                post.setPost_date_time(TimeMethods.getUTCdatetimeAsString());
                post.setPost_type(type[0]);
                post.setPost_is_shared(true);
                post.setOriginal_post_id(post.getPost_id());


                PostMethods.sharePost(post, new PostMethods.OnSharingPostListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess: shared a post :D " + post);
                        Toast.makeText(mContext, "Post Shared", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void onServerException(String ex) {
                        Log.d(TAG, "onServerException: error sharing post " + ex);
                        Toast.makeText(mContext, mContext.getString(R.string.ERROR_TOAST), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(String ex) {
                        Log.d(TAG, "onFailure: error while sharing a post" + ex);
                        Toast.makeText(mContext, mContext.getString(R.string.ERROR_TOAST), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });



            }
        });

        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }




    /**
     * adding shared Preferences
     */
    public static void deleteUserIdSharedPreferences(Context context, Activity activity) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.settings), Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();

        ed.remove(context.getString(R.string.sharedPref_userId));
        ed.remove(context.getString(R.string.sharedPref_userfristname));
        ed.remove(context.getString(R.string.sharedPref_userlastname));
        ed.remove(context.getString(R.string.sharedPref_profilepic));
        ed.remove(context.getString(R.string.private_profile_switch));
        ed.remove(context.getString(R.string.location_switch));

        ed.apply();
    }


    /**
     * update profile pic in shared preference
     */


    public static void updateProfilePic(String imageUrl, Context context) {
        SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(context).edit();
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.settings), Context.MODE_PRIVATE);
        ed = preferences.edit();
        ed.putString(context.getString(R.string.sharedPref_profilepic), imageUrl);
        ed.apply();

    }


    public static void updateName(String firstName, String lastName, Context context) {
        SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(context).edit();
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.settings), Context.MODE_PRIVATE);
        ed = preferences.edit();
        ed.putString(context.getString(R.string.sharedPref_userfristname), firstName);
        ed.putString(context.getString(R.string.sharedPref_userlastname), lastName);
        ed.apply();
    }


    /**
     * getting shared Preferences
     */
    public static Setting getSharedPreferences(Context context) {
        Setting setting = new Setting();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        setting.setAccount_is_private(preferences.getBoolean(context.getString(R.string.private_profile_switch), false));
        setting.setUser_location_switch(preferences.getBoolean(context.getString(R.string.location_switch), false));
        setting.setFingerprint(preferences.getBoolean(context.getString(R.string.fingerPrint), false));

        preferences = context.getSharedPreferences(context.getString(R.string.settings), Context.MODE_PRIVATE);
        setting.setUser_id(preferences.getInt(context.getString(R.string.sharedPref_userId), -1));
        setting.setUser_first_name(preferences.getString(context.getString(R.string.sharedPref_userfristname), ""));
        setting.setUser_last_name(preferences.getString(context.getString(R.string.sharedPref_userlastname), ""));
        setting.setProfilePic(preferences.getString(context.getString(R.string.sharedPref_profilepic), ""));


        return setting;
    }


    /**
     * adding shared Preferences
     */
    public static void addToken(String token, Context context) {
        SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(context).edit();
        ed.putString("token", token);
        ed.apply();
    }


    /**
     * adding shared Preferences
     */
    public static void addChatNum(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = preferences.edit();

        int num=preferences.getInt("chatCount",0);
        num++;
        ed.putInt("chatCount", num);
        ed.apply();
    }

    public static void clearChatNum(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = preferences.edit();
        ed.remove("chatCount");
        ed.apply();
    }

    public static Integer getChatNum(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int num=preferences.getInt("chatCount",0);

        if (num==0)
            return null;
        else
            return num;
    }




    /**
     * adding shared Preferences
     */
    public static String getToken(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("token", null);
    }


    /**
     * adding shared Preferences
     */
    public static void addSharedPreferences(Setting setting, Context context) {
        Log.d(TAG, "addSharedPreferences: adding shared preferences " + setting);


        SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(context).edit();
        ed.putBoolean(context.getString(R.string.private_profile_switch), setting.isAccount_is_private());
        ed.putBoolean(context.getString(R.string.location_switch), setting.isUser_location_switch());
        ed.apply();


        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.settings), Context.MODE_PRIVATE);
        ed = preferences.edit();

        ed.putInt(context.getString(R.string.sharedPref_userId), setting.getUser_id());
        ed.putString(context.getString(R.string.sharedPref_userfristname), setting.getUser_first_name());
        ed.putString(context.getString(R.string.sharedPref_userlastname), setting.getUser_last_name());
        ed.putString(context.getString(R.string.sharedPref_profilepic), setting.getProfilePic());

        ed.apply();
    }


    //geeting first and last name and profile pic shared prefe
    public static String get_user_name_sharedprefernces(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.settings), 0);
        String firstname = sharedPreferences.getString(context.getString(R.string.sharedPref_userfristname), "null");
        String lastname = sharedPreferences.getString(context.getString(R.string.sharedPref_userlastname), "null");
        String name = firstname + " " + lastname;
        return name;
    }

    //getting user profile pic shared prefe link
    public static String get_user_profile_pic_sharedprefernces(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.settings), 0);
        String path_profile_pic = sharedPreferences.getString(context.getString(R.string.sharedPref_profilepic), "");
        return path_profile_pic;
    }


    //getting user profile pic shared prefe link
    public static int get_userid_sharedp(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.settings), 0);
        int userid = sharedPreferences.getInt(context.getString(R.string.sharedPref_userId), -1);
        return userid;
    }

    /**
     * checking the shared preferences for a user_id
     * <p>
     * if the user logged in previously
     *
     * @return
     */

    public static int checkSharedPreferencesForUserId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.settings), Context.MODE_PRIVATE);
        int user_id = preferences.getInt(context.getString(R.string.sharedPref_userId), context.getResources().getInteger(R.integer.defaultValue));

        Log.d(TAG, "checkSharedPreferencesForUserId: getting user id " + user_id);
        return user_id;
    }


    /**
     * closing keyboard
     */

    public static void closeKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public static void closeKeyboard2(View view, Context context) {
        View view1 = view.findFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

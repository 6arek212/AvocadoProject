package com.example.testavocado;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import com.example.testavocado.methods.help_methods.Help_methods;
import com.example.testavocado.objects.user;
import com.example.testavocado.objects.user_post;
import com.example.testavocado.validation.validations;

public class fill_objects {
    public static final String TAG = "fill_objects";
    private Context context;

    public fill_objects(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "fill_objects{" +
                "context=" + context +
                '}';
    }

//methods----------------------------------------------------------------------------------------->
    public static final user fill_register_object(EditText firstname, EditText lastname, EditText emil, EditText password) {
        Log.d(TAG, "fill_register_object: fill register object");

        String userfirstname, userlastname, useremil, userpassword;
        userfirstname = firstname.getText().toString();
        userlastname = lastname.getText().toString();
        useremil = emil.getText().toString();
        userpassword = password.getText().toString();
        //remove spaceing if there space
        userfirstname = validations.space_remove(userfirstname);
        userlastname = validations.space_remove(userlastname);
        useremil = validations.space_remove(useremil);
        userpassword = validations.space_remove(userpassword);

        user register_account = new user();//userfirstname, userlastname, useremil, userpassword
        register_account.setUser_firstname(userfirstname);
        register_account.setUser_lastname(userlastname);
        register_account.setUser_emil(useremil);
        register_account.setUser_password(userpassword);

        return register_account;
    }


    public static user_post fillpost_object(String post_text, Context context, int howcansee) {
        int userid=Help_methods.get_user_id_sharedprefernces(context);
        //------------------------------------------------------------->

        user_post mypost = new user_post();
        try {
            mypost.setUserid(userid);
            mypost.setPost_text(post_text);
            mypost.setPost_imageurl("c//");
            mypost.setPost_date(Help_methods.get_utctime());
            mypost.setPostlastchange_date(Help_methods.get_utctime());
            mypost.setHow_can_see(howcansee);
            Log.d(TAG, "fillpost_object:mypost= " + mypost);
        }
        catch (Exception ex)
        {
            Log.d(TAG, "fillpost_object:"+ex.getMessage().toString());
            mypost=null;
        }
        return mypost;
    }
}

package com.example.testavocado;

import android.content.Context;
import android.util.Log;

import com.example.testavocado.methods.help_methods.Help_methods;
import com.example.testavocado.objects.friends_requests;
import com.example.testavocado.objects.my_profile;
import com.example.testavocado.objects.user;
import com.example.testavocado.objects.user_post;

import org.json.JSONObject;

public class fill_json_objects {
public static final String TAG="fill_json_objects";
    private Context mycontext;

    public fill_json_objects(Context mycontext) {
        this.mycontext = mycontext;
    }

    public Context getMycontext() {
        return mycontext;
    }

    public void setMycontext(Context mycontext) {
        this.mycontext = mycontext;
    }

    static public user fill_json_objects_user(JSONObject myobject, Context context)
    {
        Log.d(TAG, "fill_json_objects_user: ");
        user myuser=new user();

        try {
            myuser.setUserid(myobject.getInt("User_id"));
            myuser.setUser_firstname(myobject.getString("First_name"));
            myuser.setUser_lastname(myobject.getString("Last_name"));
            myuser.setUser_emil(myobject.getString("Emil"));

            // split to date to remove hours and minute and sec split to space (" ")
            String birth1=myobject.getString("Birth_day");
            String []str=birth1.split(" ");
            myuser.setUser_birthday(str[0]);

            //myuser.setUser_birthday(myobject.getString("Birth_day"));
           // myuser.setUser_gender(myobject.getBoolean("Gender"));
            myuser.setUser_lastlogin(myobject.getString("Lastlogin"));
            myuser.setUser_password(myobject.getString("Password"));
            myuser.setIsactive(myobject.getBoolean("Is_active"));
        }
        catch (Exception ex)
        {
            Log.d(TAG, "fill_json_objects_user: "+ex.getMessage().toString());
        }
        return myuser;
    }


  static   public user_post fill_objectpost(JSONObject jsonObject, Context context)
    {
        user_post post=new user_post();
        try {
            Log.d(TAG, "fill_objectpost: ");
            post.setPostid(jsonObject.getInt("Postid"));
            post.setUserid(jsonObject.getInt("Userid"));
            post.setPost_text(jsonObject.getString("Post_text"));
            post.setPost_imageurl(jsonObject.getString("Post_imageurl"));
            // getting post utc time and convert it to local time
            String time= Help_methods.convert_utc_time_to_current_locale_time(jsonObject.getString("Post_date"));
            //make date in sec or min or hour or day or week
            String datedifference=Help_methods.gettime_Difference(time);
            post.setPost_date(datedifference);

            post.setPostlastchange_date(Help_methods.convert_utc_time_to_current_locale_time(jsonObject.getString("Postlastchange_date")));

            post.setHow_can_see(jsonObject.getInt("How_can_see"));
            post.setLikes_counter(jsonObject.getInt("Likes_counter"));
            post.setComments_counter(jsonObject.getInt("Comments_counter"));
            post.setDis_likes_counter(jsonObject.getInt("Dis_likes_counter"));
        }
        catch (Exception ex)
        {
            post=null;
            Log.d(TAG, "fill_objectpost: ");
        }
        return post;
    }



  static  public my_profile fill_object_myprofile(JSONObject jsonObject)
  {
      my_profile profile=new my_profile();
      try {
          profile.setProfileid(jsonObject.getInt("Profile_id"));
          profile.setUserid(jsonObject.getInt("User_id"));
          profile.setFirstlogin(jsonObject.getString("Firstlogin"));
          profile.setProfile_picture_url(jsonObject.getString("Porfile_picture_url"));
          profile.setPosts_counter(jsonObject.getInt("Posts_counter"));
          profile.setPhotos_counter(jsonObject.getInt("Photos_counter"));
          profile.setConnection_count(jsonObject.getInt("Connection_count"));
          profile.setCity(jsonObject.getString("City"));
          profile.setCountry(jsonObject.getString("Country"));
          profile.setPhonenumber(jsonObject.getString("Phonenumber"));
          profile.setProfile_bio(jsonObject.getString("Profile_bio"));

      }
      catch (Exception ex)
      {
          profile=null;
          Log.d(TAG, "fill_object_myprofile: exception="+ex.getMessage().toString());
      }
      return  profile;
  }

  //fill friends requests object
    static  public friends_requests fill_object_friends_requests(JSONObject jsonObject)
    {
        friends_requests requests1=new friends_requests();
        try {
            requests1.setRequestid(jsonObject.getInt("Requestid"));
            requests1.setUserid_sender(jsonObject.getInt("Userid_sender"));
            requests1.setUserid_recipient(jsonObject.getInt("Userid_recipient"));
            requests1.setRequest_date(jsonObject.getString("Request_date"));
            requests1.setFreinds_type(jsonObject.getInt("Freinds_type"));
            requests1.setApprovaldate(jsonObject.getString("Approvaldate"));
        }
        catch (Exception ex)
        {
            requests1=null;
            Log.d(TAG, "fill_object_myprofile: exception="+ex.getMessage().toString());
        }
        return  requests1;
    }
}

package com.example.testavocado.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class friends_posts extends user_post implements Serializable {
    @SerializedName("Friend_id")
    private int friend_id;


    //Ctor------------------------------------------------------------------------------------------------------------>

    public friends_posts(int friend_id, int postid, int userid, String post_text, String post_imageurl, String post_date,
                         String postlastchange_date, int how_can_see, int likes_counter, int comments_counter,
                         int dis_likes_counter, String user_firstname, String user_lastname, boolean isactive) {
       super(postid, userid, post_text, post_imageurl, post_date, postlastchange_date, how_can_see, likes_counter, comments_counter
               , dis_likes_counter);
        this.friend_id = friend_id;
    }
    public friends_posts()
    {}
    //Get/Set------------------------------------------------------------------------------------------------------------>

    public int getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(int friend_id) {
        this.friend_id = friend_id;
    }


}

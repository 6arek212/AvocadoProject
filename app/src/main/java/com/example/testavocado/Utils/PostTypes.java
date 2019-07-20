package com.example.testavocado.Utils;

import android.content.Context;

import com.example.testavocado.R;

public class PostTypes {



    public static  String[] getPostTypes(Context context){
        String [] strings=new String[2];

        strings[0]=context.getString(R.string.friends_only);
        strings[1]=context.getString(R.string.public_post);


        return strings;
    }



}

package com.example.testavocado;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProfileInfoDB extends SQLiteOpenHelper {

    public ProfileInfoDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table profile_info_tbl(" +
                "user_first_name text ," +
                "user_last_name text ," +
                "user_email text," +
                "user_birth_date date," +
                "user_gender int , " +
                "user_profile_photo blob," +
                "user_posts_count int," +
                "user_photos_count int," +
                "user_connection_count int," +
                "user_reputation int ," +
                "user_city text," +
                "user_country text ," +
                "user_job text , " +
                "user_location_swich bit," +
                "account_is_private bit," +
                "notification_count int," +
                "unread_messages_count int )";

        db.execSQL(query);
    }


    public byte[] getProfilePohot() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "select user_first_name,user_last_name,user_profile_photo from profile_info_tbl";
        Cursor cursor = db.rawQuery(query, null);

        byte[] image="".getBytes();


        if (cursor != null)
            if (cursor.moveToFirst())
                do {
                    image = cursor.getBlob(2);
                } while (cursor.moveToNext());

        return image;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}

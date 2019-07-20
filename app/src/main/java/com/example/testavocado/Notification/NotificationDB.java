package com.example.testavocado.Notification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.testavocado.Models.Notification;
import com.example.testavocado.Utils.TimeMethods;

import java.util.ArrayList;
import java.util.List;

public class NotificationDB extends SQLiteOpenHelper {

    public NotificationDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table notification_tbl(notification_id int primary key," +
                "user_id int," +
                "user_name text ," +
                "user_profile_photo text," +
                "notification_type int ," +
                "notification_datetime datetime," +
                "post_id int ," +
                "notification_type_name text)";

        db.execSQL(query);
    }






    public void insertNotification(Notification notification){
        ContentValues c1=new ContentValues();
        c1.put("notification_id",notification.getNotification_id());
        c1.put("user_id",notification.getUser_id_sent_notification());
        c1.put("user_name",notification.getUser_sent_name());
        c1.put("user_profile_photo",notification.getUser_sent_profile_image());
        c1.put("notification_type",notification.getNotification_type());
        c1.put("notification_datetime",notification.getNotification_datetime());
        c1.put("post_id",notification.getPost_id());
        c1.put("notification_type_name",notification.getType_txt());

        SQLiteDatabase db=getWritableDatabase();
        db.insert("notification_tbl",null,c1);
    }






    public List<Notification> getNotifications(int offest){
        SQLiteDatabase db=getWritableDatabase();
        String query="select * from notification_tbl " +
                "order by notification_datetime DESC " +
                "limit 20,"+offest;

        Cursor cursor=db.rawQuery(query,null);
        List<Notification> notifications=new ArrayList<>();

        if(cursor!=null){

            if(cursor.moveToFirst())
                do {

                    Notification notification=new Notification();

                    notification.setNotification_id(cursor.getInt(0));
                    notification.setUser_id_sent_notification(cursor.getInt(1));
                    notification.setUser_sent_name(cursor.getString(2));
                    notification.setUser_sent_profile_image(cursor.getString(3));
                    notification.setNotification_type(cursor.getInt(4));
                    notification.setNotification_datetime(TimeMethods.convertDateTimeFormat(cursor.getString(5)));
                    notification.setPost_id(cursor.getInt(6));
                    notification.setType_txt(cursor.getString(7));

                }while (cursor.moveToNext());
        }

        return notifications;
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
